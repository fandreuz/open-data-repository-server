package io.github.fandreuz.open.data.server.fetch;

import lombok.NonNull;

/**
 * Interface for providers of URLs.
 *
 * @author fandreuz
 */
public interface UrlBuilderService {

   /**
    * Get the URL to the given collection.
    *
    * @param collectionName
    *            collection name.
    * @return URL to the collection.
    */
   String getCollectionUrl(@NonNull String collectionName);

   /**
    * Get the URL to the given dataset file.
    *
    * @param collectionName
    *            collection name.
    * @param fileName
    *            file name.
    * @return URL to the file.
    */
   String getFileUrl(@NonNull String collectionName, @NonNull String fileName);
}
