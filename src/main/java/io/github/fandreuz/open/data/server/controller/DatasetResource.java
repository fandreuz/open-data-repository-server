package io.github.fandreuz.open.data.server.controller;

import io.github.fandreuz.open.data.server.controller.validation.InputValidationService;
import io.github.fandreuz.open.data.server.controller.validation.ValidDatasetLocator;
import io.github.fandreuz.open.data.server.model.dataset.DatasetMetadata;
import io.github.fandreuz.open.data.server.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;
import java.util.SortedMap;

/**
 * Dataset resource controller.
 *
 * @author fandreuz
 */
@Path("/v1")
public final class DatasetResource {

   @Inject
   private InputValidationService inputValidationService;

   @Inject
   private DatasetService datasetService;

   @Tag(name = "importDataset", description = "Import the given dataset to the database (idempotent)")
   @PUT
   public DatasetMetadata create( //
         @Valid @ValidDatasetLocator DatasetLocator datasetLocator //
   ) {
      return datasetService.createDataset(datasetLocator.getCollectionId(), datasetLocator.getFileName());
   }

   @Tag(name = "getColumn", description = "List the content of the given column")
   @GET
   @Path("/{id}/{columnName}")
   public SortedMap<String, String> getColumn( //
         @PathParam("id") @NotBlank String id, //
         @PathParam("columnName") @NotBlank String columnName //
   ) {
      return datasetService.getDatasetColumn(id, columnName);
   }

   @Tag(name = "getEntriesMatching", description = "List the entries matching the given query")
   @GET
   @Path("/{id}")
   public Set<SortedMap<String, String>> getEntriesMatching( //
         @PathParam("id") @NotBlank String id, //
         @NotBlank String query //
   ) {
      return datasetService.getDatasetEntriesMatching(id, query);
   }

}
