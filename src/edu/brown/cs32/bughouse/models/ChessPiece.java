package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.global.Model;

public class ChessPiece extends Model {
	private final int id;
	private String name;
	private static enum ChessPieceColor {
		BLACK, WHITE
	}
	private static enum ChessPieceType {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
	}
	private final ChessPieceColor color;
	private final ChessPieceType type;
	
	private ChessPiece(Builder builder) {
		this.id = builder.id;
		this.type = builder.type;
		this.color = builder.color;
	}
	
	/****************************************************************************************************************
	 * Builder
	 * To get new piece do something like:
	 * ChessPiece p = new ChessPiece.Builder(10).white().pawn().build();
	 * - chtran
	 ****************************************************************************************************************/ 
	public static class Builder {
		private ChessPieceColor color;
		private ChessPieceType type;
		private final int id;
		
		public Builder(int id) {
			this.id = id;
		}
		
		public Builder black() {
			this.color = ChessPieceColor.BLACK;
			return this;
		}
		public Builder white() {
			this.color = ChessPieceColor.WHITE;
			return this;
		}
		public Builder king() {
			this.type = ChessPieceType.KING;
			return this;
		}
		public Builder queen() {
			this.type = ChessPieceType.QUEEN;
			return this;
		}
		public Builder rook() {
			this.type = ChessPieceType.ROOK;
			return this;
		}
		public Builder bishop() {
			this.type = ChessPieceType.BISHOP;
			return this;
		}
		public Builder knight() {
			this.type = ChessPieceType.KNIGHT;
			return this;
		}
		public Builder pawn() {
			this.type = ChessPieceType.PAWN;
			return this;
		}
		public ChessPiece build() {
			return new ChessPiece(this);
		}
	}
	
	public ChessPiece setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean isWhite() {
		return (this.color==ChessPieceColor.WHITE);
	}
	
}
