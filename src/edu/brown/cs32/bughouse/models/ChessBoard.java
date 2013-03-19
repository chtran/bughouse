package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.global.Model;

/**
 * hasMany: ChessPieces, Players
 * belongsTo: Room
 * @author chtran
 *
 */
public class ChessBoard extends Model {
	private int id;
	private ChessPiece[][] board;
	
	public ChessBoard() {
		this.board = new ChessPiece[8][8];
		//TODO: populate initial chess pieces
	}

	public int getId() {
		return this.id;
	}
}
