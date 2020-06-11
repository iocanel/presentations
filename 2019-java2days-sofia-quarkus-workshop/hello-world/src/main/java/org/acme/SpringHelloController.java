
package org.acme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spring")
public class SpringHelloController {

  @Autowired
  SpringDataGreetingService service;

    @GetMapping("/hello")
    public String hello() {
      return service.greet();
    }
}
