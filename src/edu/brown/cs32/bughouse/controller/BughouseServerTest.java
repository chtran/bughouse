package edu.brown.cs32.bughouse.controller;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BughouseServerTest {
	// client stuff
	private Socket m_socket;
	private BufferedReader m_input;
	private PrintWriter m_output;
	
	private BughouseServer m_server;

	@Before
	public void setUp() throws Exception {
		m_server = new BughouseServer(3333);
		
		m_socket = new Socket("localhost", 3333);
		m_input = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
		m_output = new PrintWriter(m_socket.getOutputStream(), true);
	}

	@After
	public void tearDown() throws Exception {
		m_input.close();
		m_output.close();
	}
	
	@Test
	public void test() {
		send("ADD_PLAYER:Mackenzie Clark\n");
		String msg;
		int playerId = -1;
		int gameId = -1;
		
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		playerId = Integer.parseInt(headerSplit[1]);
	    		assertTrue(playerId >= 0);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		send("CREATE_GAME:" + playerId + "\n");
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		gameId = Integer.parseInt(headerSplit[1]);
	    		assertTrue(gameId >= 0);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		send("GET_GAMES: \n");
		// response GAMES:[gameId1]\n....
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		String[] content = headerSplit[1].split("\t");
	    		assertTrue(content.length == 1);
	    		assertTrue(Integer.parseInt(headerSplit[1]) == gameId);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		send("GAME_IS_ACTIVE:" + gameId + "\n");
		// response: GAME_ACTIVE:[gameId]\ttrue\n
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		String[] content = headerSplit[1].split("\t");
	    		assertTrue(content.length == 2);
	    		assertTrue(Integer.parseInt(content[0]) == gameId);
	    		assertTrue(content[1].compareTo("true") == 0);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		send("GET_PLAYERS:" + gameId + "\n");
		// response: PLAYERS:[gameId]\t[userId1]\n
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		String[] content = headerSplit[1].split("\t");
	    		assertTrue(content.length == 2);
	    		assertTrue(Integer.parseInt(content[0]) == gameId);
	    		assertTrue(Integer.parseInt(content[1]) == playerId);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		send("GET_OWNER:" + gameId + "\n");
		// response: GAME_OWNER:[gameId]\t[userId]\n
		try {
			if ((msg = m_input.readLine()) != null) {
	    		System.out.println(msg);
	    		String[] headerSplit = msg.split(":");
	    		assertTrue(headerSplit.length == 2);
	    		String[] content = headerSplit[1].split("\t");
	    		assertTrue(content.length == 2);
	    		assertTrue(Integer.parseInt(content[0]) == gameId);
	    		assertTrue(Integer.parseInt(content[1]) == playerId);
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * A method that sends a message to the server.
	 * 
	 * @param message that will be sent to the server for broadcasting.
	 */
	private void send(String message) {
		//TODO: Set up the methods, so it will send the message to the server
		m_output.println(message);
		m_output.flush();
	}
}
