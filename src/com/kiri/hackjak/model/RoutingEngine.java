package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.kiri.hackjak.model.SearchResult.Step;

public class RoutingEngine {
	
	RoutingEngine() {
		// Constructor only allowed from the same package
	}
	
	Map<String, Waypoint> availableWaypoints = new TreeMap<String, Waypoint>();
	Set<Waypoint> waypoints = new TreeSet<Waypoint>();
	List<Trayek> trayeks = new ArrayList<Trayek>();
	Graph graph = null;
	
	public static RoutingEngine createFromSQLLite() {
		// TODO
		return null;
	}
	
	public static void populateSQLLiteFromUK4P() {
		// TODO
	}
	
	public List<SearchResult> findRoute(Waypoint from, Waypoint to) {
		Node fromNode = null;
		Node toNode = null;
		for (Trayek trayek: trayeks) {
			for (Node node: trayek.route) {
				if (node.waypoint == from) {
					fromNode = node;
				}
				if (node.waypoint == to) {
					toNode = node;
				}
			}
			if (fromNode != null && toNode != null) {
				break;
			}
		}
		List<Node> nodes = graph.findShortestPath(fromNode, toNode);
		List<SearchResult> searchResults = new ArrayList<SearchResult>();
		if (nodes != null) {
			Step currentStep = null;
			List<Step> steps = new ArrayList<Step>();
			for (Node node: nodes) {
				if (currentStep != null && node.owner == currentStep.track) {
					currentStep.alightIndex++;
				} else {
					currentStep = new Step();
					currentStep.track = node.owner;
					currentStep.boardIndex = 0;
					while (node.owner.route.get(currentStep.boardIndex) != node) {
						currentStep.boardIndex++;
					}
					currentStep.alightIndex = currentStep.boardIndex;
					steps.add(currentStep);
				}
			}
			searchResults.add(new SearchResult(steps));
		}
		return searchResults;
	}
	
	public Set<Waypoint> getWaypoints() {
		return waypoints;
	}
	
	public List<Trayek> getTrayeks() {
		return trayeks;
	}
	
	public Waypoint getWaypointFromString(String text) {
		return availableWaypoints.get(text);
	}
}
