package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.Player;

public class GameLobby extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BackEnd backend_;
	private JPanel infoArea_;
	private JTextArea team1_;
	private JTextArea team2_;
	private BughouseGUI front_;
	private boolean isDisplayed_;
	private ChessPieceImageFactory imgFactory_;
	
	
	public GameLobby(BackEnd backend, BughouseGUI front){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = front;
		this.backend_ =backend;
		this.isDisplayed_ =true;
		JLabel label = new JLabel ("Waiting for other players to join....");
		label.setFont(new Font ("Serif", Font.PLAIN,28));
		try {
			this.add(label, BorderLayout.NORTH);
			this.add(new BughouseBoard(),BorderLayout.CENTER);
			this.add(this.setupInfoArea(),BorderLayout.EAST);
			this.add(this.setupButtonPanel(),BorderLayout.SOUTH);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RequestTimedOutException e) {
			BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
					, JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	
	public synchronized void updateLobbyInfo() throws IOException, RequestTimedOutException{
		if (isDisplayed_){
			team1_.setText(" ");
			team2_.setText(" ");
			team1_.append("Team 1 :"+"\n");
			team2_.append("Team 2 :"+"\n");
			System.out.println("Printing in game lobby");
			System.out.println("Printing team 1 for client named "+backend_.me().getName());
			List<Player> team = backend_.me().getCurrentGame().getPlayersByTeam(1);
			System.out.println("TEAM SIZE: "+team.size());
			for (Player player : team){
				System.out.println("Printing player's name "+player.getName());
				team1_.append(player.getName()+"\n");
			}
			team1_.repaint();
			System.out.println("Printing team 2 for client named "+backend_.me().getName());
			team = backend_.me().getCurrentGame().getPlayersByTeam(2);
			for (Player player2: team){
				System.out.println("Printing player's name "+player2.getName());
				team2_.append(player2.getName()+"\n");
			}
			team2_.repaint();
		}
	}
	
	public void displayPanel(boolean flag){
		isDisplayed_ = flag;
	}
	
	private JPanel setupInfoArea() throws IOException, RequestTimedOutException{
		infoArea_ = new JPanel(new BorderLayout());
		JPanel info = new JPanel(new GridLayout(0,1));
		info.setPreferredSize(new Dimension(200,200));
		team1_ = new JTextArea(3,20);
		team2_ = new JTextArea(3,20);
		team1_.setEditable(false);
		team2_.setEditable(false);
		info.add(team1_);
		info.add(team2_);
		JPanel personal = new JPanel();
		JTextArea message = new JTextArea();
		message.setLineWrap(true);
		message.setPreferredSize(new Dimension(200,100));
		message.setWrapStyleWord(true);
		message.append("Hi there "+ backend_.me().getName()+ " !\n");
		message.append("Hang in there as we wait for 4 players to join the game and " +
				"then the owner will start the game");
		infoArea_.add(info,BorderLayout.NORTH);
		personal.add(message);
		infoArea_.add(personal,BorderLayout.CENTER);
		return infoArea_;
	}
	
	private JPanel setupButtonPanel(){
		JPanel buttonPanel = new JPanel();
		JButton start = new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						backend_.startGame();
						isDisplayed_ =false;
						backend_.frontEnd().gameStarted();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (RequestTimedOutException e1) {
						BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
								, JOptionPane.ERROR_MESSAGE);
						isDisplayed_ = true;
						return;
					} catch (GameNotReadyException e1) {
						BughouseGUI.showMyPane(null, "The game does not have 4 players yet"
								, JOptionPane.ERROR_MESSAGE);
						isDisplayed_ = true;
						return;
					} catch (UnauthorizedException e1) {
						BughouseGUI.showMyPane(null, "Only the owner can start the game."
								, JOptionPane.ERROR_MESSAGE);
						isDisplayed_ = true;
						return;
					}
					
			}
		});
		JButton cancel = new JButton("Leave Game");
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					isDisplayed_ = false;
					backend_.quit(); 
					front_.gameListUpdated();
					front_.displayCard("Rooms");
				} catch (IOException e1) {
					e1.printStackTrace();
					isDisplayed_ = false;
				} catch (RequestTimedOutException e1) {
					BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
							, JOptionPane.ERROR_MESSAGE);
					isDisplayed_ = false;
				}
			}
			
		});
		buttonPanel.add(start);
		buttonPanel.add(cancel);
		return buttonPanel;
	}

}
