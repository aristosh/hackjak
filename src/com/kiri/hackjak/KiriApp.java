package com.kiri.hackjak;

import java.util.List;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.kiri.hackjak.db.DaoMaster;
import com.kiri.hackjak.db.DaoSession;
import com.kiri.hackjak.db.TrayekDao;
import com.kiri.hackjak.db.TrayekRouteDao;
import com.kiri.hackjak.db.TrayekRouteDetailDao;
import com.kiri.hackjak.db.TrayekWaypointDao;

public class KiriApp extends Application {
	private static SQLiteDatabase db;
	private static DaoMaster.DevOpenHelper dbHelper;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	private static TrayekDao trayekDao;
	private static TrayekRouteDao trayekRouteDao;
	private static TrayekRouteDetailDao trayekRouteDetailDao;
	private static TrayekWaypointDao trayekWaypointDao;

	@Override
	public void onCreate() {
		super.onCreate();

		initDatabase();
	}

	public static String implodeArray(List<String> inputArray, String glueString) {
		/** Output variable */
		String output = "";

		if (inputArray.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(inputArray.get(0));

			for (int i = 1; i < inputArray.size(); i++) {
				sb.append(glueString);
				sb.append(inputArray.get(i));
			}

			output = sb.toString();
		}

		return output;
	}

	private void initDatabase() {
		dbHelper = new DaoMaster.DevOpenHelper(getApplicationContext(),
				"hackjack_db", null);
		db = dbHelper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();

		trayekDao = daoSession.getTrayekDao();
		trayekRouteDao = daoSession.getTrayekRouteDao();
		trayekRouteDetailDao = daoSession.getTrayekRouteDetailDao();
		trayekWaypointDao = daoSession.getTrayekWaypointDao();
	}

	public static TrayekDao getTrayekDao() {
		return trayekDao;
	}

	public static TrayekWaypointDao getTrayekWaypointDao() {
		return trayekWaypointDao;
	}

	public static TrayekRouteDao getTrayekRouteDao() {
		return trayekRouteDao;
	}

	public static TrayekRouteDetailDao getTrayekRouteDetailDao() {
		return trayekRouteDetailDao;
	}

}
