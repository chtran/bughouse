package edu.brown.cs32.bughouse.controller;

import java.io.*;
import java.net.*;
import java.util.List;

/**
 * Encapsulate IO for the given client {@link Socket}, with a group of
 * other clients in the given {@link ClientPool}.
 */
public class BughouseClientHandler extends Thread {
	private ClientPool m_pool;
	private Socket m_client;
	private BufferedReader m_input;
	private PrintWriter m_output;
	
	private ServerData m_data;
	private PlayerInfo m_playerInfo;
	
	/**
	 * Constructs a {@link BughouseClientHandler} on the given client with the given pool.
	 * 
	 * @param pool All clients currently connected to server
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if pool or client is null
	 */
	public BughouseClientHandler(ClientPool pool, Socket client, ServerData data) throws IOException {
		if (pool == null || client == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
		
		m_pool = pool;
		m_client = client;
		m_data = data;
		m_playerInfo = null;
		
		//TODO: Set up the buffered reader for the sockets to communicate with
		m_input = new BufferedReader(new InputStreamReader(client.getInputStream()));
		m_output = new PrintWriter(client.getOutputStream(), true);
	}
	
	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		String msg;
		String[] headerSplit;
		String[] msgSplit;
		int id;
		int team;
		try {
			while (true) {
				if ((msg = m_input.readLine()) != null) {
					System.out.println("RECEIVED: " + msg);
					if (m_playerInfo != null)
						System.out.println("m_playerInfo " + m_playerInfo.getId() + " " + m_playerInfo.getName());
					headerSplit = msg.split(":");
					if (msg.compareTo("GET_GAMES:") ==0)
						sendGameList();
					
					if (headerSplit.length == 2) {
						switch (headerSplit[0]) {
							// ADD_PLAYER:[name]\n
							case "ADD_PLAYER":
								addPlayer(headerSplit[1]);
								break;
							// CREATE_GAME:[userId]\n
							case "CREATE_GAME":
								id = Integer.parseInt(headerSplit[1]);
								addGame(id);
								break;
							// GAME_IS_ACTIVE:[gameId]\n
							case "GAME_IS_ACTIVE":
								id = Integer.parseInt(headerSplit[1]);
								sendIsActive(id);
								break;
//								GET_PLAYERS:[gameId]\n
							case "GET_PLAYERS":
								msgSplit = headerSplit[1].split("\t");
								id = Integer.parseInt(msgSplit[0]);
								team = Integer.parseInt(msgSplit[1]);
								sendPlayerIdList(id,team);
								break;
							// GET_OWNER:[gameId]\n
							case "GET_OWNER":
								id = Integer.parseInt(headerSplit[1]);
								sendGameOwner(id);
								break;
							// GET_CURRENT_GAME:[playerId]\n	
							case "GET_CURRENT_GAME":
								id = Integer.parseInt(headerSplit[1]);
								sendCurrentGame(id);
								break;
							// GET_BOARDS:[gameId]\n
							case "GET_BOARDS":
								id = Integer.parseInt(headerSplit[1]);
								sendBoards(id);
								break;
							// START_GAME:[gameId]\n
							case "START_GAME":
								id = Integer.parseInt(headerSplit[1]);
								startGame(id);
								break;
							// GET_NAME:[playerId]\n
							case "GET_NAME":
								id = Integer.parseInt(headerSplit[1]);
								sendPlayerName(id);
								break;
							// IS_WHITE:[playerId]\n
							case "IS_WHITE":
								id = Integer.parseInt(headerSplit[1]);
								sendIsWhite(id);
								break;
							// GET_TEAM:[playerId]\n
							case "GET_TEAM":
								id = Integer.parseInt(headerSplit[1]);
								sendPlayerTeam(id);
								break;
							// JOIN_GAME:[playerId]\t[gameId]\t[team]\n
							case "JOIN_GAME":
								msgSplit = headerSplit[1].split("\t");
								if (msgSplit.length == 3) {
									id = Integer.parseInt(msgSplit[0]);
									int gameID = Integer.parseInt(msgSplit[1]);
									team = Integer.parseInt(msgSplit[2]);
									addPlayerToGame(id, gameID, team);
								} else {
									send("ERROR: JOIN_GAME in wrong format");
								}
								break;
							// GET_CURRENT_BOARD:[playerId]\n
							case "GET_CURRENT_BOARD":
								id = Integer.parseInt(headerSplit[1]);
								sendCurrentBoard(id);
								break;
							// MOVE:[boardId]\t[from_x]\t[from_y]\t[to_x]\t[to_y]\n
							case "MOVE":
								move(msg);
								break;
							// PUT:[boardId]\t[userId]\t[pieceType]\t[pieceIsWhite]\t[toX]\t[toY]\n
							case "PUT":
								sendPutMessage(msg);
								break;
							// QUIT:[playerId]
							case "QUIT":
								id = Integer.parseInt(headerSplit[1]);
								quit(id);
								break;
							case "GAME_OVER":
								msgSplit = headerSplit[1].split("\t");
								id = Integer.parseInt(msgSplit[0]);
								team = Integer.parseInt(msgSplit[1]);
								List<Integer> ids = m_data.getPlayerIdsByTeam(id, team);
								String message = "BROADCAST:GAME_OVER:"+id;
								for (int playerId: ids) message+="\t"+m_data.getPlayerName(playerId);
								m_pool.broadcastToGame(id,message+"\n",this);
							// PASS:[fromPlayerId]\t[toPlayerId]\t[chessPieceType]
							case "PASS":
								msgSplit = headerSplit[1].split("\t");
								send("\n");
								if (msgSplit.length == 3) {
									id = Integer.parseInt(msgSplit[1]);
									m_pool.broadcastToGame(m_data.getCurrentGame(id),"BROADCAST:"+msg+"\n",this);
								}
								break;
							default:
								System.out.println("Unknown message " + msg);
						}
					}
				}
			}// MOVE:[from_x]\t[from_y]\t[to_x]\t[to_y]\n
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Broadcasts put message to all players in the game 
	 * @param msg
	 */
	private void sendPutMessage(String msg) {
		int gameId = m_playerInfo.getGameId();
		send("\n");
		m_pool.broadcastToGame(gameId, "BROADCAST:" + msg + "\n", this);
		int next = m_data.getNextTurn(gameId);
		System.out.println("Next turn: " + next);
		m_pool.sendToPlayer(next, "BROADCAST:YOUR_TURN\n");
	}

	/**
	 * Gets game ID for player associated with this client
	 * @return ID or -1 if m_playerInfo not set
	 */
	public int getGameId() {
		System.out.println("m_playerInfo: "+m_playerInfo);
		if (m_playerInfo != null)
			return m_playerInfo.getGameId();
		else
			return -1;
	}
	
	/**
	 * Gets player ID for player associated wit this client
	 * @return ID or -1 if m_playerInfo not set
	 */
	public int getPlayerId() {
		if (m_playerInfo != null)
			return m_playerInfo.getId();
		else
			return -1;
	}
	
	/**
	 * Sends message about chess move to all players in game
	 * Sends response to player making move:
	 * "MOVE_OK\n" if everything went well
       "MOVE_FAILED\n" if move failed
	 * @param msg Message to broadcast to other players in game
	 */
	private void move(String msg) {
		System.out.println("Player " + m_playerInfo.getId() + " " + m_playerInfo.getName() + " moved");
		int gameID = m_playerInfo.getGameId();
		if (gameID > 0) {
			// MOVE:[boardID]\t[from_x]\t[from_y]\t[to_x]\t[to_y]\n
				String ret = "BROADCAST:" + msg + "\n";
				m_pool.broadcastToGame(gameID, ret, this);
				send("MOVE_OK\n");
					
				// notify player with next turn
				int next = m_data.getNextTurn(gameID);
				System.out.println("Next turn: " + next);
				m_pool.sendToPlayer(next, "BROADCAST:YOUR_TURN\n");
			
		} else {
			System.out.println("GameId incorrect: "+gameID);
			send("MOVE_FAILED\n");
		}
	}

	/**
	 * Sends id of player's current board
	 * CURRENT_BOARD:[playerId]\t[boardId]\n
	 * @param id
	 */
	private void sendCurrentBoard(int id) {
		int board = m_data.getPlayerBoard(id);
		if (board > 0)
			send(board + "\n");
		else
			send("ERROR: board not initialized for player\n " + id);
	}

	/**
	 * Sends "TEAM:[playerId]\t1\n" or "TEAM:[playerId]\t2\n"
	 * @param id
	 */
	private void sendPlayerTeam(int id) {
		int team = m_data.getPlayerTeam(id);
		if (team == 1)
			send("1\n");
		else if (team == 2)
			send("2\n");
		else {
			send("ERROR: player not in game\n");
		}
	}

	/**
	 * Sends true if player is white, false if not
	 * "IS_WHITE:[playerId]\tfalse\n" if the player is not playing or the player's color is black. 
       "IS_WHITE:[playerId]\ttrue\n" otherwise
	 * @param id
	 */
	private void sendIsWhite(int id) {
		if (m_data.isWhite(id))
			send("true\n");
		else
			send("false\n");
	}

	/**
	 * Sends name of player with given ID
	 * NAME:[playerId]\t[name]\n
	 * @param id
	 */
	private void sendPlayerName(int id) {
		String name = m_data.getPlayerName(id);
		if (name == null)
			send("ERROR: player with id " + id + " does not exist\n");
		else
			send(name + "\n");
	}

	/**
	 * Adds player to server data and this handler and sends response to client with id
	 * @param name Name of new player
	 */
	private void addPlayer(String name) {
		PlayerInfo p = m_data.addPlayer(name);
		System.out.println("Setting m_playerInfo to "+ p.getId() + " " + p.getName());
		m_playerInfo = p;
		int id = p.getId();
		m_pool.addToMap(id, this);
		send(id + "\n");
	}

	/**
	 * Adds player to game on specified team and establishes player color in game
	 * Sends "GAME_JOINED:[playerId]\n" if everything went well
        - "GAME_FULL:[gameId]\n" if the room is full
	 * @param playerId
	 * @param gameId
	 * @param teamNum
	 */
	public void addPlayerToGame(int playerId, int gameId, int teamNum) {
		if (m_data.addPlayerToGame(playerId, gameId, teamNum)) {
			send("GAME_JOINED\n");
			m_pool.broadcast("BROADCAST:JOIN_GAME:" + playerId + "\t" + gameId + "\n", this);
		} else {
			send("GAME_FULL\n");
		}
	}
	
	/**
	 * Creates new game in server data and sends game id to client
	 * @param ownerId ID of game owner/client
	 */
	public void addGame(int ownerId) {
		int id = m_data.addGame(ownerId);
		send(id + "\n");
		
		if (id != -1)
			m_pool.broadcast("BROADCAST:NEW_GAME:" + ownerId + "\t" + id + "\n", this);
	}

	/**
	 * - Check if there are 4 players in the game
        - Create two new chess boards
        - Set the gameId of the 2 newly created boards to the requested gameId (or add the 2 newly created boards to the requested game, depending on your implementation)
        - Assign colors to players (1 black and 1 white for each team)
        - Assign players to boards
        - Set game's active to false
    	+ Response:
        - "GAME_STARTED:[gameId]\n" if everything went well
        - "NOT_READY:[gameId]\n" if there're fewer than 4 players
        - "UNAUTHORIZED:[gameId]\n" if not owner
	 * @param gameId
	 */
	public void startGame(int gameId) {
		// send unauthorized message if client not game owner
		System.out.println("m_playerInfo: " + m_playerInfo.getId() + " " + m_playerInfo.getName());
		if (m_data.getGameOwner(gameId) != m_playerInfo.getId()) {
			send("UNAUTHORIZED:" + gameId + "\n");
		} else if (m_data.startGame(gameId)) {
			send("GAME_STARTED:" + gameId + "\n");
			m_pool.broadcast("BROADCAST:GAME_STARTED:" + gameId + "\n", this);
			send("BROADCAST:YOUR_TURN\n");
		} else {
			send("NOT_READY:" + gameId + "\n");
		}
	}
	
	/**
	 * Sends all available games to client
	 */
	public void sendGameList() {
		String msg = getGameList();
		send(msg);
	}
	
	/**
	 * TODO: maybe use this
	 * GAMES:[gameId] [name1,...,nameN] [name1,...,nameN]\t...\n
	 * Sends info about all available games to client
	 */
	public void sendGamesInfo() {
		List<GameInfo> games = m_data.getGames();
		List<String> names = null;
		String msg = "GAMES:";
		for (GameInfo e : games) {
			msg += e.getId() + " ";
			names = e.getTeamOne();
			if (names != null) {
				for (String p : names) {
					msg += p + ",";
				}
				msg = msg.substring(0, msg.length()-1) + " ";
			} else {
				msg += "none ";
			}
			
			names = e.getTeamTwo();
			if (names != null) {
				for (String p : names){
					msg += p + ",";
				}
				msg = msg.substring(0, msg.length()-1) + "\t";
			} else {
				msg += "none\t";
			}
		}
		msg = msg.substring(0, msg.length()-1) + "\n";
		send(msg);
	}
	
	/**
	 * Forms string of list of available games
	 * @return String listing available games
	 */
	// TODO: figure out if we need to add teams and players
	private String getGameList() {
		List<GameInfo> games = m_data.getGames();
		String msg = "";
		
		if (games.isEmpty()) {
			return "\n";
		} else {
			for (GameInfo e : games) {
				msg += e.getId() + "\t";
			}
			msg = msg.substring(0, msg.length()-1) + "\n";
			return msg;
		}
	}

	/**
	 * Sends PLAYERS:[gameId]\t[userId1]\t[userId2]\n...
	 * @param gameId
	 */
	public void sendPlayerIdList(int gameId,int team) {
		List<Integer> ids = m_data.getPlayerIdsByTeam(gameId, team);
		String msg="";
		for (Integer id : ids) {
			msg += id+"\t";
		}
		if (!ids.isEmpty()) msg = msg.substring(0, msg.length()-1);
		send(msg+"\n");
	}

	/**
	 * Sends BOARDS:[gameId]\t[board1Id]\t[board2Id]\n
	 * @param gameId
	 */
	public void sendBoards(int gameId) {
		int[] boards = m_data.getBoards(gameId);
		if (boards != null)
			send(boards[0] + "\t" + boards[1] + "\n");
		else
			send("\n");
	}
	
	/**
	 * Sends GAME_ACTIVE:[gameId]\ttrue\n if active, GAME_ACTIVE:[gameId]\tfalse\n otherwise
	 * @param gameId
	 */
	private void sendIsActive(int gameId) {
		boolean isActive = m_data.gameIsActive(gameId);
		if (isActive)
			send("true\n");
		else
			send("false\n");
	}
	
	/**
	 * Sends GAME_OWNER:[gameId]\t[userId]\n
	 * @param gameId
	 */
	private void sendGameOwner(int gameId) {
		int owner = m_data.getGameOwner(gameId);
		send(owner + "\n");
	}
	/**
	 * Sends GET_CURRENT_GAME:[userId]\n
	 * @param gameId
	 */
	private void sendCurrentGame(int playerId) {
		//chtran: Sometimes, m_playerInfo just returns null =.=
		//int gameId;
		//if (playerId == m_playerInfo.getId()) {
			//gameId = m_playerInfo.getGameId();
		//} else {
			int gameId = m_data.getCurrentGame(playerId);
		//}
		send(gameId + "\n");
	}
	
	/**
	    - Set current gameId of the player to -1
        - Remove the player from the game's player list
        - Broadcast a quit message to all the players in the room
    + Response:
        - "QUIT_OK\n" if everything went well
        - "QUIT_FAILED\n" if failed
	 * @param id
	 */
	public void quit(int playerId) {
		int gameID = m_data.getCurrentGame(playerId);
		if (gameID >= 0) {
			boolean isOwner = m_data.getGameOwner(gameID) == playerId ? true : false;
			boolean gameStarted = m_data.gameIsActive(gameID) ? false : true;
			boolean roomEmpty = m_data.numPlayers(gameID) == 1 ? true : false;
			String msg;
					
			if (gameStarted) {
				// resets all players and deletes game
				m_data.playerQuit(playerId);
				msg = String.format("BROADCAST:GAME_CANCELED:%d\n", gameID);			
				m_pool.broadcastToGame(gameID, msg, this);
			} else if (!gameStarted && isOwner && roomEmpty) {
				// game becomes empty so delete from server and reset owner
				m_data.playerQuit(playerId);
				msg = String.format("BROADCAST:GAME_DELETED:%d\n", gameID);
				m_pool.broadcast(msg, this);
			} else if (!gameStarted && isOwner && !roomEmpty) {
				// game not started and player is owner so new owner must be assigned
				// new owner color changes to white
				int newOwner = m_data.setNewOwner(gameID);
				msg = String.format("BROADCAST:NEW_OWNER:%d\n", gameID);
				m_pool.sendToPlayer(newOwner, msg);
			} else {
				m_data.removePlayerFromGame(playerId);
			}
			send("QUIT_OK\n");
			
			// broadcast message for everyone: BROADCAST:LEAVE_GAME:[playerId]\t[gameId]
			msg = String.format("BROADCAST:LEAVE_GAME:%d\t%d\n", playerId, gameID);
			m_pool.broadcast(msg, this);
		} else {
			send("QUIT_FAILED\n");
		}
	}
	
	/**
	 * Send a string to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(String message) {
		//TODO: Set up the methods, so it will send the message to the client
		System.out.println("SENDING: " + message);
		m_output.print(message);
		m_output.flush();
	}

	/**
	 * Close this socket and its related streams.
	 * 
	 * @throws IOException Passed up from socket
	 */
	public void kill() throws IOException {
		//TODO: Close all the streams after the client disconnects.
		m_input.close();
		m_output.close();
		m_client.close();
	}
}


