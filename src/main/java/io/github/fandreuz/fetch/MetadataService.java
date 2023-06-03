package io.github.fandreuz.fetch;

import io.github.fandreuz.model.DatasetMetadata;

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
     * @param file dataset file name.
     * @param fileContent content of the dataset.
     * @return metadata for the given dataset.
     */
    DatasetMetadata buildMetadata(String collectionId, String file, String fileContent);
}
