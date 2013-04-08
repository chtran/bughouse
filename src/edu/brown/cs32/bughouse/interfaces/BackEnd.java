package edu.brown.cs32.bughouse.interfaces;

import edu.brown.cs32.bughouse.models.*;
import edu.brown.cs32.bughouse.exceptions.*;


public interface BackEnd {
	/**
	 * @return 2 initial chessboards (flipped accordingly)
	 */
	public ChessBoard[] getInitialBoard();
	/**
	 * Move a chess piece
	 * @param piece: a chess piece
	 * @param destination: the destination
	 * @return
	 * @throws IllegalMoveException
	 */
	public ChessBoard[] move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException;
	/**
	 * Check if the game is over
	 * @return the 2 winning players
	 */
	public Player[] isGameOver();
	
	/**
	 * Put a piece in the board
	 * @param piece: piece
	 * @param destination: the destination
	 * @return
	 * @throws IllegalMoveException
	 */
	public ChessBoard[] put(ChessPiece piece, int x, int y) throws IllegalPlacementException;
	
	/**
	 * Quit the game
	 */
	public void quit();
	
	/**
	 * Check if the player is white
	 * @param p
	 * @return
	 */
	public boolean isWhite(Player p);
	
	public Client getClient();
}
