package io.github.fandreuz.fetch.impl;

import java.util.Optional;
import lombok.NonNull;

/**
 * Utility class for remote dataset services.
 *
 * @author fandreuz
 */
final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Instances not allowed");
    }

    /**
     * Extract the file extension from the given file name.
     *
     * @param fileName file name.
     * @return the file extension if available.
     */
    static Optional<String> extractExtension(@NonNull String fileName) {
        if (!fileName.contains(".")) {
            return Optional.empty();
        }
        return Optional.of(fileName.substring(fileName.lastIndexOf(".") + 1)) //
                .map(String::toLowerCase);
    }
}
