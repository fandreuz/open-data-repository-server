package io.github.fandreuz.model;

import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@NonNull
@Getter
@ToString
public class DatasetCoordinates {

    private final String id;
    private final Path localFileLocation;
}
