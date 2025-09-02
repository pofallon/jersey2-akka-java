package com.ofallonfamily.jersey2akka;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import org.glassfish.jersey.server.ManagedAsync;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;

@Path("/doubler/{value}")
public class ExampleService {

    @Context ActorSystem actorSystem;
    LoggingAdapter log;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getExamples (
            @PathParam("value") Integer value,
            @Suspended final AsyncResponse res) {

        ActorSelection doublingActor = actorSystem.actorSelection("/user/doublingRouter");

        Timeout timeout = new Timeout(Duration.create(2, "seconds"));

        Future<Object> future = Patterns.ask(doublingActor, value, timeout);

        future.onComplete(new OnComplete<Object>() {

            public void onComplete(Throwable failure, Object result) {

                if (failure != null) {

                    if (failure.getMessage() != null) {
                        HashMap<String,String> response = new HashMap<String,String>();
                        response.put("error", failure.getMessage());
                        res.resume(Response.serverError().entity(response).build());
                    } else {
                        res.resume(Response.serverError());
                    }

                } else {

                    HashMap<String,Object> response = new HashMap<String,Object>();
                    response.put("results",result);
                    res.resume(Response.ok().entity(response).build());

                }

            }
        }, actorSystem.dispatcher());

    }

}
