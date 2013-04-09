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
		game.add(new JLabel(" "), BorderLayout.EAST);
		game.add(new JLabel(" "), BorderLayout.SOUTH);
		game.add(new JLabel(" "), BorderLayout.NORTH);
		return game;
	}
	
	private JPanel createBoard(){
		JPanel board = new JPanel(new GridLayout(8,8,1,0));
		board.setPreferredSize(new Dimension(300,300));
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
		return board;
	}
	
	public static void main (String[] argv){
		new BughouseGUI();
	}

	@Override
	public void showEndGameMessage() {
		// TODO Auto-generated method stub
		
	}
	

}
