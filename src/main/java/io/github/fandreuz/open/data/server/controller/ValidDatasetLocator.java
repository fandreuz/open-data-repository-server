package io.github.fandreuz.open.data.server.controller;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to validate {@link DatasetLocator}.
 *
 * @author fandreuz
 */
@Constraint(validatedBy = InputValidationService.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ValidDatasetLocator {

   String message() default "{io.github.fandreuz.root.data.server.controller.ValidDatasetLocator.message}";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};
}
