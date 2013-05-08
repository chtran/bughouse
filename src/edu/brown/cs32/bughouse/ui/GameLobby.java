package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Font;
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
	private Box infoArea_;
	private JTextArea team1_;
	private JTextArea team2_;
	private BughouseGUI front_;
	private boolean isDisplayed_;
	
	
	public GameLobby(BackEnd backend, BughouseGUI front){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = front;
		this.backend_ =backend;
		this.isDisplayed_ =true;
		JLabel label = new JLabel ("Waiting for other players to join");
		label.setFont(new Font ("Serif", Font.PLAIN,28));
		this.add(label, BorderLayout.NORTH);
		this.add(this.setupInfoArea(),BorderLayout.CENTER);
		this.add(this.setupButtonPanel(),BorderLayout.SOUTH);
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
	
	private Box setupInfoArea(){
		infoArea_ = Box.createVerticalBox();
		team1_ = new JTextArea(3,20);
		team2_ = new JTextArea(3,20);
		team1_.setEditable(false);
		team2_.setEditable(false);
		infoArea_.add(team1_);
		infoArea_.add(team2_);
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
		JButton cancel = new JButton("Cancel Game");
		cancel.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					isDisplayed_ = false;
					backend_.quit();  // --> client disconnects? Once fixed, just uncomment the two lines below
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
