package io.github.fandreuz.root.data.server.fetch;

import io.github.fandreuz.root.data.server.model.DatasetMetadata;
import lombok.NonNull;

import java.nio.file.Path;

/**
 * Interface for services providing metadata for in-memory remote datasets.
 *
 * @author fandreuz
 */
public interface MetadataService {

   /**
    * Build metadata for the given dataset.
    *
    * @param collectionId
    *            dataset collection ID.
    * @param file
    *            path to the dataset.
    * @return metadata for the given dataset.
    */
   DatasetMetadata buildMetadata(@NonNull String collectionId, @NonNull Path file);
}
