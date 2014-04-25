package com.kiri.hackjak.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CSVRouteWorldFactory {
	public static RoutingEngine createFromCSV(File csvFile) throws IOException {
		RoutingEngine newWorld = new RoutingEngine();
		BufferedReader reader = new BufferedReader(new FileReader(csvFile));
		Map<String, Waypoint> availableWaypoints = new TreeMap<String, Waypoint>();
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
			newWorld.trayeks.add(generateTrayek(columns[0], columns[1], columns[2], columns[3], stringToWaypoints(availableWaypoints, columns[8])));
			// Add returning trayek
			newWorld.trayeks.add(generateTrayek(columns[0], columns[1], columns[2], columns[3], stringToWaypoints(availableWaypoints, columns[9])));
			// Add available waypoints
			for (String key: availableWaypoints.keySet()) {
				newWorld.waypoints.add(availableWaypoints.get(key));
			}
		}
		reader.close();
		return newWorld;
	}
	
	private static Trayek generateTrayek(String jenisAngkutan, String jenisTrayek, String noTrayek, String namaTrayek, List<Waypoint> waypoints) {
		Trayek newTrayek = new Trayek();
		newTrayek.kategori = jenisTrayek == "-" ? jenisAngkutan : jenisTrayek;
		newTrayek.namaSingkat = String.format("%s %s (arah %s)", noTrayek, namaTrayek, waypoints.get(waypoints.size() - 1));
		newTrayek.route = waypoints;
		return newTrayek;
	}
	
	private static List<Waypoint> stringToWaypoints(Map<String, Waypoint> availableWaypoints, String dashSeparatedText) {
		String[] roadsText = dashSeparatedText.split(" *-+ *");
		List<Waypoint> returnedRoads = new ArrayList<Waypoint>(roadsText.length);
		for (String roadText: roadsText) {
			returnedRoads.add(createOrReuseWaypoint(availableWaypoints, roadText));
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
