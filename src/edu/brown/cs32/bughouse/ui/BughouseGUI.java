package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;

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
	private BughouseBoard userBoard_, otherBoard_;
	private JTextArea messageBox_,clock_;
	private BackEnd backend_;

	public BughouseGUI(){
		super("Bughouse Chess");
	//	this.backend_ = backend;
		Container content = this.getContentPane();
		this.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
		content.add(setupGameView());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#notifyUserTurn()
	 *  Notifies this user that it is his/her turn
	 */
	@Override
	public void notifyUserTurn() {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#showEndGameMessage()
	 * notifies the user in the information box that the game has ended.
	 */
	@Override
	public void showEndGameMessage() {
		// TODO Auto-generated method stub
		
	}
	
	
	/*
	 * sets up the game view for the user.
	 */
	private JPanel setupGameView(){
		JPanel game = new JPanel(new BorderLayout());
		game.add(createBoard(),BorderLayout.CENTER); 
		game.add(createOptionMenu(), BorderLayout.EAST);
		game.add(createPieceHolder(), BorderLayout.SOUTH);
		return game;
	}
	
	
	/*
	 * creates the initial board for both the user's game and the user's team
	 * mate's game. Needs to check what color the player is playing as to get 
	 * the correct board
	 */
	private JComponent createBoard(){
		JTabbedPane boardContainer = new JTabbedPane();
		userBoard_ = new BughouseBoard(true);
		boardContainer.addTab("Your Game", userBoard_);
		otherBoard_ = new BughouseBoard(false);
		boardContainer.addTab("Other Game", otherBoard_);
		return boardContainer;
	}
	
	/*l
	 * sets up the option menu for users where information gets 
	 * displayed
	 */
	private JComponent createOptionMenu(){
		JPanel options = new JPanel();
		options.setPreferredSize(new Dimension(250,190));
		clock_ = new JTextArea();
		clock_.setPreferredSize(new Dimension(200,50));
		clock_.setEditable(false);
		clock_.setText("Time is ticking....");
		messageBox_ = new JTextArea();
		messageBox_.setPreferredSize(new Dimension(200,190));
		messageBox_.setEditable(false);
		messageBox_.setText("Template text");
		JButton quit  = new JButton("Click me!");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO call backend.quit();
				
			}
			
		});
		quit.setPreferredSize(new Dimension(100,60));
		options.add(clock_);
		options.add(messageBox_);
		options.add(quit);
		return options;
	}
	
	private JComponent createPieceHolder(){
		JScrollPane pieceHolderPanel = new JScrollPane();
		pieceHolderPanel.setPreferredSize(new Dimension(200,110));
		pieceHolderPanel.setBackground(Color.YELLOW);
		return pieceHolderPanel;
	}
	
	public static void main (String[] argv){
		new BughouseGUI();
	}


	@Override
	public void addPrisoner(int playerId, ChessPiece piece) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void movePiece(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		// TODO Auto-generated method stub
		
	}

	



}
