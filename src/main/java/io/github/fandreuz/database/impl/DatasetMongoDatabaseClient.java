package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.fandreuz.database.DatabaseException;
import io.github.fandreuz.database.DatabaseNotFoundException;
import io.github.fandreuz.database.DatabaseTypedClient;
import io.github.fandreuz.model.DatasetCoordinates;
import io.github.fandreuz.model.StoredDataset;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.SortedSet;
import java.util.TreeSet;
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
public class DatasetMongoDatabaseClient implements DatabaseTypedClient<DatasetCoordinates, StoredDataset> {

    private static final String DATASET_NAME = "dataset-db";
    private static final CSVFormat csvFormat =
            CSVFormat.Builder.create().setHeader().build();

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
        log.info("Getting dataset for ID={} ...", id);
        Document result = collection.find(Filters.eq("_id", id)).first();
        if (result == null) {
            String msg = String.format("Dataset not found for ID=%s", id);
            throw new DatabaseNotFoundException(msg);
        }

        StoredDataset storedDataset = new StoredDataset(id, new TreeSet<>(result.keySet()));
        log.info("Found dataset: {}", storedDataset);
        return storedDataset;
    }

    @Override
    public SortedSet<StoredDataset> getAll() {
        return null;
    }

    private MongoCollection<Document> getDatasetCollection(@NonNull String datasetId) {
        return databaseClientSetup
                .getMongoClient() //
                .getDatabase(DATASET_NAME) //
                .getCollection(datasetId);
    }
}
