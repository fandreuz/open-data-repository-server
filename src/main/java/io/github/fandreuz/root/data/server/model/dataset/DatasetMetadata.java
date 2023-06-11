package io.github.fandreuz.root.data.server.model.dataset;

import java.util.Comparator;

import io.github.fandreuz.root.data.server.model.collection.CollectionMetadata;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.bson.codecs.pojo.annotations.BsonIgnore;

/**
 * Stored dataset metadata.
 *
 * @author fandreuz
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DatasetMetadata implements Comparable<DatasetMetadata> {

   private static final Comparator<DatasetMetadata> COMPARATOR = Comparator.comparing(DatasetMetadata::getId);

   @NonNull
   private String id;

   @NonNull
   private String fileName;
   @NonNull
   private DatasetType type;
   private long sizeInBytes;

   // These are set when we have a CSV file.
   @Nullable
   private Long numberOfColumns;
   @Nullable
   private String commaSeparatedColumnNames;

   private long importTimestamp;

   @BsonIgnore
   @Nullable
   private CollectionMetadata collectionMetadata;

   public DatasetMetadata() {
      // Required by the serialization layer
   }

   @Override
   public int compareTo(@NonNull DatasetMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }

   /**
    * Attach the given collection metadata to the provided dataset metadata.
    *
    * @param datasetMetadata
    *            source dataset metadata.
    * @param collectionMetadata
    *            collection metadata to be attached.
    * @return a shallow copy of the given dataset metadata.
    */
   public static DatasetMetadata attachCollectionMetadata(DatasetMetadata datasetMetadata,
         CollectionMetadata collectionMetadata) {
      return DatasetMetadata.builder() //
            .id(datasetMetadata.getId()) //
            .fileName(datasetMetadata.getFileName()) //
            .type(datasetMetadata.getType()) //
            .sizeInBytes(datasetMetadata.getSizeInBytes()) //
            .numberOfColumns(datasetMetadata.getNumberOfColumns()) //
            .commaSeparatedColumnNames(datasetMetadata.getCommaSeparatedColumnNames()) //
            .importTimestamp(datasetMetadata.getImportTimestamp()) //
            .collectionMetadata(collectionMetadata) //
            .build();
   }

   /**
    * Attach the given CSV metadata to the provided dataset metadata.
    *
    * @param datasetMetadata
    *            source dataset metadata.
    * @param numberOfColumns
    *            number of columns in the dataset.
    * @param commaSeparatedColumnNames
    *            columns of the dataset.
    * @return a shallow copy of the given dataset metadata.
    */
   public static DatasetMetadata attachCsvMetadata(DatasetMetadata datasetMetadata, long numberOfColumns,
         @NonNull String commaSeparatedColumnNames) {
      return DatasetMetadata.builder() //
            .id(datasetMetadata.getId()) //
            .fileName(datasetMetadata.getFileName()) //
            .type(datasetMetadata.getType()) //
            .sizeInBytes(datasetMetadata.getSizeInBytes()) //
            .numberOfColumns(numberOfColumns) //
            .commaSeparatedColumnNames(commaSeparatedColumnNames) //
            .importTimestamp(datasetMetadata.getImportTimestamp()) //
            .collectionMetadata(datasetMetadata.getCollectionMetadata()) //
            .build();
   }
}
