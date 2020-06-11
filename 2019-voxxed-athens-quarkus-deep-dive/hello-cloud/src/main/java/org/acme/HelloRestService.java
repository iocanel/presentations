
package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HelloRestService {

    @Inject
    @RestClient
    HelloRestClient client;

    @Fallback(fallbackMethod="fallback")
    String greet() {
        return client.hello();
    }

    String fallback() {
        return "Hello (fallback)!";
    }
}
