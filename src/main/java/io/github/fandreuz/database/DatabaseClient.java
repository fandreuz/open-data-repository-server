package io.github.fandreuz.database;

import java.util.Optional;
import java.util.SortedSet;

import io.github.fandreuz.model.Dataset;
import lombok.NonNull;

/**
 * Interface for database clients.
 *
 * @author fandreuz
 */
public interface DatabaseClient {

    /**
     * Create a new dataset in the database.
     *
     * @param dataset the object to be inserted.
     * @return the newly created dataset if available.
     */
    Optional<Dataset> createDataset(@NonNull Dataset dataset);

    /**
     * Find an existing dataset in the database.
     *
     * @param datasetId ID of the dataset to be found.
     * @return the dataset object if it exists.
     */
    Optional<Dataset> getDataset(long datasetId);

    /**
     * Get all the datasets stored in the database.
     *
     * @return a set containing all the datasets in the database.
     */
    SortedSet<Dataset> getAllDatasets();
}
