package com.ofallonfamily.jersey2akka;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DoublerTest extends JerseyTest {

    protected Application configure() {
        return new ExampleApplication();
    }

    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(new JacksonJsonProvider());
    }

    @Test
    public void testWithTwo() {

        HashMap<String,Integer> map = target("doubler").path("2")
                .request().get(new GenericType<HashMap<String, Integer>>() {});

        assertEquals(new Integer(4), map.get("results"));

    }

}
