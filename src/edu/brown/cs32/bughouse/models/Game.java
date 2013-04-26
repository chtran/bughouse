package edu.brown.cs32.bughouse.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;


/**
 * Describing a chess room. I called it room instead of game because there're different states (waiting, playing..) - chtran
 * hasMany: Players, ChessBoards
 * belongsTo: Server
 * @author chtran
 */

public class Game extends Model {
	public Game(int id) {
		super(id);
	}
	public int getOwnerId() throws IOException, RequestTimedOutException {
		return client.getOwnerId(id);
	}
	
	public List<Player> getPlayers() throws IOException, RequestTimedOutException {
		List<Player> toReturn = new ArrayList<Player>();
		List<Integer> playerIds = client.getPlayers(id);
		for (int playerId: playerIds)
			toReturn.add(new Player(playerId));
		return toReturn;
	}
	
	public List<ChessBoard> getBoards() throws IOException, RequestTimedOutException {
		List<ChessBoard> toReturn = new ArrayList<ChessBoard>();
		List<Integer> boardIds = client.getBoards(id);
		for (int boardId: boardIds)
			toReturn.add(new ChessBoard(boardId));
		return toReturn;
	}
	public List<Player> getPlayersByTeam(int team) throws IOException, RequestTimedOutException {
		List<Player> toReturn = new ArrayList<Player>();
		List<Integer> playerIds = client.getPlayers(id);
		for (int playerId: playerIds)
			if (client.getGame(playerId)==team) toReturn.add(new Player(playerId));
		return toReturn;
	}
}
