package io.github.fandreuz.open.data.server.controller.validation.impl;

import io.github.fandreuz.open.data.server.controller.DatasetLocator;
import io.github.fandreuz.open.data.server.controller.validation.InputValidationService;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementation of {@link InputValidationService}.
 *
 * @author fandreuz
 */
@Singleton
class InputValidationServiceImpl implements InputValidationService {

   @Override
   public boolean isValid(DatasetLocator value, ConstraintValidatorContext context) {
      // Paths are not allowed
      return !value.getFileName().contains("/");
   }
}
