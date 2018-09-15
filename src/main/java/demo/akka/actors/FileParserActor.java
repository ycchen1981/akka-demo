package demo.akka.actors;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import demo.akka.messages.AggregatorMessage;
import demo.akka.messages.FileParserMessage;
import demo.akka.messages.FileScannerMessage;
import demo.akka.messages.FileParserMessage.State;

public class FileParserActor extends AbstractActor {
	private ActorRef sender;
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
		.match(FileScannerMessage.class, m -> FileScannerMessage.State.PARSE.equals(m.getState()), m -> {
			final ActorRef aggregator = getContext().actorOf(Props.create(AggregatorActor.class), "aggregator");
			String fileName = m.getFile().getName();
			
			try (Stream<String> lines = Files.lines(Paths.get(m.getFile().getAbsolutePath()))) {
				aggregator.tell(new FileParserMessage(m.getTraceId(), State.START_OF_FILE, null, fileName), self());
				lines.forEach(l -> {
					aggregator.tell(new FileParserMessage(m.getTraceId(), State.LINE, l, fileName), self());
				});
				aggregator.tell(new FileParserMessage(m.getTraceId(), State.END_OF_FILE, null, fileName), self());
			}
			
			this.sender = getSender();
		})
		.match(AggregatorMessage.class, m -> AggregatorMessage.State.DONE.equals(m.getState()) , m -> {
			sender.tell(new FileParserMessage(m.getTraceId(), FileParserMessage.State.DONE, null, m.getFileName()), self());
		})
		.matchAny(this::unhandled)
		.build();
	}
}
