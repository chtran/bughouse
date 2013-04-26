package edu.brown.cs32.bughouse.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerData {
	private Map<Integer, GameInfo> m_games;
	private Map<Integer, PlayerInfo> m_players;

	private int m_nextBoardId = 1; // increment every time after setting new boardId
	private int m_nextPlayerId = 1;
	private int m_nextGameId = 1;

	public ServerData() {
		m_games = new HashMap<Integer, GameInfo>();
		m_players = new HashMap<Integer, PlayerInfo>();
	}

	/**
	 * Adds player to game
	 * @param playerId
	 * @param gameId
	 * @return true if successful, false if unable to add player to game
	 */
	public boolean addPlayerToGame(int playerId, int gameId, int teamNum) {
		GameInfo g = m_games.get(gameId);
		PlayerInfo p = m_players.get(playerId);
		if (g != null && p != null) {
			if (!g.addPlayer(p, teamNum))
				return false;
			
			p.setGameId(gameId);
			p.setTeamId(teamNum);
			return true;
		}
		return false;
	}

	/**
	 * Adds game to server info
	 * @param ownerId
	 */
	public int addGame(int ownerId) {
		int board1Id = m_nextBoardId;
		int gameId = m_nextGameId;
		m_nextGameId++;
		m_nextBoardId += 2;
		GameInfo g = new GameInfo(gameId, ownerId, board1Id, board1Id+1);
		m_games.put(gameId, g);
		addPlayerToGame(ownerId,gameId,1);

		return gameId;
	}

	/**
	 * Adds new user to the server data
	 * @param name Name of new player
	 */
	public PlayerInfo addPlayer(String name) {
		int id = m_nextPlayerId;
		m_nextPlayerId++;
		PlayerInfo p = new PlayerInfo(name, id);
		m_players.put(id, p);
		return p;
	}
	
	/**
	 * Gets all available games to join
	 * @return list of available games
	 */
	public List<GameInfo> getGames() {
		List<GameInfo> games = new ArrayList<GameInfo>();
		
		// iterate through games and send available ones
		GameInfo curr;
		for (Map.Entry<Integer, GameInfo> e : m_games.entrySet()) {
			curr = e.getValue();
			if (curr.canJoin())
				games.add(curr);
		}
		return games;
	}
	
	// TODO: figure out which one we actually want to send
	public List<Integer> getPlayerIds(int gameId) {
		GameInfo g = m_games.get(gameId);
		return g.getPlayerIds();
	}
	
	/**
	 * Returns true if game is active, false if not
	 * @param id ID of game
	 */
	public boolean gameIsActive(int id) {
		GameInfo g = m_games.get(id);
		return g.isActive();
	}

	/**
	 * Returns id of game owner
	 * @param gameId
	 */
	public int getGameOwner(int gameId) {
		GameInfo g = m_games.get(gameId);
		return g.getOwner();
	}
	
	/**
	 * Returns array of board IDs
	 * @param gameId
	 */
	public int[] getBoards(int gameId) {
		GameInfo g = m_games.get(gameId);
		return g.getBoardIds();
	}

	/**
	 *  - Check if there are 4 players in the game
        - Assign players to boards
        - Set game's active to false
	 * @return true if game started successfully, false if < 4 players in game and/or game not active
	 */
	public boolean startGame(int gameId) {
		GameInfo g = m_games.get(gameId);
		List<Integer> players = g.getPlayerIds();
		if (players != null && players.size() == 4 && g.isActive()) {
			g.setIsActive(false);
			g.assignBoards();
			return true;
		}
		return false;
	}

	/**
	 * Returns name of player with given ID
	 * @param id
	 */
	public String getPlayerName(int id) {
		PlayerInfo p = m_players.get(id);
		if (p != null)
			return p.getName();
		else
			return null;
	}

	/**
	 * Returns true if player ID is white, false if not
	 * @param id
	 */
	public boolean isWhite(int id) {
		PlayerInfo p = m_players.get(id);
		if (p.getColor())
			return true;
		else
			return false;
	}

	/**
	 * Return id of player's team
	 * @param id
	 */
	public int getPlayerTeam(int id) {
		PlayerInfo p = m_players.get(id);
		int g = p.getGameId();
		if (g > 0) {
			GameInfo game = m_games.get(g);
			return game.getPlayerTeam(id);
		}
		return -1;
	}

	/**
	 * Gets id of player's current board
	 * @param id
	 * @return board ID or -1 if not in game or assigned to board
	 */
	public int getPlayerBoard(int id) {
		PlayerInfo p = m_players.get(id);
		if (p != null)
			return p.getBoardId();
		else
			return -1;
	}

	/**
	 * Sets given player and all players in same game to initialized gameID, 
	 * boardID, and teamNum values and deletes game. If given player not in game,
	 * does nothing.
	 * @param id
	 */
	public void playerQuit(int id) {
		PlayerInfo p = m_players.get(id);
		int gameID;
		if ((gameID = p.getGameId()) > 0) {
			GameInfo g = m_games.get(gameID);
			g.resetPlayers();
			m_games.remove(g);
		}
	}
	
	public int getCurrentGame(int playerId) {
		return m_players.get(playerId).getGameId();
	}
}
