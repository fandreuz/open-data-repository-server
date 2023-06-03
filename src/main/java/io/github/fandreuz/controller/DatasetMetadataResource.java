package io.github.fandreuz.controller;

import io.github.fandreuz.model.DatasetMetadata;
import io.github.fandreuz.model.DatasetService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import java.util.SortedSet;
import lombok.NonNull;

/**
 * Dataset resource controller.
 *
 * @author fandreuz
 */
@Path("/metadata")
public final class DatasetMetadataResource {

    @Inject
    private DatasetService datasetService;

    @POST
    public DatasetMetadata create(String collectionId, String file) {
        return datasetService.createDataset(collectionId, file);
    }

    @GET
    @Path("{id}")
    public DatasetMetadata get(@PathParam("id") String id) {
        return datasetService.getMetadata(paramToId(id));
    }

    @GET
    public SortedSet<DatasetMetadata> list() {
        return datasetService.getAllMetadata();
    }

    private long paramToId(@NonNull String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception exception) {
            String msg = String.format("An error occurred while parsing the given ID: '%s'", id);
            throw new RuntimeException(msg, exception);
        }
    }
}
