package io.github.fandreuz.database;

import io.github.fandreuz.model.DatasetMetadata;
import java.util.Optional;
import java.util.SortedSet;
import lombok.NonNull;

/**
 * Interface for database clients.
 *
 * @param <T> type of the controlled type.
 * @author fandreuz
 */
public interface DatabaseClient<T> {

    /**
     * Create a new object in the database.
     *
     * @param dataset the object to be inserted.
     * @return the newly created dataset if available.
     */
    Optional<T> createDataset(@NonNull T dataset);

    /**
     * Find an existing object in the database.
     *
     * @param datasetId ID of the dataset to be found.
     * @return the dataset object if it exists.
     */
    Optional<T> getDataset(long datasetId);

    /**
     * Get all the objects stored in the database.
     *
     * @return a set containing all the datasets in the database.
     */
    SortedSet<T> getAllDatasets();
}
