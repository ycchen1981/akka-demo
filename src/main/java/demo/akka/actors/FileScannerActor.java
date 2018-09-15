package demo.akka.actors;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;

import com.fasterxml.uuid.Generators;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import demo.akka.messages.FileParserMessage;
import demo.akka.messages.FileScannerMessage;

public class FileScannerActor extends AbstractActor {
	public static enum Action {
		SCAN;
	}
	
	@SuppressWarnings("serial")
	public static Set<String> fileExts = Collections.unmodifiableSet(new HashSet<String>(){{
		add("log");
		add("txt");
	}});
	
	private int totalFileCnt = 0;
	private int processedFileCnt = 0;
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
		.matchEquals(Action.SCAN, m -> {
			Path target = Paths.get(System. getProperty("user.dir"));
			
			List<File> logFiles = Stream.of(target.toFile().listFiles())
					.filter(File::isFile)
					.filter(i -> fileExts.stream().filter(ext -> i.getName().endsWith("."+ext)).findAny().isPresent())
					.collect(Collectors.toList());
			
			if (CollectionUtils.isEmpty(logFiles)) {
				System.out.println(String.format("The target folder [%s] does not have files with the extension %s.",
						target.toString(),
						fileExts.stream().collect(Collectors.joining("' or '", "'", "'"))));
				
				getContext().stop(self());
				return;
			}
			
			totalFileCnt = logFiles.size();
			
			System.out.println(String.format("%d file(s) will be processed and displayed respectively.", totalFileCnt));
			
			for(File log : logFiles) {
				UUID traceId = Generators.timeBasedGenerator().generate();
				
				final ActorRef parser = getContext().actorOf(Props.create(FileParserActor.class), "fileParser-" + traceId);
				parser.tell(new FileScannerMessage(traceId, FileScannerMessage.State.PARSE, log), self());
			}
		})
		.match(FileParserMessage.class, m -> FileParserMessage.State.DONE.equals(m.getState()), m -> {
			processedFileCnt ++;
			if(processedFileCnt == totalFileCnt) {
				System.out.println(String.format("All %d file(s) were processed and displayed respectively.", totalFileCnt));
				getContext().stop(self());
			}
		})
		.matchAny(this::unhandled)
		.build();
	}
}
