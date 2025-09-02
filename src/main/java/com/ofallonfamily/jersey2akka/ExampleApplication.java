package com.ofallonfamily.jersey2akka;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.annotation.PreDestroy;
import jakarta.ws.rs.ApplicationPath;
import java.util.concurrent.TimeUnit;

@ApplicationPath("examples")
public class ExampleApplication extends ResourceConfig {

    private ActorSystem system;

    public ExampleApplication() {

        system = ActorSystem.create("ExampleSystem");
        system.actorOf(DoublingActor.mkProps().withRouter(new RoundRobinPool(5)), "doublingRouter");

        register(new AbstractBinder() {
            protected void configure() {
                bind(system).to(ActorSystem.class);
            }
        });

        packages("com.ofallonfamily.jersey2akka");

    }

    @PreDestroy
    private void shutdown() {
        system.terminate();
        try {
            system.getWhenTerminated().toCompletableFuture().get(15, TimeUnit.SECONDS);
        } catch (Exception e) {
            // Ignore timeout exceptions
        }
    }

}