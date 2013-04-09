package edu.brown.cs32.bughouse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Describing a chess room. I called it room instead of game because there're different states (waiting, playing..) - chtran
 * hasMany: Players, ChessBoards
 * belongsTo: Server
 * @author chtran
 */

public class Game extends Model {
	private List<Player> team1;
	private List<Player> team2;
	ChessBoard[] chessBoards;
	private static enum GameState {
		WAITING, PLAYING
	}
	GameState currentState;
	
	public Game() {
		super();
		this.team1 = new ArrayList<Player>();
		this.team2 = new ArrayList<Player>();
		this.currentState = GameState.WAITING;
		this.chessBoards = new ChessBoard[2];
		chessBoards[0] = new ChessBoard();
		chessBoards[1] = new ChessBoard();
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
		List<Player> toReturn = new ArrayList<Player>();
		toReturn.addAll(team1);
		toReturn.addAll(team2);
		return toReturn;
	}
	
	public List<Player> getTeam1() {
		List<Player> toReturn = new ArrayList<Player>(team1);
		return toReturn;
	}
	public List<Player> getTeam2() {
		List<Player> toReturn = new ArrayList<Player>(team2);
		return toReturn;
	}
	public boolean isWaiting() {
		return (this.currentState==GameState.WAITING);
	}
	
	public boolean isPlaying() {
		return (this.currentState==GameState.PLAYING);
	}
	
}
