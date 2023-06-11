package io.github.fandreuz.open.data.server.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import io.github.fandreuz.open.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.open.data.server.database.DatabaseIdempotentTypedClient;
import io.github.fandreuz.open.data.server.database.DatabaseNotFoundException;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * MongoDB implementation of {@link DatabaseTypedClient} for
 * {@link CollectionMetadata} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class CollectionMetadataMongoDatabaseClient
      implements
         DatabaseIdempotentTypedClient<CollectionMetadata, CollectionMetadata> {

   private static final String DATASET_NAME = "dataset-db";
   private static final String COLLECTION_NAME = "collections-metadata";

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public void createOrUpdate(@NonNull CollectionMetadata metadata) {
      log.info("Storing collection metadata '{}' in the DB ...", metadata);

      // Create if not found
      var options = new ReplaceOptions();
      options.upsert(true);

      var collection = getMetadataCollection();
      var result = collection.replaceOne(Filters.eq("_id", metadata.getId()), metadata, options);

      log.info("Stored collection metadata '{}' in the DB (old was {})", metadata, result);
   }

   @Override
   public CollectionMetadata get(String id) {
      var collection = getMetadataCollection();
      log.info("Getting collection metadata for ID={} ...", id);
      CollectionMetadata result = collection.find(Filters.eq("_id", id)).first();
      if (result == null) {
         String msg = String.format("Metadata not found for ID=%s", id);
         throw new DatabaseNotFoundException(msg);
      }
      log.info("Found metadata: {}", result);
      return result;
   }

   @Override
   public SortedSet<CollectionMetadata> getAll() {
      var collection = getMetadataCollection();
      log.info("Getting all stored collections metadata ...");
      var result = collection.find().into(new TreeSet<>());
      log.info("Found {} collections metadata", result.size());
      return result;
   }

   private MongoCollection<CollectionMetadata> getMetadataCollection() {
      return databaseClientSetup.getMongoClient() //
            .getDatabase(DATASET_NAME) //
            .getCollection(COLLECTION_NAME, CollectionMetadata.class);
   }
}
