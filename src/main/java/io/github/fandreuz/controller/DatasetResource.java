package io.github.fandreuz.controller;

import io.github.fandreuz.model.Dataset;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.SortedSet;

/**
 * Dataset resource controller.
 *
 * @author fandreuz
 */
@Path("/dataset")
public final class DatasetResource {

    @Inject
    private DatasetService datasetService;

    @POST
    public Dataset create(Dataset dataset) {
        return datasetService.createDataset(dataset);
    }

    @GET
    public Dataset get(long id) {
        return datasetService.getDataset(id);
    }

    @GET
    public SortedSet<Dataset> list() {
        return datasetService.getAllDatasets();
    }
}
