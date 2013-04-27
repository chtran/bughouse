package edu.brown.cs32.bughouse.ui;

import java.awt.Color;

import javax.swing.ImageIcon;

public class ChessPieceImageFactory {

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
