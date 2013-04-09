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

	private int currentState;
	
	public Game(int id) {
		super(id);
		this.team1 = new ArrayList<Player>();
		this.team2 = new ArrayList<Player>();
		this.chessBoards = new ChessBoard[2];
		chessBoards[0] = new ChessBoard();
		chessBoards[1] = new ChessBoard();
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
	public void addToTeam1(Player p) {
		team1.add(p);
	}
	public void addToTeam2(Player p) {
		team2.add(p);
	}
}
