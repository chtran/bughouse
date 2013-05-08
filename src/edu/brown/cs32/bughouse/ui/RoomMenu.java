package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class RoomMenu extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BackEnd backend_;
	private JTextArea team1_, team2_;
	private List<Game> activeGames_;
	private JPanel roomList_, roomPanel_;
	private GameLobby lobby_;
	private int selectedGameID_, selectedTeamID_;
	private BughouseGUI front_;
	private JScrollPane rooms_;
	private JButton currentRoom_, joinTeam1_, joinTeam2_;
	private Box gameinfo_;
	
	public RoomMenu(BughouseGUI frame,BackEnd backend){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = frame;
		this.backend_ = backend;
		//this.add(listOfRooms(),BorderLayout.CENTER);
		try {
			this.add(gameInfo(), BorderLayout.EAST);
			this.add(getRooms(), BorderLayout.CENTER);
			this.add(userControl(), BorderLayout.SOUTH);	
		}catch (IOException e){
			e.printStackTrace();
		}catch (RequestTimedOutException e){
			BughouseGUI.showMyPane(this, "The server timed out. Please check your connection", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Box gameInfo() {
		gameinfo_ = Box.createVerticalBox(); 
		gameinfo_.setVisible(false);
		team1_ = new JTextArea(3,20);
		team2_ = new JTextArea(3,20);
		team1_.setEditable(false);
		team2_.setEditable(false);
		joinTeam1_ =  new JButton("Join Team 1");
		joinTeam1_.addActionListener(new JoinTeamListener(1));
		joinTeam2_ =  new JButton("Join Team 2");
		joinTeam2_.addActionListener(new JoinTeamListener(2));
		gameinfo_.add(team1_);
		gameinfo_.add(joinTeam1_);
		gameinfo_.add(team2_);
		gameinfo_.add(joinTeam2_);
		return gameinfo_;
		
	}
	
	public void displayGameInfoPanel(Game selected) throws IOException, RequestTimedOutException{
		team1_.setText(" ");
		team2_.setText(" ");
		gameinfo_.setVisible(true);
		team1_.append("Team 1 :"+"\n");
		team2_.append("Team 2 :"+"\n");
		for (Player player :selected.getPlayersByTeam(1)){
			team1_.append(player.getName()+"\n");
		}
		team1_.repaint();
		for (Player player2: selected.getPlayersByTeam(2)){
			team2_.append(player2.getName()+"\n");
		}
		team2_.repaint();
	}
	
	public void displayGameInfo() throws IOException, RequestTimedOutException{
		System.out.println("Displaying info for client named "+backend_.me().getName());
		if (gameinfo_.isVisible()){
			team1_.setText(" ");
			team2_.setText(" ");
			team1_.append("Team 1 :"+"\n");
			team2_.append("Team 2 :"+"\n");
			System.out.println("Printing team 1 for client named "+backend_.me().getName());
			if (gameinfo_.isVisible()){
			}
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
		System.out.println("Finished rendering text info for client named "+backend_.me().getName());
	}
	
	public void displayPanel(boolean flag){
		if (lobby_!= null){
			lobby_.displayPanel(flag);
		}
	}
	

	public JPanel getRooms() throws IOException, RequestTimedOutException{
		roomList_ = new JPanel(new BorderLayout());
		JLabel header =  new JLabel("List of active games to join");
		header.setFont(new Font("Serif", Font.PLAIN,24));
		roomList_.add(header,BorderLayout.NORTH);
		rooms_ = new JScrollPane();
		roomPanel_ = new JPanel(new GridBagLayout());
		rooms_.setViewportView(roomPanel_);
		roomList_.add(rooms_,BorderLayout.CENTER);
		if (!backend_.getActiveGames().isEmpty()){
			updateGames();
		}
		return roomList_;
	}
	
	public synchronized void updateGames () throws IOException, RequestTimedOutException{
		activeGames_ = backend_.getActiveGames();
		roomPanel_.removeAll();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(4, 2, 4, 2);
		for (Game game : activeGames_){
			int gameID = game.getId();
			System.out.println("Adding game id "+gameID + " to the list");
			JButton room =  new JButton("Room "+Integer.toString(gameID));
			room.setFont(new Font("Serif", Font.PLAIN,20));
			room.addActionListener(new ChooseRoomListener(game));
			c.gridy = activeGames_.indexOf(game);
			roomPanel_.add(room,c);
			System.out.println("Adding room to display for client named "+backend_.me().getName());
		}
		System.out.println("Refreshing view "+backend_.me().getName());
		this.displayGameInfo();
		if (lobby_!= null){
			lobby_.updateLobbyInfo();
		}
		System.out.println("Revalidating and repainting "+backend_.me().getName());
		roomPanel_.revalidate();
		roomPanel_.repaint();
	}
	
	public JPanel userControl() {
		JPanel buttonPanel = new JPanel();
		JButton create  = new JButton("Create Game");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						backend_.createGame();
						gameinfo_.setVisible(false);
						lobby_ = new GameLobby(backend_,front_);
						front_.add(lobby_,"Lobby");
						lobby_.updateLobbyInfo();
						front_.displayCard("Lobby");
					} catch (IOException e2){
						e2.printStackTrace();
					}catch (RequestTimedOutException e1){
						BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
								, JOptionPane.ERROR_MESSAGE);
						front_.displayCard("Rooms");
					}
				
			}
		});
		buttonPanel.add(create);
		return buttonPanel;
		
	}
	
	private class ChooseRoomListener implements ActionListener{
		
		private Game game_;
		private Border currentBorder_;
		
		public ChooseRoomListener(Game game) {
			this.game_ = game;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentRoom_!= null){
				currentRoom_.setBorder(currentBorder_);
				currentRoom_.repaint();
			}
			currentRoom_ = (JButton) e.getSource();
			currentBorder_ = currentRoom_.getBorder();
			currentRoom_.setBorder(new LineBorder(Color.RED,3));
			selectedGameID_ = game_.getId();
			try {
				displayGameInfoPanel(game_);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (RequestTimedOutException e1) {
				BughouseGUI.showMyPane(null, "The server timed out.Please check your connection"
						, JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
		
	}
	
	private class JoinTeamListener implements ActionListener{
		
		private int teamID_;
		
		public JoinTeamListener(int teamID){
			this.teamID_ = teamID;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
				selectedTeamID_ = teamID_;
				try {
					backend_.joinGame(selectedGameID_, selectedTeamID_);
					gameinfo_.setVisible(false);
					lobby_ = new GameLobby(backend_, front_);
					front_.add(lobby_,"Lobby");
					lobby_.updateLobbyInfo();
					front_.displayCard("Lobby");
				} catch (IOException e1){
					e1.printStackTrace();
				}catch (RequestTimedOutException e1){
					BughouseGUI.showMyPane(null, "The server timed out. Please check your connection",
							JOptionPane.ERROR_MESSAGE);
				}catch(TeamFullException e1){
					BughouseGUI.showMyPane(null, "You can't join that team - it's full ", JOptionPane.ERROR_MESSAGE);
				}
		}
		
	}

}
