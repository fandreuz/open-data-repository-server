package io.github.fandreuz.open.data.server.controller.validation;

import io.github.fandreuz.open.data.server.controller.DatasetLocator;
import jakarta.validation.ConstraintValidator;

/**
 * Interface for services providing input validation.
 *
 * @author fandreuz
 */
public interface InputValidationService extends ConstraintValidator<ValidDatasetLocator, DatasetLocator> {

   // Marker
}
