
package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class JpaGreetingService {
  
    @Inject
    EntityManager em;

    String greet() {
        return em.find(Greeting.class, 3L).getGreeting();
    }
}
