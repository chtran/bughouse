package edu.brown.cs32.bughouse.interfaces;

import java.util.List;

import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.Game;

/**
 * Communicating with the server
 * @author chtran
 *
 */
public interface Client {

	
	public int createGame();
	public List<Integer> getGames();
	public boolean gameIsActive(int gameId);
	public List<Integer> getPlayers(int gameId);
	public int getOwnerId(int gameId);
	public List<Integer> createBoards(int gameId);
	public List<Integer> getBoards(int gameId);
	
	public int addNewPlayer(String name);
	public String getName(int playerId);
	public boolean isWhite(int playerId);
	public int getCurrentTeam(int playerId);
	public boolean joinGame(int playerId, int gameId);
	public boolean getBoardId(int playerId);
	
	public void updateBoards(ChessBoard[] boards);
}
