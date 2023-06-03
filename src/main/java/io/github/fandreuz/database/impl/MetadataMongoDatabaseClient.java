package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import io.github.fandreuz.database.DatabaseException;
import io.github.fandreuz.database.DatabaseNotFoundException;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.model.DatasetMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

/**
 * MongoDB implementation of {@link DatabaseTypeClient} for {@link DatasetMetadata} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
public class MetadataMongoDatabaseClient implements DatabaseTypeClient<DatasetMetadata> {

    private static final String DATASET_NAME = "dataset-db";
    private static final String COLLECTION_NAME = "datasets-metadata";

    @Inject
    private MongoClientSetup databaseClientSetup;

    @Override
    public DatasetMetadata create(@NonNull DatasetMetadata dataset) {
        var collection = getMetadataCollection();
        log.info("Inserting new dataset in the DB: {} ...", dataset);
        InsertOneResult result = collection.insertOne(dataset);
        if (!result.wasAcknowledged() || result.getInsertedId() == null) {
            String msg = String.format("The write operation wasn't acknowledged (ID=%s)", dataset.getId());
            throw new DatabaseException(msg);
        }
        log.info("Inserted new dataset in the DB: {}", dataset);
        return get(dataset.getId());
    }

    @Override
    public DatasetMetadata get(String datasetId) {
        var collection = getMetadataCollection();
        log.info("Getting dataset (id={}) ...", datasetId);
        DatasetMetadata result = collection.find(new Document("_id", datasetId)).first();
        if (result == null) {
            String msg = String.format("Metadata not found for ID=%s", datasetId);
            throw new DatabaseNotFoundException(msg);
        }
        return result;
    }

    @Override
    public SortedSet<DatasetMetadata> getAll() {
        var collection = getMetadataCollection();
        log.info("Getting all stored datasets ...");
        return collection.find().into(new TreeSet<>());
    }

    private MongoCollection<DatasetMetadata> getMetadataCollection() {
        return databaseClientSetup
                .getMongoClient() //
                .getDatabase(DATASET_NAME) //
                .getCollection(COLLECTION_NAME, DatasetMetadata.class);
    }
}
