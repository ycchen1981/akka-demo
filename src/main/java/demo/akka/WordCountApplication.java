package demo.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import demo.akka.actors.FileScannerActor;
import demo.akka.actors.Terminator;
import demo.akka.actors.FileScannerActor.Action;

public class WordCountApplication {
	public static void main(String[] args) throws Exception {
		ActorSystem system = ActorSystem.create("WordCount");
		ActorRef scanner = system.actorOf(Props.create(FileScannerActor.class), "fileScanner");
		scanner.tell(Action.SCAN, ActorRef.noSender());
		system.actorOf(Props.create(Terminator.class, scanner), "terminator");
	}
}
