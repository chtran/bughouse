package edu.brown.cs32.bughouse.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.ui.BughouseGUI;


/**
 * The class that communicates with the Server to get map data
 * @author chtran
 *
 */
public class ClientSocket{
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private boolean running;
	private Thread receiving;
	private ResponseReader foreground; //The stream that reads the response from server when the client sends a request
	private ResponseReader background; //The stream that reads traffic data from server
	private Client client;
	public ClientSocket(String hostname, int port, Client client) throws UnknownHostException, IOException, 
	IllegalArgumentException {

		this.socket  = new Socket(hostname, port);
		this.client = client;
		this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.output = new PrintWriter(socket.getOutputStream());
		this.running = true;
		this.foreground = new ResponseReader();
		this.background = new ResponseReader();
	}
	
	public void run() throws IOException {
		//Create a new thread that receives everything from the server
		this.receiving = new ReceiveThread();
		receiving.start();
		
		//Create a new thread to handle the background stream (reading traffic input)
		Runnable backgroundHandler = new Runnable() {
			@Override
			public void run() {
				while(running) {
					String line;
					try {
						line = background.readLine();
						client.receive(line);
					} catch (IOException e) {
						System.out.println("ERROR: IO Error in traffic handling thread");
						e.printStackTrace();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (RequestTimedOutException e) {
						System.out.println("ERROR: Request timed out in traffic handling thread");
					}
				}
			}
		};
		
		new Thread(backgroundHandler).start();
	}
	
	/**
	 * Send the request to the server and get the response
	 * @param message: the request message
	 * @return: the response string
	 * @throws IOException
	 * @throws RequestTimedOutException
	 */
	public synchronized String getResponse(String message) throws IOException, RequestTimedOutException {
		//System.out.println("Request: [" + message + "]");

		output.print(message);
		output.flush();
		String response="";
		response = foreground.readLine();
		/*StringBuilder builder = new StringBuilder();
		while(!line.isEmpty()) {
			//System.out.println("line:" +line);
			builder.append(String.format("%s\n", line));
			line = foreground.readLine();
		}
		String response = new String(builder);*/
		//System.out.println("Response:"+response+".Size: "+response.length());

		if (response.equals("Request timed out\n")) {
			System.out.println("Server response timed out");
			throw new RequestTimedOutException();
		}
		return response;
	}
	
	public void kill() throws IOException {
		running = false;
		input.close();
		output.close();
		//foreground.close();
		//background.close();
		socket.close();

	}
	/**
	 * The thread that reads everything from the server and forwards the response to the corresponding stream.
	 * Lines starting with "traffic" will be forwarded to the background stream
	 * Other lines will be forwared to the foreground stream
	 * @author chtran
	 *
	 */
	private class ReceiveThread extends Thread {
        public void run() {
        	while(running) {
				try {
					String line = input.readLine();
	        		if (line!=null) {
	        			//If the first word in the line is traffic then forward it to the background stream
	        			if(line.split(":")[0].equals("BROADCAST")) {
	        				//System.out.println("Forwarding to background: "+line);
	        				background.send(line);
	        				//System.out.println("received TRAFFIC BOT data: [" + line + "]");
	        			} else {
	        				//System.out.println("Forwarding to foreground: "+line);
	        				foreground.send(line);
	        			}
	        		}
	        		if (line == null) {
	        			System.out.println("Server disconnected");
	        			JOptionPane.showMessageDialog(null, "Server disconnected. Press OK to close the program", "Server disconnected", JOptionPane.ERROR_MESSAGE);
	        			kill();
	        			System.exit(0);
	        		}
				} catch (IOException e) {
					e.printStackTrace();
					try {
						kill();
					} catch (IOException e1) {
						e1.printStackTrace();
						System.exit(0);
					}
				}
        	}
        }
    }
}
