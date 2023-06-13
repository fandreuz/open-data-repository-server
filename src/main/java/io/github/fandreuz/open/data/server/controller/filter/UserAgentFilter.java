package io.github.fandreuz.open.data.server.controller.filter;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

/**
 * Declares a filter which extracts the User-Agent from incoming HTTP requests
 * and puts it into a thread local.
 *
 * @author fandreuz
 */
@Singleton
@Slf4j
public class UserAgentFilter {

   @Inject
   private UserAgentContainer userAgentContainer;

   @ServerRequestFilter(preMatching = true)
   public void preMatchingFilter(HttpHeaders httpHeaders) {
      log.info("Extracting User-Agent");
      userAgentContainer.setUserAgent(httpHeaders.getHeaderString("User-Agent"));
      log.info("User-Agent: {}", userAgentContainer.getUserAgent());
   }

}
