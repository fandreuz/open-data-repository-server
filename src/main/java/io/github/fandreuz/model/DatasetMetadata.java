package io.github.fandreuz.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Object representation of stored ROOT datasets.
 *
 * @author fandreuz
 */
@Getter
@ToString
@Slf4j
public class Dataset implements Comparable<Dataset> {

    private long id;
    private String name;

    @Override
    public int compareTo(@NonNull Dataset dataset) {
        return getName().compareTo(dataset.getName());
    }
}
