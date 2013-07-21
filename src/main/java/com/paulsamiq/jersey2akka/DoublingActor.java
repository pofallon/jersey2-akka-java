package com.paulsamiq.jersey2akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class DoublingActor extends UntypedActor {
	
	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	public static Props mkProps() {
		return Props.create(DoublingActor.class);
	}
	
	@Override
	public void preStart() {
	    log.debug("starting");
	}

	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof Integer) {
			log.debug("received message: " + (Integer)message);
			getSender().tell((Integer)message*2, getSelf());
		} else {
			unhandled(message);
		}

	}

}
