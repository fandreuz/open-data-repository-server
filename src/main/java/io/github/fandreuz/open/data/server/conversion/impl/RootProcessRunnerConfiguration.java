package io.github.fandreuz.open.data.server.conversion.impl;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

/**
 * Configuration class for {@link ProcessRunner} for ROOT datasets.
 *
 * @author fandreuz
 */
@Singleton
class RootProcessRunnerConfiguration {

   private static final String ROOT_BIN_DIRECTORY_ENV_PROPERTY = "root.executables.path";

   @Produces
   @Named("root")
   ProcessRunner getProcessRunner() {
      return new ProcessRunner(System.getProperty(ROOT_BIN_DIRECTORY_ENV_PROPERTY));
   }
}
