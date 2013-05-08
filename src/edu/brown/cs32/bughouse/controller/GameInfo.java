package edu.brown.cs32.bughouse.controller;

import java.util.*;

public class GameInfo {
	private int m_id; // game id
	private int m_ownerId; // playerId of game owner (has ability to start and stop game)
	private boolean m_isActive = true; // default to true because players can join game
	private Map<Integer, Integer[]> m_boards; // boardId -> [player1ID, player2ID]
	private int[] m_boardIds;
	private List<PlayerInfo> m_team1; // Team 1: {white, black}
	private List<PlayerInfo> m_team2; // Team 2: {white, black}
	
	// 0 -> Team 1 white, 1 -> Team 2 black, 2 -> Team 1 black, 3 -> Team 2 white
	private int m_turn = 0;

	public GameInfo(int id, int ownerId, int board1Id, int board2Id) {
		m_id = id;
		m_ownerId = ownerId;
		m_team1 = new ArrayList<PlayerInfo>(); // ids of players in team1
		m_team2 = new ArrayList<PlayerInfo>(); // ids of players in team2
		m_boards = new HashMap<Integer, Integer[]>();
		m_boardIds = new int[]{board1Id, board2Id};
		System.out.println("BOARD 1: " + board1Id + " BOARD 2: " + board2Id);
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
	
	/**
	 * Gets number of players currently in game
	 */
	public int numPlayers() {
		return m_team1.size() + m_team2.size();
	}
	
	/*
	 * Returns true if players can join team, false if not
	 */
	public boolean canJoinTeam(int teamNum) {
		if (teamNum == 1 && m_isActive && m_team1.size() < 2)
			return true;
		else if (teamNum == 2 && m_isActive && m_team2.size() < 2)
			return true;
		
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
		// return false if unable to add player to team
		if (!canJoinTeam(teamNum))
			return false;
		
		boolean isWhite;
		if (teamNum == 1) {
			if (m_team1.size() > 0) {
				isWhite = m_team1.get(0).getColor() ? false : true;
			} else {
				isWhite = true;
			}
			
			player.setColor(isWhite);
			m_team1.add(player);
		} else {
			if (m_team2.size() > 0) {
				isWhite = m_team2.get(0).getColor() ? false : true;
			} else {
				isWhite = true;
			}
			
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
		System.out.println("Set player " + p.getId() + " board: " + boardID);
		return p.getId();
	}

	/**
	 * Returns team number for player with given id
	 * @param id
	 */
	/*public int getPlayerTeam(int id) {
		if (m_team1.contains(id))
			return 1;
		else if (m_team2.contains(id))
			return 2;
		else
			return -1;
	}*/
	
	public int getPlayerTeam(int id) {
		for (PlayerInfo p: m_team1) if (p.getId()==id) return 1;
		for (PlayerInfo p: m_team2) if (p.getId()==id) return 2;
		return -1;
	}
	/**
	 * Returns playerID of player with next turn
	 * @return
	 */
	public int getNextTurn() {
		switch (m_turn) {
			case 0:
				m_turn++;
				return m_team1.get(0).getId();
			case 1:
				m_turn++;
				return m_team2.get(1).getId();
			case 2:
				m_turn++;
				return m_team1.get(1).getId();
			default:
				m_turn = 0;
				return m_team2.get(0).getId();
		}
	}

	/**
	 * Sets new owner in game and removes previous owner from game
	 * @param prevID ID of previous game owner
	 * @return playerID of new owner
	 */
	public int setNewOwner(int prevID) {
		int team = -1;
		PlayerInfo prev = null;
		for (PlayerInfo p : m_team1) {
			if (p.getId() == prevID) {
				prev = p;
				team = 1;
				break;
			}
		}
		
		// only check team 2 if owner not in team 1
		if (prev == null) {
			for (PlayerInfo p : m_team2) {
				if (p.getId() == prevID) {
					prev = p;
					team = 2;
					break;
				}
			}
		}
		
		// reset prev owner to remove from game
		prev.setBoardId(-1);
		prev.setGameId(-1);
		prev.setTeamId(-1);
		
		PlayerInfo p;
		if (team == 1) {
			// set to other team 1 player if still playing and change them to white
			if (m_team1.size() == 2) {
				p = m_team1.get(1);
				p.setColor(true);
			// otherwise set first player on team 2
			} else {
				p = m_team2.get(0);
			}
			m_team1.remove(prev);
		} else if (team == 2) {
			// set to first team 1 player if still playing
			if (m_team1.size() > 0) {
				p = m_team1.get(0);
			// otherwise set as remaining player on team 2
			} else {
				p = m_team2.get(1);
			}
			m_team2.remove(prev);
		} else {
			return -1;
		}
		
		m_ownerId = p.getId();
		return m_ownerId;
	}

	/**
	 * Removes player from game
	 * @param playerId
	 */
	public void removePlayer(PlayerInfo p) {
		if (m_team1.contains(p))
			m_team1.remove(p);
		else if (m_team2.contains(p))
			m_team2.remove(p);		
	}
}
