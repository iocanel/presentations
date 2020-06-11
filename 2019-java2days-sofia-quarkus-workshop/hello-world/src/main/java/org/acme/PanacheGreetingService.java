
package org.acme;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PanacheGreetingService {
  
    String greet() {
        PanacheGreeting pg = PanacheGreeting.findById(4L);
        return pg.greeting;
    }
}
