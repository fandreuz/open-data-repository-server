package io.github.fandreuz.root.data.server.model.dataset;

import java.util.Comparator;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Stored dataset metadata.
 *
 * @author fandreuz
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class DatasetMetadata implements Comparable<DatasetMetadata> {

   private static final Comparator<DatasetMetadata> COMPARATOR = Comparator.comparing(DatasetMetadata::getId);

   @NonNull
   private String id;

   @NonNull
   private String fileName;
   @NonNull
   private DatasetType type;
   private long sizeInBytes;

   @Nullable
   private Integer numberOfColumns;
   @Nullable
   private String commaSeparatedColumnNames;

   private long importTimestamp;

   public DatasetMetadata() {
      // Required by the serialization layer
   }

   @Override
   public int compareTo(@NonNull DatasetMetadata dataset) {
      return COMPARATOR.compare(this, dataset);
   }
}
