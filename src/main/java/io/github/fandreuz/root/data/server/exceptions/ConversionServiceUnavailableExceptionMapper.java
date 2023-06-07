package io.github.fandreuz.root.data.server.exceptions;

import io.github.fandreuz.root.data.server.conversion.ConversionServiceUnavailableException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for
 * {@link ConversionServiceUnavailableException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class ConversionServiceUnavailableExceptionMapper
      implements
         ExceptionMapper<ConversionServiceUnavailableException> {

   @Override
   public Response toResponse(ConversionServiceUnavailableException exception) {
      log.error("ConversionServiceUnavailableException caught", exception);
      return Response.status(Response.Status.BAD_REQUEST) //
            .entity(exception.getMessage()) //
            .build();
   }
}
