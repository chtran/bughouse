package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
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
	private JFrame home_;
	private Container content_;
	private ConnectToServerMenu joinServerMenu_;
	

	public BughouseGUI(String[] argv){
		super("Bughouse Chess");
		try {
			this.backend_ = new BughouseBackEnd(this,argv[0],new Integer(argv[1]));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		BughouseGUI ui3 = new BughouseGUI(argv);
		BughouseGUI ui4 = new BughouseGUI(argv);
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
		// TODO create JoptionPane to tell user it is her/his turn
		game_.notifyUser();
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#showEndGameMessage()
	 * notifies the user in the information box that the game has ended.
	 */
	@Override
	public void showEndGameMessage() {
		// TODO Auto-generated method stub
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
		game_.getBoardID();
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
		rooms_.updateGames();
	}
	

	



}
