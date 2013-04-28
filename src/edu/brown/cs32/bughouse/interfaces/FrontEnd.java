package edu.brown.cs32.bughouse.interfaces;

import edu.brown.cs32.bughouse.models.ChessPiece;

public interface FrontEnd {

	public void showEndGameMessage();
	public void notifyUserTurn();
	public void repaint();
	//Add the piece on front end
	public void addPrisoner(int playerId, ChessPiece piece);
	//Move the piece on the board on front end
	public void movePiece(int boardId, int from_x, int from_y, int to_x, int to_y);

}
