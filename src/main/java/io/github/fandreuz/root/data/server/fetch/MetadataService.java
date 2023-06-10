package io.github.fandreuz.root.data.server.fetch;

import lombok.NonNull;

import java.nio.file.Path;

/**
 * Interface for services providing metadata for in-memory remote datasets.
 *
 * @param <M>
 *            metadata type.
 * @author fandreuz
 */
public interface MetadataService<M> {

   /**
    * Build metadata for the given dataset.
    *
    * @param collectionId
    *            dataset collection ID.
    * @param file
    *            path to the dataset.
    * @return metadata for the given dataset.
    */
   M buildMetadata(@NonNull String collectionId, @NonNull Path file);
}
