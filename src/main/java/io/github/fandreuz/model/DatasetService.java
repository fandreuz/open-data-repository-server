package io.github.fandreuz.model;

import io.github.fandreuz.database.DatabaseTransactionService;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.database.TransactionController;
import io.github.fandreuz.fetch.DatasetFetchService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.SortedSet;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Dataset service.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
public final class DatasetService {

    @Inject
    private DatabaseTypeClient<DatasetMetadata> metadataDatabaseClient;

    @Inject
    private DatabaseTypeClient<Dataset> datasetDatabaseClient;

    @Inject
    private DatasetFetchService datasetFetchService;

    @Inject
    private DatabaseTransactionService transactionService;

    /**
     * Create a new dataset. A dataset is identified by the ID of the collection it belongs to, and by the file name.
     *
     * @param collectionId unique ID of the collection.
     * @param file name of the file to be imported.
     * @return the newly created dataset metadata if available.
     */
    public DatasetMetadata createDataset(@NonNull String collectionId, @NonNull String file) {
        DatasetMetadata storedMetadata;
        try (TransactionController transactionController = transactionService.start()) {
            var pair = datasetFetchService.fetchDataset(collectionId, file);
            storedMetadata = metadataDatabaseClient.create(pair.getKey()).orElseThrow();
            datasetDatabaseClient.create(pair.getValue()).orElseThrow();
            transactionController.commit();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        return storedMetadata;
    }

    /**
     * Find an existing dataset in the service.
     *
     * @param datasetId ID of the dataset to be found.
     * @return the dataset object if it exists.
     */
    public DatasetMetadata getMetadata(long datasetId) {
        return metadataDatabaseClient.get(datasetId).orElseThrow();
    }

    /**
     * Get all the datasets stored in the service.
     *
     * @return a set containing all the datasets.
     */
    public SortedSet<DatasetMetadata> getAllMetadata() {
        return metadataDatabaseClient.getAll();
    }
}
