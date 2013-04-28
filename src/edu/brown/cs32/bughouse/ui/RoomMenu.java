package edu.brown.cs32.bughouse.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
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
	private JList<String> list_, team1_, team2_;
	private List<Game> activeGames_;
	private JPanel roomList_;
	private int selectedGameID_, selectedTeamID_;
	
	public RoomMenu(BackEnd backend){
		super();
		this.setLayout(new BorderLayout());
		this.backend_ = backend;
		this.add(getRooms(), BorderLayout.CENTER);
		this.add(userControl(), BorderLayout.SOUTH);
		this.add(gameInfo(), BorderLayout.EAST);
	}
	
	public JPanel gameInfo() {
		JPanel gameinfo = new JPanel(); 
		team1_ = new JList<>();
		team1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		team2_  = new JList<>();
		team2_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedTeamID_ = 1;
		gameinfo.add(team1_);
		gameinfo.add(team2_);
		return gameinfo;
		
	}
	
	public JPanel getRooms(){
		roomList_  = new JPanel();
		list_ = new JList<>();
		list_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				selectedGameID_ = new Integer(list_.getSelectedValue());
				Game selected = activeGames_.get(list_.getSelectedIndex());
				try {
					List<Player> team1 = selected.getPlayersByTeam(1);
					DefaultListModel<String> mates = new DefaultListModel<>();
					for (Player player:team1){
						mates.addElement(player.getName());
					}
					team1_.setModel(mates);
					List<Player> team2 = selected.getPlayersByTeam(2);
					DefaultListModel<String> mates2 = new DefaultListModel<>();
					for (Player player:team2){
						mates2.addElement(player.getName());
					}
					team2_.setModel(mates2);
					team2_.getParent().repaint();
				} catch (IOException | RequestTimedOutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		roomList_.add(list_);
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
		JButton join =  new JButton("Join Game");
		join.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					backend_.joinGame(selectedGameID_, selectedTeamID_);
				} catch (IOException | RequestTimedOutException
						| TeamFullException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton joinServer =  new JButton("Join Server");
		joinServer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try {
					backend_.joinServer("Player 4");
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RequestTimedOutException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		buttonPanel.add(joinServer);
		buttonPanel.add(create);
		buttonPanel.add(start);
		buttonPanel.add(join);
		return buttonPanel;
		
	}
	
	public void updateGames(){
		try {
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
		roomList_.repaint();
	}

}
