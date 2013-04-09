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
	public boolean joinGame(int gameId);
	public boolean createGame(int gameId);
	public List<Game> getGames();
	public void updateBoards(ChessBoard[] boards);
}
