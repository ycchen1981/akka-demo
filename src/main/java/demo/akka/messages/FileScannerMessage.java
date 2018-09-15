package demo.akka.messages;

import java.io.File;
import java.util.UUID;

public class FileScannerMessage {
	public static enum State {
		PARSE;
	}
	
	private UUID traceId;
	private State state;
	private File file;
	
	public FileScannerMessage() {}
	public FileScannerMessage(UUID traceId, State state, File file) {
		this.traceId = traceId;
		this.state = state;
		this.file = file;
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
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
}
