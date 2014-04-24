package com.kiri.hackjak.apis.daos;

import com.google.myjson.annotations.SerializedName;

public class BuswayHalteDao {
	String halteId;
	String halteName;
	String koridor;
	double lat;
	@SerializedName("long")
	double lng;

	public String getHalteId() {
		return halteId;
	}

	public String getHalteName() {
		return halteName;
	}

	public String getKoridor() {
		return koridor;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

}
