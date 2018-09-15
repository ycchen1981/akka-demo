package demo.akka.actors;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;

public class Terminator extends AbstractLoggingActor {

	private final ActorRef ref;

	public Terminator(ActorRef ref) {
		this.ref = ref;
		getContext().watch(ref);
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(Terminated.class, t -> {
			log().info("{} has terminated, shutting down system", ref.path());
			getContext().system().terminate();
		}).build();
	}
}
