package io.github.fandreuz.controller.impl;

import io.github.fandreuz.controller.DatasetLocator;
import io.github.fandreuz.controller.InputValidationService;
import jakarta.inject.Singleton;

/**
 * Implementation of {@link InputValidationService} for CERN open data.
 *
 * @author fandreuz
 */
@Singleton
public class CernInputValidationService implements InputValidationService {

   @Override
   public void validateInput(DatasetLocator datasetLocator) {
      try {
         Long.parseLong(datasetLocator.getCollectionId());
      } catch (NumberFormatException exception) {
         throw new IllegalArgumentException("The given collection ID is not a valid integer", exception);
      }

      if (datasetLocator.getFileName().contains("/")) {
         throw new IllegalArgumentException("The given file name is not a plain file name");
      }
   }
}
