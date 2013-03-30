package edu.brown.cs32.bughouse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Describing a chess room. I called it room instead of game because there're different states (waiting, playing..) - chtran
 * hasMany: Players, ChessBoards
 * belongsTo: Server
 * @author chtran
 */

public class Game extends Model {
	private Map<Integer,Player> players; //Be careful, never return players directly because it's mutable
	ChessBoard[] chessBoards;
	private static enum GameState {
		WAITING, PLAYING
	}
	GameState currentState;
	
	public Game() {
		super();
		this.players = new HashMap<Integer, Player>();
		this.currentState = GameState.WAITING;
		this.chessBoards = new ChessBoard[2];
	}
	
	public Player getPlayerById(int playerId) {
		return players.get(playerId);
	}
	
	public Game setStatePlaying() {
		this.currentState = GameState.PLAYING;
		return this;
	}
	
	public Game setStateWaiting() {
		this.currentState = GameState.WAITING;
		return this;
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(this.players.values());
	}
	
	public boolean isWaiting() {
		return (this.currentState==GameState.WAITING);
	}
	
	public boolean isPlaying() {
		return (this.currentState==GameState.PLAYING);
	}
}
