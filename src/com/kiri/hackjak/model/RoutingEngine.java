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
	
	Map<String, WaypointModel> availableWaypoints = new TreeMap<String, WaypointModel>();
	Set<WaypointModel> waypoints = new TreeSet<WaypointModel>();
	List<TrayekModel> trayeks = new ArrayList<TrayekModel>();
	Graph graph = null;
	
	public static RoutingEngine createFromSQLLite() {
		// TODO
		return null;
	}
	
	public static void populateSQLLiteFromUK4P() {
		// TODO
	}
	
	public List<SearchResult> findRoute(WaypointModel from, WaypointModel to) {
		Node fromNode = null;
		Node toNode = null;
		for (TrayekModel trayek: trayeks) {
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
	
	public Set<WaypointModel> getWaypoints() {
		return waypoints;
	}
	
	public List<TrayekModel> getTrayeks() {
		return trayeks;
	}
	
	public WaypointModel getWaypointFromString(String text) {
		return availableWaypoints.get(text);
	}
}
