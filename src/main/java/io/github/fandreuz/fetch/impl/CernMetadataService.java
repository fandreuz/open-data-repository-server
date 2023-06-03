package io.github.fandreuz.fetch.impl;

import io.github.fandreuz.fetch.MetadataService;
import io.github.fandreuz.model.DatasetMetadata;
import jakarta.inject.Singleton;

/**
 * Implementation of {@link MetadataService} for CERN Open data.
 *
 * @author fandreuz
 */
@Singleton
final class CernMetadataService implements MetadataService {

    private static final String UID_PATTERN = "%s-%s";

    @Override
    public DatasetMetadata buildMetadata(String collectionId, String file, String fileContent) {
        String uid = makeDatasetUid(collectionId, file);
        return null;
    }

    private static String makeDatasetUid(String id, String file) {
        return String.format(UID_PATTERN, id, file);
    }
}
