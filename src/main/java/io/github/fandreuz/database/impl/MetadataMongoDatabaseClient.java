package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.model.DatasetMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonInt64;
import org.bson.BsonValue;
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
    public Optional<DatasetMetadata> create(@NonNull DatasetMetadata dataset) {
        var collection = getMetadataCollection();
        log.info("Inserting new dataset: {} ...", dataset);
        InsertOneResult result = collection.insertOne(dataset);
        if (!result.wasAcknowledged()) {
            log.info("The write wasn't acknowledged");
            return Optional.empty();
        }

        log.info("Inserted new dataset: {}", dataset);
        return Optional.ofNullable(result.getInsertedId()) //
                .map(BsonValue::asInt64) //
                .map(BsonInt64::getValue) //
                .flatMap(this::get);
    }

    @Override
    public Optional<DatasetMetadata> get(long datasetId) {
        var collection = getMetadataCollection();
        log.info("Getting dataset (id={}) ...", datasetId);
        return Optional.ofNullable(
                collection.find(new Document("_id", datasetId)).first());
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
