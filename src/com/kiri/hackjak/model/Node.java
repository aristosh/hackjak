package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.List;

class Node {
	public Waypoint waypoint;
	public List<Node> connections;
	public Trayek owner;

	int bestDistance;
	Node parent;
	
	public Node(Waypoint waypoint, Trayek owner) {
		this.waypoint = waypoint;
		this.owner = owner;
		connections = new ArrayList<Node>();
	}
}
