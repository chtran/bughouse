package edu.brown.cs32.bughouse.global;

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
	private Board userBoard_, otherBoard_;
	private JTextArea messageBox_,clock_;
	private BackEnd backend_;

	public BughouseGUI(){
		super("Bughouse Chess");
	//	this.backend_ = backend;
		Container content = this.getContentPane();
		this.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(true);
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
		//game.add(createBoard(backend)),BorderLayout.CENTER);
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
		userBoard_ = new Board();
		boardContainer.addTab("Your Game", userBoard_);
		otherBoard_ = new Board();
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

	
	
	
	/**
	 * 
	 * @author mp42
	 *	Board class which graphically represents the a current 
	 *	game with pieces and players. A private class since no 
	 *	other class except BughouseGUI needs to know about it - might be implemented
	 *	as its own class which is package private in 
	 *	the future for separation between BughouseGUI and its content
	 */
	
	private class Board extends JPanel {
		
		private static final long serialVersionUID = 1L;
		private final java.net.URL W_PAWN = getClass().getResource("img/48/wp.png");
		private final java.net.URL W_KNIGHT = getClass().getResource("img/48/wn.png");
		private final java.net.URL W_BISHOP = getClass().getResource("img/48/wb.png");
		private final java.net.URL W_ROOK = getClass().getResource("img/48/wr.png");
		private final java.net.URL W_KING = getClass().getResource("img/48/wk.png");
		private final java.net.URL W_QUEEN = getClass().getResource("img/48/wq.png");
		private final java.net.URL B_PAWN = getClass().getResource("img/48/bp.png");
		private final java.net.URL B_KNIGHT = getClass().getResource("img/48/bn.png");
		private final java.net.URL B_BISHOP = getClass().getResource("img/48/bb.png");
		private final java.net.URL B_ROOK = getClass().getResource("img/48/br.png");
		private final java.net.URL B_KING = getClass().getResource("img/48/bk.png");
		private final java.net.URL B_QUEEN = getClass().getResource("img/48/bq.png");

		public Board() {
			//To DO: get a reference to the backend
			super(new GridLayout(8,8,1,0));
			this.setPreferredSize(new Dimension(400,400));
			Color current = Color.GRAY;
			for (int i = 0; i<8;i++){
				for (int j = 0; j<8;j++){
					JPanel box = new JPanel();
					if (current == Color.GRAY){
						box.setBackground(Color.WHITE);
						current = Color.WHITE;
					}
					else {
						box.setBackground(Color.GRAY);
						current = Color.GRAY;
					}
					box.setBorder(null);
					if (i<2 || i>5){
						box.add(createPiece(i,j));
					}
					this.add(box);
				}
				if (current == Color.GRAY){
					current = Color.WHITE;
				}
				else {
					current = Color.GRAY;
				}
				
			}
		}
		
	/*
	 * Helper method which sets up the pieces at the start of the game;
	 */
		private JComponent createPiece(int row, int col){
			JLabel piece = new JLabel();	
			piece.setTransferHandler(new TransferHandler("icon"));
			piece.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					System.out.println("PRessed");
					JComponent source = (JComponent) e.getSource();
					TransferHandler dd = source.getTransferHandler();
					dd.exportAsDrag(source, e,TransferHandler.COPY);
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			if (row == 6){
					piece.setIcon(new ImageIcon(W_PAWN,"pawn"));	
					return piece;
			}
			if (row == 1){
				piece.setIcon(new ImageIcon(B_PAWN,"pawn"));	
				return piece;
			}
			else {
				switch(col){
				case 0: case 7:
					if (row ==7){
						piece.setIcon(new ImageIcon(W_ROOK,"rook"));
						return piece;
					}
					piece.setIcon(new ImageIcon(B_ROOK,"rook")); // add rook sprite
					break;
				
				case 1: case 6:
					if (row ==7){
						piece.setIcon(new ImageIcon(W_KNIGHT,"knight"));
						return piece;
					}
					piece.setIcon(new ImageIcon(B_KNIGHT,"knight")); // add knight sprite
					break;
					
				case 2: case 5:
					if (row ==7){
						piece.setIcon(new ImageIcon(W_BISHOP,"bishop")); 
						return piece;
					}
					piece.setIcon(new ImageIcon(B_BISHOP,"bishop")); // add bishop sprite
					break;
					
				case 3:
					if (row ==7){
						piece.setIcon(new ImageIcon(W_QUEEN,"queen"));
						return piece;
					}
					piece.setIcon(new ImageIcon(B_QUEEN,"queen")); // add queen sprite
					break;
				case 4: 
					if (row ==7){
						piece.setIcon(new ImageIcon(W_KING,"king")); 
						return piece;
					}
					piece.setIcon(new ImageIcon(B_KING,"king")); // add king sprite
					break;
				}
			}
					
			return piece;
		
		}
	}



}
