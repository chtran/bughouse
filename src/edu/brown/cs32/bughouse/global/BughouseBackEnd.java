package edu.brown.cs32.bughouse.global;

import edu.brown.cs32.bughouse.exceptions.*;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.*;

public class BughouseBackEnd implements BackEnd {
	private ChessBoard currentBoard;
	private ChessBoard otherBoard;
	
	private boolean isWhite;
	@Override
	public ChessBoard[] getInitialBoard() {
		ChessBoard left = new ChessBoard();
		ChessBoard right = new ChessBoard();
		currentBoard = left;
		ChessBoard[] toReturn = new ChessBoard[2];
		toReturn[0] = left.getView(isWhite);
		toReturn[1] = right.getView(!isWhite);
		return toReturn;
	}

	@Override
	public ChessBoard[] move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException {
		to_y = (isWhite) ? to_y :7-to_y;
		from_y = (isWhite) ? from_y :7-from_y;

		currentBoard.move(from_x, from_y, to_x, to_y);
		return null;
	}

	@Override
	public Player[] isGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessBoard[] put(ChessPiece piece, int x, int y)	throws IllegalPlacementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWhite(Player p) {
		// TODO Auto-generated method stub
		return false;
	}

}
