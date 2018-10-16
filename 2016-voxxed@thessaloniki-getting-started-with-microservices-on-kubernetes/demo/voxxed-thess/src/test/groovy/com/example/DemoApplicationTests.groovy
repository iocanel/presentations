package com.example

import okhttp3.OkHttpClient
import okhttp3.Request
import org.arquillian.cube.kubernetes.annotations.Named
import org.arquillian.cube.kubernetes.annotations.PortForward
import org.jboss.arquillian.junit.Arquillian
import org.jboss.arquillian.test.api.ArquillianResource
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith;


@RunWith(Arquillian)
class DemoApplicationTests {

	@ArquillianResource
	@PortForward
	@Named("voxxed-thess")
	URL url;

	@Test
	void contextLoads() {
		OkHttpClient c = new OkHttpClient();
		Request r = new Request.Builder().get().url(url).build();
		String s = c.newCall(r).execute().body().string();
		Assert.assertEquals("Hello Thessaloniki using Spring!", s);
	}

}
