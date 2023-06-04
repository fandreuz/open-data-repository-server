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
public class Dataset {

    private final String uniqueId;
    private final Path localFileLocation;
}
