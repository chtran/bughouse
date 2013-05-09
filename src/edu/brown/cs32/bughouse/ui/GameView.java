package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Player;

public class GameView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BughouseBoard userBoard_, otherBoard_;
	private ChessPieceImageFactory imgFactory_;
	private JTextArea playerList_;
	private JPanel prison_,selectedPrisoner_, legendBox_;
	private List<ChessPiece> myPrisoners_;
	private BackEnd backend_;
	private int myBoardID_, otherBoardID_;
	private JTabbedPane boardContainer_;
	private BughouseGUI front_;

	public GameView(BackEnd backend, BughouseGUI front) throws IOException, RequestTimedOutException, GameNotReadyException{
		super(new BorderLayout());
		this.front_ = front;
		this.backend_ = backend;
		this.myPrisoners_ = new ArrayList<>();
		this.imgFactory_ = new ChessPieceImageFactory();
		this.setupBoardID();
		this.add(this.createBoard(), BorderLayout.CENTER);
		this.add(this.createOptionMenu(),BorderLayout.EAST);
		this.add(this.createPieceHolder(),BorderLayout.SOUTH);
		this.displayPlayerName();
	}
	
	public void switchBoard(int boardId) throws IOException, RequestTimedOutException{
		if (backend_.me().getCurrentBoardId() == boardId){
			boardContainer_.setSelectedIndex(0);
		}
		else {
			boardContainer_.setSelectedIndex(1);
		}
	}
	
	
	public void addPrisoner (ChessPiece prisoner){
		BughouseGUI.showMyPane(userBoard_, "You have received a "+prisoner.getName()+" " +
				"from your teammate!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void notifyEndGame() {
		front_.gameListUpdated();
		front_.displayCard("Rooms");
	} 
	
	public void notifyUser() throws IOException, RequestTimedOutException{
		userBoard_.startTurn();
	}
	
	public void cancelGame(){
		System.out.println("Received cancel request. Will now quit to the room menu");
		JOptionPane.showMessageDialog(this, "A player has left the game. The game will now quit", 
				"Game Canceled", JOptionPane.ERROR_MESSAGE);
		front_.displayCard("Rooms");
		System.out.println("displaying room menu");
	}
	
	public void pieceMoved (int boardId, int from_x, int from_y, int to_x ,int to_y){
		if (boardId == myBoardID_){
			System.out.println("Moving piece");
			userBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
		else {
			otherBoard_.updatePieceMoved(from_x, from_y, to_x, to_y);
		}
	}
	

	public void setupBoardID() throws IOException, RequestTimedOutException, GameNotReadyException{
		myBoardID_ = backend_.me().getCurrentBoardId();
		for (int boardId: backend_.getCurrentBoards().keySet()) {
			if (boardId!=myBoardID_) {
				otherBoardID_ = boardId;
				return;
			}
		}
	}
	
	public void displayPlayerName() throws IOException, RequestTimedOutException{
		List<Player> team1 = backend_.me().getCurrentGame().getPlayersByTeam(1);
		List<Player> team2 = backend_.me().getCurrentGame().getPlayersByTeam(2);
		playerList_.append("Team 1: \n");
		for (Player player : team1){
			playerList_.append(player.getName()+"\n");
		}
		playerList_.append("Team 2: \n");
		for (Player player: team2){
			playerList_.append(player.getName()+"\n");
		}
		
	}
	/*
	 * creates the initial board for both the user's game and the user's team
	 * mate's game. Needs to check what color the player is playing as to get 
	 * the correct board
	 */
	private JComponent createBoard(){
		boardContainer_ = new JTabbedPane();
		userBoard_ = new BughouseBoard(backend_,imgFactory_,true, backend_.getCurrentBoards().get(myBoardID_));
		boardContainer_.addTab("Your Game", userBoard_);
		otherBoard_ = new BughouseBoard(backend_,imgFactory_,false,backend_.getCurrentBoards().get(myBoardID_));
		boardContainer_.addTab("Other Game", otherBoard_);
		return boardContainer_;
	}
	
	
	private void createLegend(){
		JPanel header = new JPanel();
		JLabel headerMsg = new JLabel("Legend");
		headerMsg.setFont(new Font("Serif",Font.BOLD,12));
		header.add(headerMsg);
		legendBox_.add(header);
		JPanel row1 = new JPanel();
		JLabel userPiece = new JLabel("Pieces owned by you");
		JPanel square = new JPanel();
		square.setPreferredSize(new Dimension(20,20));
		square.setBorder(new LineBorder(Color.BLUE,2));
		row1.add(square);
		row1.add(userPiece);
		legendBox_.add(row1);
		JPanel row2 = new JPanel();
		JLabel userPiece2 = new JLabel("Currently selected piece");
		JPanel square2 = new JPanel();
		square2.setPreferredSize(new Dimension(20,20));
		square2.setBorder(new LineBorder(Color.RED,2));
		row2.add(square2);
		row2.add(userPiece2);
		legendBox_.add(row2);
		JPanel row3 = new JPanel();
		JLabel userPiece3 = new JLabel("<html>Valid moves for the<br>currently selected piece</html>", 
				SwingConstants.CENTER);
		JPanel square3 = new JPanel();
		square3.setPreferredSize(new Dimension(20,20));
		square3.setBorder(new LineBorder(Color.GREEN,2));
		row3.add(square3);
		row3.add(userPiece3);
		legendBox_.add(row3);
		legendBox_.setBorder(new LineBorder(Color.BLACK,1));
	}
	
	/*
	 * sets up the option menu for users where information gets 
	 * displayed
	 */
	private JComponent createOptionMenu() throws IOException, RequestTimedOutException{
		JPanel options = new JPanel(new BorderLayout());
		JPanel optionMiddle = new JPanel(new BorderLayout());
		options.setPreferredSize(new Dimension(250,190));
		playerList_ = new JTextArea();
		playerList_.setEditable(false);
		playerList_.setPreferredSize(new Dimension(200,110));
		playerList_.setBorder(new LineBorder(Color.BLACK,1));
		legendBox_ = new JPanel();
		legendBox_.setPreferredSize(new Dimension(200,190));
		this.createLegend();
		JButton quit  = new JButton("Quit Game");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					backend_.quit(); 
			} catch (IOException e1) {
					e1.printStackTrace();
				} catch (RequestTimedOutException e1) {
					BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
							, JOptionPane.ERROR_MESSAGE);
				}
				
			}
			
		});
		JCheckBox hints = new JCheckBox("Show me hints for moves", true);
		hints.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					userBoard_.notifyHintSelection();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (RequestTimedOutException e1) {
					BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
							, JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		quit.setPreferredSize(new Dimension(100,60));
		optionMiddle.add(hints,BorderLayout.CENTER);
		optionMiddle.add(legendBox_, BorderLayout. NORTH);
		options.add(optionMiddle,BorderLayout.CENTER);
		options.add(playerList_, BorderLayout.NORTH);
		options.add(quit,BorderLayout.SOUTH);
		return options;
	}
	
	private JComponent createPieceHolder(){
		JPanel prisonPanel = new JPanel(new BorderLayout());
		JLabel prisonPanelHeader = new JLabel("Pieces that you can put down");
		prisonPanel.add(prisonPanelHeader,BorderLayout.NORTH);
		prison_ = new JPanel();
		prison_.setPreferredSize(new Dimension(200,110));
		prisonPanel.add(prison_);
		return prisonPanel;
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
		Color c = (piece.isWhite()) ? Color.WHITE : Color.BLACK;
		switch(piece.getType()){
		case 1:
			return imgFactory_.getPawn(c);
		case 2:
			return imgFactory_.getKnight(c);
		case 3:
			return imgFactory_.getBishop(c);
		case 4:
			return imgFactory_.getRook(c);
		case 5:
			return imgFactory_.getQueen(c);
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
			if (userBoard_.isMyTurn()){
				JPanel current = (JPanel) e.getSource();
				if (selectedPrisoner_ != null){
					selectedPrisoner_.setBorder(null);
					if (selectedPrisoner_ == current){
						selectedPrisoner_ = null;
						userBoard_.setPrisonertoPut(null, 0, false);
						return;
					}					
				}
				selectedPrisoner_ = current;
				selectedPrisoner_.setBorder(new LineBorder(Color.RED,3));	
				userBoard_.setPrisonertoPut(piece_,index_,true);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
			
		}
		
	}
}
