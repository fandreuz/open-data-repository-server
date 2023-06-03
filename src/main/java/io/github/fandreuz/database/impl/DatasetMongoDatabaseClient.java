package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoCollection;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.model.Dataset;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.SortedSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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

    @Inject
    private MongoClientSetup databaseClientSetup;

    @Override
    public Optional<Dataset> create(@NonNull Dataset dataset) {
        log.info("Storing dataset '{}' in the database ...", dataset.getUniqueId());
        var reader = new StringReader(dataset.getFileCsvContent());
        CSVFormat csvFormat = CSVFormat.Builder.create().setHeader().build();
        CSVParser parser;
        try {
            parser = csvFormat.parse(reader);
        } catch (IOException exception) {
            log.error("An error occurred while parsing the CSV file", exception);
            return Optional.empty();
        }

        MongoCollection<Document> collection = getDatasetCollection(dataset);
        var headers = parser.getHeaderMap();
        for (var record : parser) {
            Document document = new Document();
            for (var headerEntry : headers.entrySet()) {
                document.append(headerEntry.getKey(), record.get(headerEntry.getValue()));
            }
            collection.insertOne(document);
        }

        log.info("Stored dataset '{}' in the database", dataset.getUniqueId());
        return Optional.of(dataset);
    }

    @Override
    public Optional<Dataset> get(long datasetId) {
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
