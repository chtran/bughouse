package edu.brown.cs32.bughouse.global;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class BughouseBackEnd implements BackEnd {
	@SuppressWarnings("unused")
	private String host;
	@SuppressWarnings("unused")
	private String port;
	private Client client;
	private Player me;
	private FrontEnd frontEnd;
	
	public BughouseBackEnd(String host, int port, FrontEnd frontEnd) throws UnknownHostException, IOException {
		this.client = new BughouseClient(host,port);
		this.frontEnd = frontEnd;
	}
	
	@Override
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException {
		to_y = (me.isWhite()) ? to_y :7-to_y;
		from_y = (me.isWhite()) ? from_y :7-from_y;

		ChessPiece captured = me.getCurrentBoard().move(from_x, from_y, to_x, to_y);
		if (captured!=null) {
			me.getTeammate().addPrisoner(captured);
			if (captured.isKing()) {
				frontEnd.showEndGameMessage();
			}
		}
	}
	

	@Override
	public void quit() {
		me.setCurrentGame(null);
		client.joinGame(me.getId(), 0);
	}

	@Override
	public Player joinServer(String host, int port) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Game> getActiveGames() {
		List<Game> games = new ArrayList<Game>();
		List<Integer> gameIds = client.getGames();
		for (Integer gameId: gameIds) {
			if (!client.gameIsActive(gameId)) continue;
			Game g = new Game(gameId);
			List<Integer> playerIds = client.getPlayers(gameId);
			for (int playerId: playerIds) {
				String name = client.getName(playerId);
				Player p = new Player(playerId,name);
				addPlayerToGame(p, g);
			}
			games.add(g);
		}
		return games;
	}
	private void addPlayerToGame(Player p, Game g) {
		if (client.getCurrentTeam(p.getId())==1) {
			g.addToTeam1(p);
		} else {
			g.addToTeam2(p);
		}
	}
	
	@Override
	public void joinGame(Game g) {
		client.joinGame(me.getId(), g.getId());
		me.setCurrentGame(g);
		addPlayerToGame(me, g);
		
	}

	@Override
	public void createGame() {
		int gameId = client.createGame();
		Game g = new Game(gameId);
		addPlayerToGame(me, g);
		me.setCurrentGame(g);
	}

	@Override
	public void startGame() {
		// TODO Auto-generated method stub
		
	}
}
