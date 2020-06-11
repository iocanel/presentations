
package java2days;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@ApplicationScoped
public class TwitterService {
  

    @ConfigProperty(name="twitter.filter")
    String filter;
    
    @Outgoing("out-tweet")
    public Publisher<String> tweets() {
        return Flowable.create(s->{
                final TwitterStream tw = new TwitterStreamFactory().getInstance();
                tw.addListener(new StatusAdapter() {
                        public void onStatus(Status status) {
                            System.out.println(status.getText());
                            s.onNext(status.getText());
                        }
                    });
                tw.filter(filter);

            }, BackpressureStrategy.DROP);

    }
}
