package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.global.Model;
/**
 * belongsTo: ChessBoard, Room, Server
 * hasMany: ChessPieces
 * @author chtran
 *
 */
public class Player extends Model {
	private final int id;
	private int currentRoomId;
	private String name;
	
	public Player(int id) {
		this.id = id;
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

	public int getId() {
		return this.id;
	}
}
