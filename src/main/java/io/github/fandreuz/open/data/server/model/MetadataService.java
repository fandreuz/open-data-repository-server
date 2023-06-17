package io.github.fandreuz.open.data.server.model;

import io.github.fandreuz.open.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadataDO;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadataDO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NonNull;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Component to manage metadata resources.
 *
 * @author fandreuz
 */
@Singleton
public final class MetadataService {

   @Inject
   private DatabaseTypedClient<CollectionMetadataDO, CollectionMetadataDO> collectionMetadataDatabaseClient;

   @Inject
   private DatabaseTypedClient<DatasetMetadataDO, DatasetMetadataDO> datasetMetadataDatabaseClient;

   public DatasetMetadata getDatasetMetadata(@NonNull String metadataId) {
      var datasetMetadata = DatasetMetadata.fromDatabaseObject(datasetMetadataDatabaseClient.getEntry(metadataId));
      return attachCollectionMetadata(datasetMetadata);
   }

   public CollectionMetadata getCollectionMetadata(@NonNull String collectionId) {
      return CollectionMetadata.fromDatabaseObject(collectionMetadataDatabaseClient.getEntry(collectionId));
   }

   public SortedSet<DatasetMetadata> getDatasetMetadataMatching(@NonNull String query) {
      var output = datasetMetadataDatabaseClient.getEntriesMatching(query) //
            .stream() //
            .map(DatasetMetadata::fromDatabaseObject) //
            .map(this::attachCollectionMetadata) //
            .collect(Collectors.toUnmodifiableSet());
      return new TreeSet<>(output);
   }

   public SortedSet<CollectionMetadata> getCollectionMetadataMatching(@NonNull String query) {
      var output = collectionMetadataDatabaseClient.getEntriesMatching(query) //
            .stream() //
            .map(CollectionMetadata::fromDatabaseObject) //
            .collect(Collectors.toUnmodifiableSet());
      return new TreeSet<>(output);
   }

   private DatasetMetadata attachCollectionMetadata(DatasetMetadata datasetMetadata) {
      String collectionMetadataId = extractCollectionMetadataId(datasetMetadata.getDatasetId());
      var collectionMetadata = CollectionMetadata
            .fromDatabaseObject(collectionMetadataDatabaseClient.getEntry(collectionMetadataId));
      return DatasetMetadata.attachCollectionMetadata(datasetMetadata, collectionMetadata);
   }

   // Leverage URN structure
   private static String extractCollectionMetadataId(@NonNull String datasetMetadataId) {
      return datasetMetadataId.substring(0, datasetMetadataId.lastIndexOf(":"));
   }
}
