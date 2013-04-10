package edu.brown.cs32.bughouse.global;

import java.awt.*;
import javax.swing.*;
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

	public BughouseGUI(){
		super("Bughouse Chess");
		Container content = this.getContentPane();
		this.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,600));
		this.setResizable(false);
		content.add(setupGameView());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}
	
	/*
	 * GameView - what the user sees when playing a game. 
	 */
	private JPanel setupGameView(){
		JPanel game = new JPanel(new BorderLayout());
		game.add(createBoard(),BorderLayout.CENTER);
		game.add(new JLabel(" This is the options panel showing information"), BorderLayout.EAST);
		game.add(new JLabel("This is to show pieces that are available to the player to put down"), BorderLayout.SOUTH);
		return game;
	}
	
	private JComponent createBoard(){
		JTabbedPane boardContainer = new JTabbedPane();
		userBoard_ = new Board();
		boardContainer.addTab("Your Game", userBoard_);
		otherBoard_ = new Board();
		boardContainer.addTab("Other Game", otherBoard_);
		return boardContainer;
	}
	
	public static void main (String[] argv){
		new BughouseGUI();
	}

	@Override
	public void showEndGameMessage() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 
	 * @author mp42
	 *	Board class which graphically represents the a current 
	 *	game with pieces and players. A private class since no 
	 *	other class except BughouseGUI needs to know about it - might be implemented
	 *	as its own class in the future for separation between JFrame and its content
	 */
	
	private class Board extends JPanel {
		
		private static final long serialVersionUID = 1L;

		public Board() {
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
		
	}
	

}
