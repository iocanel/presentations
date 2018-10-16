package com.jbcnconf.func.diff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class EvalEndpoint {

    @Autowired
    DiffService service;

    @RequestMapping("/eval/{x}/{y}")
    public Double eval(@PathVariable("x") String x, @PathVariable("y") String y) {
        return service.diff(x , y);
    }
}
