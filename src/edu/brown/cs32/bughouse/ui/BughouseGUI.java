package edu.brown.cs32.bughouse.ui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.net.UnknownHostException;

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
			e.printStackTrace();
		}
		content_ = this.getContentPane();
		content_.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
		content_.add(setupJoinServerMenu(),"Join");
		content_.add(setupRoomMenu(), "Rooms");
		content_.add(setupGameView(), "Game");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	public static void main(String[] argv){
		BughouseGUI ui = new BughouseGUI(argv);
		BughouseGUI ui2 = new BughouseGUI(argv);
	}
	
	@Override
	public void addPrisoner(int playerId, ChessPiece piece) {
		// TODO generate image for piece and add it the display + notify user
		game_.addPrisoner(playerId,piece);
	}

	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#notifyUserTurn()
	 *  Notifies this user that it is his/her turn
	 */
	@Override
	public void notifyUserTurn() {
		game_.notifyUser();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#showEndGameMessage()
	 * notifies the user in the information box that the game has ended.
	 */
	@Override
	public void showEndGameMessage() {
		game_.notifyEndGame();
		
		
	}
	
	/*
	 * sets up the game view for the user.
	 */
	private JPanel setupGameView(){
		game_ =  new GameView(backend_);
		return game_;
	}


	@Override
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		game_.pieceMoved(boardId,from_x, from_y,to_x,to_y);
		
		
	}

	@Override
	public void gameStarted() {
		CardLayout cards = (CardLayout) content_.getLayout();
		cards.show(content_, "Game");
		try {
			game_.getBoardID();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
					"Connection timed out", JOptionPane.ERROR_MESSAGE);
			return;
		} catch (GameNotReadyException e) {
			JOptionPane.showMessageDialog(null, "The game does not have 4 players yet", 
					"Cannot start game", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public void joinServer(){
		CardLayout cards = (CardLayout)content_.getLayout();
		cards.show(content_, "Rooms");
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
			rooms_.updateGames();
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
		// TODO Auto-generated method stub
		game_.updatePrison();
	}

	@Override
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x,
			int y) {
		// TODO Auto-generated method stub
		
	}

}
