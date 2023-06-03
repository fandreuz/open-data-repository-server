package io.github.fandreuz.model;

import io.github.fandreuz.database.DatabaseClient;
import jakarta.inject.Inject;
import java.util.SortedSet;
import lombok.NonNull;

/**
 * Dataset service.
 *
 * @author fandreuz
 */
public final class DatasetService {

    @Inject
    private DatabaseClient databaseClient;

    /**
     * Create a new dataset.
     *
     * @param dataset the object to be created.
     * @return the newly created dataset if available.
     */
    public Dataset createDataset(@NonNull Dataset dataset) {
        return databaseClient.createDataset(dataset).orElseThrow();
    }

    /**
     * Find an existing dataset in the service.
     *
     * @param datasetId ID of the dataset to be found.
     * @return the dataset object if it exists.
     */
    public Dataset getDataset(long datasetId) {
        return databaseClient.getDataset(datasetId).orElseThrow();
    }

    /**
     * Get all the datasets stored in the service.
     *
     * @return a set containing all the datasets.
     */
    public SortedSet<Dataset> getAllDatasets() {
        return databaseClient.getAllDatasets();
    }
}
