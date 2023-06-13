package io.github.fandreuz.open.data.server.controller.filter;

import jakarta.enterprise.context.RequestScoped;
import lombok.Getter;
import lombok.Setter;

@RequestScoped
@Getter
@Setter
public class UserAgentContainer {

   private volatile String userAgent;

}
