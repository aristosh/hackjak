package com.kiri.hackjak.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.sqlite.Trayek;

import de.greenrobot.dao.query.QueryBuilder;

public class SQLiteRouteWorldFactory {
	public static RoutingEngine createFromSQL() {
		RoutingEngine newWorld = new RoutingEngine();

		QueryBuilder<Trayek> qb = KiriApp.getTrayekDao().queryBuilder();
		List<Trayek> trayeks = qb.list();

		for(Trayek trayek : trayeks) {
			TrayekModel tm = new TrayekModel();
			tm.kategori = trayek.getKategori();
			tm.namaSingkat = trayek.getNamasingkat();
			tm.route = stringToNodes(trayek.getRoute(), tm, newWorld.availableWaypoints);
			newWorld.trayeks.add(tm);
			
			for (String key: newWorld.availableWaypoints.keySet()) {
				newWorld.waypoints.add(newWorld.availableWaypoints.get(key));
			}
		}
		
		newWorld.graph = new Graph(newWorld);
		return newWorld;
	}
	
	private static List<Node> stringToNodes(String route, TrayekModel owner, Map<String, WaypointModel> availableWaypoints) {
		List<Node> returnedRoads = new ArrayList<Node>();
		String[] parts = route.split(",");
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i].trim();
			WaypointModel waypoint = new WaypointModel(part);
			returnedRoads.add(new Node(waypoint, owner));
			availableWaypoints.put(part, waypoint);
		}
		return returnedRoads;
	}
}
