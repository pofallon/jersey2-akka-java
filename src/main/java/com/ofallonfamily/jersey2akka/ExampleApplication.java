package com.ofallonfamily.jersey2akka;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinPool;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import scala.concurrent.duration.Duration;

import javax.annotation.PreDestroy;
import javax.ws.rs.ApplicationPath;
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

        register(new JacksonJsonProvider().
            configure(SerializationFeature.INDENT_OUTPUT, true));
		
	}
	
	@PreDestroy
	private void shutdown() {
		system.shutdown();
		system.awaitTermination(Duration.create(15, TimeUnit.SECONDS));
	}
	
}