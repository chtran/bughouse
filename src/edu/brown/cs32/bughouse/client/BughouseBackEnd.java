package edu.brown.cs32.bughouse.client;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Model;
import edu.brown.cs32.bughouse.models.Player;

public class BughouseBackEnd implements BackEnd {
	private Client client;
	private Player me;
	private FrontEnd frontEnd;
	public BughouseBackEnd(FrontEnd frontEnd,String host, int port) throws UnknownHostException, IllegalArgumentException, IOException {
		this.frontEnd = frontEnd;
		this.client = new BughouseClient(host,port,this);
		Model.setClient(client);
	}
	
	@Override
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException, IOException, RequestTimedOutException {
		ChessPiece captured = me.getCurrentBoard().move(from_x, from_y, to_x, to_y);
		client.move(me.getCurrentBoard().getId(), from_x, from_y, to_x, to_y);
		if (captured!=null) {
			me.getTeammate().addPrisoner(captured);
			if (captured.isKing()) {
				client.gameOver(me.getCurrentGame().getId(), client.getCurrentTeam(me.getId()));
			}
		}
	}
	

	@Override
	public void quit() throws IOException, RequestTimedOutException {
		client.quit(me.getId());
	}

	@Override
	public Player joinServer(String name) throws UnknownHostException, IOException, RequestTimedOutException {
		int playerId = client.addNewPlayer(name);
		this.me = new Player(playerId);
		return me;
	}

	@Override
	public List<Game> getActiveGames() throws IOException, RequestTimedOutException {
		List<Game> games = new ArrayList<Game>();
		List<Integer> gameIds = client.getGames();
		for (int gameId: gameIds)
			games.add(new Game(gameId));
		return games;
	}
	

	@Override
	public void joinGame(int gameId, int team) throws IOException, RequestTimedOutException, TeamFullException {
		client.joinGame(me.getId(), gameId, team);
	}

	@Override
	public void createGame() throws IOException, RequestTimedOutException {
		client.createGame(me.getId());
	}

	@Override
	public void startGame() throws IOException, RequestTimedOutException, GameNotReadyException {
		Game game = me.getCurrentGame();
		if (me.getId()==game.getOwnerId()) {
			client.startGame(game.getId());
		}
	}
	

	@Override
	public Player me() {
		return me;
	}
	

	@Override
	public void shutdown() throws IOException {
		// TODO Auto-generated method stub
		client.shutdown();
	}
	
}
