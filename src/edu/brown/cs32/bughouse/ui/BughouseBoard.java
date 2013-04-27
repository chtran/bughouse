package edu.brown.cs32.bughouse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class BughouseBoard extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private final java.net.URL W_PAWN = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wp.png");
	private final java.net.URL W_KNIGHT = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wn.png");
	private final java.net.URL W_BISHOP = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wb.png");
	private final java.net.URL W_ROOK = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wr.png");
	private final java.net.URL W_KING = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wk.png");
	private final java.net.URL W_QUEEN = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/wq.png");
	private final java.net.URL B_PAWN = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/bp.png");
	private final java.net.URL B_KNIGHT = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/bn.png");
	private final java.net.URL B_BISHOP = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/bb.png");
	private final java.net.URL B_ROOK = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/br.png");
	private final java.net.URL B_KING = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/bk.png");
	private final java.net.URL B_QUEEN = getClass().getResource("/edu/brown/cs32/bughouse/global/img/48/bq.png");
	private boolean isManipulable_;
	private JLabel source_, current_;
	private Icon piece_;

	public BughouseBoard(boolean isManipulable){
		super(new GridLayout(8,8,1,0));
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
				piece.setIcon(new ImageIcon(W_PAWN,"pawn"));	
				return piece;
		}
		if (row == 1){
			piece.setIcon(new ImageIcon(B_PAWN,"pawn"));	
			return piece;
		}
		if (row == 0 || row == 7) {
			switch(col){
			case 0: case 7:
				if (row ==7){
					piece.setIcon(new ImageIcon(W_ROOK,"rook"));
					return piece;
				}
				piece.setIcon(new ImageIcon(B_ROOK,"rook")); // add rook sprite
				break;
			
			case 1: case 6:
				if (row ==7){
					piece.setIcon(new ImageIcon(W_KNIGHT,"knight"));
					return piece;
				}
				piece.setIcon(new ImageIcon(B_KNIGHT,"knight")); // add knight sprite
				break;
				
			case 2: case 5:
				if (row ==7){
					piece.setIcon(new ImageIcon(W_BISHOP,"bishop")); 
					return piece;
				}
				piece.setIcon(new ImageIcon(B_BISHOP,"bishop")); // add bishop sprite
				break;
				
			case 3:
				if (row ==7){
					piece.setIcon(new ImageIcon(W_QUEEN,"queen"));
					return piece;
				}
				piece.setIcon(new ImageIcon(B_QUEEN,"queen")); // add queen sprite
				break;
			case 4: 
				if (row ==7){
					piece.setIcon(new ImageIcon(W_KING,"king")); 
					return piece;
				}
				piece.setIcon(new ImageIcon(B_KING,"king")); // add king sprite
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
			int originX = (int) Math.round((square.getLocation().getX()-2)/69);
			int originY = (int) Math.round((-square.getLocation().getY()-2)/68)+7;
			piece_ = source_.getIcon();
			System.out.println("Panel x "+square.getLocation().getX()+" "+square.getLocation().getY());
			System.out.println("Coordinates to send x "+ originX + " "+originY);
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			if (source_ != null && (!(source_.equals(current_)))){
				JPanel curSquare = (JPanel) current_.getParent();
				int destX = (int) Math.round((curSquare.getLocation().getX()-2)/69);
				int destY = (int) Math.round((-curSquare.getLocation().getY()-2)/68)+7;
				System.out.println("Dest x "+destX+ " "+destY);
				current_.setIcon(piece_);
				source_.setIcon(null);
			}
		}
		
	}
	
}
