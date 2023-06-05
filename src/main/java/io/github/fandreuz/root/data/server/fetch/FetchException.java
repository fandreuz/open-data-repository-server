package io.github.fandreuz.root.data.server.fetch;

/**
 * Marks an exception which occurred while fetching a remote dataset.
 *
 * @author fandreuz
 */
public class FetchException extends RuntimeException {

   private static final long serialVersionUID = 0L;

   public FetchException(String message) {
      super(message);
   }

   public FetchException(String message, Throwable cause) {
      super(message, cause);
   }
}
