package edu.brown.cs32.bughouse.models;

import java.io.IOException;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;

/**
 * belongsTo: ChessBoard, Room, Server
 * hasMany: ChessPieces
 * @author chtran
 */
public class Player extends Model {
	public Player(int id) {
		super(id);
	}
	public String getName() throws IOException, RequestTimedOutException {
		return client.getName(id);
	}
	public Game getCurrentGame() throws IOException, RequestTimedOutException {
		int gameId = client.getGame(id);
		Game toReturn = new Game(gameId);
		return toReturn;
	}
	
	public ChessBoard getCurrentBoard() throws IOException, RequestTimedOutException {
		int boardId = client.getBoardId(id);
		return new ChessBoard(boardId);
	}

	public boolean isWhite() throws IOException, RequestTimedOutException {
		return client.isWhite(id);
	}
	
	public Player getTeammate() throws IOException, RequestTimedOutException {
		List<Integer> playerIds = client.getPlayers(client.getGame(id));
		for (int playerId: playerIds) {
			if (client.getCurrentTeam(playerId)==client.getCurrentTeam(id))
				return new Player(playerId);
		}
		return null;
	}
	public void put(ChessPiece piece, int x, int y) throws IllegalPlacementException {
		//TODO
		/*if (prisoners.contains(piece)) throw new IllegalPlacementException();
		if (currentBoard==null) return;
		currentBoard.put(piece, x, y);
		prisoners.remove(piece);*/
	}
	public void pass(ChessPiece piece) throws IOException, RequestTimedOutException {
		client.pass(id, getTeammate().getId(), piece.getType());
	}
	
}
