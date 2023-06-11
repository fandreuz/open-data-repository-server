package io.github.fandreuz.open.data.server.exceptions;

import io.github.fandreuz.open.data.server.database.DatabaseException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for {@link DatabaseException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

   @Override
   public Response toResponse(DatabaseException exception) {
      log.error("DatabaseException caught", exception);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR) //
            .entity(exception.getMessage()) //
            .build();
   }
}
