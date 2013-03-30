package edu.brown.cs32.bughouse.interfaces;

import java.util.List;

import edu.brown.cs32.bughouse.models.ChessBoard;

/**
 * Communicating with the server
 * @author chtran
 *
 */
public interface Client {
	public boolean joinGame(int gameId);
	public boolean createGame(int gameId);
	public List<Integer> getPlayers(int gameId);
	public List<Integer> getGames();
	public void updateBoards(ChessBoard[] boards);
}
