package com.kiri.hackjak;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.kiri.hackjak.grdaos.DaoMaster;
import com.kiri.hackjak.grdaos.DaoSession;
import com.kiri.hackjak.grdaos.TrayekDao;
import com.kiri.hackjak.grdaos.TrayekRouteDao;
import com.kiri.hackjak.grdaos.TrayekRouteDetailDao;
import com.kiri.hackjak.grdaos.TrayekWaypointDao;

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

	public static TrayekRouteDao getTrayekRouteDao() {
		return trayekRouteDao;
	}

	public static TrayekRouteDetailDao getTrayekRouteDetailDao() {
		return trayekRouteDetailDao;
	}

	public static TrayekWaypointDao getTrayekWaypointDao() {
		return trayekWaypointDao;
	}

}
