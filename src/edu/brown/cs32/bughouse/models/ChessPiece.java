package edu.brown.cs32.bughouse.models;

/**
 * belongsTo: ChessBoard, Player
 * @author chtran
 *
 */
public class ChessPiece extends Model {
	public static final int EMPTY = 0;
	public static final int PAWN = 1;
	public static final int KNIGHT = 2;
	public static final int BISHOP = 3;
	public static final int ROOK = 4;
	public static final int QUEEN = 5;
	public static final int KING = 6;

	private final int type;
	private final boolean isWhite;
	
	private ChessPiece(int type, boolean isWhite) {
		super();
		this.type = type;
		this.isWhite = isWhite;
	}
	
	

	
}
