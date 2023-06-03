package io.github.fandreuz.fetch;

import io.github.fandreuz.model.Dataset;
import io.github.fandreuz.model.DatasetMetadata;
import org.apache.commons.lang3.tuple.Pair;

public interface DatasetFetchService {

    Pair<DatasetMetadata, Dataset> fetchDataset(String id, String file);
}
