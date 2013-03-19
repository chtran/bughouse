package edu.brown.cs32.bughouse.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.brown.cs32.bughouse.global.Model;
/**
 * Describing a server.
 * Has many: Rooms
 * @author chtran
 *
 */
public class Server extends Model {
	private final int id;
	private Map<Integer,Player> rooms; //Be careful, never return players directly because it's mutable
	public Server(int id) {
		this.id=id;
		this.rooms = new HashMap<Integer, Player>() ; 
	}
	
	public Player getPlayerById(int playerId) {
		return rooms.get(playerId);
	}
	
	public List<Player> getAllPlayers() {
		return new ArrayList<Player>(this.rooms.values());
	}
}
