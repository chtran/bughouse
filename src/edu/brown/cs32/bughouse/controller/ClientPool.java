package edu.brown.cs32.bughouse.controller;

import java.io.IOException;
import java.util.*;
/**
 * A group of {@link BughouseClientHandler}s representing a "chat room".
 */
public class ClientPool {
	private LinkedList<BughouseClientHandler> m_clients;
	private Map<Integer, BughouseClientHandler> m_clientMap;
	
	/**
	 * Initialize a new {@link ClientPool}.
	 */
	public ClientPool() {
		m_clients = new LinkedList<BughouseClientHandler>();
		m_clientMap = new HashMap<Integer, BughouseClientHandler>();
	}
	
	/**
	 * Add a new client to the chat room.
	 * 
	 * @param client to add
	 */
	public synchronized void add(BughouseClientHandler client) {
		m_clients.add(client);
	}
	
	public synchronized void addToMap(int playerID, BughouseClientHandler client) {
		m_clientMap.put(playerID, client);
	}
	
	/**
	 * Remove a client from the pool. Only do this if you intend to clean up
	 * that client later.
	 * 
	 * @param client to remove
	 * @return true if the client was removed, false if they were not there.
	 */
	public synchronized boolean remove(BughouseClientHandler client) {
		return m_clients.remove(client);
	}
	
	/**
	 * Send a message to clients in the pool, but the sender.
	 * 
	 * @param message to send
	 * @param sender the client _not_ to send the message to (send to everyone
	 *          if null)
	 */
	public synchronized void broadcast(String message, BughouseClientHandler sender) {
		for (BughouseClientHandler client : m_clients) {
			if (sender != null && sender == client) {
				continue;
			}

			client.send(message);
		}
	}
	
	/**
	 * Sends message about move to players in given game
	 * @param gameID
	 * @param msg
	 */
	public synchronized void broadcastToGame(int gameID, String msg, BughouseClientHandler sender) {
		for (BughouseClientHandler client : m_clients) {
			// only broadcast message to clients in given game
			if (client.getGameId() == gameID && sender != null) {
				System.out.println("Sending " + msg + " to " + client.getName());
				client.send(msg);
			}
		}
	}
	
	/**
	 * Sends message notifying next player that it's their turn
	 * @param playerID
	 */
	public synchronized void sendToPlayer(int playerID, String msg) {
		BughouseClientHandler client = m_clientMap.get(playerID);
		System.out.println("Sending " + msg + " to " + client.getName());
		client.send(msg);
	}
	
	/**
	 * Close all {@link BughouseClientHandler}s and empty the pool
	 */
	public synchronized void killall() {
		this.broadcast("The server is quitting now. Goodbye.", null);

		for (BughouseClientHandler client : m_clients) {
			try {
				client.kill();
			} catch (IOException e) {
				// There's nothing we can do here.
			}
		}

		m_clients.clear();
	}
	
	/* TEST CODE: functions used for BughouseServerTest only */
	
	/**
	 * Checks if clients connected
	 * @return true if clients exist, false if not
	 */
	public boolean hasClients() {
		if (m_clients.isEmpty())
			return false;
		else
			return true;
	}
}

