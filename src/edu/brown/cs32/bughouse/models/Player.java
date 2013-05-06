package edu.brown.cs32.bughouse.models;

import java.io.IOException;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.WrongColorException;

/**
 * belongsTo: ChessBoard, Room, Server
 * hasMany: ChessPieces
 * @author chtran
 */
public class Player extends Model {
	private ChessBoard currentBoard;
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
	public void setBoard(ChessBoard board) {
		this.currentBoard = board;
	}

	public int getCurrentBoardId() throws IOException, RequestTimedOutException {
		return client.getBoardId(id);
	}
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException, IOException, RequestTimedOutException, WrongColorException {
		ChessPiece captured = currentBoard.movePiece(isWhite(),from_x, from_y, to_x, to_y);
		System.out.println("captured="+captured);
		if (captured!=null && captured.isKing()) {
			System.out.println("Preparing to send game over message");
			client.gameOver(getCurrentGame().getId(), client.getCurrentTeam(getId()));
			System.out.println("Sent game over message");
			return;
		}
		System.out.println("Sending move to server");
		client.move(getCurrentBoardId(),from_x, from_y, to_x, to_y);
		System.out.println("Done sending move to server");
		if (captured!=null)	pass(captured);
	}
	public boolean isWhite() throws IOException, RequestTimedOutException {
		return client.isWhite(id);
	}
	public Player getTeammate() throws IOException, RequestTimedOutException {
		List<Integer> playerIds = client.getPlayers(client.getGame(id),client.getCurrentTeam(id));
		for (int playerId: playerIds) {
			if (playerId!=id)
				return new Player(playerId);
		}
		return null;
	}
	public void put(int index, int x, int y) throws IllegalPlacementException, IOException, RequestTimedOutException {
		if (currentBoard.isOccupied(x, y)) throw new IllegalPlacementException();

		client.put(getCurrentBoardId(), id, index, x, y);
	}
	public void pass(ChessPiece piece) throws IOException, RequestTimedOutException {
		client.pass(id, getTeammate().getId(), piece.getType());
	}
	
}
