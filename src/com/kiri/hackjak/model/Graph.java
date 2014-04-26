package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;

class Graph {
	
	RoutingEngine engine;
	
	/**
	 * Construct graph from the engine.
	 * @param engine the engine to input
	 */
	public Graph(RoutingEngine engine) {
		this.engine = engine;
		List<TrayekModel> trayeks = engine.getTrayeks();
		// Create owner mappings for better performance
		Map<WaypointModel, List<TrayekModel>> waypointOwners = new TreeMap<WaypointModel, List<TrayekModel>>();
		for (TrayekModel trayek: trayeks) {
			for (Node node: trayek.route) {
				WaypointModel waypoint = node.waypoint;
				List<TrayekModel> existingList = waypointOwners.get(waypoint);
				if (existingList == null) {
					existingList = new ArrayList<TrayekModel>(1);
					existingList.add(trayek);
					waypointOwners.put(waypoint, existingList);
				} else {
					existingList.add(trayek);
				}
			}
		}
		// Construct graph
		for (TrayekModel trayek: trayeks) {
			for (int i = 0, size = trayek.route.size(); i < size; i++) {
				Node node = trayek.route.get(i);
				// Forward edge
				if (i + 1 < size) {
					node.connections.add(trayek.route.get(i + 1));
				}
				// Link to similar waypoint.
				for (TrayekModel adjacentTrayek: waypointOwners.get(node.waypoint)) {
					if (adjacentTrayek != trayek) {
						for (Node potentialNode: adjacentTrayek.route) {
							if (potentialNode.waypoint == node.waypoint) {
								node.connections.add(potentialNode);
							}
						}
					}
				}
			}
		}
	}
	public List<Node> findShortestPath(Node start, Node finish) {
		Queue<Node> queue = new LinkedList<Node>();
		// Set all as unreachable except start.
		for (TrayekModel trayek: engine.trayeks) {
			for (Node node: trayek.route) {
				if (node == start) {
					node.bestDistance = 0;
					queue.offer(node);
				} else {
					node.bestDistance = Integer.MAX_VALUE;
				}
				node.parent = null;
			}
		}
		// Run BFFs. Sorry, BFS. Break when finish is found.
		while (!queue.isEmpty() && finish.bestDistance == Integer.MAX_VALUE) {
			Node node = queue.poll();
			for (Node adjacentNode: node.connections) {
				if (adjacentNode.bestDistance == Integer.MAX_VALUE) {
					adjacentNode.bestDistance = node.bestDistance + 1;
					adjacentNode.parent = node;
					queue.offer(adjacentNode);
				}
			}
		}
		// Construct result
		if (finish.bestDistance == Integer.MAX_VALUE) {
			return null;
		} else {
			List<Node> result = new ArrayList<Node>(finish.bestDistance + 1);
			for (Node node = finish; node != null; node = node.parent) {
				result.add(node);
			}
			// Reverse order
			for (int i = 0, size = result.size(); i < size / 2; i++) {
				Node temp = result.get(i);
				result.set(i, result.get(size - i - 1));
				result.set(size - i - 1, temp);
			}
			return result;
		}
	}
}
