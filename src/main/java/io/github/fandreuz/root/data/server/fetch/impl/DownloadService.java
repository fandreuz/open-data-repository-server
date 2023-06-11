package io.github.fandreuz.root.data.server.fetch.impl;

import io.github.fandreuz.root.data.server.fetch.FetchException;
import jakarta.inject.Singleton;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to read the content of remote files.
 *
 * @author fandreuz
 */
@Slf4j
@Singleton
final class DownloadService {

   /**
    * Try to read the file at {@code fileUrl}.
    *
    * @param fileUrl
    *            url to the file to be read.
    * @return the file content if available.
    */
   Path download(@NonNull String fileUrl) {
      log.info("Downloading URL '{}'", fileUrl);
      URL url;
      try {
         url = new URL(fileUrl);
      } catch (Exception exception) {
         throw new FetchException("An error occurred while parsing the URL", exception);
      }

      Path localFile = Path.of(generateUniqueFileName(fileUrl));
      log.info("Target file name for '{}': '{}'", fileUrl, localFile);
      try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(localFile.toFile());
            FileChannel fileChannel = fileOutputStream.getChannel() //
      ) {
         fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
      } catch (Exception exception) {
         try {
            Files.deleteIfExists(localFile);
            log.info("The file '{}' was removed", localFile);
         } catch (Exception deleteException) {
            log.warn("Could not delete the partially downloaded file '{}' (URL='{}')", localFile, fileUrl);
         }

         String msg = String.format("An error occurred while reading the file at '%s'", fileUrl);
         throw new FetchException(msg, exception);
      }

      log.info("Download completed: '{}'", fileUrl);
      return localFile;
   }

   private static String generateUniqueFileName(@NonNull String fileUrl) {
      int lastSlashIndex = fileUrl.lastIndexOf('/');
      String extension = Utils.extractExtension(fileUrl.substring(lastSlashIndex + 1));
      return Instant.now().toEpochMilli() + (extension.isBlank() ? "" : "." + extension);
   }
}
