package io.github.fandreuz.root.data.server.controller.impl;

import io.github.fandreuz.root.data.server.controller.DatasetLocator;
import io.github.fandreuz.root.data.server.controller.InputValidationService;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementation of {@link InputValidationService} for CERN open data.
 *
 * @author fandreuz
 */
@Singleton
class CernInputValidationService implements InputValidationService {

   @Override
   public boolean isValid(DatasetLocator value, ConstraintValidatorContext context) {
      // Paths are not allowed
      return !value.getFileName().contains("/");
   }
}
