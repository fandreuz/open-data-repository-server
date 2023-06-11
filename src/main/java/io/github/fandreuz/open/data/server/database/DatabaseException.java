package io.github.fandreuz.open.data.server.database;

/**
 * Marks an exception occurred during a database operation.
 *
 * @author fandreuz
 */
public class DatabaseException extends RuntimeException {

   private static final long serialVersionUID = 0L;

   public DatabaseException(String message) {
      super(message);
   }

   public DatabaseException(String message, Throwable cause) {
      super(message, cause);
   }
}
