package edu.brown.cs32.bughouse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.WrongColorException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;

public class BughouseBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ChessBoard chessBoard_;
	private boolean isManipulable_,turn_, isPuttingPrisoner_,isMovingPiece_, isHintOn_;
	private JLabel source_, current_;
	private Icon piece_;
	private int originX_, originY_, destX_, destY_, index_;
	private ChessPieceImageFactory imgFactory_;
	private BackEnd backend_;
	private JLabel [][] board_;
	private ChessPiece selectedPrisoner_;
	private JCheckBox hint_;
	private List<JLabel> suggestions_;


	public BughouseBoard(BackEnd backend, ChessPieceImageFactory imgFactory, boolean isManipulable,ChessBoard chessBoard){
		super(new GridLayout(8,8,1,0));
		this.chessBoard_ = chessBoard;
		this.imgFactory_ = imgFactory;
		this.board_ = new JLabel [8][8];
		this.backend_ = backend;
		this.isManipulable_ = isManipulable;
		this.isPuttingPrisoner_ = false;
		this.isHintOn_ = false;
		this.isMovingPiece_ = false;
		this.turn_ = false;
		this.setPreferredSize(new Dimension(400,400));
		for (int i = 0; i<8;i++){
			for (int j = 0; j<8;j++){
				JPanel box = new JPanel();
				Color background = ((i+j)%2==0) ? Color.WHITE : Color.GRAY;
				box.setBackground(background);
				box.setBorder(null);
				box.add(this.createPiece(i,j));
				this.add(box);		
				System.out.println(box.getMouseListeners());
			}
		}
	}
	
	/*
	 * Dummy constructor for a view-only board with no pieces
	 */
	public BughouseBoard(){
		super(new GridLayout(8,8,1,0));
		isManipulable_ = false;
		this.setPreferredSize(new Dimension(400,400));
		for (int i = 0; i<8;i++){
			for (int j = 0; j<8;j++){
				JPanel box = new JPanel();
				Color background = ((i+j)%2==0) ? Color.WHITE : Color.GRAY;
				box.setBackground(background);
				box.setBorder(null);
				this.add(box);		
			}
		}
	}
	
	public void startTurn(){
		turn_ = true;
		this.requestFocusInWindow();
		BughouseGUI.showMyPane(this, "It is your turn now"
				, JOptionPane.INFORMATION_MESSAGE);
		if (turn_){
			System.out.println("ITS YOUR TURN");
		}
		
	}
	
	public void notifyHintSelection() throws IOException, RequestTimedOutException{
		if (isHintOn_){
			isHintOn_ = false;
			for (JLabel square: suggestions_){
				JPanel parent = (JPanel) square.getParent();
				parent.setBorder(null);
			}
			return;
		}
		isHintOn_ = true;
		if (isMovingPiece_){
			showHintedMoves();
		}
	}
	
	public void piecePut(Icon piece,int playerId, int x, int y){
		board_[y][x].setIcon(piece);
		this.revalidate();
		this.repaint();
	}
	
	
	public void updatePieceMoved(int from_x, int from_y, int to_x, int to_y){
		Icon piece = board_[from_y][from_x].getIcon();
		board_[to_y][to_x].setIcon(piece);
		board_[from_y][from_x].setIcon(null);		
		System.out.printf("Just moved piece from %d,%d to %d,%d", from_x,from_y,to_x,to_y);
	}
	
	public void setPrisonertoPut(ChessPiece piece, int index, boolean flag)	{
		this.selectedPrisoner_ = piece;
		this.isPuttingPrisoner_ = flag;
		this.index_ = index;
	}
	
	public boolean isMyTurn(){
		return turn_;
	}
	
	private void showHintedMoves() throws IOException, RequestTimedOutException{
		suggestions_ = new ArrayList<>();
		for (int i =0;i<8;i++){
			for (int j=0;j<8;j++){
				if (backend_.canMove(backend_.me().getCurrentBoardId(), originX_, originY_, j, i)){
					suggestions_.add(board_[i][j]);
					JPanel parent = (JPanel) board_[i][j].getParent();
					parent.setBorder(new LineBorder(Color.GREEN,3));
				}
			}
		}
	}
	
