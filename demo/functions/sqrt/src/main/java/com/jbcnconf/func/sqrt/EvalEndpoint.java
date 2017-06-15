package com.jbcnconf.func.sqrt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class EvalEndpoint {

    @Autowired
    SqrtService service;

    @RequestMapping("/eval/{x}")
    public Double eval(@PathVariable("x") String x) {
        return service.sqrt(x);
    }
}
