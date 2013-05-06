package edu.brown.cs32.bughouse.ui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
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
		System.out.println("Newly created game has the following players "+ backend.me().getCurrentGame().getPlayersByTeam(1).get(0).getName());
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
	private void showPrisoners(String line) {
		String[] splitted = line.split(" ");
		int playerId = Integer.parseInt(splitted[1]);
		List<ChessPiece> prisoners = backend.getPrisoners(playerId);
		if (prisoners==null) {
			System.out.println("Player not in game");
			return;
		} else if (prisoners.isEmpty()) {
			System.out.println("Player doesn't have any prisoner");
			return;
		}
		System.out.println("Prisoners of player #"+playerId);
		for (int i=0; i<prisoners.size();i++) {
			System.out.printf("%d. %s\n",i,prisoners.get(i));
		}
	}
	
	private void put(String line) throws IllegalPlacementException, IOException, RequestTimedOutException {
		String[] splitted = line.split(" ");
		int index = Integer.parseInt(splitted[1]);
		int x = Integer.parseInt(splitted[2]);
		int y = Integer.parseInt(splitted[3]);
		backend.me().put(index, x, y);
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
					case "show_prisoners":
						showPrisoners(line);
						break;
					case "put":
						put(line);
						break;
					default:
						System.out.println("Unknown command: "+line);
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
			} catch (WrongColorException e) {
				System.out.println("ERROR: Wrong color");
			} catch (IllegalPlacementException e) {
				System.out.println("ERROR: You cannot put the piece there");
			}
		}
		
		System.out.println("Bye.");
		stdIn.close();
		backend.shutdown();
		System.exit(0);
	}
 
	@Override
	public void showEndGameMessage(List<String> winners) {
		System.out.printf("%s and %s won!",winners.get(0), winners.get(1));
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
		for (ChessBoard board: backend.getCurrentBoards().values())
			System.out.println(board);
	}
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new CommandLine(host,port);
		
	}
	public void addPrisoner(ChessPiece piece) {
		try {
			System.out.printf("%s got a new %s\n",backend.me().getName(),piece.getName());
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
	@Override
	public void prisonersUpdated() {
		System.out.println("Prisoner list updated");
	}
	@Override
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x, int y) {
		Player player = new Player(playerId);
		try {
			System.out.printf("%s put a %s to (%d, %d)\n",player.getName(),piece.getName(), x,y);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			System.out.println("Request timed out");
		}
	}
	@Override
	public void notifyNewOwner(int gameId) {
		// TODO Auto-generated method stub
		System.out.println("You are now the owner of game #"+gameId);
		
	}
	@Override
	public void gameCanceled() {
		System.out.println("Your game has been canceled");
	}
	@Override
	public void updatePlayerList() {
		
	}
}
