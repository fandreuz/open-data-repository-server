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

   public static final String ROOT_PROCESS_RUNNER_BEAN_NAME = "rootProcessRunner";
   private static final String ROOT_BIN_DIRECTORY_ENV_PROPERTY = "root.executables.path";

   @Produces
   @Named(RootProcessRunnerConfiguration.ROOT_PROCESS_RUNNER_BEAN_NAME)
   ProcessRunner getProcessRunner() {
      return new ProcessRunner(System.getProperty(ROOT_BIN_DIRECTORY_ENV_PROPERTY));
   }
}
