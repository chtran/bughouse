package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.brown.cs32.bughouse.exceptions.GameNotReadyException;
import edu.brown.cs32.bughouse.exceptions.RequestTimedOutException;
import edu.brown.cs32.bughouse.exceptions.TeamFullException;
import edu.brown.cs32.bughouse.exceptions.UnauthorizedException;
import edu.brown.cs32.bughouse.interfaces.BackEnd;
import edu.brown.cs32.bughouse.models.Game;
import edu.brown.cs32.bughouse.models.Player;

public class RoomMenu extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BackEnd backend_;
	private JList<String> list_;
	private JTextArea team1_, team2_;
	private List<Game> activeGames_;
	private JPanel roomList_, roomPanel_;
	private int selectedGameID_, selectedTeamID_;
	private BughouseGUI front_;
	private JScrollPane rooms_;
	private boolean lockScreen_;
	private JButton currentRoom_;
	
	public RoomMenu(BughouseGUI frame,BackEnd backend){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = frame;
		this.backend_ = backend;
		this.lockScreen_= false;
		try {
			this.add(gameInfo(), BorderLayout.EAST);
			this.add(getRooms(), BorderLayout.CENTER);
			this.add(userControl(), BorderLayout.SOUTH);
			
		}catch (IOException e){
			e.printStackTrace();
		}catch (RequestTimedOutException e){
			JOptionPane.showMessageDialog(this, "The connection to the server timed out", 
					"Connection time out", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public Box gameInfo() {
		Box gameinfo = Box.createVerticalBox(); 
		gameinfo.setVisible(false);
		team1_ = new JTextArea(3,20);
		team2_ = new JTextArea(3,20);
		team1_.setEditable(false);
		team2_.setEditable(false);
		JButton joinTeam1 =  new JButton("Join Team 1");
		joinTeam1.addActionListener(new JoinTeamListener(1));
		JButton joinTeam2 =  new JButton("Join Team 2");
		joinTeam2.addActionListener(new JoinTeamListener(2));
		gameinfo.add(team1_);
		gameinfo.add(joinTeam1);
		gameinfo.add(team2_);
		gameinfo.add(joinTeam2);
		return gameinfo;
		
	}
	
	public void displayGameInfoPanel(Game selected) throws IOException, RequestTimedOutException{
		team1_.setText(" ");
		team2_.setText(" ");
		team1_.getParent().setVisible(true);
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
		if (team1_.getParent().isVisible()){
			team1_.setText(" ");
			team2_.setText(" ");
			team1_.append("Team 1 :"+"\n");
			team2_.append("Team 2 :"+"\n");
			for (Player player : backend_.me().getCurrentGame().getPlayersByTeam(1)){
				team1_.append(player.getName()+"\n");
			}
			team1_.repaint();
			for (Player player2: backend_.me().getCurrentGame().getPlayersByTeam(2)){
				team2_.append(player2.getName()+"\n");
			}
			team2_.repaint();
		}
	}
	
	public JPanel getRooms() throws IOException, RequestTimedOutException{
		roomList_ = new JPanel(new BorderLayout());
		JLabel header =  new JLabel("List of active games to join");
		header.setFont(new Font("Serif", Font.PLAIN,24));
		roomList_.add(header,BorderLayout.NORTH);
		rooms_ = new JScrollPane();
		roomPanel_ = new JPanel(new GridLayout(0,1));
		rooms_.setViewportView(roomPanel_);
		roomList_.add(rooms_,BorderLayout.CENTER);
		if (!backend_.getActiveGames().isEmpty()){
			updateGames();
		}
		return roomList_;
	}
	
	public void updateGames () throws IOException, RequestTimedOutException{
		if (backend_.me()!= null ){
			System.out.println(backend_.me().getName());
		}
		activeGames_ = backend_.getActiveGames();
		roomPanel_.removeAll();
		for (Game game : activeGames_){
			int gameID = game.getId();
			JButton room =  new JButton("Room "+Integer.toString(gameID));
			room.setFont(new Font("Serif", Font.PLAIN,20));
			room.addActionListener(new ChooseRoomListener(game));
			roomPanel_.add(room);
		}
		displayGameInfo();
		roomPanel_.revalidate();
		roomPanel_.repaint();
	}
	
	
	public JPanel userControl() {
		JPanel buttonPanel = new JPanel();
		JButton create  = new JButton("Create Game");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!(lockScreen_)){
					try {
						backend_.createGame();
						lockScreen_ = true;
					} catch (IOException e2){
						e2.printStackTrace();
					}catch (RequestTimedOutException e1){
						JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
								"Connection time out", JOptionPane.ERROR_MESSAGE);
						lockScreen_ = false;
						return;
					}
				}
				
			}
		});
		JButton start = new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
					try {
						backend_.startGame();
						front_.gameStarted();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (RequestTimedOutException e1) {
						JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
								"Connection time out", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (GameNotReadyException e1) {
						JOptionPane.showMessageDialog(null, "The game does not have 4 players yet", 
								"Cannot start game", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (UnauthorizedException e1) {
						JOptionPane.showMessageDialog(null, "You are not authorized to that action", 
								"Authorization error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
			}
		});
		buttonPanel.add(create);
		buttonPanel.add(start);
		return buttonPanel;
		
	}
	
	private class ChooseRoomListener implements ActionListener{
		
		private Game game_;
		
		public ChooseRoomListener(Game game) {
			this.game_ = game;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (currentRoom_!= null){
				currentRoom_.setBorder(null);
			}
			currentRoom_ = (JButton) e.getSource();
			currentRoom_.setBorder(new LineBorder(Color.RED,3));
			selectedGameID_ = game_.getId();
			try {
				displayGameInfoPanel(game_);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (RequestTimedOutException e1) {
				JOptionPane.showMessageDialog(null, "You are not authorized to that action", 
						"Authorization error", JOptionPane.ERROR_MESSAGE);
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
			if (!lockScreen_){
				selectedTeamID_ = teamID_;
				try {
					backend_.joinGame(selectedGameID_, selectedTeamID_);
					lockScreen_ = true;
				} catch (IOException e1){
					e1.printStackTrace();
				}catch (RequestTimedOutException e1){
					JOptionPane.showMessageDialog(null, "You are not authorized to that action", 
							"Authorization error", JOptionPane.ERROR_MESSAGE);
					lockScreen_ = false;
				}catch(TeamFullException e1){
					JOptionPane.showMessageDialog(null, "You can't join that team", 
							"Team is full", JOptionPane.ERROR_MESSAGE);
					lockScreen_ = false;	
				}
			}
		}
		
	}

}
