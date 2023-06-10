package io.github.fandreuz.root.data.server.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import io.github.fandreuz.root.data.server.database.DatabaseException;
import io.github.fandreuz.root.data.server.database.DatabaseNotFoundException;
import io.github.fandreuz.root.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.root.data.server.model.dataset.DatasetMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * MongoDB implementation of {@link DatabaseTypedClient} for
 * {@link DatasetMetadata} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class DatasetMetadataMongoDatabaseClient implements DatabaseTypedClient<DatasetMetadata, DatasetMetadata> {

   private static final String DATASET_NAME = "dataset-db";
   private static final String COLLECTION_NAME = "datasets-metadata";

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public void create(@NonNull DatasetMetadata metadata) {
      log.info("Storing dataset metadata '{}' in the DB ...", metadata);

      var collection = getMetadataCollection();
      InsertOneResult result = collection.insertOne(metadata);
      if (!result.wasAcknowledged() || result.getInsertedId() == null) {
         String msg = String.format("The write operation wasn't acknowledged (ID=%s)", metadata.getId());
         throw new DatabaseException(msg);
      }

      log.info("Stored dataset metadata '{}' in the DB", metadata);
   }

   @Override
   public DatasetMetadata get(String id) {
      var collection = getMetadataCollection();
      log.info("Getting dataset metadata for ID={} ...", id);
      DatasetMetadata result = collection.find(Filters.eq("_id", id)).first();
      if (result == null) {
         String msg = String.format("Metadata not found for ID=%s", id);
         throw new DatabaseNotFoundException(msg);
      }
      log.info("Found metadata: {}", result);
      return result;
   }

   @Override
   public SortedSet<DatasetMetadata> getAll() {
      var collection = getMetadataCollection();
      log.info("Getting all stored datasets metadata ...");
      var result = collection.find().into(new TreeSet<>());
      log.info("Found {} dataset metadata", result.size());
      return result;
   }

   private MongoCollection<DatasetMetadata> getMetadataCollection() {
      return databaseClientSetup.getMongoClient() //
            .getDatabase(DATASET_NAME) //
            .getCollection(COLLECTION_NAME, DatasetMetadata.class);
   }
}
