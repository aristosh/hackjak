package com.kiri.hackjak.model;

import java.util.List;

public class Trayek {
	
	public String kategori;	
	public String namaSingkat;	
	public List<Waypoint> route;

	// optional FIXME nanti diganti List<LatLng>
	public List<String> polylineData;
}
