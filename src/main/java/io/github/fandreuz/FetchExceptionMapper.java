package io.github.fandreuz;

import io.github.fandreuz.fetch.FetchException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for {@link FetchException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class FetchExceptionMapper implements ExceptionMapper<FetchException> {

    @Override
    public Response toResponse(FetchException exception) {
        log.error("FetchException caught", exception);
        return Response.status(Response.Status.BAD_REQUEST) //
                .entity(exception.getMessage()) //
                .build();
    }
}
