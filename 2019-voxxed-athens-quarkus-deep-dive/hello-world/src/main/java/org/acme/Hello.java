package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

import java.util.concurrent.CompletionStage;

@Path("/hello")
public class Hello {

    @Inject
    GreetingService greeting;
    
    @Inject
    StreaminGreetingService stream;

    @Inject
    PgGreetingService pg;

    @Inject
    JpaGreetingService jpa;
    
    @Inject
    PanacheGreetingService panache;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return greeting.greet();
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> stream() {
        return stream.greet();
    }

    @GET
    @Path("/pg")
    @Produces(MediaType.TEXT_PLAIN)
    public CompletionStage<String> pg() {
        return pg.greet();
    }
    @GET
    @Path("/jpa")
    @Produces(MediaType.TEXT_PLAIN)
    public String jpa() {
        return jpa.greet();
    }
    @GET
    @Path("/panache")
    @Produces(MediaType.TEXT_PLAIN)
    public String panache() {
        return panache.greet();
    }
}
