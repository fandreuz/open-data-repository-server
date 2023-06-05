package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
    public DatasetMetadata create(DatasetLocator datasetLocator) {
        inputValidationService.validateInput(datasetLocator);
        return datasetService.createDataset(datasetLocator.getCollectionId(), datasetLocator.getFileName());
    }

    @GET
    @Path("/{id}/column-names")
    public SortedSet<String> getColumnNames(@PathParam("id") String id) {
        return datasetService.getColumnNames(id);
    }

    // TODO Endpoints for basic search & operations
}
