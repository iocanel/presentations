
package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class GreetingService {
  
    @ConfigProperty(name="message")
    String message;


    String greet() {
        return message;
    }
    
}
