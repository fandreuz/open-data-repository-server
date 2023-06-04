package io.github.fandreuz;

import io.github.fandreuz.fetch.FetchException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Implementation of {@link ExceptionMapper} for {@link FetchException}.
 *
 * @author fandreuz
 */
@Provider
public class FetchExceptionMapper implements ExceptionMapper<FetchException> {

    @Override
    public Response toResponse(FetchException exception) {
        return Response.status(Response.Status.BAD_REQUEST) //
                .entity(exception.getMessage()) //
                .build();
    }
}
