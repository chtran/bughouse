package edu.brown.cs32.bughouse.interfaces;

import java.net.Socket;

public interface Server {
	boolean sendMessage(Socket s, long messgae);
	
	void addPlayerToGame(int playerId, int gameId, int teamNum);
	
	void startGame(int gameId);
	
	void notifyTurn(Socket s);
	
	void sendGameList(Socket s);
	
	void sendPlayerList(Socket s, int gameId);
	
	void sendBoard(Socket s, int gameId);

	void addGame(int ownerId);

	void addPlayer(String name);
}
