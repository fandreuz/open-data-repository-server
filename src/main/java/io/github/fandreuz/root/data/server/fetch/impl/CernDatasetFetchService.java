package io.github.fandreuz.root.data.server.fetch.impl;

import io.github.fandreuz.root.data.server.fetch.DatasetFetchService;
import io.github.fandreuz.root.data.server.model.collection.CollectionMetadata;
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
final class CernDatasetFetchService implements DatasetFetchService {

   private static final String COLLECTION_URL_PATTERN = "https://opendata.cern.ch/record/%s";
   private static final String BASE_URL_FILE_PATTERN = "https://opendata.cern.ch/record/%s/files/%s";

   @Inject
   private CernDatasetMetadataService datasetMetadataService;
   @Inject
   private CernCollectionMetadataService collectionMetadataService;
   @Inject
   private DownloadService downloadService;

   @Override
   public Pair<DatasetMetadata, Path> fetchDataset(String collectionId, String file) {
      // Download the collection reference page to build metadata
      String collectionUrl = String.format(COLLECTION_URL_PATTERN, collectionId);
      Path localCollectionFile = downloadService.download(collectionUrl);
      CollectionMetadata collectionMetadata = collectionMetadataService.buildMetadata(collectionId,
            localCollectionFile);

      // Download the dataset file
      String fileUrl = String.format(BASE_URL_FILE_PATTERN, collectionId, file);
      Path localDatasetFile = downloadService.download(fileUrl);
      DatasetMetadata datasetMetadata = datasetMetadataService.buildMetadata(collectionId, localDatasetFile);

      return Pair.of(DatasetMetadata.attachCollectionMetadata(datasetMetadata, collectionMetadata), localDatasetFile);
   }
}
