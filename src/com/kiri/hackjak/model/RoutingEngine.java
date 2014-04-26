package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RoutingEngine {
	
	RoutingEngine() {
		// Constructor only allowed from the same package
	}
	
	Set<Waypoint> waypoints = new TreeSet<Waypoint>();
	List<Trayek> trayeks = new ArrayList<Trayek>();
	
	
	public static RoutingEngine createFromSQLLite() {
		// TODO
		return null;
	}
	
	public static void populateSQLLiteFromUK4P() {
		// TODO
	}
	
	public List<SearchResult> findRoute(Waypoint from, Waypoint to) {
		// TODO
		return null;
	}
	
	public Set<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public List<Trayek> getTrayeks() {
		return trayeks;
	}
}
