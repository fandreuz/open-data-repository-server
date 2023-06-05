package io.github.fandreuz.model;

/**
 * Marks an exception occurred while waiting for the completion of a concurrent
 * operation.
 *
 * @author fandreuz
 */
public class ConcurrentOperationException extends RuntimeException {

   private static final long serialVersionUID = 0L;

   public ConcurrentOperationException(String message) {
      super(message);
   }

   public ConcurrentOperationException(String message, Throwable cause) {
      super(message, cause);
   }
}
