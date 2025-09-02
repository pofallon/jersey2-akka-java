package com.ofallonfamily.jersey2akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class DoublingActor extends AbstractActor {

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props mkProps() {
        return Props.create(DoublingActor.class);
    }

    @Override
    public void preStart() {
        log.debug("starting");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
            .match(Integer.class, message -> {
                log.debug("received message: " + message);
                getSender().tell(message * 2, getSelf());
            })
            .matchAny(this::unhandled)
            .build();
    }

}