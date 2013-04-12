package edu.brown.cs32.bughouse.controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import edu.brown.cs32.bughouse.interfaces.Server;

/**
 * Server for communicating to clients playing games
 * @author mackenzie
 *
 */
public class BughouseServer implements Server{
	private ServerData m_data;
	private Socket m_sock;

	public BughouseServer(String host, int port) throws UnknownHostException, IOException {
		m_sock = new Socket(host,port);
		m_data = new ServerData();
	}

	@Override
	public boolean sendMessage(Socket s, long messgae) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void addPlayer(String name) {
		m_data.addPlayer(name);
	}

	@Override
	public void addPlayerToGame(int playerId, int gameId, int teamNum) {
		m_data.addPlayerToGame(playerId, gameId, teamNum);
	}
	
	@Override
	public void addGame(int ownerId) {
		m_data.addGame(ownerId);
	}

	@Override
	public void startGame(int gameId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyTurn(Socket s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendGameList(Socket s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendPlayerList(Socket s, int gameId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendBoard(Socket s, int gameId) {
		// TODO Auto-generated method stub
		
	}

}
