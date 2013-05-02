package edu.brown.cs32.bughouse.interfaces;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;



public interface BackEnd {
	public Player joinServer(String name) throws UnknownHostException, IOException, RequestTimedOutException;
	public List<Game> getActiveGames() throws IOException, RequestTimedOutException;
	public void joinGame(int gameId, int team) throws IOException, RequestTimedOutException, TeamFullException;
	public void startGame() throws IOException, RequestTimedOutException, GameNotReadyException, UnauthorizedException;
	public void gameStarted() throws IOException, RequestTimedOutException;
	public void updateBoard(int boardId, int from_x, int from_y, int to_x, int to_y);
	public Map<Integer,ChessBoard> getCurrentBoards();
	public void createGame() throws IOException, RequestTimedOutException;
	public void quit() throws IOException, RequestTimedOutException;
	public void notifyNewPrisoner(int playerId, int chessPieceType) throws IOException, RequestTimedOutException;
	public void notifyPut(int boardId, int playerId, int index,int x, int y) throws IllegalPlacementException, IOException, RequestTimedOutException;
	public Player me();
	public void shutdown() throws IOException;
	public List<ChessPiece> getPrisoners(int playerId);
	public boolean canMove(int boardId, int from_x, int from_y, int to_x, int to_y);
	public FrontEnd frontEnd();
}
