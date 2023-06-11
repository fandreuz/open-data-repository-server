package io.github.fandreuz.open.data.server.conversion;

/**
 * Thrown when an exception occurred during the conversion of a file.
 *
 * @author fandreuz
 */
public class ConversionServiceException extends RuntimeException {

   private static final long serialVersionUID = 0L;

   public ConversionServiceException(String message) {
      super(message);
   }

   public ConversionServiceException(String message, Throwable cause) {
      super(message, cause);
   }
}
