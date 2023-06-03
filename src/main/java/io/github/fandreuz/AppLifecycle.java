package io.github.fandreuz;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class AppLifecycle {

    void onStart(@Observes StartupEvent event) {
        log.info("root-data-server starting...");
    }

    void onStop(@Observes ShutdownEvent event) {
        log.info("root-data-server stopping...");
    }
}
