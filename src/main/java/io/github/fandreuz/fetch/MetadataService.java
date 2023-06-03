package io.github.fandreuz.fetch;

import io.github.fandreuz.model.DatasetMetadata;

public interface MetadataService {

    DatasetMetadata buildMetadata(String id, String file, String fileContent);
}
