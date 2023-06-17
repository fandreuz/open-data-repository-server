package io.github.fandreuz.open.data.server.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOptions;
import io.github.fandreuz.open.data.server.database.DatabaseBadQueryException;
import io.github.fandreuz.open.data.server.database.DatabaseNotFoundException;
import io.github.fandreuz.open.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import io.github.fandreuz.open.data.server.model.collection.CollectionMetadataDO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

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
         DatabaseTypedClient<CollectionMetadataDO, CollectionMetadataDO> {

   private static final String DATASET_NAME = "dataset-db";
   private static final String COLLECTION_NAME = "collections-metadata";

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public void create(@NonNull CollectionMetadataDO metadata) {
      log.info("Storing collection metadata '{}' in the DB ...", metadata);

      // Create if not found
      var options = new ReplaceOptions();
      options.upsert(true);

      var collection = getMetadataCollection();
      var result = collection.replaceOne(Filters.eq("_id", metadata.getId()), metadata, options);

      log.info("Stored collection metadata '{}' in the DB (old was {})", metadata, result);
   }

   @Override
   public CollectionMetadataDO getEntry(@NonNull String id) {
      return getEntriesMatching(String.format("{_id: \"%s\"}", id)).stream() //
            .findFirst() //
            .orElseThrow(() -> {
               String msg = String.format("Metadata not found for ID=%s", id);
               return new DatabaseNotFoundException(msg);
            });
   }

   // TODO Unify with DatasetMetadata code
   @Override
   public Set<CollectionMetadataDO> getEntriesMatching(@NonNull String query) {
      var collection = getMetadataCollection();
      log.info("Querying collection metadata database, query: '{}'...", query);

      if (collection.find(Filters.empty()) //
            .projection(Projections.include()) //
            .into(new HashSet<>()).isEmpty() //
      ) {
         throw new DatabaseNotFoundException("The database is empty");
      }

      Document parsedQuery;
      try {
         // See https://www.mongodb.com/docs/manual/tutorial/query-documents/ for valid
         // queries
         parsedQuery = Document.parse(query);
      } catch (Exception exception) {
         String msg = String.format("An error occurred while parsing the query: '%s'", query);
         throw new DatabaseBadQueryException(msg, exception);
      }
      return collection.find(parsedQuery).into(new HashSet<>());
   }

   private MongoCollection<CollectionMetadataDO> getMetadataCollection() {
      return databaseClientSetup.getMongoClient() //
            .getDatabase(DATASET_NAME) //
            .getCollection(COLLECTION_NAME, CollectionMetadataDO.class);
   }
}
