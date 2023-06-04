package io.github.fandreuz;

import io.github.fandreuz.conversion.ConversionServiceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for {@link io.github.fandreuz.conversion.ConversionServiceException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class ConversionServiceExceptionMapper implements ExceptionMapper<ConversionServiceException> {

    @Override
    public Response toResponse(ConversionServiceException exception) {
        log.error("ConversionServiceException caught", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR) //
                .entity(exception.getMessage()) //
                .build();
    }
}
