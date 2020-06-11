
package org.acme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringDataGreetingService {

  @Autowired
  GreetingRepository greetingRepository;


  String greet() {
    return greetingRepository.findById(1L).map(g -> g.getGreeting()).orElseThrow(() -> new IllegalStateException("No greeting found!"));
  }
}
