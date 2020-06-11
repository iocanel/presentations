package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.reactivestreams.Publisher;

import io.smallrye.reactive.messaging.annotations.Stream;

@Path("/hello")
public class Hello {

    @Inject
    @Stream("in-tweet")
    Publisher<String> tweets;
    
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Publisher<String> hello() {
        return tweets;
    }
}
