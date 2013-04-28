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
		try {
			while (true) {
				if ((msg = m_input.readLine()) != null) {
					System.out.println("RECEIVED: " + msg);
					headerSplit = msg.split(":");
					
					if (msg.compareTo("GET_GAMES:") ==0)
						sendGameList();
					
					if (headerSplit.length == 2) {
						// ADD_PLAYER:[name]\n
						if (headerSplit[0].compareTo("ADD_PLAYER") == 0) {
							addPlayer(headerSplit[1]);
						// CREATE_GAME:[userId]\n
						} else if (headerSplit[0].compareTo("CREATE_GAME") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							addGame(id);
						// GAME_IS_ACTIVE:[gameId]\n
						} else if (headerSplit[0].compareTo("GAME_IS_ACTIVE") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendIsActive(id);
						//	GET_PLAYERS:[gameId]\n
						} else if (headerSplit[0].compareTo("GET_PLAYERS") == 0) {
							msgSplit = headerSplit[1].split("\t");
							id = Integer.parseInt(msgSplit[0]);
							int team = Integer.parseInt(msgSplit[1]);
							sendPlayerIdList(id,team);
						// GET_OWNER:[gameId]\n
						} else if (headerSplit[0].compareTo("GET_OWNER") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendGameOwner(id);
						// GET_CURRENT_GAME:[playerId]\n
						} else if (headerSplit[0].compareTo("GET_CURRENT_GAME") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendCurrentGame(id);
						// GET_BOARDS:[gameId]\n
						} else if (headerSplit[0].compareTo("GET_BOARDS") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendBoards(id);
						// START_GAME:[gameId]\n
						} else if (headerSplit[0].compareTo("START_GAME") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							startGame(id);
						// GET_NAME:[playerId]\n
						} else if (headerSplit[0].compareTo("GET_NAME") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendPlayerName(id);
						// IS_WHITE:[playerId]\n
						} else if (headerSplit[0].compareTo("IS_WHITE") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendIsWhite(id);
						// GET_TEAM:[playerId]\n
						} else if (headerSplit[0].compareTo("GET_TEAM") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendPlayerTeam(id);
						// JOIN_GAME:[playerId]\t[gameId]\t[team]\n
						} else if (headerSplit[0].compareTo("JOIN_GAME") == 0) {
							msgSplit = headerSplit[1].split("\t");
							if (msgSplit.length == 3) {
								id = Integer.parseInt(msgSplit[0]);
								int gameID = Integer.parseInt(msgSplit[1]);
								int team = Integer.parseInt(msgSplit[2]);
								addPlayerToGame(id, gameID, team);
							} else {
								send("ERROR: JOIN_GAME in wrong format");
							}
						// GET_CURRENT_BOARD:[playerId]\n
						} else if (headerSplit[0].compareTo("GET_CURRENT_BOARD") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							sendCurrentBoard(id);
						// MOVE:[boardId]\t[from_x]\t[from_y]\t[to_x]\t[to_y]\n
						} else if (headerSplit[0].compareTo("MOVE") == 0) {
							msgSplit = headerSplit[1].split("\t");
							if (msgSplit.length == 5) {
								id = Integer.parseInt(msgSplit[0]);
								int toY = Integer.parseInt(msgSplit[4]);
								move(id, msg);
							}
						// QUIT:[playerId]
						} else if (headerSplit[0].compareTo("QUIT") == 0) {
							id = Integer.parseInt(headerSplit[1]);
							quit(id);
						// PUT:[fromPlayerId]\t[toPlayerId]\t[chessPieceType]
						} else if (headerSplit[0].compareTo("PUT") == 0) {
							msgSplit = headerSplit[1].split("\t");
							if (msgSplit.length == 3) {
								id = Integer.parseInt(msgSplit[1]);
								m_pool.sendToPlayer(id, msg);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets game ID for player associated with this client
	 * @return ID
	 */
	public int getGameId() {
		return m_playerInfo.getGameId();
	}
	
	/**
	 * Gets player ID for player associated wit this client
	 * @return ID
	 */
	public int getPlayerId() {
		return m_playerInfo.getId();
	}
	
	/**
	 * Sends message about chess move to all players in game
	 * Sends response to player making move:
	 * "MOVE_OK:[boardId]\n" if everything went well
       "MOVE_FAILED:[boardId]\n" if move failed
	 * @param id Board id
	 * @param msg Message to broadcast to other players in game
	 */
	private void move(int id, String msg) {
		if (m_playerInfo.getBoardId() == id) {
			int gameID = m_playerInfo.getGameId();
			if (gameID > 0) {
				m_pool.broadcastToGame(gameID, msg, this);
				send("MOVE_OK:" + id + "\n");
				
				// notify player with next turn
				int next = m_data.getNextTurn(gameID);
				m_pool.sendToPlayer(next, "BROADCAST:YOUR_TURN\n");
			} else {
				send("MOVE_FAILED" + id + "\n");
			}
		} else {
			send("MOVE_FAILED:" + id + "\n");
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
			send("ERROR: board not initialized for player " + id);
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
		else
			send("ERROR: player not in game");
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
	public void addPlayer(String name) {
		PlayerInfo p = m_data.addPlayer(name);
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
		
//		// send new game option to all other clients
//		String msg = getGameList();
//		m_pool.broadcast(msg, this);
		
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
		if (m_data.getGameOwner(gameId) != m_playerInfo.getId()) {
			send("UNAUTHORIZED:" + gameId + "\n");
		} else if (m_data.startGame(gameId)) {
			send("GAME_STARTED:" + gameId + "\n");
			m_pool.broadcast("BROADCAST:GAME_STARTED:" + gameId + "\n", this);
		} else {
			send("NOT_READY:" + gameId + "\n");
		}
	}

	public void notifyTurn(Socket s) {
		// TODO Auto-generated method stub
		
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
		send(boards[0] + "\t" + boards[1] + "\n");
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
		int gameId;
		if (playerId == m_playerInfo.getId()) {
			gameId = m_playerInfo.getGameId();
		} else {
			gameId = m_data.getCurrentGame(playerId);
		}
		send(gameId + "\n");
	}
	/**
	    - Set current gameId of the player to -1
        - Remove the player from the game's player list
        - Broadcast a quit message to all the players in the room
    + Response:
        - "QUIT_OK:[playerId]\n" if everything went well
	 * @param id
	 */
	public void quit(int id) {
		m_data.playerQuit(id);
		int gameId = m_playerInfo.getGameId();
		if (gameId > 0) {
			String msg = "BROADCAST:QUIT_GAME:" + id + "\t" + gameId + "\n";
			m_pool.broadcastToGame(gameId, msg, this);
			send("QUIT_OK:" + id + "\n");
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


