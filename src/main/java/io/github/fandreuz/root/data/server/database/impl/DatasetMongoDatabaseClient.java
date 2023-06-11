package io.github.fandreuz.root.data.server.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import io.github.fandreuz.root.data.server.database.DatabaseBadQueryException;
import io.github.fandreuz.root.data.server.database.DatabaseException;
import io.github.fandreuz.root.data.server.database.DatabaseNotFoundException;
import io.github.fandreuz.root.data.server.database.DatabaseTypedClient;
import io.github.fandreuz.root.data.server.database.ExtractibleDatabaseTypedClient;
import io.github.fandreuz.root.data.server.model.dataset.DatasetCoordinates;
import io.github.fandreuz.root.data.server.model.dataset.StoredDataset;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.bson.Document;

/**
 * MongoDB implementation of {@link DatabaseTypedClient} for dataset objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class DatasetMongoDatabaseClient implements ExtractibleDatabaseTypedClient<DatasetCoordinates, StoredDataset> {

   private static final String DATASET_NAME = "dataset-db";
   private static final CSVFormat csvFormat = CSVFormat.Builder.create().setHeader().build();

   @Inject
   private MongoClientSetup databaseClientSetup;

   @Override
   public void create(@NonNull DatasetCoordinates datasetCoordinates) {
      log.info("Storing dataset '{}' in the DB ...", datasetCoordinates);

      MongoCollection<Document> collection = getDatasetCollection(datasetCoordinates.getId());
      log.info("DB Collection: {}", collection.getNamespace());

      try (BufferedReader reader = Files.newBufferedReader(datasetCoordinates.getLocalFileLocation());
            CSVParser parser = csvFormat.parse(reader)) {
         var iterator = parser.iterator();
         if (!iterator.hasNext()) {
            throw new IllegalArgumentException("The dataset is empty");
         }

         var headers = parser.getHeaderMap();
         while (iterator.hasNext()) {
            var record = iterator.next();
            Document document = new Document();
            for (var headerEntry : headers.entrySet()) {
               document.append(headerEntry.getKey(), record.get(headerEntry.getValue()));
            }
            collection.insertOne(document);
         }
      } catch (IOException exception) {
         throw new DatabaseException("An error occurred while transferring CSV records to the DB", exception);
      }

      log.info("Stored dataset '{}' in the database", datasetCoordinates);
   }

   @Override
   public StoredDataset get(String id) {
      var collection = getDatasetCollection(id);
      log.info("Getting dataset with ID={} ...", id);

      var firstDocument = collection.find().first();
      if (firstDocument == null) {
         throw new DatabaseNotFoundException(String.format("Dataset with ID=%s not found", id));
      }

      StoredDataset storedDataset = new StoredDataset(id, firstDocument.keySet());
      log.info("Found dataset: {}", storedDataset);

      return storedDataset;
   }

   @Override
   public SortedMap<String, String> getColumn(@NonNull String id, @NonNull String columnName) {
      var collection = getDatasetCollection(id);
      log.info("Querying dataset with ID={}, column: '{}' ...", id, columnName);

      var projection = Projections.fields(Projections.include(columnName));
      var entries = collection.find(Filters.empty()) //
            .projection(projection) //
            .into(new HashSet<>());
      if (entries.isEmpty()) {
         String msg = String.format("Dataset with ID=%s not found", id);
         throw new DatabaseNotFoundException(msg);
      }

      return entries.stream() //
            .collect(Collectors.toMap( //
                  document -> document.getObjectId("_id").toString(), //
                  document -> document.get(columnName, String.class), //
                  (value1, value2) -> { //
                     throw new RuntimeException(
                           String.format("Duplicate key for values %s and %s", value1, value2));
                  }, //
                  TreeMap::new) //
            );
   }

   @Override
   public SortedSet<String> getIdsWhere(@NonNull String id, @NonNull String query) {
      var collection = getDatasetCollection(id);
      log.info("Querying dataset with ID={}, query: '{}'...", id, query);

      if (collection.find(Filters.empty()) //
            .projection(Projections.include()) //
            .into(new HashSet<>()).isEmpty() //
      ) {
         String msg = String.format("Dataset with ID=%s not found", id);
         throw new DatabaseNotFoundException(msg);
      }

      Document parsedQuery;
      try {
         // See https://www.mongodb.com/docs/manual/tutorial/query-documents/ for valid
         // queries
         parsedQuery = Document.parse(query);
      } catch (Exception exception) {
         throw new DatabaseBadQueryException("An error occurred while parsing the query", exception);
      }
      return collection.find(parsedQuery) //
            .projection(Projections.include("_id")) //
            .map(Document::toString) //
            .into(new TreeSet<>());
   }

   @Override
   public SortedSet<StoredDataset> getAll() {
      return null;
   }

   private MongoCollection<Document> getDatasetCollection(@NonNull String datasetId) {
      return databaseClientSetup.getMongoClient() //
            .getDatabase(DATASET_NAME) //
            .getCollection(datasetId);
   }
}
