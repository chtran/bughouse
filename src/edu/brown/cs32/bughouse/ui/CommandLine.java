package edu.brown.cs32.bughouse.ui;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class CommandLine implements FrontEnd{
	BackEnd backend;
	String host;
	int port;
	public CommandLine(String host, int port) {
		try {
			this.backend = new BughouseBackEnd(this);
			this.host = host;
			this.port = port;
			this.run();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			e.printStackTrace();
		}
	}
	private void showGames() throws IOException, RequestTimedOutException {
		System.out.println("Showing games");
		List<Game> gameList = backend.getActiveGames();
		System.out.println(gameList);
		if (gameList.isEmpty()) System.out.println("No game available.");
		for (Game g: gameList) {
			showGame(g);
		}
	}
	
	private void createGame() throws IOException, RequestTimedOutException {
		backend.createGame();
		System.out.println("Created new game. You are now in game #"+backend.me().getCurrentGame().getId());
	}
	
	private void showPlayers() {
		showGame(backend.me().getCurrentGame());
	}
	
	private void showGame(Game g) {
		System.out.println("Game #"+g.getId());
		System.out.print("Team 1: ");
		for (Player p: g.getPlayerByTeam(1)) System.out.print(p.getName()+" ");
		System.out.println();
		System.out.print("Team 2: ");
		for (Player p: g.getPlayerByTeam(2)) System.out.print(p.getName()+" ");
		System.out.println();
	}
 	public void run() throws UnknownHostException, IOException, RequestTimedOutException {
		System.out.print("Enter your name: ");
		Scanner stdIn = new Scanner(System.in);
		String line = stdIn.nextLine();
		backend.joinServer(host, port, line);
		System.out.println("Hello "+line);
		while(!line.equals("exit")) {
			line = stdIn.nextLine();
			String type = line.split("\t")[0];
			switch (type) {
				case "show_games":
					showGames();
				case "create_game":
					createGame();
				case "show_players":
					showPlayers();
				case "exit":
					break;
				default:
					System.out.println("Unknown command: "+line);;
			}
		}
		System.out.println("Bye.");
		stdIn.close();
		backend.shutdown();
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
	
	public static void main(String[] args) {
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		new CommandLine(host,port);
		
	}
}
