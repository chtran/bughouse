package edu.brown.cs32.bughouse.global;

import edu.brown.cs32.bughouse.exceptions.*;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.*;

public class BughouseBackEnd implements BackEnd {

	@Override
	public ChessBoard[] getInitialBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessBoard[] move(ChessPiece piece, Position destination)
			throws IllegalMoveException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player[] isGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessBoard[] put(ChessPiece piece, Position destination)
			throws IllegalPlacementException {
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
