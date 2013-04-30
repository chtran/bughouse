package edu.brown.cs32.bughouse.interfaces;

import java.io.IOException;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;

/**
 * Communicating with the server
 * @author chtran
 *
 */
public interface Client {
	
	public int createGame(int playerId) throws IOException, RequestTimedOutException;
	public List<Integer> getGames() throws IOException, RequestTimedOutException;
	public boolean gameIsActive(int gameId) throws IOException, RequestTimedOutException;
	public List<Integer> getPlayers(int gameId, int team) throws IOException, RequestTimedOutException;
	public int getOwnerId(int gameId) throws IOException, RequestTimedOutException;
	public List<Integer> getBoards(int gameId) throws IOException, RequestTimedOutException;
	public void startGame(int gameId) throws IOException, RequestTimedOutException, GameNotReadyException, UnauthorizedException;
	public int getGame(int playerId) throws IOException, RequestTimedOutException;
	public int addNewPlayer(String name) throws IOException, RequestTimedOutException;
	public String getName(int playerId) throws IOException, RequestTimedOutException;
	public boolean isWhite(int playerId) throws IOException, RequestTimedOutException;
	public int getCurrentTeam(int playerId) throws IOException, RequestTimedOutException;
	public void joinGame(int playerId, int gameId, int team) throws TeamFullException, IOException, RequestTimedOutException;
	public int getBoardId(int playerId) throws IOException, RequestTimedOutException;
	public void quit(int playerId) throws IOException, RequestTimedOutException;
	public void pass(int fromId, int toId, int chessPieceType) throws IOException, RequestTimedOutException;
	public void put(int boardID, int playerId, int pieceIndex, int x, int y) throws IOException, RequestTimedOutException;
	public void move(int boardId, int from_x, int from_y, int to_x, int to_y) throws IOException, RequestTimedOutException;
	public void gameOver(int gameId, int team) throws IOException, RequestTimedOutException;
	public void receive(String message) throws NumberFormatException, IOException, RequestTimedOutException, IllegalPlacementException;
	public void shutdown() throws IOException;
}
