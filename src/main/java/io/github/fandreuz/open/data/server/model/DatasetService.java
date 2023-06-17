package io.github.fandreuz.open.data.server.model;

import io.github.fandreuz.open.data.server.conversion.ConversionServiceOrchestrator;
import io.github.fandreuz.open.data.server.database.DatabaseTransactionService;
import io.github.fandreuz.open.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.open.data.server.database.MonolithicDatabaseTypedClient;
import io.github.fandreuz.open.data.server.database.TransactionController;
import io.github.fandreuz.open.data.server.fetch.DatasetFetchService;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadataDO;
import io.github.fandreuz.open.data.server.model.dataset.DatasetCoordinates;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadataDO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Dataset service.
 *
 * @author fandreuz
 */
@Singleton
public final class DatasetService {

   @Inject
   private DatabaseTypedClient<CollectionMetadataDO, CollectionMetadataDO> collectionMetadataDatabaseClient;

   @Inject
   private DatabaseTypedClient<DatasetMetadataDO, DatasetMetadataDO> datasetMetadataDatabaseClient;

   @Inject
   private MonolithicDatabaseTypedClient<DatasetCoordinates> datasetDatabaseClient;

   @Inject
   private DatasetFetchService datasetFetchService;

   @Inject
   private DatabaseTransactionService transactionService;

   @Inject
   private ConversionServiceOrchestrator conversionServiceOrchestrator;

   private final Map<String, Future<DatasetMetadata>> datasetMetadataPool = new ConcurrentHashMap<>();

   /**
    * Create a new dataset. A dataset is identified by the ID of the collection it
    * belongs to, and by the file name.
    *
    * @param collectionId
    *            unique ID of the collection.
    * @param file
    *            name of the file to be imported.
    * @return the newly created dataset metadata if available.
    */
   public DatasetMetadata createDataset(@NonNull String collectionId, @NonNull String file) {
      String datasetLockKey = buildDatasetLockKey(collectionId, file);
      boolean leader = datasetMetadataPool.putIfAbsent(datasetLockKey, new CompletableFuture<>()) == null;
      var future = datasetMetadataPool.get(datasetLockKey);
      if (leader) {
         var metadata = datasetCreationTransaction(collectionId, file);
         ((CompletableFuture<DatasetMetadata>) future).complete(metadata);
      }

      try {
         return future.get();
      } catch (Exception exception) {
         throw new ConcurrentOperationException(
               "An exception occurred while waiting for the completion of a concurrent operation on the same dataset",
               exception);
      }
   }

   private DatasetMetadata datasetCreationTransaction(@NonNull String collectionId, @NonNull String file) {
      var triple = datasetFetchService.fetchDataset(collectionId, file);
      DatasetMetadata datasetMetadata = triple.getMiddle();
      try {
         // If the metadata is already in the DB, stop the operation
         return getMetadata(datasetMetadata.getDatasetId());
      } catch (Exception exception) {
         // The exception is expected
      }

      Path converted = conversionServiceOrchestrator.getConversionService(datasetMetadata.getType())
            .convert(triple.getRight());
      try {
         var lines = Files.readAllLines(converted);
         if (!lines.isEmpty()) {
            String columnNames = lines.get(0);
            long numberOfColumns = 1 + columnNames.chars() //
                  .filter(ch -> ch == ',') //
                  .count();
            datasetMetadata = DatasetMetadata.attachCsvMetadata(datasetMetadata, numberOfColumns, columnNames);
         }
      } catch (Exception exception) {
         // Skip CSV metadata
      }

      DatasetCoordinates datasetCoordinates = new DatasetCoordinates(datasetMetadata.getDatasetId(), converted);

      try (TransactionController transactionController = transactionService.start()) {
         datasetDatabaseClient.create(datasetCoordinates);
         datasetMetadataDatabaseClient.create(datasetMetadata.asDatabaseObject());
         collectionMetadataDatabaseClient.create(triple.getLeft().asDatabaseObject());
         transactionController.commit();
      } catch (Exception exception) {
         throw new RuntimeException("An exception occurred while closing the transaction", exception);
      }

      return DatasetMetadata.attachCollectionMetadata(datasetMetadata, triple.getLeft());
   }

   public DatasetMetadata getMetadata(@NonNull String metadataId) {
      var datasetMetadata = DatasetMetadata.fromDatabaseObject(datasetMetadataDatabaseClient.get(metadataId));
      return attachCollectionMetadata(datasetMetadata);
   }

   public SortedSet<DatasetMetadata> getAllMetadata() {
      var output = datasetMetadataDatabaseClient.getAll() //
            .stream() //
            .map(DatasetMetadata::fromDatabaseObject) //
            .map(this::attachCollectionMetadata) //
            .collect(Collectors.toUnmodifiableSet());
      return new TreeSet<>(output);
   }

   private DatasetMetadata attachCollectionMetadata(DatasetMetadata datasetMetadata) {
      String collectionMetadataId = extractCollectionMetadataId(datasetMetadata.getDatasetId());
      var collectionMetadata = CollectionMetadata
            .fromDatabaseObject(collectionMetadataDatabaseClient.get(collectionMetadataId));
      return DatasetMetadata.attachCollectionMetadata(datasetMetadata, collectionMetadata);
   }

   public SortedMap<String, String> getColumn(@NonNull String datasetId, @NonNull String columnName) {
      return new TreeMap<>(datasetDatabaseClient.getColumn(datasetId, columnName));
   }

   public SortedSet<String> getIdsWhere(@NonNull String datasetId, @NonNull String query) {
      return new TreeSet<>(datasetDatabaseClient.getIdsWhere(datasetId, query));
   }

   // Leverage URN structure
   private static String extractCollectionMetadataId(@NonNull String datasetMetadataId) {
      return datasetMetadataId.substring(0, datasetMetadataId.lastIndexOf(":"));
   }

   private static String buildDatasetLockKey(@NonNull String collectionId, @NonNull String file) {
      return collectionId + "-" + file;
   }
}
