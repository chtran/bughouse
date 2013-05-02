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
	
	private final Object m_lock = new Object();

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
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameId);
			PlayerInfo p = m_players.get(playerId);
			if (g != null && p != null) {
				if (p.getGameId() != -1)
					return false;
				
				if (!g.addPlayer(p, teamNum))
					return false;
				
				p.setGameId(gameId);
				p.setTeamId(teamNum);
				return true;
			}
			return false;
		}
	}

	/**
	 * Adds game to server info
	 * @param ownerId
	 * @return ID of new game or -1 if something went wrong
	 */
	public int addGame(int ownerId) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(ownerId);
			if (p == null)
				return -1;
			if (p.getGameId() != -1)
				return -1;
			
			int board1Id = m_nextBoardId;
			int gameId = m_nextGameId;
			m_nextGameId++;
			m_nextBoardId += 2;
			GameInfo g = new GameInfo(gameId, ownerId, board1Id, board1Id+1);
			g.addPlayer(p, 1);
			p.setTeamId(1);
			p.setGameId(gameId);
			m_games.put(gameId, g);
			return gameId;
		}
	}

	/**
	 * Adds new user to the server data
	 * @param name Name of new player
	 */
	public PlayerInfo addPlayer(String name) {
		synchronized (m_lock) {
			int id = m_nextPlayerId;
			m_nextPlayerId++;
			PlayerInfo p = new PlayerInfo(name, id);
			m_players.put(id, p);
			return p;
		}
	}
	
	/**
	 * Gets all available games to join
	 * @return list of available games
	 */
	public List<GameInfo> getGames() {
		synchronized (m_lock) {
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
	}
	
	// TODO: figure out which one we actually want to send
	/**
	 * Returns list of player IDs
	 * @param gameId
	 * @return list of player IDs for game or null if game does not exist
	 */
	public List<Integer> getPlayerIds(int gameId) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameId);
			if (g != null)
				return g.getPlayerIds();
			else
				return null;
		}
	}
	
	/**
	 * Returns list of player IDs on team
	 * @param gameId
	 * @return list of player IDs for team or null if game and/or team does not exist
	 */
	public List<Integer> getPlayerIdsByTeam(int gameId,int team) {
		GameInfo g = m_games.get(gameId);
		if (g != null)
			return g.getPlayerIdsByTeam(team);
		else
			return null;
	}
	/**
	 * Returns true if game is active, false if not
	 * @param id ID of game
	 * @return true if active, false if inactive or does not exist
	 */
	public boolean gameIsActive(int id) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(id);
			if (g != null)
				return g.isActive();
			else
				return false;
		}
	}

	/**
	 * Returns id of game owner
	 * @param gameId
	 */
	public int getGameOwner(int gameId) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameId);
			if (g != null)
				return g.getOwner();
			else
				return -1;
		}
	}
	
	/**
	 * Returns array of board IDs
	 * @param gameId
	 */
	public int[] getBoards(int gameId) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameId);
			if (g != null)
				return g.getBoardIds();
			else
				return null;
		}
	}

	/**
	 *  - Check if there are 4 players in the game
        - Assign players to boards
        - Set game's active to false
	 * @return true if game started successfully, false if < 4 players in game and/or game not active
	 */
	public boolean startGame(int gameId) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameId);
			List<Integer> players = g.getPlayerIds();
			if (players != null && players.size() == 4 && g.isActive()) {
				g.setIsActive(false);
				g.assignBoards();
				return true;
			}
			return false;
		}
	}

	/**
	 * Returns name of player with given ID
	 * @param id
	 */
	public String getPlayerName(int id) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(id);
			if (p != null)
				return p.getName();
			else
				return null;
		}
	}

	/**
	 * Returns true if player ID is white, false if not
	 * @param id Player id
	 * @return true if white, false if not or does not exist
	 */
	public boolean isWhite(int id) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(id);
			if (p != null && p.getColor())
				return true;
			else
				return false;
		}
	}

	/**
	 * Return id of player's team
	 * @param id
	 */
	public int getPlayerTeam(int id) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(id);
			if (p != null)
				return p.getTeamId();
			else
				return -1;
		}
	}

	/**
	 * Gets id of player's current board
	 * @param id
	 * @return board ID or -1 if not in game or assigned to board
	 */
	public int getPlayerBoard(int id) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(id);
			if (p != null)
				return p.getBoardId();
			else
				return -1;
		}
	}

	/**
	 * Sets given player and all players in same game to initialized gameID, 
	 * boardID, and teamNum values and deletes game. If given player not in game,
	 * does nothing.
	 * @param id
	 */
	public void playerQuit(int id) {
		synchronized (m_lock) {
			PlayerInfo p = m_players.get(id);
			if (p != null) {
				int gameID;
				if ((gameID = p.getGameId()) > 0) {
					GameInfo g = m_games.get(gameID);
					if (g != null) {
						g.resetPlayers();
						m_games.remove(g);
					}
				}
			}
		}
	}
	
	/**
	 * Determines if given player is game owner of current game
	 * @param playerId
	 * @return true if player is game owner, false if not owner or not currently in a game
	 */
	public boolean isGameOwner(int playerId) {
		PlayerInfo p = m_players.get(playerId);
		if (p != null) {
			int gameID = p.getGameId();
			if (gameID >= 0) {
				GameInfo g = m_games.get(gameID);
				if (g != null && g.getOwner() == p.getId())
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns id of player whose turn is next in game
	 * @param gameID
	 * @return id of next player or -1 if game does not exist
	 */
	public int getNextTurn(int gameID) {
		synchronized (m_lock) {
			GameInfo g = m_games.get(gameID);
			if (g != null)
				return g.getNextTurn();
			else
				return -1;
		}
	}
	
	/**
	 * Gets gameID of current player 
	 * @param playerId
	 * @return gameID or -1 if player not in game or player does not exist
	 */
	public int getCurrentGame(int playerId) {
		PlayerInfo p = m_players.get(playerId);
		if (p != null)
			return p.getGameId();
		else
			return -1;
	}

	/**
	 * Determines if given player is playing game in progress
	 * @param playerId
	 * @return true if player currently in game that has started, false if not
	 */
	public boolean playerGameStarted(int playerId) {
		PlayerInfo p = m_players.get(playerId);
		if (p != null) {
			int gameID = p.getGameId();
			GameInfo g = m_games.get(gameID);
			if (g != null && !g.isActive())
				return true;
		}
		return false;
	}

	/**
	 * Returns number of players in given game
	 * @param gameID
	 * @return
	 */
	public int numPlayers(int gameID) {
		GameInfo g = m_games.get(gameID);
		if (g != null) {
			return g.numPlayers();
		}
		return 0;
	}

	/**
	 * Sets new game owner and removes previous owner from game
	 * @param gameID
	 * @return playerID of new game owner
	 */
	public int setNewOwner(int gameID) {
		GameInfo g = m_games.get(gameID);
		if (g != null) 
			return g.setNewOwner();
		return -1;
	}

	/**
	 * Removes player from game
	 * @param playerId
	 */
	public void removePlayerFromGame(int playerId) {
		PlayerInfo p = m_players.get(playerId);
		int gameID;
		if ((gameID = p.getGameId()) >= 0) {
			GameInfo g = m_games.get(gameID);
			// TODO: fancy logic to remove this player from game and keep board assignment logic ok aka
			// shuffle people around
			 
			// reset player
			p.setBoardId(-1);
			p.setGameId(-1);
			p.setTeamId(-1);
		}
	}
}
