package edu.brown.cs32.bughouse.ui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.brown.cs32.bughouse.client.BughouseBackEnd;
import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.interfaces.FrontEnd;
import edu.brown.cs32.bughouse.models.ChessPiece;

/**
 * 
 * @author mp42
 *	BughouseGUI is a JFrame that holds all the different menus of in Bughouse.
 *	Each player only sees one JFrame with different menus implemented as JPanels
 *	laid out with the CardLayout manager. This is the only GUI class that should
 * 	expose methods to other logic classes. 
 */

public class BughouseGUI extends JFrame implements FrontEnd{

	private static final long serialVersionUID = 1L;
	private GameView game_;
	private BackEnd backend_;
	private RoomMenu rooms_;
	private Container content_;
	private ConnectToServerMenu joinServerMenu_;
	

	public BughouseGUI(String[] argv){
		super("Bughouse Chess");
		try {
			this.backend_ = new BughouseBackEnd(this,argv[0],new Integer(argv[1]));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Failed to connect to the server.");
			System.exit(1);
		}
		content_ = this.getContentPane();
		content_.setLayout(new CardLayout());
		this.setPreferredSize(new Dimension(800,700));
		this.setResizable(false);
		content_.add(setupJoinServerMenu(),"Join");
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing (WindowEvent e){
				try {
					backend_.quit();
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (RequestTimedOutException e1) {
					showMyPane(content_, "The server timed out. Please check your connection",
							JOptionPane.ERROR_MESSAGE);

				}
				System.exit(0);
			}
		});
		this.pack();
		this.setVisible(true);
	}
	
	
	public static void main(final String[] argv){
		Runnable r1 = new Runnable() {

			@Override
			public void run() {
				BughouseGUI u1 = new BughouseGUI(argv);
			}
			
		};
		
		new Thread(r1).start();
	

	}
	
	
	/* 
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#addPrisoner(edu.brown.cs32.bughouse.models.ChessPiece)
	 * Notifies that the user has just received a new piece
	 */
	@Override
	public void addPrisoner(ChessPiece piece) {
		if (game_!= null){
			game_.addPrisoner(piece);
		}
	}

	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#notifyUserTurn()
	 *  Notifies this user that it is his/her turn
	 */
	@Override
	public void notifyUserTurn() {
		if (game_ != null){
			try {
				game_.notifyUser();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RequestTimedOutException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.brown.cs32.bughouse.interfaces.FrontEnd#showEndGameMessage()
	 * notifies the user in the information box that the game has ended.
	 */
	@Override
	public void showEndGameMessage(List<String> winners) {
		if (game_ != null){
			String message = new String("The game has ended. The winning team is "+ winners.get(0)+ 
					" and "+winners.get(1));
			JOptionPane.showMessageDialog(this, message, "Game over",
                    JOptionPane.INFORMATION_MESSAGE);
			game_.notifyEndGame();
			System.out.println("Game ended");
		}	
	}
	public synchronized static void showMyPane(final Component parent, final String text, final int type) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(parent, text, text,
                       type);
            }
        });
    }
	/*
	 * sets up the game view for the user.
	 */
	private JPanel setupGameView(){
		try {
			game_ =  new GameView(backend_,this);
			return game_;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (RequestTimedOutException e) {
			showMyPane(game_, "The server timed out. Please check your connection", JOptionPane.ERROR_MESSAGE);
			return null;
		} catch (GameNotReadyException e) {
			showMyPane(game_, "The game does not have 4 players yet", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}


	@Override
	public void pieceMoved(int boardId, int from_x, int from_y, int to_x,
			int to_y) {
		if (game_ != null){
			game_.pieceMoved(boardId,from_x, from_y,to_x,to_y);
		}
		
	}

	@Override
	public void gameStarted() {
		rooms_.displayPanel(false);
		content_.add(setupGameView(), "Game");
		this.displayCard("Game");
		
	}
	
	public void displayCard(String cardName){
		System.out.println("Request to change menu to " +cardName);
		CardLayout cards = (CardLayout) content_.getLayout();
		System.out.println("Getting layout");
		cards.show(content_,cardName);
		System.out.println("Displayed " +cardName);
	}
	
	public void joinServer(){
		content_.add(setupRoomMenu(), "Rooms");
		this.displayCard("Rooms");
		System.out.println("Just displayed card for rooms");
	}
	
	private JPanel setupJoinServerMenu(){
		joinServerMenu_ = new ConnectToServerMenu(this,backend_);
		return joinServerMenu_;
	}
	
	private JPanel setupRoomMenu(){
		rooms_ = new RoomMenu(this,backend_);
		return rooms_;
	}

	@Override
	public void gameListUpdated() {
		try {
			if (rooms_ != null){
				rooms_.updateGames();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			showMyPane(game_, "The server timed out. Please check your connection", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	@Override
	public void prisonersUpdated() {
		if (game_!= null){
			game_.updatePrison();
		}
	}

	@Override
	public void piecePut(int boardId, int playerId, ChessPiece piece, int x,
			int y) {
		if (game_!= null){
			game_.piecePut(boardId, playerId, piece, x, y);
		}
	}


	@Override
	public void notifyNewOwner(int gameId) {
		if (rooms_!= null){
			try {
				if (backend_.me().getCurrentGame().getOwnerId()== backend_.me().getId()){
					rooms_.showStartButton();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (RequestTimedOutException e) {

			}
		}
		showMyPane(this,"You are the new owner of game #"+gameId, JOptionPane.OK_OPTION);
	}

	@Override
	public void gameCanceled() {
		try {
			System.out.println("RECEIVED cancel game"+backend_.me().getName());
		} catch (IOException | RequestTimedOutException e) {
			e.printStackTrace();
		}
		if (game_!= null){
			game_.cancelGame();
		}
		
	}

	@Override
	public void updatePlayerList() {
		System.out.println("Call to update the player list in lobby");
		try {
			if(rooms_!= null){
				rooms_.updateGames();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			showMyPane(game_, "The server timed out. Please check your connection", JOptionPane.ERROR_MESSAGE);
		}
	}

}
