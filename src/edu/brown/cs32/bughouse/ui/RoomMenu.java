package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;

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
	private TableModel model_;
	private JTable table_;
	
	public RoomMenu(BughouseGUI frame,BackEnd backend){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = frame;
		this.backend_ = backend;
		//listOfRooms();
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
	
/*	public void listOfRooms(){
		table_ = new JTable(0,2){
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
		};
		table_.getTableHeader().setReorderingAllowed(false);
		model_ = table_.getModel();
		table_.getColumnModel().getColumn(0).setHeaderValue("Game Number");
		table_.getColumnModel().getColumn(1).setHeaderValue("No. of players");
		
	}*/
	
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
	
	public void showStartButton(){
		if (lobby_!= null){
			lobby_.showStartButton();
		}
	}
	
	public synchronized void displayGameInfoPanel(Game selected) throws IOException, RequestTimedOutException{
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
	
	
	public void displayPanel(boolean flag){
		if (lobby_!= null){
			lobby_.displayPanel(flag);
		}
	}
	

	public JPanel getRooms() throws IOException, RequestTimedOutException{
		roomList_ = new JPanel(new BorderLayout()); 
		JLabel header =  new JLabel("Welcome "+ backend_.me().getName()+". Here is a list of active games to join.");
		header.setFont(new Font("Serif", Font.PLAIN,18));
		roomList_.add(header,BorderLayout.NORTH);
		rooms_ = new JScrollPane();
	/*	rooms_ = new JScrollPane(table_);
		roomList_.add(rooms_, BorderLayout.CENTER);*/
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
		if (activeGames_.isEmpty()){
			gameinfo_.setVisible(false);
		}
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
			if  (gameID == selectedGameID_){
				displayGameInfoPanel(game);
			}
			System.out.println("Adding room to display for client named "+backend_.me().getName());
		}
		System.out.println("Refreshing view "+backend_.me().getName());
		if (lobby_!= null){
			lobby_.updateLobbyInfo();
		}
		System.out.println("Revalidating and repainting "+backend_.me().getName());
		roomPanel_.revalidate();
		System.out.println("Revalidated panel, now repainting");
		roomPanel_.repaint();
		System.out.println("repainted, requesting focus");
		this.requestFocusInWindow();
		System.out.println("Refreshed room menu. User should interact with UI now");
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
