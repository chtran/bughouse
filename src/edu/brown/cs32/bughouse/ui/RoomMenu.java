package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.Font;
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
		try {
			this.add(getRooms(), BorderLayout.CENTER);
			this.add(userControl(), BorderLayout.SOUTH);
			this.add(gameInfo(), BorderLayout.EAST);
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
	
	public void displayGameInfo(Game selected) throws IOException, RequestTimedOutException{
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
	}
	
	public JPanel getRooms() throws IOException, RequestTimedOutException{
		roomList_  = new JPanel();
		JLabel header = new JLabel("List of active games to join");
		header.setFont(new Font("Serif", Font.PLAIN, 24));
		roomList_.add(header);
		list_ = new JList<>();
		list_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!list_.isSelectionEmpty()){
					selectedGameID_ = new Integer(list_.getSelectedValue());
					Game selected = activeGames_.get(list_.getSelectedIndex());
					try {
						displayGameInfo(selected);
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (RequestTimedOutException e1) {
						JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
								"Connection time out", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}		
		});
		roomList_.add(list_);
			if (backend_.getActiveGames().isEmpty()){
				list_.setEnabled(false);
			}
			else {
				updateGames();
			}
		return roomList_;
	}
	
	public JPanel userControl() {
		JPanel buttonPanel = new JPanel();
		JButton create  = new JButton("Create Game");
		create.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					backend_.createGame();
					updateGames();
				} catch (IOException e2){
					e2.printStackTrace();
				}catch (RequestTimedOutException e1){
					JOptionPane.showMessageDialog(null, "The connection to the server timed out", 
							"Connection time out", JOptionPane.ERROR_MESSAGE);
					return;
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
	
	public void updateGames() throws IOException, RequestTimedOutException{

		 list_.setEnabled(true);
		 roomList_.remove(list_);
	     DefaultListModel<String> options = new DefaultListModel<>();
		 activeGames_ = backend_.getActiveGames();
		 for (Game activeGame : activeGames_){
				options.addElement(Integer.toString(activeGame.getId()));
			}
		 list_.setModel(options);
		roomList_.add(list_);
		list_.setSelectedValue(selectedGameID_, true);
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
			selectedTeamID_ = teamID_;
			try {
				backend_.joinGame(selectedGameID_, selectedTeamID_);
				updateGames();
				//displayGameInfo(activeGames_.get(list_.getSelectedIndex()));
			} catch (IOException e1){
				e1.printStackTrace();
			}catch (RequestTimedOutException e1){
				JOptionPane.showMessageDialog(null, "You are not authorized to that action", 
						"Authorization error", JOptionPane.ERROR_MESSAGE);
				return;
			}catch(TeamFullException e1){
				JOptionPane.showMessageDialog(null, "You can't join that team", 
						"Team is full", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
	}

}
