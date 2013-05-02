package edu.brown.cs32.bughouse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.WrongColorException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;

public class BughouseBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ChessBoard chessBoard_;
	private boolean isManipulable_;
	private JLabel source_, current_;
	private Icon piece_;
	private int originX_, originY_, destX_, destY_;
	private ChessPieceImageFactory imgFactory_;
	private BackEnd backend_;
	private Border unselected_;
	private JLabel [][] board_;
	private boolean turn_;


	public BughouseBoard(BackEnd backend, ChessPieceImageFactory imgFactory, boolean isManipulable,ChessBoard chessBoard){
		super(new GridLayout(8,8,1,0));
		this.chessBoard_ = chessBoard;
		this.imgFactory_ = imgFactory;
		this.board_ = new JLabel [8][8];
		this.backend_ = backend;
		this.isManipulable_ = isManipulable;
		this.setPreferredSize(new Dimension(400,400));
		this.turn_ = false;
		for (int i = 0; i<8;i++){
			for (int j = 0; j<8;j++){
				JPanel box = new JPanel();
				Color background = ((i+j)%2!=0) ? Color.WHITE : Color.GRAY;
				box.setBackground(background);
				box.setBorder(null);
				box.add(this.createPiece(i,j));
				this.add(box);		
			}
		}
	}
	
	public void startTurn(){
		turn_ = true;
		JOptionPane.showMessageDialog(this, "Your turn");
	}
	
	
	public void updatePieceMoved(int from_x, int from_y, int to_x, int to_y){
		Icon piece = board_[from_y][from_x].getIcon();
		board_[to_y][to_x].setIcon(piece);
		board_[from_y][from_x].setIcon(null);		
		System.out.printf("Just moved piece from %d,%d to %d,%d", from_x,from_y,to_x,to_y);
	}
	
	
/*
 * Helper method which sets up the pieces at the start of the game;
 */
	private JLabel createPiece(int row, int col){
		JLabel piece = new JLabel();
		piece.setPreferredSize(new Dimension(50,50));
		board_[7-row][col] = piece;
		if (isManipulable_){
			piece.addMouseListener(new UserInputListener());

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
		private Border  currentSquareBorder_;
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			current_ = (JLabel) arg0.getSource();
			/*if (source_ != null && turn_){
				JPanel parent = (JPanel) current_.getParent();
				currentSquareBorder_ = parent.getBorder();
				parent.setBorder(new LineBorder(Color.GREEN,3));
			}*/
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			if (turn_){
				if (source_ != null){
					JPanel oldPanel = (JPanel) source_.getParent();
					oldPanel.setBorder(unselected_);
					unselected_ = null;
				}
				source_  = (JLabel) arg0.getSource();
				JPanel square = (JPanel) source_.getParent();
				unselected_ = square.getBorder();
				square.setBorder(new LineBorder(Color.RED,3));
				originX_ = (int) Math.round((square.getLocation().getX()-2)/69);
				originY_ = (int) Math.round((-square.getLocation().getY()-2)/68)+7;
				piece_ = source_.getIcon();
				System.out.println("Coordinates to send x "+ originX_ + " "+originY_);
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			if (source_ != null && (!(source_.equals(current_))) && turn_){
				JPanel curSquare = (JPanel) current_.getParent();
				destX_ = (int) Math.round((curSquare.getLocation().getX()-2)/69);
				destY_ = (int) Math.round((-curSquare.getLocation().getY()-2)/68)+7;
				System.out.println("Dest x "+destX_+ " "+destY_);
					 try {
						turn_ = false;
						backend_.me().move(originX_, originY_, destX_, destY_);
					} catch (IllegalMoveException e) {
						turn_ = true;
						JOptionPane.showMessageDialog(null, "That is an illegal move. Consider choosing another move", 
								"Illegal Move Error", JOptionPane.ERROR_MESSAGE);					
					} catch (IOException e) {
						e.printStackTrace();
					} catch (RequestTimedOutException e) {
						JOptionPane.showMessageDialog(null, "Connection to the server timed out", 
								"Timeout Error", JOptionPane.ERROR_MESSAGE);
					}catch (WrongColorException e) {
						// TODO Auto-generated catch block
						turn_ = true;
						JOptionPane.showMessageDialog(null, "You've attempted to move your opponent's piece. Please " +
								"move another chess piece of yours", 
								"Piece Error", JOptionPane.ERROR_MESSAGE);
					}finally {
						JPanel originPanel = (JPanel)source_.getParent();
						originPanel.setBorder(unselected_);
						unselected_ = null;
					}
			}
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			if (source_ != null){
				/*JLabel current = (JLabel) arg0.getSource();
				JPanel parent = (JPanel) current.getParent();
				parent.setBorder(currentSquareBorder_);
				currentSquareBorder_= null;*/
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	}
	
}
