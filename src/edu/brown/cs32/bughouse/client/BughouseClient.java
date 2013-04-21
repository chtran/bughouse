package edu.brown.cs32.bughouse.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.interfaces.Client;

public class BughouseClient implements Client {
	private BughouseBackEnd backend;
	private ClientSocket socket;
	public BughouseClient(String host, int port, BughouseBackEnd backend) throws UnknownHostException, IllegalArgumentException, IOException {
		this.socket = new ClientSocket(host,port);
		this.backend = backend;
	}
	@Override
	public int createGame(int userId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("createGame\t%s\n",userId));
		int gameId = Integer.parseInt(response);
		return gameId;
	}

	@Override
	public List<Integer> getGames() throws IOException, RequestTimedOutException {
		String response = socket.getResponse("getGames\n");
		String[] lines = response.split("\n");
		List<Integer> toReturn = new ArrayList<Integer>();
		for (String line: lines) {
			int gameId = Integer.parseInt(line);
			toReturn.add(gameId);
		}
		return toReturn;
	}

	@Override
	public boolean gameIsActive(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("gameIsActive\t%d\n",gameId));
		return (response.trim().equals("true"));
	}

	@Override
	public List<Integer> getPlayers(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("getPlayers\t%d\n",gameId));
		String[] lines = response.split("\n");
		List<Integer> toReturn = new ArrayList<Integer>();
		for (String line: lines) {
			int userId = Integer.parseInt(line);
			toReturn.add(userId);
		}
		return toReturn;
	}

	@Override
	public int getOwnerId(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("getOwnerId\t%d\n",gameId));
		int ownerId = Integer.parseInt(response);
		return ownerId;
	}


	@Override
	public List<Integer> getBoards(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("getBoards\t%d\n",gameId));
		String[] lines = response.split("\n");
		List<Integer> toReturn = new ArrayList<Integer>();
		for (String line: lines) {
			int boardId = Integer.parseInt(line);
			toReturn.add(boardId);
		}
		return toReturn;
	}


	@Override
	public void startGame(int gameId) throws IOException, RequestTimedOutException, GameNotReadyException {
		String response = socket.getResponse(String.format("startGame\t%d\n",gameId));
		if (response.trim().equals("Not ready.")) {
			throw new GameNotReadyException();
		}
		
	}

	@Override
	public int addNewPlayer(String name) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("addNewPlayer\t%s\n",name));
		int playerId = Integer.parseInt(response.trim());
		return playerId;
	}

	@Override
	public String getName(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("getName\t%d\n",playerId));
		String name = response.trim();
		return name;
	}

	@Override
	public boolean isWhite(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("isWhite\t%d\n",playerId));
		return (response.trim().equals("true"));
	}

	@Override
	public int getCurrentTeam(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("getCurrentTeam\t%d\n",playerId));
		int team = Integer.parseInt(response);
		return team;
	}

	@Override
	public void joinGame(int playerId, int gameId, int team) throws TeamFullException, IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("joinGame\t%d\t%d\t%d\n",playerId,gameId,team));
		if (response.trim().equals("Full")) {
			throw new TeamFullException();
		}
	}

	@Override
	public boolean getBoardId(int playerId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void broadcastMove(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void quit(int playerId) {
		// TODO Auto-generated method stub
		
	}

}
