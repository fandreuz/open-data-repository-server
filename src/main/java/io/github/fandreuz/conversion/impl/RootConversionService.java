package io.github.fandreuz.conversion.impl;

import io.github.fandreuz.conversion.ConversionService;
import jakarta.inject.Singleton;
import java.nio.file.Path;

/**
 * Implementation of {@link ConversionService} for ROOT files.
 *
 * @author fandreuz
 */
@Singleton
public class RootConversionService implements ConversionService {

    @Override
    public Path convert(Path source) {
        return null;
    }
}
