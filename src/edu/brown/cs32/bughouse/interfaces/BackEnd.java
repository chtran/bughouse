package edu.brown.cs32.bughouse.interfaces;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;


public interface BackEnd {
	public Player joinServer(String host, int port, String name) throws UnknownHostException, IOException;
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException;
	public List<Game> getActiveGames();
	public void joinGame(Game g);
	public void startGame();
	public void createGame();

	public void quit();
	public void updateGame();
	public void updateBoard(int boardId, int from_x, int from_y, int to_x, int to_y);
	public void updatePlayer();
}
