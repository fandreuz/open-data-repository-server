package io.github.fandreuz.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@NonNull
@Getter
public class Dataset {

    private final String uniqueId;
    private final String fileCsvContent;
}
