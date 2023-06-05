package io.github.fandreuz.root.data.server;

import io.github.fandreuz.root.data.server.model.ConcurrentOperationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for
 * {@link ConcurrentOperationException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class ConcurrentOperationExceptionMapper implements ExceptionMapper<ConcurrentOperationException> {

   @Override
   public Response toResponse(ConcurrentOperationException exception) {
      log.error("ConcurrentOperationException caught", exception);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR) //
            .entity(exception.getMessage()) //
            .build();
   }
}
