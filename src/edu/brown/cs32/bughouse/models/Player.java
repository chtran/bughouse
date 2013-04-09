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
	private Player teammate;
	private ChessBoard currentBoard;
	private boolean isWhite;
	
	public Player(int id, String name) {
		super(id);
		this.name = name;
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
	public ChessBoard getCurrentBoard() {
		return this.currentBoard;
	}
	public Player getTeammate() {
		return teammate;
	}
	public boolean isWhite() {
		return isWhite;
	}
	public void put(ChessPiece piece, int x, int y) throws IllegalPlacementException {
		if (prisoners.contains(piece)) throw new IllegalPlacementException();
		if (currentBoard==null) return;
		currentBoard.put(piece, x, y);
		prisoners.remove(piece);
	}
	public void addPrisoner(ChessPiece piece) {
		this.prisoners.add(piece);
	}
	public void setCurrentGame(Game g) {
		this.currentGame = g;
	}
	
}
