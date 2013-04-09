package edu.brown.cs32.bughouse.models;

import java.util.HashSet;
import java.util.Set;

import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;

/**
 * belongsTo: ChessBoard, Room, Server
 * hasMany: ChessPieces
 * @author chtran
 *
 */
public class Player extends Model {
	private Game currentGame;
	private String name;
	private Set<ChessPiece> prisoners;
	private ChessBoard currentBoard;
	
	public Player() {
		super();
		this.prisoners = new HashSet<ChessPiece>();
	}
	
	/**
	 * 
	 * @param name
	 * @return this player (to enable cascading)
	 */
	public Player setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	public Game getCurrentGame() {
		return this.currentGame;
	}
	public void put(ChessPiece piece, int x, int y) throws IllegalPlacementException {
		if (prisoners.contains(piece)) throw new IllegalPlacementException();
		if (currentBoard==null) return;
		currentBoard.put(piece, x, y);
		prisoners.remove(piece);
	}
	
}
