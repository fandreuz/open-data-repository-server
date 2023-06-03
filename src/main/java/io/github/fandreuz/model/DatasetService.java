package io.github.fandreuz.model;

import io.github.fandreuz.conversion.ConversionServiceOrchestrator;
import io.github.fandreuz.database.DatabaseTransactionService;
import io.github.fandreuz.database.DatabaseTypeClient;
import io.github.fandreuz.database.TransactionController;
import io.github.fandreuz.fetch.DatasetFetchService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.nio.file.Path;
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

    @Inject
    public ConversionServiceOrchestrator conversionServiceOrchestrator;

    /**
     * Create a new dataset. A dataset is identified by the ID of the collection it belongs to, and by the file name.
     *
     * @param collectionId unique ID of the collection.
     * @param file name of the file to be imported.
     * @return the newly created dataset metadata if available.
     */
    public DatasetMetadata createDataset(@NonNull String collectionId, @NonNull String file) {
        var pair = datasetFetchService.fetchDataset(collectionId, file);
        DatasetMetadata metadata = pair.getKey();
        Path converted = conversionServiceOrchestrator
                .getConversionService(metadata.getType()) //
                .convert(pair.getValue());
        Dataset dataset = new Dataset(pair.getKey().getId(), converted);

        try (TransactionController transactionController = transactionService.start()) {
            datasetDatabaseClient.create(dataset);
            DatasetMetadata storedMetadata = metadataDatabaseClient.create(metadata);
            transactionController.commit();
            return storedMetadata;
        } catch (Exception exception) {
            throw new RuntimeException("An exception occurred while closing the transaction", exception);
        }
    }

    /**
     * Find an existing dataset in the service.
     *
     * @param datasetId ID of the dataset to be found.
     * @return the dataset object if it exists.
     */
    public DatasetMetadata getMetadata(String datasetId) {
        return metadataDatabaseClient.get(datasetId);
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
