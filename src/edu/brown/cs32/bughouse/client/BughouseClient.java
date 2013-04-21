package edu.brown.cs32.bughouse.client;

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

	@Override
	public int getOwnerId(int gameId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> createBoards(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getBoards(int gameId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWhite(int playerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoardId(int playerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int createChessBoard(int gameId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void startGame(int gameId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastMove(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		// TODO Auto-generated method stub
		
	}

}
