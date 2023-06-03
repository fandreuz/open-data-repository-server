package io.github.fandreuz.model;

import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@NonNull
@Getter
public class Dataset {

    private final String uniqueId;
    private final Path localFileLocation;
}
