
package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/")
@RegisterRestClient(baseUri="http://hello-world-iocanel.195.201.87.126.nip.io")
public interface HelloRestClient {

    @GET
    @Path("/hello")
    @Produces("text/plain")
    String hello();

}
