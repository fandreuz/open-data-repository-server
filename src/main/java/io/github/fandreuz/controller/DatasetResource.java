package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatabaseClient;
import io.github.fandreuz.model.Dataset;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.SortedSet;

@Path("/dataset")
public class DatasetResource {

    @Inject
    private DatabaseClient databaseClient;

    @GET
    public SortedSet<Dataset> list() {
        return databaseClient.getAllDatasets();
    }

    @POST
    public Dataset create(Dataset dataset) {
        return databaseClient.createDataset(dataset).orElseThrow();
    }
}
