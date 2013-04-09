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
	public boolean joinGame(int playerId, int gameId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateBoards(ChessBoard[] boards) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int createGame() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getGames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean gameIsActive(int gameId) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getName(int playerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getPlayers(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getCurrentTeam(int playerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addNewPlayer(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

}
