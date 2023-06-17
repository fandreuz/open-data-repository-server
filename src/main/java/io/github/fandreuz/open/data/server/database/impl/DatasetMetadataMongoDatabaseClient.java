package io.github.fandreuz.open.data.server.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.result.InsertOneResult;
import io.github.fandreuz.open.data.server.database.DatabaseBadQueryException;
import io.github.fandreuz.open.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.open.data.server.database.DatabaseException;
import io.github.fandreuz.open.data.server.database.DatabaseNotFoundException;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadataDO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 * MongoDB implementation of {@link DatabaseTypedClient} for
 * {@link DatasetMetadata} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class DatasetMetadataMongoDatabaseClient implements DatabaseTypedClient<DatasetMetadataDO, DatasetMetadataDO> {

   private static final String DATASET_NAME = "dataset-db";
   private static final String COLLECTION_NAME = "datasets-metadata";

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public void create(@NonNull DatasetMetadataDO metadata) {
      log.info("Storing dataset metadata '{}' in the DB ...", metadata);

      var collection = getMetadataCollection();
      InsertOneResult result = collection.insertOne(metadata);
      if (!result.wasAcknowledged() || result.getInsertedId() == null) {
         String msg = String.format("The write operation wasn't acknowledged (ID=%s)", metadata.getDatasetId());
         throw new DatabaseException(msg);
      }

      log.info("Stored dataset metadata '{}' in the DB", metadata);
   }

   @Override
   public DatasetMetadataDO getEntry(@NonNull String id) {
      return getEntriesMatching(String.format("{ _id: \"%s\"}", id)).stream() //
            .findFirst() //
            .orElseThrow(() -> {
               String msg = String.format("Metadata not found for ID=%s", id);
               return new DatabaseNotFoundException(msg);
            });
   }

   // TODO Unify with CollectionMetadata code
   @Override
   public Set<DatasetMetadataDO> getEntriesMatching(@NonNull String query) {
      var collection = getMetadataCollection();
      log.info("Querying dataset metadata database, query: '{}'...", query);

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

   private MongoCollection<DatasetMetadataDO> getMetadataCollection() {
      return databaseClientSetup.getMongoClient() //
            .getDatabase(DATASET_NAME) //
            .getCollection(COLLECTION_NAME, DatasetMetadataDO.class);
   }
}
