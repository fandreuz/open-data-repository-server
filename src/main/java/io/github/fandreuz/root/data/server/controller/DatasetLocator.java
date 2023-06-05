package io.github.fandreuz.root.data.server.controller;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * DTO for dataset locations.
 *
 * @author fandreuz
 */
@AllArgsConstructor
@Getter
@NonNull
public class DatasetLocator {

   @NotBlank
   @Digits(integer = 10, fraction = 0)
   private String collectionId;

   @NotBlank
   private String fileName;

   public DatasetLocator() {
      // Required by the serialization layer
   }
}
