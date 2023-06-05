package io.github.fandreuz.model;

import io.github.fandreuz.conversion.ConversionServiceOrchestrator;
import io.github.fandreuz.database.DatabaseNotFoundException;
import io.github.fandreuz.database.DatabaseTransactionService;
import io.github.fandreuz.database.DatabaseTypedClient;
import io.github.fandreuz.database.ExtractibleDatabaseTypedClient;
import io.github.fandreuz.database.TransactionController;
import io.github.fandreuz.fetch.DatasetFetchService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Dataset service.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
public final class DatasetService {

   @Inject
   private DatabaseTypedClient<DatasetMetadata, DatasetMetadata> metadataDatabaseClient;

   @Inject
   private ExtractibleDatabaseTypedClient<DatasetCoordinates, StoredDataset> datasetDatabaseClient;

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
      var pair = datasetFetchService.fetchDataset(collectionId, file);
      DatasetMetadata metadata = pair.getKey();

      // If the metadata is already in the DB, stop the operation
      var dbMetadata = getMetadataIfAvailable(metadata.getId());
      if (dbMetadata.isPresent()) {
         log.info("The operation aborted because the dataset was already imported");
         return dbMetadata.get();
      }

      Path converted = conversionServiceOrchestrator.getConversionService(metadata.getType()) //
            .convert(pair.getValue());
      DatasetCoordinates datasetCoordinates = new DatasetCoordinates(metadata.getId(), converted);

      try (TransactionController transactionController = transactionService.start()) {
         datasetDatabaseClient.create(datasetCoordinates);
         metadataDatabaseClient.create(metadata);
         transactionController.commit();
      } catch (Exception exception) {
         throw new RuntimeException("An exception occurred while closing the transaction", exception);
      }

      return pair.getKey();
   }

   private Optional<DatasetMetadata> getMetadataIfAvailable(@NonNull String metadataId) {
      try {
         return Optional.of(getMetadata(metadataId));
      } catch (DatabaseNotFoundException exception) {
         log.info("Metadata not found in the database, continuing the operation", exception);
         return Optional.empty();
      }
   }

   private static String buildDatasetLockKey(@NonNull String collectionId, @NonNull String file) {
      return collectionId + "-" + file;
   }

   public SortedSet<String> getColumnNames(@NonNull String datasetId) {
      return datasetDatabaseClient.get(datasetId).getColumnNames();
   }

   public SortedMap<String, String> getColumn(@NonNull String datasetId, @NonNull String columnName) {
      return datasetDatabaseClient.getColumn(datasetId, columnName);
   }

   /**
    * Find an existing dataset in the service.
    *
    * @param datasetId
    *            ID of the dataset to be found.
    * @return the dataset object if it exists.
    */
   public DatasetMetadata getMetadata(String datasetId) {
      return metadataDatabaseClient.get(datasetId);
   }

   /**
    * Get all the datasets stored in the service.
    *
    * @return a set containing all the datasets.
    */
   public SortedSet<DatasetMetadata> getAllMetadata() {
      return metadataDatabaseClient.getAll();
   }
}
