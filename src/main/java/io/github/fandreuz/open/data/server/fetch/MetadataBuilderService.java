package io.github.fandreuz.open.data.server.fetch;

import lombok.NonNull;

import java.nio.file.Path;

/**
 * Interface for services to generate metadata for datasets.
 *
 * @param <M>
 *            metadata type.
 * @author fandreuz
 */
public interface MetadataBuilderService<M> {

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
