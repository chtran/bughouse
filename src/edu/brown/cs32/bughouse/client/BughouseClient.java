package edu.brown.cs32.bughouse.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.Client;

public class BughouseClient implements Client {
	private BackEnd backend;
	private ClientSocket socket;
	public BughouseClient(String host, int port, BackEnd backend) throws UnknownHostException, IllegalArgumentException, IOException {
		this.socket = new ClientSocket(host,port,this);
		this.socket.run();
		this.backend = backend;
	}
	@Override
	public int createGame(int userId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("CREATE_GAME:%s\n",userId));
		int gameId = Integer.parseInt(response.trim());
		return gameId;
		
	}

	@Override
	public List<Integer> getGames() throws IOException, RequestTimedOutException {
		
		List<Integer> toReturn = new ArrayList<Integer>();
		String response = socket.getResponse("GET_GAMES:\n");
		if (response.length()==0) return toReturn;
		String[] lines = response.split("\t");

		for (String line: lines) {
			int gameId = Integer.parseInt(line);
			toReturn.add(gameId);
		}
		return toReturn;
	}

	@Override
	public boolean gameIsActive(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GAME_IS_ACTIVE:%d\n",gameId));
		return (response.trim().equals("true"));
	}

	@Override
	public List<Integer> getPlayers(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_PLAYERS:%d\n",gameId));
		String[] lines = response.split("\t");
		List<Integer> toReturn = new ArrayList<Integer>();
		for (String line: lines) {
			int userId = Integer.parseInt(line);
			toReturn.add(userId);
		}
		return toReturn;
	}

	@Override
	public int getOwnerId(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_OWNER:%d\n",gameId));
		int ownerId = Integer.parseInt(response);
		return ownerId;
	}


	@Override
	public List<Integer> getBoards(int gameId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_BOARDS:%d\n",gameId));
		String[] lines = response.split("\t");
		List<Integer> toReturn = new ArrayList<Integer>();
		for (String line: lines) {
			int boardId = Integer.parseInt(line);
			toReturn.add(boardId);
		}
		return toReturn;
	}


	@Override
	public void startGame(int gameId) throws IOException, RequestTimedOutException, GameNotReadyException {
		String response = socket.getResponse(String.format("START_GAME:%d\n",gameId));
		if (response.split(":")[0].trim().equals("NOT_READY")) {
			throw new GameNotReadyException();
		}
		
	}

	@Override
	public int addNewPlayer(String name) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("ADD_PLAYER:%s\n",name));
		int playerId = Integer.parseInt(response.trim());
		return playerId;
	}

	@Override
	public String getName(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_NAME:%d\n",playerId));
		String name = response.trim();
		return name;
	}

	@Override
	public boolean isWhite(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("IS_WHITE:%d\n",playerId));
		return (response.trim().equals("true"));
	}

	@Override
	public int getCurrentTeam(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_TEAM:%d\n",playerId));
		int team = Integer.parseInt(response);
		return team;
	}

	@Override
	public void joinGame(int playerId, int gameId, int team) throws TeamFullException, IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("JOIN_GAME:%d\t%d\t%d\n",playerId,gameId,team));
		if (response.trim().equals("GAME_FULL")) {
			throw new TeamFullException();
		}
	}

	@Override
	public int getBoardId(int playerId) throws IOException, RequestTimedOutException {
		String response = socket.getResponse(String.format("GET_CURRENT_BOARD:%d\n",playerId));
		int boardId = Integer.parseInt(response);
		return boardId;
	}

	@Override
	public void move(int boardId, int from_x, int from_y, int to_x,
			int to_y) throws IOException, RequestTimedOutException {
		socket.getResponse(String.format("MOVE:%d\t%d\t%d\t%d\t%d\n", boardId,from_x,from_y,to_x,to_y));
		
	}
	@Override
	public void quit(int playerId) throws IOException, RequestTimedOutException {
		socket.getResponse(String.format("QUIT:%d\\n", playerId));
	}
	@Override
	public void receive(String message) {
		String[] splitted = message.split("\t");
		switch (splitted[1]) {
			case "MOVE":
				broadcastMove(message);
			default:
				return;
		}
	}

	private void broadcastMove(String message) {
		String[] splitted = message.split("\t");
		int boardId = Integer.parseInt(splitted[2]);
		int from_x = Integer.parseInt(splitted[3]);
		int from_y = Integer.parseInt(splitted[4]);
		int to_x = Integer.parseInt(splitted[5]);
		int to_y = Integer.parseInt(splitted[6]);
		
		backend.updateBoard(boardId, from_x , from_y, to_x, to_y);
	}
	
	public static void main(String[] args) throws UnknownHostException, IllegalArgumentException, IOException, RequestTimedOutException {
		BughouseClient client = new BughouseClient("localhost",3333,null);
		client.addNewPlayer("Chau");
		System.out.println(client.getName(0));
		
	}
	@Override
	public void shutdown() throws IOException {
		socket.kill();
	}
	@Override
	public void gameOver(int gameId, int team) throws IOException, RequestTimedOutException {
		socket.getResponse(String.format("GAME_OVER:%d\t%d",gameId,team));
	}
	
}
