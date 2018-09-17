package demo.akka.actors;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import akka.actor.AbstractActor;
import demo.akka.messages.AggregatorMessage;
import demo.akka.messages.FileParserMessage;

public class AggregatorActor extends AbstractActor {
	private Map<String, Integer> reducedMap = new ConcurrentHashMap<String, Integer>();
	private volatile int inputLineCnt = 0, processedLineCnt = 0;
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
		.match(FileParserMessage.class, m -> FileParserMessage.State.START_OF_FILE.equals(m.getState()), this::unhandled)
		.match(FileParserMessage.class, m -> FileParserMessage.State.END_OF_FILE.equals(m.getState()), m -> {
			while (inputLineCnt != processedLineCnt) {
				Thread.sleep(10);
			}
			
			String result = reducedMap.entrySet().stream()
					.sorted(
							Map.Entry.<String, Integer>comparingByValue()
							.reversed()
							.thenComparing(Map.Entry.comparingByKey()))
					.map(kv -> String.format("%s = %d", kv.getKey(), kv.getValue()))
					.collect(Collectors.joining("\n\t", "\t", ""));
			
			System.out.println(String.format("The file name: %s, results =>\n%s",
					m.getFileName(),
					result));
			
			getSender().tell(new AggregatorMessage(m.getTraceId(), AggregatorMessage.State.DONE, null, m.getFileName()), self());
		})
		.match(FileParserMessage.class, m -> FileParserMessage.State.LINE.equals(m.getState()), m -> {
			inputLineCnt ++;
			//split words and reduce
			StringTokenizer words = new StringTokenizer(m.getMessage());
			while (words.hasMoreTokens()) {
				String word = words.nextToken().toLowerCase();
				
				word = word.replaceAll("^([^a-zA-Z0-9_-]*)(.*?)([^a-zA-Z0-9_-]*)$", "$2")
						.replaceAll("^(.*?)('s*)$", "$1");
				
				if (!word.matches(".*?[\\w\\d]+.*?")) {
					continue;
				}
				
				if (reducedMap.containsKey(word)) {
					Integer value = (Integer) reducedMap.get(word);
					value++;
					reducedMap.put(word, value);
				} else {
					reducedMap.put(word, Integer.valueOf(1));
				}
			}
			
			processedLineCnt ++;
		})
		.matchAny(this::unhandled)
		.build();
	}
}
