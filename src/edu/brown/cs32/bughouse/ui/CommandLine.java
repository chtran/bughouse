package edu.brown.cs32.bughouse.ui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;
import edu.brown.cs32.bughouse.exceptions.WrongColorException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class CommandLine implements FrontEnd{
	BackEnd backend;
	List<Game> currentGames;
	
	public CommandLine(String host, int port) {
		try {
			this.backend = new BughouseBackEnd(this,host,port);
			this.run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			System.out.println("Server timed out.");
		}
	}
	private void showGames() throws IOException, RequestTimedOutException {
		List<Game> gameList = backend.getActiveGames();
		
		if (gameList.isEmpty()) System.out.println("No game available.");
		for (Game g: gameList) {
			showGame(g);
		}
	}
	
	private void createGame() throws IOException, RequestTimedOutException {
		backend.createGame();
		System.out.println("Created new game. You are now in game #"+backend.me().getCurrentGame().getId());
	}
	
	private void showPlayers() throws IOException, RequestTimedOutException {
		showGame(backend.me().getCurrentGame());
	}
	
	private void showGame(Game g) throws IOException, RequestTimedOutException {
		System.out.println("Game #"+g.getId());
		System.out.print("Team 1: ");
		for (Player p: g.getPlayersByTeam(1)) System.out.print(p.getName()+" ");
		System.out.println();
		System.out.print("Team 2: ");
		for (Player p: g.getPlayersByTeam(2)) System.out.print(p.getName()+" ");
		System.out.println();
	}
	
	private void joinGame(String line) throws IOException, RequestTimedOutException, TeamFullException {
		String[] splitted = line.split(" ");
		int gameId = Integer.parseInt(splitted[1]);
		int team = Integer.parseInt(splitted[2]);
		backend.joinGame(gameId, team);
		System.out.printf("Joined game %d on team %d\n",gameId,team);
 	}
	//When client starts the game (client is owner of the room)
	private void startGame() throws IOException, RequestTimedOutException, GameNotReadyException {
		try {
			backend.startGame();
		} catch (UnauthorizedException e) {
			System.out.println("You are not authorized to start game");
		}
	}
	//When server starts the game (client not owner)
	public void gameStarted() {
		System.out.println("Your game started!");
	}
	private void movePiece(String line) throws IllegalMoveException, IOException, RequestTimedOutException, WrongColorException {
		String[] splitted = line.split(" ");
		int from_x = Integer.parseInt(splitted[1]);
		int from_y = Integer.parseInt(splitted[2]);
		int to_x = Integer.parseInt(splitted[3]);
		int to_y = Integer.parseInt(splitted[4]);
		backend.me().move(from_x, from_y, to_x, to_y);
		System.out.println("Moved successfully");
	}
 	public void run() throws IOException, RequestTimedOutException {
		System.out.print("Enter your name: ");
		Scanner stdIn = new Scanner(System.in);
		String line = stdIn.nextLine();
		
		backend.joinServer(line);
		System.out.println("Hello "+line);
		while(!line.equals("exit")) {
			try {
				line = stdIn.nextLine();
				String type = line.split(" ")[0];
				switch (type) {
					case "show_games":
						showGames();
						break;
					case "create_game":
						createGame();
						break;
					case "show_players":
						showPlayers();
						break;
					case "join":
						joinGame(line);
						break;
					case "exit":
						break;
					case "start_game":
						startGame();
						break;
					case "move":
						movePiece(line);
						break;
					case "quit":
						backend.quit();
						break;
					case "print_board":
						printBoards();
						break;
					default:
						System.out.println("Unknown command: "+line);;
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RequestTimedOutException e) {
				System.out.println("ERROR: Request timed out.");
			} catch (TeamFullException e) {
				System.out.println("ERROR: Team is full");
			} catch (GameNotReadyException e) {
				System.out.println("ERROR: Game not ready");
			} catch (IllegalMoveException e) {
				System.out.println("ERROR: Move not legal");
				e.printStackTrace();
			} catch (WrongColorException e) {
				System.out.println("ERROR: Wrong color");
			}
		}
		
		System.out.println("Bye.");
		stdIn.close();
		backend.shutdown();
		System.exit(0);
	}
 
	@Override
	public void showEndGameMessage() {
		System.out.println("Game ended.");
	}

	@Override
	public void notifyUserTurn() {
		System.out.println("It's your turn");
	}

	@Override
	public void repaint() {
		return;
	}
	public void printBoards() throws IOException, RequestTimedOutException {
		System.out.printf("You are %s in board %d\n",backend.me().isWhite() ? "white" : "black", backend.me().getCurrentBoardId());
		for (ChessBoard board: backend.getCurrentBoards())
			System.out.println(board);
	}
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new CommandLine(host,port);
		
	}
	@Override
	public void addPrisoner(int playerId, ChessPiece piece) {
		String name;
		try {
			name = (new Player(playerId)).getName();
			System.out.printf("%s got a new %s\n",name,piece.getName());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			System.out.println("Request timed out.");
		}
	}
	@Override
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		System.out.printf("Board #%d: (%d,%d) moved to (%d,%d)\n",boardId,from_x,from_y,to_x,to_y);
	}
	@Override
	public void gameListUpdated() {
		System.out.println("Game list updated");
	}
}
