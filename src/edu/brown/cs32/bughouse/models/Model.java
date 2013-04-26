package edu.brown.cs32.bughouse.models;

import edu.brown.cs32.bughouse.interfaces.Client;

public class Model {
	private static int lastId=0;
	protected final int id;
	private static Client STATIC_CLIENT;
	protected Client client;
	
	public static void setClient(Client client) {
		Model.STATIC_CLIENT = client;
	}
	
	public Model(int id) {
		this.id = id;
		lastId++;
		this.client = Model.STATIC_CLIENT;
	}
	public Model() {
		this.id = ++lastId;
	}
	
	public int getId() {
		return id;
	}
}
