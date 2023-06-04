package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.SortedSet;

/**
 * Dataset resource controller.
 *
 * @author fandreuz
 */
@Path("/metadata")
public final class DatasetMetadataResource {

    @Inject
    private InputValidationService inputValidationService;

    @Inject
    private DatasetService datasetService;

    @POST
    public DatasetMetadata create(DatasetLocator datasetLocator) {
        inputValidationService.validateInput(datasetLocator);
        return datasetService.createDataset(datasetLocator.getCollectionId(), datasetLocator.getFileName());
    }

    @GET
    @Path("{id}")
    public DatasetMetadata get(@PathParam("id") String id) {
        return datasetService.getMetadata(id);
    }

    @GET
    public SortedSet<DatasetMetadata> list() {
        return datasetService.getAllMetadata();
    }
}
