package edu.brown.cs32.bughouse.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
				box.add(createPiece(i, j));
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
	
	
	private void setManipulable(JComponent piece){
		piece.setTransferHandler(new TransferHandler("icon"));
		piece.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				System.out.println("PRessed");
				JComponent source = (JComponent) e.getSource();
				TransferHandler dd = source.getTransferHandler();
				dd.exportAsDrag(source, e,TransferHandler.COPY);
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
			
		});
	}
	
/*
 * Helper method which sets up the pieces at the start of the game;
 */
	private JComponent createPiece(int row, int col){
		JLabel piece = new JLabel();
		if (isManipulable_){ this.setManipulable(piece);}
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
	
}
