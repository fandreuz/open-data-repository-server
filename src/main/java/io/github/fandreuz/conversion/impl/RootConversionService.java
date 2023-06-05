package io.github.fandreuz.conversion.impl;

import io.github.fandreuz.conversion.ConversionService;
import io.github.fandreuz.conversion.ConversionServiceException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.nio.file.Path;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link ConversionService} for ROOT files.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
class RootConversionService implements ConversionService {

   private static final String ROOT_TREE_READER_COMMAND_PATTERN = "root-ls '%s'";
   private static final String ROOT_CONVERTER_COMMAND_PATTERN = "root2csv -f '%s' -o '%s' -t '%s'";
   private static final String ROOT_LS_ERR = "root-ls.err";
   private static final String ROOT_2_CSV_ERR = "root2csv.err";

   @Inject
   private ProcessRunner processRunner;

   @Override
   public synchronized Path convert(Path source) {
      String rootLsCommand = String.format(ROOT_TREE_READER_COMMAND_PATTERN, source);
      String rootLsOutput = processRunner.runCommand(rootLsCommand, ROOT_LS_ERR);
      log.info("root-ls output: {}", rootLsOutput);
      String rootTreeName = extractRootTreeName(rootLsOutput);
      log.info("ROOT tree name: {}", rootTreeName);
      String destName = getFileNameAsCsv(source);
      String root2CsvCommand = String.format(ROOT_CONVERTER_COMMAND_PATTERN, source, destName, rootTreeName);
      String root2CsvOutput = processRunner.runCommand(root2CsvCommand, ROOT_2_CSV_ERR);
      log.info("root2csv output: {}", root2CsvOutput);
      return Path.of(destName);
   }

   private static String extractRootTreeName(@NonNull String rootLsOutput) {
      // Typical root-ls output
      // === [1685895641024.root] ===
      // version: 53201
      // TTree events (cycle=1)
      var splitLines = rootLsOutput.split("\\n");
      if (splitLines.length < 2) {
         throw malformedRootLsOutputException(rootLsOutput);
      }

      var splitSpaces = splitLines[2] // Drop first 2 lines
            .split("\\s+");
      if (splitSpaces.length < 2) {
         throw malformedRootLsOutputException(rootLsOutput);
      }

      return splitSpaces[1];
   }

   private static ConversionServiceException malformedRootLsOutputException(String output) {
      String msg = String.format("The output of root-ls looks malformed, could not extract the root tree name: '%s'",
            output);
      return new ConversionServiceException(msg);
   }

   private static String getFileNameAsCsv(@NonNull Path path) {
      return path.toFile().getName().split("\\.")[0] + ".csv";
   }
}
