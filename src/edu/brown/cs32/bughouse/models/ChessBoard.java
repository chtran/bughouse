package edu.brown.cs32.bughouse.models;


/**
 * hasMany: ChessPieces, Players
 * belongsTo: Room
 * @author chtran
 *
 */
public class ChessBoard extends Model {
	private ChessPiece[][] board;
	
	public ChessBoard() {
		super();
		this.board = new ChessPiece[8][8];
		//TODO: populate initial chess pieces
	}

}
