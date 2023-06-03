package io.github.fandreuz.fetch;

import io.github.fandreuz.model.DatasetMetadata;
import lombok.NonNull;

/**
 * Interface for services providing metadata for in-memory remote datasets.
 *
 * @author fandreuz
 */
public interface MetadataService {

    /**
     * Build metadata for the given dataset.
     *
     * @param collectionId dataset collection ID.
     * @param fileName dataset fileName name.
     * @param fileContent content of the dataset.
     * @return metadata for the given dataset.
     */
    DatasetMetadata buildMetadata(@NonNull String collectionId, @NonNull String fileName, @NonNull String fileContent);
}
