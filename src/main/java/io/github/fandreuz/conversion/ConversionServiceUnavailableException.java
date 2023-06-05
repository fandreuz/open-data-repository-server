package io.github.fandreuz.conversion;

import io.github.fandreuz.model.DatasetType;

/**
 * Thrown when there's {@link ConversionService} for the requested dataset type
 * {@link DatasetType}.
 *
 * @author fandreuz
 */
public class ConversionServiceUnavailableException extends RuntimeException {

   private static final long serialVersionUID = 0L;

   public ConversionServiceUnavailableException(DatasetType datasetType) {
      super("Conversion service not available for dataset type: " + datasetType);
   }
}
