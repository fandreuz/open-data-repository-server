package io.github.fandreuz.model;

import java.util.Comparator;
import lombok.AllArgsConstructor;
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
@NonNull
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DatasetMetadata implements Comparable<DatasetMetadata> {

    private static final Comparator<DatasetMetadata> COMPARATOR = Comparator //
            .comparing(DatasetMetadata::getCollectionName) //
            .thenComparing(DatasetMetadata::getFileName);

    private String id;
    private String collectionName;
    private String fileName;
    private DatasetType type;
    private long importTimestamp;

    public DatasetMetadata() {
        // Required by the serialization layer
    }

    @Override
    public int compareTo(@NonNull DatasetMetadata dataset) {
        return COMPARATOR.compare(this, dataset);
    }
}
