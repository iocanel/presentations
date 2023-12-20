package org.acme;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleGreetingService implements GreetingService {

    @ConfigProperty(name = "message")
    String message;

  @Override
  public String greet() {
    return message;
  }
}
