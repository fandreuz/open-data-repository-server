package io.github.fandreuz.open.data.server.fetch;

import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import java.nio.file.Path;

import org.apache.commons.lang3.tuple.Triple;

/**
 * Interface for services which can fetch a remote dataset.
 *
 * <p>
 * Typically, implementations will already know some base URL to infer the
 * global location of the dataset.
 *
 * @author fandreuz
 */
public interface DatasetFetchService {

   /**
    * Fetch a dataset based on the ID of the collection it belongs to, and the name
    * of the file where the dataset is stored.
    *
    * @param collectionId
    *            ID of the collection.
    * @param file
    *            local file where the dataset is stored.
    * @return a triple of metadata and the object representation of the dataset.
    */
   Triple<CollectionMetadata, DatasetMetadata, Path> fetchDataset(String collectionId, String file);
}