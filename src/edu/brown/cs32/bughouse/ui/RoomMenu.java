package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
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
	private JPanel roomList_;
	private int selectedGameID_, selectedTeamID_;
	private BughouseGUI front_;
	
	public RoomMenu(BughouseGUI frame,BackEnd backend){
		super();
		this.setLayout(new BorderLayout());
		this.front_ = frame;
		this.backend_ = backend;
		this.add(getRooms(), BorderLayout.CENTER);
		this.add(userControl(), BorderLayout.SOUTH);
		this.add(gameInfo(), BorderLayout.EAST);
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
	
	public void displayGameInfo(Game selected){
		try {
			team1_.setText(" ");
			team2_.setText(" ");
			List<Player> team1 = selected.getPlayersByTeam(1);
			List<Player> team2 = selected.getPlayersByTeam(2);
			team1_.getParent().setVisible(true);
			team1_.append("Team 1 :"+"\n");
			team2_.append("Team 2 :"+"\n");
			for (Player player : team1){
				team1_.append(player.getName()+"\n");
			}
			for (Player player: team2){
				team2_.append(player.getName()+"\n");
			}
			team1_.repaint();
			team2_.repaint();
		} catch (IOException | RequestTimedOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public JPanel getRooms(){
		roomList_  = new JPanel();
		list_ = new JList<>();
		list_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (!list_.isSelectionEmpty()){
					selectedGameID_ = new Integer(list_.getSelectedValue());
					Game selected = activeGames_.get(list_.getSelectedIndex());
					displayGameInfo(selected);
				}
			}
				
				
			
		});
		roomList_.add(list_);
		try {
			if (backend_.getActiveGames().isEmpty()){
				list_.setEnabled(false);
				roomList_.add(new JLabel("No rooms available"));
				roomList_.repaint();
			}
			else {
				updateGames();
			}
		} catch (IOException | RequestTimedOutException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return roomList_;
	}
	
	public JPanel userControl() {
		JPanel buttonPanel = new JPanel();
		JButton create  = new JButton("Create Game");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					backend_.createGame();
					updateGames();
				} catch (IOException | RequestTimedOutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		JButton start = new JButton("Start Game");
		start.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
						try {
							backend_.startGame();
							front_.gameStarted();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (RequestTimedOutException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (GameNotReadyException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (UnauthorizedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
			}
		});
		buttonPanel.add(create);
		buttonPanel.add(start);
		return buttonPanel;
		
	}
	
	public void updateGames(){
		try {
			 list_.setEnabled(true);
		//	 roomList_.removeAll();
		     DefaultListModel<String> options = new DefaultListModel<>();
			 activeGames_ = backend_.getActiveGames();
			 for (Game activeGame : activeGames_){
					options.addElement(Integer.toString(activeGame.getId()));
				}
			 list_.setModel(options);
		} catch (IOException | RequestTimedOutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		roomList_.add(list_);
		roomList_.revalidate();
		roomList_.repaint();
		if (!(list_.isSelectionEmpty())){
			displayGameInfo(activeGames_.get(list_.getSelectedIndex()));
		}
		
	}
	
	private class JoinTeamListener implements ActionListener{
		
		private int teamID_;
		
		public JoinTeamListener(int teamID){
			this.teamID_ = teamID;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			selectedTeamID_ = teamID_;
			try {
				backend_.joinGame(selectedGameID_, selectedTeamID_);
				updateGames();
				//displayGameInfo(activeGames_.get(list_.getSelectedIndex()));
			} catch (IOException | RequestTimedOutException | TeamFullException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}

}
