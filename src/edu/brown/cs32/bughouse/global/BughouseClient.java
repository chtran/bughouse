package edu.brown.cs32.bughouse.global;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.models.ChessBoard;

public class BughouseClient implements Client{
	private Socket sock;
	
	public BughouseClient(String host, int port) throws UnknownHostException, IOException {
		this.sock = new Socket(host,port);
	}
	
	@Override
	public boolean joinGame(int gameId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createGame(int gameId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Integer> getPlayers(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getGames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBoards(ChessBoard[] boards) {
		// TODO Auto-generated method stub
		
	}

}
