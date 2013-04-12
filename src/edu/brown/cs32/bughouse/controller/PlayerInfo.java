package edu.brown.cs32.bughouse.controller;

/**
 * Info that server needs to know about each player
 * @author mackenzie
 *
 */
public class PlayerInfo {
	private String m_name; // player name
	private int m_id;
	private boolean m_isWhite;
	private int m_teamId = -1; // initialized to -1 if not on team
	private int m_gameId; // initialized to -1 if not in game
	
	public PlayerInfo(String name, int id) {
		m_name = name;
		m_id = id;
	}
	
	// Getters
	public int getId() { return m_id; }
	public int getTeamId() { return m_teamId; }
	public int getGameId() { return m_gameId; }
	public String getName() { return m_name; }
	public boolean getColor() { return m_isWhite; }
	
	// Setters
	public void setColor(boolean isWhite) { m_isWhite = isWhite; }
	public void setTeamId(int id) { m_teamId = id; }
	public void setGameId(int id) { m_gameId = id; }
}
