package io.github.fandreuz.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Stored ROOT dataset metadata.
 *
 * @author fandreuz
 */
@Getter
@ToString
@Slf4j
public class DatasetMetadata implements Comparable<DatasetMetadata> {

    private String id;
    private String name;

    @Override
    public int compareTo(@NonNull DatasetMetadata dataset) {
        return getName().compareTo(dataset.getName());
    }
}
