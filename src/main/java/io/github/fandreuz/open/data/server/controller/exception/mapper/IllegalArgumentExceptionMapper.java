package io.github.fandreuz.open.data.server.controller.exception.mapper;

import io.github.fandreuz.open.data.server.fetch.FetchException;
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
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {

   @Override
   public Response toResponse(IllegalArgumentException exception) {
      log.error("IllegalArgumentException caught", exception);
      return Response.status(Response.Status.BAD_REQUEST) //
            .entity(exception.getMessage()) //
            .build();
   }
}
