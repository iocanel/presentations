
package org.acme;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;

import org.reactivestreams.Publisher;
import io.reactivex.Flowable;

@ApplicationScoped
public class StreaminGreetingService {
  
    String[] GREETINGS = new String[]{"Hola!", "Hi!", "Ciao!", "Bounjour!"};

    Publisher<String> greet() {
        Random random = new Random();
        return Flowable.interval(5, TimeUnit.SECONDS).map(t->GREETINGS[random.nextInt(GREETINGS.length)]);
    }
}
