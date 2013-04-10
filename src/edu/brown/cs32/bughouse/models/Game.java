package edu.brown.cs32.bughouse.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.exceptions.TeamFullException;


/**
 * Describing a chess room. I called it room instead of game because there're different states (waiting, playing..) - chtran
 * hasMany: Players, ChessBoards
 * belongsTo: Server
 * @author chtran
 */

public class Game extends Model {
	private Map<Integer, Map<Integer, Player>> players;
	private int ownerId;
	Map<Integer,ChessBoard> chessBoards;

	
	public Game(int id) {
		super(id);
		this.players = new HashMap<Integer, Map<Integer, Player>>();
		this.players.put(1, new HashMap<Integer, Player>());
		this.players.put(2, new HashMap<Integer, Player>());

		this.chessBoards = new HashMap<Integer,ChessBoard>();
	}
	
	public void addBoard(ChessBoard board) {
		this.chessBoards.put(board.getId(),board);
	}
	
	public ChessBoard getBoard(int boardId) {
		return chessBoards.get(boardId);
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getOwnerId() {
		return ownerId;
	}
	
	public List<Player> getPlayers() {
		List<Player> toReturn = new ArrayList<Player>();
		toReturn.addAll(players.get(1).values());
		toReturn.addAll(players.get(2).values());
		return toReturn;
	}
	public Collection<Player> getPlayerByTeam(int team) {
		return players.get(team).values();
	}
	public void addPlayerToTeam(int team, Player p) {
		players.get(team).put(p.getId(),p);
	}
	public void clearPlayers() {
		players.clear();
	}
}
