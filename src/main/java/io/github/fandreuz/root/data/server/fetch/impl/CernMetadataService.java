package io.github.fandreuz.root.data.server.fetch.impl;

import io.github.fandreuz.root.data.server.fetch.FileTypeNotRecognizedException;
import io.github.fandreuz.root.data.server.fetch.MetadataService;
import io.github.fandreuz.root.data.server.model.DatasetMetadata;
import io.github.fandreuz.root.data.server.model.DatasetType;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.NonNull;

/**
 * Implementation of {@link MetadataService} for CERN Open data.
 *
 * @author fandreuz
 */
@Singleton
final class CernMetadataService implements MetadataService {

   // Uniform Resource Name (URN)
   // schema:namespace:name
   private static final String UID_PATTERN = "cern-open-data:%s:%s";

   @Override
   public DatasetMetadata buildMetadata(@NonNull String collectionId, @NonNull String fileName) {
      String uid = makeDatasetUid(collectionId, fileName);
      DatasetType type = findDatasetType(fileName);
      return DatasetMetadata.builder() //
            .id(uid) //
            .collectionName(collectionId) //
            .fileName(fileName) //
            .type(type) //
            .importTimestamp(Instant.now().toEpochMilli()) //
            .build();
   }

   // cern-open-data:19090:experimentData
   private static String makeDatasetUid(@NonNull String id, @NonNull String fileName) {
      return String.format(UID_PATTERN, id, fileNameWithoutExtension(fileName));
   }

   private static String fileNameWithoutExtension(@NonNull String fileName) {
      int lastDotIndex = fileName.lastIndexOf('.');
      return lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : fileName;
   }

   private static DatasetType findDatasetType(@NonNull String fileName) {
      String fileExtension = Utils.extractExtension(fileName);
      for (DatasetType datasetType : DatasetType.values()) {
         if (fileExtension.equals(datasetType.getExtension())) {
            return datasetType;
         }
      }
      throw new FileTypeNotRecognizedException(fileName);
   }
}
