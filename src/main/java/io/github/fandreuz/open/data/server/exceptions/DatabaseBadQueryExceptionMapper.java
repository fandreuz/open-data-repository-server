package io.github.fandreuz.open.data.server.exceptions;

import io.github.fandreuz.open.data.server.database.DatabaseBadQueryException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for
 * {@link DatabaseBadQueryException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class DatabaseBadQueryExceptionMapper implements ExceptionMapper<DatabaseBadQueryException> {

   @Override
   public Response toResponse(DatabaseBadQueryException exception) {
      log.error("DatabaseBadQueryException caught", exception);
      return Response.status(Response.Status.NOT_FOUND) //
            .entity(exception.getMessage()) //
            .build();
   }
}
