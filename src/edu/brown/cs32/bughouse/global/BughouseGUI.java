package edu.brown.cs32.bughouse.global;

import java.awt.*;

import javax.swing.*;

import edu.brown.cs32.bughouse.interfaces.FrontEnd;

public class BughouseGUI extends JFrame implements FrontEnd{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		//game.add(new JLabel(" "), BorderLayout.NORTH);
		return game;
	}
	
	private JComponent createBoard(){
		JTabbedPane boardContainer = new JTabbedPane();
		JPanel board = new JPanel(new GridLayout(8,8,1,0));
		board.setPreferredSize(new Dimension(400,400));
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
				board.add(box);
			}
			if (current == Color.GRAY){
				current = Color.WHITE;
			}
			else {
				current = Color.GRAY;
			}
			
		}
		boardContainer.addTab("Your Game", board);
		boardContainer.addTab("Other Game", new JLabel("This is the other board"));
		return boardContainer;
	}
	
	public static void main (String[] argv){
		new BughouseGUI();
	}

	@Override
	public void showEndGameMessage() {
		// TODO Auto-generated method stub
		
	}
	

}
