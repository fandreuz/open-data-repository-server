package io.github.fandreuz.database.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.InsertOneResult;
import io.github.fandreuz.database.DatabaseClient;
import io.github.fandreuz.model.DatasetMetadata;
import jakarta.annotation.PreDestroy;
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
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 * MongoDB implementation of {@link DatabaseClient}.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
public class MongoDatabaseMetadataClient implements DatabaseClient<DatasetMetadata> {

    private static final String DATASET_NAME = "dataset-db";
    private static final String COLLECTION_NAME = "Datasets";

    @Inject
    private MongoClientSetup databaseClientSetup;

    private MongoClient mongoClient;

    @Override
    public Optional<DatasetMetadata> createDataset(@NonNull DatasetMetadata dataset) {
        var collection = getDatasetCollection();
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
                .flatMap(this::getDataset);
    }

    @Override
    public Optional<DatasetMetadata> getDataset(long datasetId) {
        var collection = getDatasetCollection();
        log.info("Getting dataset (id={}) ...", datasetId);
        return Optional.ofNullable(
                collection.find(new Document("_id", datasetId)).first());
    }

    @Override
    public SortedSet<DatasetMetadata> getAllDatasets() {
        var collection = getDatasetCollection();
        log.info("Getting all stored datasets ...");
        return collection.find().into(new TreeSet<>());
    }

    private MongoCollection<DatasetMetadata> getDatasetCollection() {
        if (mongoClient == null) {
            initDatabaseClient();
        }
        return mongoClient.getDatabase(DATASET_NAME).getCollection(COLLECTION_NAME, DatasetMetadata.class);
    }

    private void initDatabaseClient() {
        MongoClientSettings settings = MongoClientSettings.builder() //
                .applyConnectionString(new ConnectionString(databaseClientSetup.getDatabaseConnectionString())) //
                .codecRegistry(setupCodecRegistry()) //
                .build();
        mongoClient = MongoClients.create(settings);
    }

    private static CodecRegistry setupCodecRegistry() {
        CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder() //
                        .automatic(true) //
                        .build() //
                );
        return CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
    }

    @PreDestroy
    void cleanUp() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
