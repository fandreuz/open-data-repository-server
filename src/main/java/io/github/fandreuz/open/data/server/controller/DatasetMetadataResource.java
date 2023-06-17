package io.github.fandreuz.open.data.server.controller;

import io.github.fandreuz.open.data.server.controller.validation.InputValidationService;
import io.github.fandreuz.open.data.server.model.MetadataService;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.NonNull;
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
   private MetadataService metadataService;

   @Tag(name = "getMetadata", description = "Get a metadata matching the given ID")
   @GET
   @Path("{id}")
   public Object get( //
         @PathParam("id") @NotBlank String id //
   ) {
      if (isDatasetId(id)) {
         return metadataService.getDatasetMetadata(id);
      } else {
         return metadataService.getCollectionMetadata(id);
      }
   }

   @Tag(name = "getEntriesMatching", description = "List the metadata entries matching the given query")
   @GET
   public SortedSet<?> getDatasetsMatching( //
         String query //
   ) {
      if (query == null || query.trim().isBlank()) {
         query = "{}";
      }
      var datasetMetadataEntries = metadataService.getDatasetMetadataMatching(query);
      return datasetMetadataEntries.isEmpty()
            ? metadataService.getCollectionMetadataMatching(query)
            : datasetMetadataEntries;
   }

   private boolean isDatasetId(@NonNull String id) {
      // The ID should contain two semicolons
      return id.replaceAll(":", "").length() == id.length() - 2;
   }
}
