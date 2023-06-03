package io.github.fandreuz.model;

import lombok.Getter;

/**
 * Object representation of stored ROOT datasets.
 *
 * @author fandreuz
 */
@Getter
public class Dataset implements Comparable<Dataset> {

    private long id;
    private String name;

    @Override
    public int compareTo(Dataset dataset) {
        return getName().compareTo(dataset.getName());
    }
}
