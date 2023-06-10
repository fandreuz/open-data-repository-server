package io.github.fandreuz.root.data.server.fetch.impl;

import io.github.fandreuz.root.data.server.fetch.DatasetFetchService;
import io.github.fandreuz.root.data.server.model.dataset.DatasetMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.nio.file.Path;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Implementation of {@link DatasetFetchService} for CERN Open data.
 *
 * @author fandreuz
 */
@Singleton
public final class CernDatasetFetchService implements DatasetFetchService {

   private static final String BASE_URL_FILE_PATTERN = "https://opendata.cern.ch/record/%s/files/%s";

   @Inject
   private CernMetadataService metadataService;

   @Inject
   private DownloadService downloadService;

   @Override
   public Pair<DatasetMetadata, Path> fetchDataset(String collectionId, String file) {
      String url = String.format(BASE_URL_FILE_PATTERN, collectionId, file);
      Path localDatasetFile = downloadService.download(url);
      DatasetMetadata metadata = metadataService.buildMetadata(collectionId, localDatasetFile);
      return Pair.of(metadata, localDatasetFile);
   }
}
