package edu.brown.cs32.bughouse.interfaces;

import java.util.List;

import edu.brown.cs32.bughouse.models.ChessPiece;

public interface FrontEnd {

	public void showEndGameMessage(List<String> winners);
	public void notifyUserTurn();
	public void repaint();
	//Add the piece on front end
	public void addPrisoner(ChessPiece piece);
	//Move the piece on the board on front end
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x, int to_y);
	public void gameStarted();
	public void gameListUpdated();
	public void prisonersUpdated();
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x, int y);
	public void notifyNewOwner(int gameId);
	public void gameCanceled(); //call when the game is playing and one player quits
	public void updatePlayerList(); //call when a player joins/quits the game
}
