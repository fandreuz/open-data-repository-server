package io.github.fandreuz.root.data.server;

import io.quarkus.runtime.Application;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class QuarkusApplication extends Application {

   protected QuarkusApplication() {
      super(false);
   }

   @Override
   protected void doStart(String[] args) {
      log.info(getName() + " starting...");
   }

   @Override
   protected void doStop() {
      log.info(getName() + " stopping...");
   }

   @Override
   public String getName() {
      return "root-data-server";
   }
}
