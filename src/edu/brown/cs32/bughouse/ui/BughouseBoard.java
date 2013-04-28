package edu.brown.cs32.bughouse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;

public class BughouseBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private boolean isManipulable_;
	private JLabel source_, current_;
	private Icon piece_;
	private int originX_, originY_, destX_, destY_;
	private ChessPieceImageFactory imgFactory_;

	public BughouseBoard(ChessPieceImageFactory imgFactory, boolean isManipulable){
		super(new GridLayout(8,8,1,0));
		this.imgFactory_ = imgFactory;
		this.isManipulable_ = isManipulable;
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

	
/*
 * Helper method which sets up the pieces at the start of the game;
 */
	private JLabel createPiece(int row, int col){
		JLabel piece = new JLabel();
		piece.setPreferredSize(new Dimension(50,50));
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
		

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			current_ = (JLabel) arg0.getSource();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			source_  = (JLabel) arg0.getSource();
			JPanel square = (JPanel) source_.getParent();
			originX_ = (int) Math.round((square.getLocation().getX()-2)/69);
			originY_ = (int) Math.round((-square.getLocation().getY()-2)/68)+7;
			piece_ = source_.getIcon();
			System.out.println("Coordinates to send x "+ originX_ + " "+originY_);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO call backend move and catch errors
			if (source_ != null && (!(source_.equals(current_)))){
				JPanel curSquare = (JPanel) current_.getParent();
				destX_ = (int) Math.round((curSquare.getLocation().getX()-2)/69);
				destY_ = (int) Math.round((-curSquare.getLocation().getY()-2)/68)+7;
				System.out.println("Dest x "+destX_+ " "+destY_);
				/*try {
					 backend.move(originX_, originY_, destX_, destY_)
				}catch (IllegalMoveException e){
					source_ = null; 
					piece_ = null; 
					JDialog illegalMove = new JOptionPane("That move is illegal", ERROR_MESSAGE);
					return;
				}*/
				current_.setIcon(piece_);
				source_.setIcon(null);
				// notify backend/server/user that the current turn has ended
			}
		}
		
	}
	
}
