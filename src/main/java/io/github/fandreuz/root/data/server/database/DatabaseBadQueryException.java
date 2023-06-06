package io.github.fandreuz.root.data.server.database;

/**
 * Thrown when a query could not be parsed.
 *
 * @author fandreuz
 */
public class DatabaseBadQueryException extends DatabaseException {

   private static final long serialVersionUID = 0L;

   public DatabaseBadQueryException(String message) {
      super(message);
   }

   public DatabaseBadQueryException(String message, Throwable cause) {
      super(message, cause);
   }
}
