package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;

public class GameView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BughouseBoard userBoard_, otherBoard_;
	private ChessPieceImageFactory imgFactory_;
	private JTextArea messageBox_,clock_;
	private JScrollPane prison_;
	private List<ChessPiece> myPrisoners_;
	private BackEnd backend_;

	public GameView(BackEnd backend){
		super(new BorderLayout());
		backend_ = backend;
		myPrisoners_ = new ArrayList<>();
		imgFactory_ = new ChessPieceImageFactory();
		this.add(this.createBoard(), BorderLayout.CENTER);
		this.add(this.createOptionMenu(),BorderLayout.EAST);
		this.add(this.createPieceHolder(),BorderLayout.SOUTH);
	}
	
	
	public void addPrisoner (int playerID, ChessPiece prisoner){
		//TO DO : decide which player is getting the prisoner and add it 
		myPrisoners_.add(prisoner);
	}
	
	/*
	 * creates the initial board for both the user's game and the user's team
	 * mate's game. Needs to check what color the player is playing as to get 
	 * the correct board
	 */
	private JComponent createBoard(){
		JTabbedPane boardContainer = new JTabbedPane();
		userBoard_ = new BughouseBoard(backend_,imgFactory_,true);
		boardContainer.addTab("Your Game", userBoard_);
		otherBoard_ = new BughouseBoard(backend_,imgFactory_,false);
		boardContainer.addTab("Other Game", otherBoard_);
		return boardContainer;
	}
	
	/*
	 * sets up the option menu for users where information gets 
	 * displayed
	 */
	private JComponent createOptionMenu(){
		JPanel options = new JPanel();
		options.setPreferredSize(new Dimension(250,190));
		clock_ = new JTextArea();
		clock_.setPreferredSize(new Dimension(200,50));
		clock_.setFont(new Font("Serif", Font.PLAIN,32));
		clock_.setEditable(false);
		clock_.setText("5:00");
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
		prison_ = new JScrollPane();
		prison_.setPreferredSize(new Dimension(200,110));
		prison_.setBackground(Color.YELLOW);
		return prison_;
	}
	
	private void updatePrison(){
		for (ChessPiece piece : myPrisoners_){
			JLabel img = new JLabel();
		}
	}
}
