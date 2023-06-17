package io.github.fandreuz.open.data.server.model.dataset;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * Dataset metadata database object.
 *
 * @author fandreuz
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class DatasetMetadataDO {

   @NonNull
   private String datasetId;
   @NonNull
   private String fileName;
   @NonNull
   private DatasetType type;
   private long sizeInBytes;
   @Nullable
   private Long numberOfColumns;
   @Nullable
   private String commaSeparatedColumnNames;
   private long importTimestamp;

   public DatasetMetadataDO() {
      // Required by the serialization layer
   }
}
