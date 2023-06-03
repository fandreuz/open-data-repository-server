package io.github.fandreuz.model;

import java.util.Comparator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Stored dataset metadata.
 *
 * @author fandreuz
 */
@Getter
@ToString
@Slf4j
@Builder
@NonNull
public class DatasetMetadata implements Comparable<DatasetMetadata> {

    private static final Comparator<DatasetMetadata> COMPARATOR = Comparator //
            .comparing(DatasetMetadata::getCollectionName) //
            .thenComparing(DatasetMetadata::getFileName);

    private String id;
    private String collectionName;
    private String fileName;
    private DatasetType type;
    private long importTimestamp;

    @Override
    public int compareTo(@NonNull DatasetMetadata dataset) {
        return COMPARATOR.compare(this, dataset);
    }
}
