package io.github.fandreuz.fetch.impl;

import io.github.fandreuz.fetch.DatasetFetchService;
import io.github.fandreuz.model.Dataset;
import io.github.fandreuz.model.DatasetMetadata;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Implementation of {@link DatasetFetchService} for CERN Open data.
 *
 * @author fandreuz
 */
@Singleton
public final class CernDatasetFetchService implements DatasetFetchService {

    private static final String BASE_URL_FILE_PATTERN = "https://opendata.cern.ch/record/%s/files/%s";

    @Inject
    private CernMetadataService metadataService;

    @Override
    public Pair<DatasetMetadata, Dataset> fetchDataset(String id, String file) {
        String url = String.format(BASE_URL_FILE_PATTERN, id, file);
        String fileContent = RemoteDatasetReader.read(url).orElseThrow();

        DatasetMetadata metadata = metadataService.buildMetadata(id, file, fileContent);
        Dataset dataset = Dataset.fromFileContent(metadata.getId(), fileContent);
        return Pair.of(metadata, dataset);
    }
}
