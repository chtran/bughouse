package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.global.Model;

public class ChessBoard extends Model {
	private ChessPiece[][] board;
	
	public ChessBoard() {
		this.board = new ChessPiece[8][8];
		//TODO: populate initial chess pieces
	}
	
	
}
