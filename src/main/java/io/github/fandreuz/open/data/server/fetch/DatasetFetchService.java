package io.github.fandreuz.open.data.server.fetch;

import io.github.fandreuz.open.data.server.model.collection.CollectionMetadata;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Triple;

import java.nio.file.Path;

/**
 * Fetch a remote dataset.
 *
 * @author fandreuz
 */
public class DatasetFetchService {

   @Inject
   private MetadataBuilderService<DatasetMetadata> datasetMetadataService;
   @Inject
   private MetadataBuilderService<CollectionMetadata> collectionMetadataService;
   @Inject
   private UrlBuilderService urlBuilderService;

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
   public Triple<CollectionMetadata, DatasetMetadata, Path> fetchDataset(String collectionId, String file) {
      // Download the collection reference page to build metadata
      String collectionUrl = urlBuilderService.getCollectionUrl(collectionId);
      Path localCollectionFile = DownloadUtils.download(collectionUrl);
      CollectionMetadata collectionMetadata = collectionMetadataService.buildMetadata(collectionId,
            localCollectionFile);

      // Download the dataset file
      String fileUrl = urlBuilderService.getFileUrl(collectionId, file);
      Path localDatasetFile = DownloadUtils.download(fileUrl);
      DatasetMetadata datasetMetadata = datasetMetadataService.buildMetadata(collectionId, localDatasetFile);

      return Triple.of(collectionMetadata, datasetMetadata, localDatasetFile);
   }
}
