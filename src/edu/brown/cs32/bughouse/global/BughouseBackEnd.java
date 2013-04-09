package edu.brown.cs32.bughouse.global;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import edu.brown.cs32.bughouse.exceptions.IllegalMoveException;
import edu.brown.cs32.bughouse.exceptions.IllegalPlacementException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.Client;
import edu.brown.cs32.bughouse.models.ChessBoard;
import edu.brown.cs32.bughouse.models.ChessPiece;
import edu.brown.cs32.bughouse.models.Player;

public class BughouseBackEnd implements BackEnd {
	private String host;
	private String port;
	private Client client;
	private Player me;

	
	public BughouseBackEnd(String host, int port) throws UnknownHostException, IOException {
		this.client = new BughouseClient(host,port);
	}
	
	@Override
	public void move(int from_x, int from_y, int to_x, int to_y) throws IllegalMoveException {
		to_y = (isWhite) ? to_y :7-to_y;
		from_y = (isWhite) ? from_y :7-from_y;

		ChessPiece captured = currentBoard.move(from_x, from_y, to_x, to_y);
		if (captured!=null) {
			prisoners.add(captured);
			if (captured.isKing()) {
				isGameOver=true;
			}
		}
		return null;
	}

	@Override
	public Player[] isGameOver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChessBoard[] put(ChessPiece piece, int x, int y)	throws IllegalPlacementException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void quit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWhite(Player p) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return client;
	}

}
