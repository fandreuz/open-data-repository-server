package io.github.fandreuz;

import io.github.fandreuz.database.DatabaseNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ExceptionMapper} for
 * {@link DatabaseNotFoundException}.
 *
 * @author fandreuz
 */
@Provider
@Slf4j
public class DatabaseNotFoundExceptionMapper implements ExceptionMapper<DatabaseNotFoundException> {

   @Override
   public Response toResponse(DatabaseNotFoundException exception) {
      log.error("DatabaseNotFoundException caught", exception);
      return Response.status(Response.Status.NOT_FOUND) //
            .entity(exception.getMessage()) //
            .build();
   }
}
