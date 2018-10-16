package com.example.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by iocanel on 6/20/17.
 */
@RestController
public class DemoController {

    @Autowired
    DemoService demoService;

    @RequestMapping("/eval/{x1}/{x2}/{y1}/{y2}")
    public String eval(@PathVariable("x1") Double x1, @PathVariable("x2") Double x2, @PathVariable("y1") Double y1, @PathVariable("y2") Double y2) {
        return "Hello Barcelona, The distance is: "+ demoService.distance(x1, x2, y1, y2);
    }
}

