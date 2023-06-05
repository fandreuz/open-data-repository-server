package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.SortedSet;

/**
 * Dataset metadata resource controller.
 *
 * @author fandreuz
 */
@Path("/metadata")
public final class DatasetMetadataResource {

   @Inject
   private InputValidationService inputValidationService;

   @Inject
   private DatasetService datasetService;

   @GET
   @Path("{id}")
   public DatasetMetadata get( //
         @PathParam("id") @NotBlank String id //
   ) {
      return datasetService.getMetadata(id);
   }

   @GET
   public SortedSet<DatasetMetadata> list() {
      return datasetService.getAllMetadata();
   }
}
