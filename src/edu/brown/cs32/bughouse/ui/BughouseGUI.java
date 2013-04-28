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
import javax.swing.SwingUtilities;
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
	private GameView game_;
	private BackEnd backend_;
	private RoomMenu rooms_;
	

	public BughouseGUI(BackEnd backend){
		super("Bughouse Chess");
		this.backend_ = backend;
		Container content = this.getContentPane();
		content.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
	//	content.add(setupMainMenu());
		content.add(setupRoomMenu(backend_));
		content.add(setupGameView());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
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
	
	private JPanel setupMainMenu(){
		JPanel main = new JPanel(new BorderLayout());
		JPanel buttonGroup = new JPanel(new GridLayout(2,1));
		JButton multiplayer = new JButton("Multiplayer");
		multiplayer.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JButton button = (JButton)e.getSource();
				JFrame frame = (JFrame)SwingUtilities.getRoot(button);
				CardLayout card = (CardLayout) frame.getLayout();
				card.next(frame);
			}
		});
		JButton quit = new JButton("Quit");
		quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		JPanel buttonWrapper1 = new JPanel();
		JPanel buttonWrapper2 = new JPanel();
		buttonWrapper1.add(multiplayer);
		buttonWrapper2.add(quit);
		buttonGroup.add(buttonWrapper1);
		buttonGroup.add(buttonWrapper2);
		JLabel title = new JLabel("Welcome to Bughouse!");
		title.setFont(new Font("Serif", Font.BOLD, 48));
		main.add(title, BorderLayout.NORTH);
		main.add(buttonGroup,BorderLayout.CENTER);
		return main;
	}
	
	public static void main(String[] argv){
		new BughouseGUI(null);
	}
	
	
	/*
	 * sets up the game view for the user.
	 */
	private JPanel setupGameView(){
		game_ =  new GameView();
		return game_;
	}


	@Override
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStarted() {
		// TODO Auto-generated method stub
	}
	
	private JPanel setupRoomMenu(BackEnd backend){
		rooms_ = new RoomMenu(backend);
		return rooms_;
	}

	@Override
	public void gameListUpdated() {
		// TODO Auto-generated method stub
		rooms_.updateGames();
	}

	



}
