package io.github.fandreuz.fetch.impl;

import io.github.fandreuz.fetch.MetadataService;
import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetType;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.NonNull;

/**
 * Implementation of {@link MetadataService} for CERN Open data.
 *
 * @author fandreuz
 */
@Singleton
final class CernMetadataService implements MetadataService {

    private static final String UID_PATTERN = "%s-%s";

    @Override
    public DatasetMetadata buildMetadata(@NonNull String collectionId, @NonNull String fileName) {
        String uid = makeDatasetUid(collectionId, fileName);
        DatasetType type = Utils.extractExtension(fileName) //
                .map(String::toUpperCase) //
                .map(DatasetType::valueOf) //
                .orElseThrow();
        return DatasetMetadata.builder() //
                .id(uid) //
                .collectionName(collectionId) //
                .fileName(fileName) //
                .type(type) //
                .importTimestamp(Instant.now().toEpochMilli()) //
                .build();
    }

    private static String makeDatasetUid(@NonNull String id, @NonNull String file) {
        return String.format(UID_PATTERN, id, file);
    }
}
