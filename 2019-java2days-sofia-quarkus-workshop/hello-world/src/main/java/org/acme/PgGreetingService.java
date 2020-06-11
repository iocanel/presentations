
package org.acme;

import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

//import io.reactiverse.axle.pgclient.PgPool;

@ApplicationScoped
public class PgGreetingService {
  
    //@Inject
    //PgPool client;

    CompletionStage<String> greet() {
        // We remove the reactive-pg-client to avoid issues and conflicts with non-reactive jdbc client and orm.
        //return client.query("select * from greeting where lang='it'").thenApply(rs->rs.iterator().next().getString("greeting"));
        return null;
    }
}
