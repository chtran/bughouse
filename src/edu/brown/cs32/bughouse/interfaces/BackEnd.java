package edu.brown.cs32.bughouse.interfaces;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;



public interface BackEnd {
	public Player joinServer(String name) throws UnknownHostException, IOException, RequestTimedOutException;
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException, IOException, RequestTimedOutException;
	public List<Game> getActiveGames() throws IOException, RequestTimedOutException;
	public void joinGame(int gameId, int team) throws IOException, RequestTimedOutException, TeamFullException;
	public void startGame() throws IOException, RequestTimedOutException, GameNotReadyException, UnauthorizedException;
	public Map<Integer,ChessBoard> getBoards() throws IOException, RequestTimedOutException, GameNotReadyException;
	public void createGame() throws IOException, RequestTimedOutException;
	public void quit() throws IOException, RequestTimedOutException;
	public void notifyNewPrisoner(int playerId, int chessPieceType) throws IOException, RequestTimedOutException;
	public Player me();
	public void shutdown() throws IOException;
	public List<ChessPiece> getPrisoners(int playerId);
	public FrontEnd frontEnd();
}
