package io.github.fandreuz.fetch;

import io.github.fandreuz.model.DatasetMetadata;
import java.nio.file.Path;
import org.apache.commons.lang3.tuple.Pair;

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
    * @return a pair of metadata and the object representation of the dataset.
    */
   Pair<DatasetMetadata, Path> fetchDataset(String collectionId, String file);
}
