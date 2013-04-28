package edu.brown.cs32.bughouse.controller;

import java.util.*;

public class GameInfo {
	private int m_id; // game id
	private int m_ownerId; // playerId of game owner (has ability to start and stop game)
	private boolean m_isActive = true; // default to true because players can join game
	private Map<Integer, Integer[]> m_boards; // boardId -> [player1ID, player2ID]
	private int[] m_boardIds;
	private List<PlayerInfo> m_team1;
	private List<PlayerInfo> m_team2;

	public GameInfo(int id, int ownerId, int board1Id, int board2Id) {
		m_id = id;
		m_ownerId = ownerId;
		m_team1 = new ArrayList<PlayerInfo>(); // ids of players in team1
		m_team2 = new ArrayList<PlayerInfo>(); // ids of players in team2
		m_boards = new HashMap<Integer, Integer[]>();
		m_boardIds = new int[]{board1Id, board2Id};
	}
	
	// Getters
	public int getId() { return m_id; }
	public int getOwner() { return m_ownerId; }
	public boolean isActive() { return m_isActive; }
	public int[] getBoardIds() { return m_boardIds; }
	
	/*
	 * Returns true if players can join team, false if not
	 */
	public boolean canJoin() {
		if (m_isActive && (m_team1.size() < 2 || m_team2.size() < 2))
			return true;
		else
			return false;
	}
	
	// Setters
	public void setIsActive(boolean isActive) { m_isActive = isActive; }

	/**
	 * Adds specified player to specified team
	 * @param player
	 * @param teamNum
	 */
	public boolean addPlayer(PlayerInfo player, int teamNum) {
		System.out.printf("Adding %s to game %d in team %d\n",player.getName(),this.m_id,teamNum);
		// return false if unable to add player to game
		if (!canJoin())
			return false;
		
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
		return true;
	}
	
	/**
	 * Gets ids of all players in game
	 * @return list of ids
	 */
	public List<Integer> getPlayerIds() {
		List<Integer> players = new ArrayList<>();
		// get team 1
		for (PlayerInfo p : m_team1) {
			players.add(p.getId());
		}
		// get team 2
		for (PlayerInfo p : m_team2) {
			players.add(p.getId());
		}
		return players;
	}
	public List<Integer> getPlayerIdsByTeam(int team) {
		List<Integer> players = new ArrayList<>();
		List<PlayerInfo> list = (team==1) ? m_team1 : m_team2;
		// get team 1
		for (PlayerInfo p : list) {
			players.add(p.getId());
		}
		return players;
	}
	/**
	 * Sets gameID, boardID, team, and color to initialized
	 * values for all players in game (called when someone quits
	 * and this game gets deleted)
	 * @param id
	 */
	public void resetPlayers() {
		// reset team 1
		for (PlayerInfo p : m_team1) {
			p.setBoardId(-1);
			p.setGameId(-1);
			p.setTeamId(-1);
		}
		
		// reset team 2
		for (PlayerInfo p : m_team2) {
			p.setBoardId(-1);
			p.setGameId(-1);
			p.setTeamId(-1);
		}
	}
	
	/**
	 * Returns list of names of team one members
	 */
	public List<String> getTeamOne() {
		List<String> names = new ArrayList<>();
		for (PlayerInfo p : m_team1) {
			names.add(p.getName());
		}
		
		if (names.size() == 0)
			return null;
		else
			return names;
	}
	
	/**
	 * Returns list of names of team two members
	 */
	public List<String> getTeamTwo() {
		List<String> names = new ArrayList<>();
		for (PlayerInfo p : m_team2) {
			names.add(p.getName());
		}
		
		if (names.size() == 0)
			return null;
		else
			return names;
	}

	/**
	 * Assigns players to boards:
	 * BoardA:
		Team 1 white, Team 2 black
	   BoardB:
		Team 1 black, Team 2 white
	 */
	public void assignBoards() {
		// assign board 1
		Integer[] boardA = new Integer[2];
		boardA[0] = getPlayer(0, m_team1, m_boardIds[0]);
		boardA[1] = getPlayer(1, m_team2, m_boardIds[0]);
		m_boards.put(m_boardIds[0], boardA);
		
		// assign board 2
		Integer[] boardB = new Integer[2];
		boardA[0] = getPlayer(1, m_team1, m_boardIds[1]);
		boardA[1] = getPlayer(0, m_team2, m_boardIds[1]);
		m_boards.put(m_boardIds[1], boardB);		
	}
	
	/**
	 * Gets player of specified color from specified team
	 * @param color 0 = white, 1 = black
	 * @param team
	 * @param boardID id of board they are joining
	 * @return ID of player of that color
	 */
	private Integer getPlayer(int color, List<PlayerInfo> team, int boardID) {
		PlayerInfo p;
		if (color == 0)
			p = team.get(0).getColor() ? team.get(0) : team.get(1);
		else
			p = team.get(0).getColor() ? team.get(1) : team.get(0);
		
		p.setBoardId(boardID);
		return p.getId();
	}

	/**
	 * Returns team number for player with given id
	 * @param id
	 */
	public int getPlayerTeam(int id) {
		if (m_team1.contains(id))
			return 1;
		else if (m_team2.contains(id))
			return 2;
		else
			return -1;
	}
}
