package com.kiri.hackjak.model;

public class WaypointModel implements Comparable<WaypointModel> {
	String name;
	
	public WaypointModel(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(WaypointModel o) {
		return name.compareTo(o.name);
	}
	
	public String toString() {
		return this.name;
	}
}
