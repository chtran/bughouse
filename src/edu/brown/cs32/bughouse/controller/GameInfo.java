package edu.brown.cs32.bughouse.controller;

import java.util.*;

public class GameInfo {
	private int m_id; // game id
	private int m_ownerId; // playerId of game owner (has ability to start and stop game)
	private boolean m_isActive = false;
	private int[] m_boardIds;
	private List<PlayerInfo> m_team1;
	private List<PlayerInfo> m_team2;
	
	private boolean m_nextColor = false; // always alternate between true/false after setting

	public GameInfo(int id, int ownerId, int board1Id, int board2Id) {
		m_id = id;
		m_ownerId = ownerId;
		m_boardIds = new int[] {board1Id, board2Id};
		m_team1 = new ArrayList<PlayerInfo>(); // ids of players in team1
		m_team2 = new ArrayList<PlayerInfo>(); // ids of players in team2
	}
	
	// Getters
	public int getId() { return m_id; }
	public int getOwner() { return m_ownerId; }
	public boolean isActive() { return m_isActive; }
	public int[] getBoardIds() { return m_boardIds; }
	
	// Setters
	public void setIsActive(boolean isActive) { m_isActive = isActive; }
	
	public void addPlayer(PlayerInfo player, int teamNum) {
		boolean isWhite;
		if (teamNum == 1) {
			if (m_team1.size() > 0)
				isWhite = m_team1.get(0).getColor() ? false : true;
			else
				isWhite = true;
			
			player.setColor(isWhite);
			m_team1.add(player);
		} else {
			if (m_team2.size() > 0)
				isWhite = m_team2.get(0).getColor() ? false : true;
			else
				isWhite = true;
			
			player.setColor(isWhite);
			m_team2.add(player);
		}
	}
}
