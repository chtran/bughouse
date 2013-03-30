package edu.brown.cs32.bughouse.models;

/**
 * belongsTo: ChessBoard, Room, Server
 * hasMany: ChessPieces
 * @author chtran
 *
 */
public class Player extends Model {
	private int currentRoomId;
	private String name;
	
	public Player(int id) {
		super(id);
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
}
