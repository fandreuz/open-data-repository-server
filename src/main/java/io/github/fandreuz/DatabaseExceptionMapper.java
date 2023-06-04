package io.github.fandreuz;

import io.github.fandreuz.database.DatabaseException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Implementation of {@link ExceptionMapper} for {@link DatabaseException}.
 *
 * @author fandreuz
 */
@Provider
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

    @Override
    public Response toResponse(DatabaseException exception) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) //
                .entity(exception.getMessage()) //
                .build();
    }
}
