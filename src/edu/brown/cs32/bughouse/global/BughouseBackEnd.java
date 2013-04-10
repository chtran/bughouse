package edu.brown.cs32.bughouse.global;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class BughouseBackEnd implements BackEnd {
	private Client client;
	private Player me;
	private FrontEnd frontEnd;
	public BughouseBackEnd(FrontEnd frontEnd) {
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
	public Player joinServer(String host, int port, String name) throws UnknownHostException, IOException {
		this.client = new BughouseClient(host,port);
		int playerId = client.addNewPlayer(name);
		this.me = new Player(playerId,name);
		return me;
	}

	@Override
	public List<Game> getActiveGames() {
		List<Game> games = new ArrayList<Game>();
		List<Integer> gameIds = client.getGames();
		for (Integer gameId: gameIds) {
			if (!client.gameIsActive(gameId)) continue;
			
			Game g = new Game(gameId);
			updateGame(g);
			g.setOwnerId(client.getOwnerId(gameId));
			games.add(g);
		}
		return games;
	}
	

	@Override
	public void joinGame(Game g) {
		client.joinGame(me.getId(), g.getId());
		me.setCurrentGame(g);
		g.addPlayerToTeam(client.getCurrentTeam(me.getId()), me);

	}

	@Override
	public void createGame() {
		int gameId = client.createGame();
		Game g = new Game(gameId);
		g.setOwnerId(me.getId());
		g.addPlayerToTeam(client.getCurrentTeam(me.getId()), me);
		me.setCurrentGame(g);
	}

	@Override
	public void startGame() {
		//TODO
		Game game = me.getCurrentGame();
		if (me.getId()==game.getOwnerId()) {
			client.startGame(game.getId());
		}
		List<Integer> chessBoardIds = client.getBoards(game.getId());
		Map<Integer, ChessBoard> boards = new HashMap<Integer,ChessBoard>();
		for (int chessBoardId: chessBoardIds) {
			ChessBoard board = new ChessBoard(chessBoardId);
			game.addBoard(board);
			boards.put(chessBoardId, board);
		}
		for (Player p: game.getPlayers()) {
			if (client.isWhite(p.getId())) {
				p.setWhite();
				boards.get(client.getBoardId(p.getId())).setWhitePlayer(p);
			} else {
				p.setBlack();
				boards.get(client.getBoardId(p.getId())).setBlackPlayer(p);
			}
		}
		
	}
	private void updateGame(Game g) {
		List<Integer> playerIds = client.getPlayers(g.getId());
		g.clearPlayers();
		for (int playerId: playerIds) {
			String name = client.getName(playerId);
			Player p = new Player(playerId,name);
			g.addPlayerToTeam(client.getCurrentTeam(playerId), p);
		}
		g.setOwnerId(client.getOwnerId(g.getId()));
	}
	@Override
	public void updateGame() {
		updateGame(me.getCurrentGame());
	}

	@Override
	public void updateBoard(int boardId, int from_x, int from_y, int to_x, int to_y) {
		try {
			me.getCurrentGame().getBoard(boardId).move(from_x, from_y, to_x, to_y);
		} catch (IllegalMoveException e) {
			System.out.println("ERROR: Illegal move");
		}
	}

	@Override
	public void updatePlayer() {
		// TODO Auto-generated method stub
		
	}
}
