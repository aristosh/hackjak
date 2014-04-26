package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.List;

class Node {
	public WaypointModel waypoint;
	public List<Node> connections;
	public TrayekModel owner;

	int bestDistance;
	Node parent;
	
	public Node(WaypointModel waypoint, TrayekModel owner) {
		this.waypoint = waypoint;
		this.owner = owner;
		connections = new ArrayList<Node>();
	}
}
