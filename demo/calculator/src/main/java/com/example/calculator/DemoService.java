package com.example.calculator;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by iocanel on 6/20/17.
 */
@Service
public class DemoService {

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
    },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "12"),
                    @HystrixProperty(name = "maxQueueSize", value = "8")
            })
    public Double distance(Double x1, Double x2,  Double y1,  Double y2) {
       Double dx = restTemplate.getForEntity("http://diff/eval/"+x2+"/"+x1+"/",Double.class).getBody();
       Double dy = restTemplate.getForEntity("http://diff/eval/"+y2+"/"+y1+"/",Double.class).getBody();

       Double x = restTemplate.getForEntity("http://pow/eval/"+dx+"/2/",Double.class).getBody();
       Double y = restTemplate.getForEntity("http://pow/eval/"+dy+"/2/",Double.class).getBody();

       Double sum = restTemplate.getForEntity("http://sum/eval/"+x+"/"+y+"/",Double.class).getBody();
       return restTemplate.getForEntity("http://sqrt/eval/"+sum+"/",Double.class).getBody();
    }
}
