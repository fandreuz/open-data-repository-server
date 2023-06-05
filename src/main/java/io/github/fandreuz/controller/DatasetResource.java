package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * Dataset resource controller.
 *
 * @author fandreuz
 */
@Path("/")
public final class DatasetResource {

   @Inject
   private InputValidationService inputValidationService;

   @Inject
   private DatasetService datasetService;

   @PUT
   public DatasetMetadata create( //
         @Valid @ValidDatasetLocator DatasetLocator datasetLocator //
   ) {
      return datasetService.createDataset(datasetLocator.getCollectionId(), datasetLocator.getFileName());
   }

   @GET
   @Path("/{id}/column-names")
   public SortedSet<String> getColumnNames( //
         @PathParam("id") @NotBlank String id //
   ) {
      return datasetService.getColumnNames(id);
   }

   @GET
   @Path("/{id}")
   public SortedMap<String, String> getColumn( //
         @PathParam("id") @NotBlank String id, //
         @NotBlank String columnName //
   ) {
      return datasetService.getColumn(id, columnName);
   }

   // TODO Endpoints for basic search & operations
}
