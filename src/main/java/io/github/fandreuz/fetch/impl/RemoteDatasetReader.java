package io.github.fandreuz.fetch.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to read the content of remote files.
 *
 * @author fandreuz
 */
@Slf4j
public class RemoteDatasetReader {

    /**
     * Try to read the file at {@code fileUrl}.
     *
     * @param fileUrl url to the file to be read.
     * @return the file content if available.
     */
    static Optional<String> read(@NonNull String fileUrl) {
        URL url;
        try {
            url = new URL(fileUrl);
        } catch (Exception exception) {
            log.error("An error occurred while parsing the given URL: '{}'", fileUrl, exception);
            return Optional.empty();
        }

        try (InputStream inputStream = url.openStream()) {
            return Optional.of(new String(inputStream.readAllBytes()));
        } catch (Exception exception) {
            log.error("An error occurred while reading the file at '{}'", fileUrl, exception);
            return Optional.empty();
        }
    }
}
