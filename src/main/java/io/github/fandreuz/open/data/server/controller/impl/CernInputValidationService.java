package io.github.fandreuz.open.data.server.controller.impl;

import io.github.fandreuz.open.data.server.controller.DatasetLocator;
import io.github.fandreuz.open.data.server.controller.InputValidationService;
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
