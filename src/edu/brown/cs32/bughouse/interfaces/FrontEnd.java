package edu.brown.cs32.bughouse.interfaces;

import edu.brown.cs32.bughouse.models.ChessPiece;

public interface FrontEnd {

	public void showEndGameMessage();
	public void notifyUserTurn();
	public void repaint();
	public void addPrisoner(int playerId, ChessPiece piece);
}
