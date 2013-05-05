package edu.brown.cs32.bughouse.ui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;

/**
 * 
 * @author mp42
 *	BughouseGUI is a JFrame that holds all the different menus of in Bughouse.
 *	Each player only sees one JFrame with different menus implemented as JPanels
 *	laid out with the CardLayout manager. This is the only GUI class that should
 * 	expose methods to other logic classes. 
 */

public class BughouseGUI extends JFrame implements FrontEnd{

	private static final long serialVersionUID = 1L;
	private GameView game_;
	private BackEnd backend_;
	private RoomMenu rooms_;
	private Container content_;
	private ConnectToServerMenu joinServerMenu_;
	

	public BughouseGUI(String[] argv){
		super("Bughouse Chess");
		try {
			this.backend_ = new BughouseBackEnd(this,argv[0],new Integer(argv[1]));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
		}
		content_ = this.getContentPane();
		content_.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
		content_.add(setupJoinServerMenu(),"Join");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	public static void main(final String[] argv){
		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				BughouseGUI u1 = new BughouseGUI(argv);
			}
			
		};
		Runnable r2 = new Runnable() {

			@Override
			public void run() {
				BughouseGUI u1 = new BughouseGUI(argv);
			}
			
		};
		Runnable r3 = new Runnable() {

			@Override
			public void run() {
				BughouseGUI u1 = new BughouseGUI(argv);
			}
			
		};
		Runnable r4 = new Runnable() {

			@Override
			public void run() {
				BughouseGUI u1 = new BughouseGUI(argv);
			}
			
		};
		new Thread(r1).start();
		new Thread(r2).start();
		new Thread(r3).start();
		new Thread(r4).start();

	}
	
	/* 
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#addPrisoner(edu.brown.cs32.bughouse.models.ChessPiece)
	 * Notifies that the user has just received a new piece
	 */
	@Override
	public void addPrisoner(ChessPiece piece) {
		if (game_!= null){
			game_.addPrisoner(piece);
		}
	}

	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#notifyUserTurn()
	 *  Notifies this user that it is his/her turn
	 */
	@Override
	public void notifyUserTurn() {
		if (game_ != null){
			game_.notifyUser();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#showEndGameMessage()
	 * notifies the user in the information box that the game has ended.
	 */
	@Override
	public void showEndGameMessage(List<String> winners) {
		if (game_ != null){
		//	game_.notifyEndGame();
		}	
	}
	
	/*
	 * sets up the game view for the user.
	 */
	private JPanel setupGameView(){
		try {
			game_ =  new GameView(backend_);
			return game_;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (RequestTimedOutException e) {
			JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
					"Connection timed out", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (GameNotReadyException e) {
			JOptionPane.showMessageDialog(null, "The game does not have 4 players yet", 
					"Cannot start game", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}


	@Override
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		if (game_ != null){
			game_.pieceMoved(boardId,from_x, from_y,to_x,to_y);
		}
		
	}

	@Override
	public void gameStarted() {
		content_.add(setupGameView(), "Game");
		this.displayCard("Game");
		
	}
	
	public void resetRoomMenu(){
		rooms_.reset();
	}
	
	public void displayCard(String cardName){
		CardLayout cards = (CardLayout) content_.getLayout();
		cards.show(content_,cardName);
	}
	
	public void joinServer(){
		content_.add(setupRoomMenu(), "Rooms");
		this.displayCard("Rooms");
		System.out.println("Just displayed card for rooms");
	}
	
	private JPanel setupJoinServerMenu(){
		joinServerMenu_ = new ConnectToServerMenu(this,backend_);
		return joinServerMenu_;
	}
	
	private JPanel setupRoomMenu(){
		rooms_ = new RoomMenu(this,backend_); // not clean way - get server to broadcast to all 
		return rooms_;
	}

	@Override
	public void gameListUpdated() {
		try {
			if (rooms_ != null){
				rooms_.updateGames();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			JOptionPane.showMessageDialog(null, "Connection to the server timed out", 
					"Time out error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void prisonersUpdated() {
		if (game_!= null){
			game_.updatePrison();
		}
	}

	@Override
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x,
			int y) {
		if (game_!= null){
			game_.piecePut(boardId, playerId, piece, x, y);
		}
	}


	@Override
	public void notifyNewOwner(int gameId) {
		// TODO Auto-generated method stub
		
	}

}
