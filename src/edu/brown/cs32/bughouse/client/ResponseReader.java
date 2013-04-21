package edu.brown.cs32.bughouse.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;

/**
 * A wrapper class around PipedInputStream and PipedOutputStream
 * Used in Client for foreground stream and background stream
 * @author chtran
 *
 */
public class ResponseReader {
	private BufferedReader input;
	private PrintWriter output;
	private PipedInputStream inputStream;
	private OutputStream outputStream;
	
	public ResponseReader() throws IOException {
		inputStream = new PipedInputStream();
		this.input = new BufferedReader(new InputStreamReader(inputStream));
		outputStream = new PipedOutputStream(inputStream);
		this.output = new PrintWriter(outputStream);
	}
	
	public void send(String message) {
		output.println(message);
		output.flush();
	}
	
	public String readLine() throws IOException {
		return input.readLine();
	}
	public void close() throws IOException {
		input.close();
		inputStream.close();
		output.close();
		outputStream.close();
	}
}
