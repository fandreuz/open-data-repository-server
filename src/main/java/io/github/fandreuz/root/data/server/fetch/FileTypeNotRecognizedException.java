package io.github.fandreuz.root.data.server.fetch;

import io.github.fandreuz.root.data.server.model.DatasetType;

/**
 * Thrown when the requested file has an extension which is not recognized
 * within those defined in {@link DatasetType}.
 *
 * @author fandreuz
 */
public class FileTypeNotRecognizedException extends FetchException {

   private static final long serialVersionUID = 0L;

   public FileTypeNotRecognizedException(String extension) {
      super(String.format("File type not recognized: '%s'", extension));
   }
}
