package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
	private JPanel prison_;
	private List<ChessPiece> myPrisoners_;
	private BackEnd backend_;
	private int myBoardID_, otherBoardID_;
	private JPanel selectedPrisoner_;
	private Border unselectedPrisonerBorder_;

	public GameView(BackEnd backend){
		super(new BorderLayout());
		backend_ = backend;
		myPrisoners_ = new ArrayList<>();
		imgFactory_ = new ChessPieceImageFactory();
		this.add(this.createBoard(), BorderLayout.CENTER);
		this.add(this.createOptionMenu(),BorderLayout.EAST);
		this.add(this.createPieceHolder(),BorderLayout.SOUTH);
	}
	
	
	public void addPrisoner (ChessPiece prisoner){
		JOptionPane.showMessageDialog(userBoard_, "You have received a "+prisoner.getName()+" from your teammate!");

	}
	
	public void notifyEndGame(){
		JOptionPane.showMessageDialog(null, "Game Over", "Finished", JOptionPane.OK_OPTION);
		
	} 
	
	public void notifyUser(){
		userBoard_.startTurn();
	}
	
	public void pieceMoved (int boardId, int from_x, int from_y, int to_x ,int to_y){
		messageBox_.setText(" ");
		messageBox_.setText("This board id is "+ Integer.toString(myBoardID_)+"\n");
		messageBox_.append("This move is to update board "+ Integer.toString(boardId)+"\n");
		messageBox_.revalidate();
		messageBox_.repaint();
		if (boardId == myBoardID_){
			System.out.println("Moving piece");
			userBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
		else {
			otherBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
	}
	

	public void getBoardID() throws IOException, RequestTimedOutException, GameNotReadyException{
		myBoardID_ = backend_.me().getCurrentBoardId();
		for (ChessBoard board: backend_.getCurrentBoards()) {
			if (board.getId()!=myBoardID_) {
				otherBoardID_ = board.getId();
				return;
			}
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
		prison_ = new JPanel();
		prison_.setPreferredSize(new Dimension(200,110));
		return prison_;
	}
	
	public void updatePrison(){
		myPrisoners_ = backend_.getPrisoners(backend_.me().getId());
		prison_.removeAll();
		for (ChessPiece piece : myPrisoners_){
			JLabel img = new JLabel();
			img.setIcon(getIcon(piece));
			JPanel piecePanel = new JPanel();
			piecePanel.add(img);
			piecePanel.addMouseListener(new PrisonPieceListener(piece,myPrisoners_.indexOf(piece)));
			prison_.add(piecePanel);
		}
		prison_.revalidate();
		prison_.repaint();
	}
	
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x,
		int y) {
		if (boardId == myBoardID_){
			userBoard_.piecePut(this.getIcon(piece),playerId,x,y);
		}
		else {
			otherBoard_.piecePut(this.getIcon(piece),playerId,x,y);
		}
		updatePrison();
	}
	
	private Icon getIcon(ChessPiece piece){
		switch(piece.getType()){
		case 1:
			if (piece.isWhite()){
				return imgFactory_.getPawn(Color.WHITE);
			}
			return imgFactory_.getPawn(Color.BLACK);
		case 2:
			if (piece.isWhite()){
				return imgFactory_.getKnight(Color.WHITE);
			}
			return imgFactory_.getKnight(Color.BLACK);
		
		case 3:
			if (piece.isWhite()){
				return imgFactory_.getBishop(Color.WHITE);
			}
			return imgFactory_.getBishop(Color.BLACK);
		case 4:
			if (piece.isWhite()){
				return imgFactory_.getRook(Color.WHITE);
			}
			return imgFactory_.getRook(Color.BLACK);
		case 5:
			if (piece.isWhite()){
				return imgFactory_.getQueen(Color.WHITE);
			}
			return imgFactory_.getQueen(Color.BLACK);
		}
		return null;
	}
	
	
	private class PrisonPieceListener implements MouseListener{
		
		private ChessPiece piece_;
		private int index_;
		
		public PrisonPieceListener(ChessPiece piece, int index){
			this.piece_ = piece;
			this.index_= index;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			if (selectedPrisoner_ != null){
				selectedPrisoner_.setBorder(unselectedPrisonerBorder_);
				unselectedPrisonerBorder_ =  null;
			}
			selectedPrisoner_ = (JPanel) e.getSource();
			unselectedPrisonerBorder_ = selectedPrisoner_.getBorder();
			selectedPrisoner_.setBorder(new LineBorder(Color.RED,3));	
			userBoard_.setPrisonertoPut(piece_,index_);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
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
		
	}
}
