package edu.brown.cs32.bughouse.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Server for communicating to clients playing games
 * @author mackenzie
 *
 */
public class BughouseServer {
	private ServerData m_data;
	private ServerSocket m_socket;
	private ClientPool m_clients;
	private boolean m_running;

	public BughouseServer(int port) throws UnknownHostException, IOException {
		m_socket = new ServerSocket(port);
		m_data = new ServerData();
		
		run();
	}
	
	public void run() throws IOException {
		m_running = true; 
		while(m_running) {
			try {
				Socket clientConnection = m_socket.accept();
				System.out.println("Connected to a client.");
				BughouseClientHandler ch = new BughouseClientHandler(m_clients, clientConnection, m_data);
				m_clients.add(ch);
				ch.start();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}	   
		}
	}
	
	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill() throws IOException {
		m_running = false;
		m_clients.killall();
		m_socket.close();
	}
	
	/* TEST CODE: functions used for BughouseServerTest only */
	
	/**
	 * Checks if client pool contains client connections
	 * @return true if yes, false if not
	 */
	public boolean hasClients() {
		return m_clients.hasClients();
	}
}
