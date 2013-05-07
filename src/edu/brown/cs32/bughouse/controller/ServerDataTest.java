package edu.brown.cs32.bughouse.controller;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ServerDataTest {
	ServerData m_data;
	int m_playerA;
	int m_playerB;
	int m_playerC;
	int m_playerD;

	@Before
	public void setUp() throws Exception {
		m_data = new ServerData();
		m_playerA = m_data.addPlayer("A");
		m_playerB = m_data.addPlayer("B");
		m_playerC = m_data.addPlayer("C");
		m_playerD = m_data.addPlayer("D");
		
		assertTrue(m_playerA >= 0 && m_playerB >= 0 && m_playerC >= 0 && m_playerD >= 0);
	}
	
	@Test
	public void testGetPlayerName() {
		assertTrue(m_data.getPlayerName(m_playerA).equals("A"));
		assertTrue(m_data.getPlayerName(m_playerB).equals("B"));
		assertTrue(m_data.getPlayerName(m_playerC).equals("C"));
		assertTrue(m_data.getPlayerName(m_playerD).equals("D"));
	}
	
	@Test
	public void testAddPlayer() {
		int playerId = m_data.addPlayer("Mackenzie");
		assertTrue(playerId >= 0);
		assertTrue(m_data.getPlayerName(playerId).equals("Mackenzie"));
	}

	@Test
	public void testAddGame() {
		int gameId = m_data.addGame(m_playerA);
		assertTrue(gameId >= 0);
	}
	
	@Test
	public void testGetCurrentGame() {
		int gameId = m_data.addGame(m_playerA);
		assertTrue(m_data.getCurrentGame(m_playerA) == gameId);
	}
	
	@Test
	public void testAddPlayerToGame() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 1);
		assertTrue(m_data.getCurrentGame(m_playerB) == gameId);
		assertTrue(m_data.getPlayerTeam(m_playerB) == 1);
	}

	@Test
	public void testGetGames() {
		int gameId = m_data.addGame(m_playerA);
		List<GameInfo> games = m_data.getGames();
		assertTrue(games != null);
		assertTrue(games.size() == 1);
		assertTrue(games.get(0).getId() == gameId);
	}

	@Test
	public void testGetPlayerIds() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		List<Integer> ids = m_data.getPlayerIds(gameId);
		assertTrue(ids != null);
		assertTrue(ids.size() == 2);
		assertTrue(ids.contains(m_playerA));
		assertTrue(ids.contains(m_playerB));
	}

	@Test
	public void testGetPlayerIdsByTeam() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		
		List<Integer> team1 = m_data.getPlayerIdsByTeam(gameId, 1);
		assertTrue(team1 != null);
		assertTrue(team1.size() == 1);
		assertTrue(team1.contains(m_playerA));
		
		List<Integer> team2 = m_data.getPlayerIdsByTeam(gameId, 2);
		assertTrue(team2 != null);
		assertTrue(team2.size() == 1);
		assertTrue(team2.contains(m_playerB));
	}
	
	@Test
	public void testStartGame() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		m_data.addPlayerToGame(m_playerC, gameId, 1);
		assertFalse(m_data.startGame(gameId));
		
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		assertTrue(m_data.startGame(gameId));
		assertFalse(m_data.gameIsActive(gameId));
	}
	
	@Test
	public void testGameIsActive() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		assertTrue(m_data.gameIsActive(gameId));
		
		m_data.addPlayerToGame(m_playerC, gameId, 1);
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		m_data.startGame(gameId);
		assertFalse(m_data.gameIsActive(gameId));
	}

	@Test
	public void testGetGameOwner() {
		int gameId = m_data.addGame(m_playerA);
		assertTrue(m_data.getGameOwner(gameId) == m_playerA);
	}

	@Test
	public void testGetBoards() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		m_data.addPlayerToGame(m_playerC, gameId, 1);
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		
		int[] boards = m_data.getBoards(gameId);
		assertTrue(boards != null);
		assertTrue(boards.length == 2);
	}

	@Test
	public void testIsWhite() {
		m_data.addGame(m_playerA);
		assertTrue(m_data.isWhite(m_playerA));
	}

	@Test
	public void testGetPlayerTeam() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		assertTrue(m_data.getPlayerTeam(m_playerA) == 1);
		assertTrue(m_data.getPlayerTeam(m_playerB) == 2);
	}

	@Test
	public void testGetPlayerBoard() {
		int gameId = m_data.addGame(m_playerA); // W, board 1
		m_data.addPlayerToGame(m_playerB, gameId, 2); // W, board 2
		m_data.addPlayerToGame(m_playerC, gameId, 1); // B, board 2
		m_data.addPlayerToGame(m_playerD, gameId, 2); // B, board 1
		m_data.startGame(gameId);
		
		assertTrue(m_data.getPlayerBoard(m_playerA) >= 0);
		assertTrue(m_data.getPlayerBoard(m_playerB) >= 0);
		assertTrue(m_data.getPlayerBoard(m_playerC) >= 0);
		assertTrue(m_data.getPlayerBoard(m_playerD) >= 0);
		
		assertTrue(m_data.getPlayerBoard(m_playerA) == m_data.getPlayerBoard(m_playerD));
		assertTrue(m_data.getPlayerBoard(m_playerB) == m_data.getPlayerBoard(m_playerC));
	}

	@Test
	public void testEndGame() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2); 
		m_data.addPlayerToGame(m_playerC, gameId, 1); 
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		m_data.startGame(gameId);
		
		m_data.endGame(gameId);
		assertTrue(m_data.getCurrentGame(m_playerA) == -1);
		assertTrue(m_data.getCurrentGame(m_playerB) == -1);
		assertTrue(m_data.getCurrentGame(m_playerC) == -1);
		assertTrue(m_data.getCurrentGame(m_playerD) == -1);
		
		assertTrue(m_data.getPlayerBoard(m_playerA) == -1);
		assertTrue(m_data.getPlayerBoard(m_playerB) == -1);
		assertTrue(m_data.getPlayerBoard(m_playerC) == -1);
		assertTrue(m_data.getPlayerBoard(m_playerD) == -1);
		
		assertTrue(m_data.getPlayerTeam(m_playerA) == -1);
		assertTrue(m_data.getPlayerTeam(m_playerB) == -1);
		assertTrue(m_data.getPlayerTeam(m_playerC) == -1);
		assertTrue(m_data.getPlayerTeam(m_playerD) == -1);
	}

	@Test
	public void testIsGameOwner() {
		m_data.addGame(m_playerA);
		assertTrue(m_data.isGameOwner(m_playerA));
	}

	@Test
	public void testGetNextTurn() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2); 
		m_data.addPlayerToGame(m_playerC, gameId, 1); 
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		m_data.startGame(gameId);
		
		assertTrue(m_data.getNextTurn(gameId) == m_playerA);
		assertTrue(m_data.getNextTurn(gameId) == m_playerD);
		assertTrue(m_data.getNextTurn(gameId) == m_playerC);
		assertTrue(m_data.getNextTurn(gameId) == m_playerB);
	}

	@Test
	public void testPlayerGameStarted() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2); 
		m_data.addPlayerToGame(m_playerC, gameId, 1); 
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		assertFalse(m_data.playerGameStarted(m_playerA));
		m_data.startGame(gameId);
		assertTrue(m_data.playerGameStarted(m_playerA));
	}

	@Test
	public void testNumPlayers() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 2);
		m_data.addPlayerToGame(m_playerC, gameId, 1); 
		m_data.addPlayerToGame(m_playerD, gameId, 2);
		assertTrue(m_data.numPlayers(gameId) == 4);
	}

	@Test
	public void testSetNewOwner() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 1);
		assertTrue(m_data.setNewOwner(gameId) == m_playerB);
		
		m_data.addPlayerToGame(m_playerA, gameId, 2);
		assertTrue(m_data.setNewOwner(gameId) == m_playerA);
	}

	@Test
	public void testRemovePlayerFromGame() {
		int gameId = m_data.addGame(m_playerA);
		m_data.addPlayerToGame(m_playerB, gameId, 1);
		m_data.removePlayerFromGame(m_playerB);
		assertTrue(m_data.getCurrentGame(m_playerB) == -1);
		assertFalse(m_data.getPlayerIds(gameId).contains(m_playerB));
	}

	@Test
	public void testDeletePlayer() {
		m_data.deletePlayer(m_playerA);
		assertTrue(m_data.getPlayerName(m_playerA) == null);
	}

}
