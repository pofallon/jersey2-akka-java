package com.ofallonfamily.jersey2akka;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class DoublerTest extends JerseyTest {

    protected Application configure() {
        return new ExampleApplication();
    }

    protected void configureClient(ClientConfig clientConfig) {
        // Jersey 3.x handles Jackson automatically
    }

    @Test
    public void testWithTwo() {

        HashMap<String,Integer> map = target("doubler").path("2")
                .request().get(new GenericType<HashMap<String, Integer>>() {});

        assertEquals(new Integer(4), map.get("results"));

    }

}
