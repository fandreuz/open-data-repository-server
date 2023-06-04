package io.github.fandreuz.conversion.impl;

import io.github.fandreuz.conversion.ConversionServiceException;
import java.nio.file.Path;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to run commands in {@link ProcessBuilder}.
 *
 * @author fandreuz
 */
@Slf4j
@AllArgsConstructor
@NonNull
final class ProcessRunner {

    private final String binDirectory;

    /**
     * Run the given command redirecting stderr to the given filename in the current working directory.
     *
     * @param command command to be run.
     * @param stderrFilename file where stderr should be redirected.
     * @return process stdout content.
     */
    String runCommand(@NonNull String command, @NonNull String stderrFilename) {
        log.info("Running command '{}'", command);
        Path processStderr = Path.of(".").resolve(stderrFilename);

        Process process;
        try {
            var builder = new ProcessBuilder("sh", "-c", command) //
                    .redirectError(ProcessBuilder.Redirect.appendTo(processStderr.toFile()));
            // Set the location of the executables
            log.info("bin is {}", binDirectory);
            builder.environment().put("PATH", binDirectory);
            log.info("env is {}", builder.environment());

            process = builder.start();
        } catch (Exception exception) {
            String msg = String.format("An exception occurred while starting the process for command '%s'", command);
            throw new ConversionServiceException(msg, exception);
        }

        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException exception) {
            String msg = String.format("The command was interrupted: '%s'", command);
            throw new ConversionServiceException(msg, exception);
        }

        String stdout;
        try {
            stdout = new String(process.getInputStream().readAllBytes());
        } catch (Exception exception) {
            String msg =
                    String.format("An exception occurred while reading process output for the command '%s'", command);
            throw new ConversionServiceException(msg, exception);
        }

        if (exitCode != 0) {
            String msg =
                    String.format("Exit code for the command '%s' is %s (output: '%s')", command, exitCode, stdout);
            throw new ConversionServiceException(msg);
        }
        log.info("Command '{}' ran successfully", command);

        return stdout;
    }
}
