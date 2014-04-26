package com.kiri.hackjak.model;

public class Waypoint implements Comparable<Waypoint> {
	String name;
	
	public Waypoint(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Waypoint o) {
		return name.compareTo(o.name);
	}
	
	public String toString() {
		return this.name;
	}
}
