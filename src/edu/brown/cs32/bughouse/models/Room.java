package edu.brown.cs32.bughouse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.global.Model;

/**
 * Describing a chess room. I called it room instead of game because there're different states (waiting, playing..) - chtran
 * Has many: Players
 * @author chtran
 */

public class Room extends Model {
	private final int id;
	private Map<Integer,Player> players; //Be careful, never return players directly because it's mutable
	ChessBoard[] chessBoards;
	private static enum RoomState {
		WAITING, PLAYING
	}
	RoomState currentState;
	
	public Room(int id) {
		this.id=id;
		this.players = new HashMap<Integer, Player>();
		this.currentState = RoomState.WAITING;
		this.chessBoards = new ChessBoard[2];
	}
	
	public Player getPlayerById(int playerId) {
		return players.get(playerId);
	}
	
	public Room setStatePlaying() {
		this.currentState = RoomState.PLAYING;
		return this;
	}
	
	public Room setStateWaiting() {
		this.currentState = RoomState.WAITING;
		return this;
	}
	
	public List<Player> getAllPlayers() {
		return new ArrayList<Player>(this.players.values());
	}
	
	public boolean isWaiting() {
		return (this.currentState==RoomState.WAITING);
	}
	
	public boolean isPlaying() {
		return (this.currentState==RoomState.PLAYING);
	}
	
}
