package edu.brown.cs32.bughouse.controller;

import java.util.HashMap;
import java.util.Map;

public class ServerData {
	private Map<Integer, GameInfo> m_games;
	private Map<Integer, PlayerInfo> m_players;
	
	
	private int m_nextBoardId = 0; // increment every time after setting new boardId
	private int m_nextPlayerId = 0;
	private int m_nextGameId = 0;

	public ServerData() {
		m_games = new HashMap<Integer, GameInfo>();
		m_players = new HashMap<Integer, PlayerInfo>();
	}

	/**
	 * Adds player to game
	 * @param playerId
	 * @param gameId
	 */
	public void addPlayerToGame(int playerId, int gameId, int teamNum) {
		GameInfo g = m_games.get(gameId);
		PlayerInfo p = m_players.get(playerId);
		
		g.addPlayer(p, teamNum);
		p.setGameId(gameId);
		p.setTeamId(teamNum);
	}

	/**
	 * Adds game to server info
	 * @param ownerId
	 */
	public void addGame(int ownerId) {
		int board1Id = m_nextBoardId;
		m_nextBoardId += 2;
		GameInfo g = new GameInfo(m_nextGameId, ownerId, board1Id, board1Id+1);
		PlayerInfo p = m_players.get(ownerId);
		g.addPlayer(p, 1);
		
		m_games.put(m_nextGameId, g);
		m_nextGameId++;
	}

	/**
	 * Adds new user to the server data
	 * @param name Name of new player
	 */
	public void addPlayer(String name) {
		PlayerInfo p = new PlayerInfo(name, m_nextPlayerId);
		m_players.put(m_nextPlayerId, p);
		m_nextPlayerId++;
	}
}
