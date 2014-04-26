package com.kiri.hackjak.model;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class TrayekModel {
	public String noTrayek;
	public String kategori;	
	public String namaSingkat;	
	public List<Node> route;

	public List<LatLng> polylineData;
}
