package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
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
	private int myBoardID_, otherBoardID_;

	public GameView(BackEnd backend){
		super(new BorderLayout());
		backend_ = backend;
		myPrisoners_ = new ArrayList<>();
		imgFactory_ = new ChessPieceImageFactory();
		this.add(this.createBoard(), BorderLayout.CENTER);
		this.add(this.createOptionMenu(),BorderLayout.EAST);
		this.add(this.createPieceHolder(),BorderLayout.SOUTH);
		this.notifyUser();
	}
	
	
	public void addPrisoner (int playerID, ChessPiece prisoner){
		//TO DO : decide which player is getting the prisoner and add it 
		myPrisoners_.add(prisoner);
	}
	
	public void notifyEndGame(){
		//To DO : Show a JDialog that the game has ended
	}
	
	public void notifyUser(){
		// To DO: Show a JDialog that it is the user's turn
		userBoard_.startTurn();
	}
	
	public void pieceMoved (int boardId, int from_x, int from_y, int to_x ,int to_y){
		//TO DO : update board identified by id's pieces
		if (boardId == myBoardID_){
			System.out.println("Moving piece");
			userBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
		else {
			otherBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
	}
	
	public void getBoardID(){
		try {
			myBoardID_ = backend_.me().getCurrentBoardId();
			Map<Integer, ChessBoard> boards;
			boards = backend_.getBoards();
			Iterator<Integer> ids = boards.keySet().iterator();
			while (ids.hasNext()){
				int id = ids.next();
				if (id != myBoardID_){
					otherBoardID_ = id;
					break;
				}
			}
		} catch (IOException | RequestTimedOutException | GameNotReadyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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
