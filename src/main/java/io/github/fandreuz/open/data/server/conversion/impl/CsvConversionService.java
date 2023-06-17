package io.github.fandreuz.open.data.server.conversion.impl;

import io.github.fandreuz.open.data.server.conversion.ConversionService;
import jakarta.inject.Singleton;
import lombok.NonNull;

import java.nio.file.Path;

/**
 * Implementation of {@link ConversionService} for CSV files.
 *
 * @author fandreuz
 */
@Singleton
class CsvConversionService implements ConversionService {

   @Override
   public Path convert(@NonNull Path source) {
      // Path is not immutable
      return Path.of(source.toUri());
   }
}
