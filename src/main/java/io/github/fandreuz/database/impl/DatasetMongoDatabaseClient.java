package io.github.fandreuz.database.impl;

import com.mongodb.client.MongoClient;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.model.Dataset;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import java.util.SortedSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * MongoDB implementation of {@link DatabaseTypeClient} for {@link Dataset} objects.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
public class DatasetMongoDatabaseClient implements DatabaseTypeClient<Dataset> {

    private static final String DATASET_NAME = "dataset-db";
    private static final String COLLECTION_NAME = "Datasets";

    @Inject
    private MongoClientSetup databaseClientSetup;

    private MongoClient mongoClient;

    @Override
    public Optional<Dataset> create(@NonNull Dataset dataset) {
        return null;
    }

    @Override
    public Optional<Dataset> get(long datasetId) {
        return null;
    }

    @Override
    public SortedSet<Dataset> getAll() {
        return null;
    }

    @PreDestroy
    void cleanUp() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
