package io.github.fandreuz.root.data.server.controller;

import io.github.fandreuz.root.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.root.data.server.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.SortedSet;

/**
 * Dataset metadata resource controller.
 *
 * @author fandreuz
 */
@Path("/v1/metadata")
public final class DatasetMetadataResource {

   @Inject
   private InputValidationService inputValidationService;

   @Inject
   private DatasetService datasetService;

   @Tag(name = "getMetadata", description = "Get a metadata matching the given ID")
   @GET
   @Path("{id}")
   public DatasetMetadata get( //
         @PathParam("id") @NotBlank String id //
   ) {
      return datasetService.getMetadata(id);
   }

   @Tag(name = "listMetadata", description = "List all metadata objects stored in the database")
   @GET
   public SortedSet<DatasetMetadata> list() {
      return datasetService.getAllMetadata();
   }
}
