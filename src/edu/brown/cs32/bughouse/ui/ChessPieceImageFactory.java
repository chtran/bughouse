package edu.brown.cs32.bughouse.ui;

import java.awt.Color;

import javax.swing.ImageIcon;

public class ChessPieceImageFactory {

	private final java.net.URL W_PAWN = Thread.currentThread().getContextClassLoader().getResource("rsrc/wp.png");
	private final java.net.URL W_KNIGHT = Thread.currentThread().getContextClassLoader().getResource("rsrc/wn.png");
	private final java.net.URL W_BISHOP = Thread.currentThread().getContextClassLoader().getResource("rsrc/wb.png");
	private final java.net.URL W_ROOK =Thread.currentThread().getContextClassLoader().getResource("rsrc/wr.png");
	private final java.net.URL W_KING = Thread.currentThread().getContextClassLoader().getResource("rsrc/wk.png");
	private final java.net.URL W_QUEEN = Thread.currentThread().getContextClassLoader().getResource("rsrc/wq.png");
	private final java.net.URL B_PAWN = Thread.currentThread().getContextClassLoader().getResource("rsrc/bp.png");
	private final java.net.URL B_KNIGHT = Thread.currentThread().getContextClassLoader().getResource("rsrc/bn.png");
	private final java.net.URL B_BISHOP = Thread.currentThread().getContextClassLoader().getResource("rsrc/bb.png");
	private final java.net.URL B_ROOK = Thread.currentThread().getContextClassLoader().getResource("rsrc/br.png");
	private final java.net.URL B_KING = Thread.currentThread().getContextClassLoader().getResource("rsrc/bk.png");
	private final java.net.URL B_QUEEN =Thread.currentThread().getContextClassLoader().getResource("rsrc/bq.png");
	
	
	public  ChessPieceImageFactory(){
		
	}
	
	public ImageIcon getPawn(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_PAWN, "pawn");
		}
		return new ImageIcon(W_PAWN, "pawn");
		
	}
	
	public ImageIcon getRook(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_ROOK,"rook");
		}
		return new ImageIcon(W_ROOK,"rook");
	}
	
	public ImageIcon getKnight(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_KNIGHT, "knight");
		}
		return new ImageIcon(W_KNIGHT,"knight");
	}	
	
	public ImageIcon getBishop(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_BISHOP, "bishop");
		}
		return new ImageIcon(W_BISHOP,"bishop");
	}
	
	public ImageIcon getKing(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_KING,"king");
		}
		return new ImageIcon(W_KING,"king");
	}
	
	public ImageIcon getQueen(Color color){
		if (color.equals(Color.BLACK)){
			return new ImageIcon(B_QUEEN, "queen");
		}
		return new ImageIcon(W_QUEEN, "queen");
	}
}
