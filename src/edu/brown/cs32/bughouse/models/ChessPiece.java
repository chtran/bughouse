package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.global.Model;

public class ChessPiece extends Model {
	private String name;
	private static enum ChessPieceColor {
		BLACK, WHITE
	}
	private static enum ChessPieceType {
		KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
	}
	private final ChessPieceColor color;
	private final ChessPieceType type;
	
	private ChessPiece(ChessPieceType type, ChessPieceColor color) {
		this.type = type;
		this.color = color;
	}
	

	public ChessPiece setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	/****************************************************************************************************************
	 * Factory methods
	 ****************************************************************************************************************/ 
	 
	public static ChessPiece createWhiteKing() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.KING, ChessPieceColor.WHITE);
		return toReturn;
	}
	
	public static ChessPiece createWhiteQueen() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.WHITE);
		return toReturn;
	}
	public static ChessPiece createWhiteRook() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.ROOK, ChessPieceColor.WHITE);
		return toReturn;
	}
	public static ChessPiece createWhiteBishop() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.WHITE);
		return toReturn;
	}
	public static ChessPiece createWhiteKnight() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.WHITE);
		return toReturn;
	}
	public static ChessPiece createWhitePawn() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.PAWN, ChessPieceColor.WHITE);
		return toReturn;
	}
	
	public static ChessPiece createBlackKing() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.KING, ChessPieceColor.BLACK);
		return toReturn;
	}
	
	public static ChessPiece createBlackQueen() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.QUEEN, ChessPieceColor.BLACK);
		return toReturn;
	}
	public static ChessPiece createBlackRook() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.ROOK, ChessPieceColor.BLACK);
		return toReturn;
	}
	public static ChessPiece createBlackBishop() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.BISHOP, ChessPieceColor.BLACK);
		return toReturn;
	}
	public static ChessPiece createBlackKnight() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.KNIGHT, ChessPieceColor.BLACK);
		return toReturn;
	}
	public static ChessPiece createBlackPawn() {
		ChessPiece toReturn = new ChessPiece(ChessPieceType.PAWN, ChessPieceColor.BLACK);
		return toReturn;
	}
}