/*
 * Helper method which sets up the pieces at the start of the game;
 */
	private JLabel createPiece(int row, int col){
		JLabel piece = new JLabel();
		piece.setPreferredSize(new Dimension(50,50));
		board_[7-row][col] = piece;
		if (isManipulable_){
			System.out.println("Panel has been added with InputListener");
			piece.addMouseListener(new UserInputListener());
			System.out.println("Added!");
		}

		ChessPiece chessPiece = chessBoard_.getPiece(col,7-row);
		if (chessPiece==null) return piece;
		Color c = chessPiece.isWhite() ? Color.WHITE : Color.BLACK;

		switch (chessPiece.getType()) {
		case 1:
			piece.setIcon(imgFactory_.getPawn(c));
			break;
		case 2:
			piece.setIcon(imgFactory_.getKnight(c));
			break;
		case 3:
			piece.setIcon(imgFactory_.getBishop(c));
			break;
		case 4:
			piece.setIcon(imgFactory_.getRook(c));
			break;
		case 5:
			piece.setIcon(imgFactory_.getQueen(c));
			break;
		case 6:
			piece.setIcon(imgFactory_.getKing(c));
			break;
		}
		return piece;
		
	}
	
	private class UserInputListener implements MouseListener {	
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			if (turn_){
				System.out.println("Detecting a mouse pressed event");
				JLabel label = (JLabel) arg0.getSource();
				JPanel parent = (JPanel) label.getParent();
				destX_ = (int) Math.round((parent.getLocation().getX()-2)/69);
				destY_ = (int) Math.round((-parent.getLocation().getY()-2)/68)+7;
				if (isPuttingPrisoner_){
					System.out.println("Putting down a prisoner");
					this.putDownPrisoner();
					return;
				}
				if (isMovingPiece_){
					if (source_.equals(label)){
						System.out.println("Deselecting a piece");
						this.deselectPiece();
						return;
					}
					System.out.println("Moving a piece");
					current_ = label;
					this.movePiece();
					return;
				}
				if (label.getIcon()!= null){
					try {
						if (backend_.isMine(destX_,destY_)){
							source_ = label;
							originX_ = destX_;
							originY_ = destY_;
							System.out.println("Grabbing a piece");
							this.grabPiece();
							return;
						}
					}catch (RequestTimedOutException e){
						BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
								, JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
				
		}
		

		
		private void deselectPiece(){
			isMovingPiece_ = false;
			piece_ = null;
			JPanel parent = (JPanel) source_.getParent();
			parent.setBorder(null);
			originX_ = -1;
			originY_ = -1;
			for (JLabel square: suggestions_){
				JPanel parents = (JPanel) square.getParent();
				parents.setBorder(null);
			}
		}
		
		private void grabPiece() throws IOException, RequestTimedOutException{
			isMovingPiece_ = true;
			piece_ = source_.getIcon();
			JPanel parent = (JPanel) source_.getParent();
			parent.setBorder(new LineBorder(Color.RED, 3));
			if (isHintOn_){
				showHintedMoves();
			}
		}
		
		private void movePiece(){
			System.out.println("Moving an existing piece on the board");
			System.out.println("Dest x "+destX_+ " "+destY_);
			System.out.println("Preparing to send move to backend");

				 try {
					turn_ = false;
					System.out.println("Turn is false");

					backend_.me().move(originX_, originY_, destX_, destY_);
					System.out.println("Sent move to backend");
				} catch (IllegalMoveException e) {
					turn_ = true;
					BughouseGUI.showMyPane(null,"That is an illegal move. Consider choosing another move",
							JOptionPane.ERROR_MESSAGE);			
				} catch (IOException e) {
					e.printStackTrace();
				} catch (RequestTimedOutException e) {
					BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
							, JOptionPane.ERROR_MESSAGE);
				}catch (WrongColorException e) {
					turn_ = true;
					e.printStackTrace();
					BughouseGUI.showMyPane(null,"You've attemped to move your opponent's piece. Please" +
							"move another chess piece that is you own", JOptionPane.ERROR_MESSAGE);
				}finally {
					JPanel originPanel = (JPanel)source_.getParent();
					JPanel destPanel = (JPanel) current_.getParent();
					originPanel.setBorder(null);
					destPanel.setBorder(null);
					isMovingPiece_ = false;
					for (JLabel square: suggestions_){
						JPanel parent = (JPanel) square.getParent();
						parent.setBorder(null);
					}
				}
		}
		
		private void putDownPrisoner(){
			System.out.println("Attempting to put prisoner down");
			try {
				backend_.me().put(index_,destX_,destY_);
				isPuttingPrisoner_ = false;
				turn_ = false;
			} catch (IllegalPlacementException e) {
				isPuttingPrisoner_ = true;
				turn_ = true;
				BughouseGUI.showMyPane(null,"That is an illegal move. Consider making another move.",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
				isPuttingPrisoner_ = true;
				turn_ = true;
			} catch (RequestTimedOutException e) {
				isPuttingPrisoner_ = true;
				turn_ = true;
				BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
						, JOptionPane.ERROR_MESSAGE);
			}
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0){
			if (turn_ && isMovingPiece_ && !isHintOn_){
				JLabel temp = (JLabel) arg0.getSource();
				if (!temp.equals(source_)){
					JPanel parent = (JPanel) temp.getParent();
					destX_ = (int) Math.round((parent.getLocation().getX()-2)/69);
					destY_ = (int) Math.round((-parent.getLocation().getY()-2)/68)+7;
					System.out.println("Entering "+destX_ + " "+destY_);
					try {
						if (backend_.canMove(backend_.me().getCurrentBoardId(), originX_, originY_,
								destX_, destY_)){
							parent.setBorder(new LineBorder(Color.GREEN,3));
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (RequestTimedOutException e) {
						BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
								, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
		@Override
		public void mouseClicked(MouseEvent arg0){}		
		
		@Override
		public void mouseReleased(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {
				if (turn_ && isMovingPiece_ && !isHintOn_){
					JLabel exited = (JLabel)arg0.getSource();
					JPanel parent = (JPanel)exited.getParent();
					if (!exited.equals(source_)){
						parent.setBorder(null);
						
					}
				}
		}
		
	}
	
	
}
