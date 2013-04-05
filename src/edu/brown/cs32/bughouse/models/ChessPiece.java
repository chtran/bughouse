package edu.brown.cs32.bughouse.models;

/**
 * belongsTo: ChessBoard, Player
 * @author chtran
 *
 */
public class ChessPiece extends Model {
	private static final int PAWN = 1;
	private static final int KNIGHT = 2;
	private static final int BISHOP = 3;
	private static final int ROOK = 4;
	private static final int QUEEN = 5;
	private static final int KING = 6;
	
	private final boolean isWhite;
	private final int type;

	private ChessPiece(Builder builder) {
		super();
		this.type = builder.type;
		this.isWhite = builder.isWhite;
	}

	/****************************************************************************************************************
	 * Builder
	 * To get new piece do something like:
	 * ChessPiece p = new ChessPiece.Builder(10).white().pawn().build();
	 * - chtran
	 ****************************************************************************************************************/ 
	public static class Builder {
		private boolean isWhite;
		private int type;

		public Builder() {
		}
		

		public Builder black() {
			this.isWhite = false;
			return this;
		}
		public Builder white() {
			this.isWhite = true;
			return this;
		}
		public Builder king() {
			this.type = KING;
			return this;
		}
		public Builder queen() {
			this.type = QUEEN;
			return this;
		}
		public Builder rook() {
			this.type = ROOK;
			return this;
		}
		public Builder bishop() {
			this.type = BISHOP;
			return this;
		}
		public Builder knight() {
			this.type = KNIGHT;
			return this;
		}
		public Builder pawn() {
			this.type = PAWN;
			return this;
		}
		public ChessPiece build() {
			return new ChessPiece(this);
		}
	}

	public boolean isWhite() {
		return this.isWhite;
	}
	
	public boolean canMove(int from_x, int from_y, int to_x, int to_y) {
		//TODO: fill
		if (to_x>=8 || to_y>=8 || to_x<0 || to_y<0) return false;
		switch (type) {
			case PAWN:
				return (from_x==to_x) && (to_y==from_y+1 || to_y==from_y-1);
			case KNIGHT:
				return true;
			default:
				return false;
			
		}
		
	}
	

	
}
