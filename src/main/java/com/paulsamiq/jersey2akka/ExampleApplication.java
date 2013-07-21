package com.paulsamiq.jersey2akka;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.core.Application;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.internal.inject.Injections;

import scala.concurrent.duration.Duration;

import akka.actor.ActorSystem;
import akka.routing.RoundRobinRouter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class ExampleApplication extends Application {
	
	private ActorSystem system;
		
	@Inject
	public ExampleApplication(ServiceLocator serviceLocator) {
		
		system = ActorSystem.create("ExampleSystem");
		system.actorOf(DoublingActor.mkProps().withRouter(new RoundRobinRouter(5)),"doublingRouter");
		
		DynamicConfiguration dc = Injections.getConfiguration(serviceLocator);
		Injections.addBinding(Injections.newBinder(system).to(ActorSystem.class), dc);
		dc.commit();
		
	}
	
	@PreDestroy
	private void shutdown() {
		system.shutdown();
		system.awaitTermination(Duration.create(15, TimeUnit.SECONDS));
	}
	
	@Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(ExampleService.class);
        return s;
    }
	
	@Override
	public Set<Object> getSingletons()
	{
		Set<Object> s = new HashSet<>();
		
		// Add this (w/ corresponding POM changes) to get "pretty printed" JSON
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		s.add(new JacksonJsonProvider(mapper));

		return s;
	}
	
}