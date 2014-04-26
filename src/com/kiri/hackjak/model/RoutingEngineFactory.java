package com.kiri.hackjak.model;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.widget.Toast;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.apis.ApiGrabberHelper;
import com.kiri.hackjak.sqlite.TrayekDao;

public class RoutingEngineFactory {
	private String ASSETS_TRAYEK = "processed_trayek_bus_jakarta.csv";
	
	Activity activity;
	
	public RoutingEngineFactory(Activity activity) {
		this.activity = activity;
	}
	
	/**
	 * Tries to get from database, if not available from CSV.
	 * @return the routing engine.
	 */
	public RoutingEngine createRoutingEngine() {
		if (KiriApp.getTrayekDao().count() > 0) {
			return SQLiteRouteWorldFactory.createFromSQL();
		} else {
			try {
				InputStream is = activity.getAssets().open(ASSETS_TRAYEK);
				return CSVRouteWorldFactory.createFromCSV(is);
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(activity, "Error importing Trayek File", Toast.LENGTH_LONG).show();
				return null;
			}
		}
	}
}
