package io.github.fandreuz.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * DTO for dataset locations.
 *
 * @author fandreuz
 */
@AllArgsConstructor
@Getter
@NonNull
public class DatasetLocator {

    private String collectionId;
    private String fileName;

    public DatasetLocator() {
        // Required by the serialization layer
    }
}
