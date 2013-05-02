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
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.WrongColorException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;

public class BughouseBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private boolean isManipulable_,turn_, isPuttingPrisoner_;
	private JLabel source_, current_;
	private Icon piece_;
	private int originX_, originY_, destX_, destY_, index_;
	private ChessPieceImageFactory imgFactory_;
	private BackEnd backend_;
	private Border unselected_;
	private JLabel [][] board_;
	private ChessPiece selectedPrisoner_;


	public BughouseBoard(BackEnd backend, ChessPieceImageFactory imgFactory, boolean isManipulable){
		super(new GridLayout(8,8,1,0));
		this.imgFactory_ = imgFactory;
		this.board_ = new JLabel [8][8];
		this.backend_ = backend;
		this.isManipulable_ = isManipulable;
		this.isPuttingPrisoner_ = false;
		this.setPreferredSize(new Dimension(400,400));
		this.turn_ = false;
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
				box.add(this.createPiece(i,j));
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
	
	public void startTurn(){
		turn_ = true;
		JOptionPane.showMessageDialog(this, "Your turn");
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
	
	public void setPrisonertoPut(ChessPiece piece, int index)	{
		this.selectedPrisoner_ = piece;
		this.isPuttingPrisoner_ = true;
		this.index_ = index;
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
		if (row == 6){
				piece.setIcon(imgFactory_.getPawn(Color.WHITE));	
				return piece;
		}
		if (row == 1){
			piece.setIcon(imgFactory_.getPawn(Color.BLACK));	
			return piece;
		}
		if (row == 0 || row == 7) {
			switch(col){
			case 0: case 7:
				if (row ==7){
					piece.setIcon(imgFactory_.getRook(Color.WHITE));
					return piece;
				}
				piece.setIcon(imgFactory_.getRook(Color.BLACK)); // add rook sprite
				break;
			
			case 1: case 6:
				if (row ==7){
					piece.setIcon(imgFactory_.getKnight(Color.WHITE));
					return piece;
				}
				piece.setIcon(imgFactory_.getKnight(Color.BLACK)); // add knight sprite
				break;
				
			case 2: case 5:
				if (row ==7){
					piece.setIcon(imgFactory_.getBishop(Color.WHITE)); 
					return piece;
				}
				piece.setIcon(imgFactory_.getBishop(Color.BLACK)); // add bishop sprite
				break;
				
			case 3:
				if (row ==7){
					piece.setIcon(imgFactory_.getQueen(Color.WHITE));
					return piece;
				}
				piece.setIcon(imgFactory_.getQueen(Color.BLACK)); // add queen sprite
				break;
			case 4: 
				if (row ==7){
					piece.setIcon(imgFactory_.getKing(Color.WHITE)); 
					return piece;
				}
				piece.setIcon(imgFactory_.getKing(Color.BLACK)); // add king sprite
				break;
			}
		}
		return piece;
		
	}
	
	private class UserInputListener implements MouseListener {	
		private Border  currentSquareBorder_;
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			current_ = (JLabel) arg0.getSource();
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			JLabel label = (JLabel) arg0.getSource();
			JPanel parent = (JPanel) label.getParent();
			int destX = (int) Math.round((parent.getLocation().getX()-2)/69);
			int destY = (int) Math.round((-parent.getLocation().getY()-2)/68)+7;
			if (isPuttingPrisoner_ && turn_){
				System.out.println("Attempting to put prisoner down");
				try {
					backend_.me().put(index_,destX,destY);
					turn_ = false;
					isPuttingPrisoner_ = false;
				} catch (IllegalPlacementException e) {
					turn_ = true;
					isPuttingPrisoner_ = true;
					JOptionPane.showMessageDialog(null, "That is an illegal move. Consider choosing another move", 
							"Illegal Move Error", JOptionPane.ERROR_MESSAGE);		
				} catch (IOException e) {
					e.printStackTrace();
					turn_ = true;
					isPuttingPrisoner_ = true;
				} catch (RequestTimedOutException e) {
					turn_ = true;
					isPuttingPrisoner_ = true;
					JOptionPane.showMessageDialog(null, "Connection to the server timed out", 
							"Timeout Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else
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
			JPanel curSquare = (JPanel) current_.getParent();
			destX_ = (int) Math.round((curSquare.getLocation().getX()-2)/69);
			destY_ = (int) Math.round((-curSquare.getLocation().getY()-2)/68)+7;
			 if (source_ != null && (!(source_.equals(current_))) && turn_){
				System.out.println("Moving an existing piece on the board");
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
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}
	}
	
}
