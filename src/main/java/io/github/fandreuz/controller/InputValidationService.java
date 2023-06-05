package io.github.fandreuz.controller;

/**
 * Interface for services providing input validation.
 *
 * <p>
 * Implementors are expected to throw {@link IllegalArgumentException} when the
 * provided input is not valid.
 *
 * @author fandreuz
 */
public interface InputValidationService {

   void validateInput(DatasetLocator datasetLocator);
}
