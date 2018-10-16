package com.jbcnconf.func.sum;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Created by iocanel on 6/12/17.
 */
@Service
public class SumService {

	@Autowired
	private Evaluator evaluator;

	@HystrixCommand(commandProperties = {
		@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
	},
	threadPoolProperties = {
		@HystrixProperty(name = "coreSize", value = "12"),
		@HystrixProperty(name = "maxQueueSize", value = "8")
	})
	public Double sum(String x, String y) {
		return evaluator.eval(x) + evaluator.eval(y);
	}
}
