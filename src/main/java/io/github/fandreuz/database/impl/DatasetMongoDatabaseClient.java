package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoCollection;
import io.github.fandreuz.database.DatabaseException;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.model.Dataset;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.SortedSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.bson.Document;

/**
 * MongoDB implementation of {@link DatabaseTypeClient} for {@link Dataset} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
public class DatasetMongoDatabaseClient implements DatabaseTypeClient<Dataset> {

    private static final String DATASET_NAME = "dataset-db";
    private static final CSVFormat csvFormat =
            CSVFormat.Builder.create().setHeader().build();

    @Inject
    private MongoClientSetup databaseClientSetup;

    @Override
    public Dataset create(@NonNull Dataset dataset) {
        log.info("Storing dataset '{}' in the database ...", dataset.getUniqueId());

        MongoCollection<Document> collection = getDatasetCollection(dataset);
        try (BufferedReader reader = Files.newBufferedReader(dataset.getLocalFileLocation());
                CSVParser parser = csvFormat.parse(reader)) {
            var headers = parser.getHeaderMap();
            for (CSVRecord record : parser) {
                Document document = new Document();
                for (var headerEntry : headers.entrySet()) {
                    document.append(headerEntry.getKey(), record.get(headerEntry.getValue()));
                }
                collection.insertOne(document);
            }
        } catch (IOException exception) {
            throw new DatabaseException("An error occurred while transferring CSV records to the DB", exception);
        }

        log.info("Stored dataset '{}' in the database", dataset.getUniqueId());
        return dataset;
    }

    @Override
    public Dataset get(String datasetId) {
        return null;
    }

    @Override
    public SortedSet<Dataset> getAll() {
        return null;
    }

    private MongoCollection<Document> getDatasetCollection(Dataset dataset) {
        return databaseClientSetup
                .getMongoClient() //
                .getDatabase(DATASET_NAME) //
                .getCollection(dataset.getUniqueId());
    }
}
