package com.kiri.hackjak.model;

import com.kiri.hackjak.apis.ApiGrabberHelper;

import android.app.Activity;

public class RoutingEngineFactory {
	Activity activity;
	
	public RoutingEngineFactory(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * Tries to get from database, if not available from CSV.
	 * @return the routing engine.
	 */
	public RoutingEngine createRoutingEngine(ApiGrabberHelper hackJakApiHelper) {
		if (hackJakApiHelper.isDatabaseAvailable()) {
			// TODO load dari database
		} else {
			// TODO load dari CSV
		}
		return null;
	}
}
