package edu.brown.cs32.bughouse.models;

public class Model {
	private static int lastId=0;
	private final int id;
	public Model(int id) {
		this.id = id;
		lastId++;
	}
	public Model() {
		this.id = ++lastId;
	}
	
	public int getId() {
		return id;
	}
}
