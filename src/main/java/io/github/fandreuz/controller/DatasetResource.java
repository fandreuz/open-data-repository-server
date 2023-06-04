package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;

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

    // TODO Endpoints for basic search & operations
}
