package com.kiri.hackjak.model;
import java.io.File;
import java.io.IOException;

/**
 * TODO hapus saat rilis
 * @author PascalAlfadian
 *
 */
public class Main {

	public static void main(String[] args) throws IOException {
		RoutingEngine engine = CSVRouteWorldFactory.createFromCSV(new File("processed-trayek-bus-jakarta.csv"));
		System.out.println(engine.getTrayeks().size() + " tracks");
		for (Trayek trayek: engine.getTrayeks()) {
			System.out.println("Kategori = " + trayek.kategori);
			System.out.println("Nama Singkat = " + trayek.namaSingkat);
			System.out.println("Route = " + trayek.route);
			System.out.println("---");
		}
		for (Waypoint waypoint: engine.getWaypoints()) {
			System.out.println(waypoint);
		}
	}
}
