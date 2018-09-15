package demo.akka.messages;

import java.util.UUID;

public class AggregatorMessage {
	public static enum State {
		DONE;
	}
	
	private UUID traceId;
	private State state;
	private String message;
	private String fileName;
	
	public AggregatorMessage() {}
	public AggregatorMessage(UUID traceId, State state, String message, String fileName) {
		this.traceId = traceId;
		this.state = state;
		this.message = message;
		this.fileName = fileName;
	}
	
	public UUID getTraceId() {
		return traceId;
	}
	public void setTraceId(UUID traceId) {
		this.traceId = traceId;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
