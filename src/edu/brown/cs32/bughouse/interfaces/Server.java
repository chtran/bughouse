package edu.brown.cs32.bughouse.interfaces;

import java.net.Socket;

public interface Server {
	boolean sendMessage(Socket s, long messgae);
	
	void addPlayer(int playerId, int gameId);
	
	void notifyTurn(Socket s);
	
	void sendGameList(Socket s);
	
	void sendPlayerList(Socket s, int gameId);
	
	void sendBoard(Socket s, int gameId);
}
