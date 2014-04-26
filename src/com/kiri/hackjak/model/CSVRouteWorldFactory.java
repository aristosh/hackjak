package com.kiri.hackjak.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVRouteWorldFactory {
	public static RoutingEngine createFromCSV(InputStream csvInputStream) throws IOException {
		RoutingEngine newWorld = new RoutingEngine();
		BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream));
		String line;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			// skip header
			lineNumber++;
			if (lineNumber == 1) {
				continue;
			}

			// Get column fields and cleanup ""
			String[] columns = line.trim().split(",");
			for (int i = 0; i < columns.length; i++) {
				if (columns[i].charAt(0) == '"' && columns[i].charAt(columns[i].length() - 1) == '"') {
					columns[i] = columns[i].substring(1, columns[i].length() - 1);
				}
			}
			
			// Add departing trayek
			newWorld.trayeks.add(generateTrayek(columns[0], columns[1], columns[2], columns[3], columns[8], newWorld.availableWaypoints));
			// Add returning trayek
			newWorld.trayeks.add(generateTrayek(columns[0], columns[1], columns[2], columns[3], columns[9], newWorld.availableWaypoints));
			// Add available waypoints
			for (String key: newWorld.availableWaypoints.keySet()) {
				newWorld.waypoints.add(newWorld.availableWaypoints.get(key));
			}
		}
		reader.close();
		newWorld.graph = new Graph(newWorld);
		return newWorld;
	}
	
	private static Trayek generateTrayek(String jenisAngkutan, String jenisTrayek, String noTrayek, String namaTrayek, String dashSeparatedWaypoints, Map<String, Waypoint> availableWaypoints) {
		Trayek newTrayek = new Trayek();
		List<Node> nodes = stringToNodes(availableWaypoints, newTrayek, dashSeparatedWaypoints);
		newTrayek.kategori = jenisTrayek == "-" ? jenisAngkutan : jenisTrayek;
		newTrayek.namaSingkat = String.format("%s %s (arah %s)", noTrayek, namaTrayek, nodes.get(nodes.size() - 1).waypoint.toString());
		newTrayek.route = nodes;
		return newTrayek;
	}
	
	private static List<Node> stringToNodes(Map<String, Waypoint> availableWaypoints, Trayek owner, String dashSeparatedText) {
		String[] roadsText = dashSeparatedText.split(" *-+ *");
		List<Node> returnedRoads = new ArrayList<Node>();
		for (String roadText: roadsText) {
			returnedRoads.add(new Node(createOrReuseWaypoint(availableWaypoints, roadText), owner));
		}
		return returnedRoads;
	}
	
	private static Waypoint createOrReuseWaypoint(Map<String, Waypoint> availableWaypoints, String name) {
		Waypoint waypoint = availableWaypoints.get(name);
		if (waypoint == null) {
			waypoint = new Waypoint(name);
			availableWaypoints.put(name, waypoint);
		}
		return waypoint;
	}
}
