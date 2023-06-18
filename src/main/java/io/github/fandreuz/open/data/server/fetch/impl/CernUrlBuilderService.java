package io.github.fandreuz.open.data.server.fetch.impl;

import io.github.fandreuz.open.data.server.fetch.UrlBuilderService;
import jakarta.inject.Singleton;
import lombok.NonNull;

/**
 * Implementation of {@link UrlBuilderService} for CERN Open Data Portal.
 *
 * @author fandreuz
 */
@Singleton
final class CernUrlBuilderService implements UrlBuilderService {

   private static final String COLLECTION_URL_PATTERN = "https://opendata.cern.ch/record/%s";
   private static final String BASE_URL_FILE_PATTERN = "https://opendata.cern.ch/record/%s/files/%s";

   @Override
   public String getCollectionUrl(@NonNull String collectionName) {
      return String.format(COLLECTION_URL_PATTERN, collectionName);
   }

   @Override
   public String getFileUrl(@NonNull String collectionName, @NonNull String fileName) {
      return String.format(BASE_URL_FILE_PATTERN, collectionName, fileName);
   }
}
