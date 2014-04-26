package com.kiri.hackjak.utils;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.db.TrayekRouteDetail;

public class Routing {
	public static List<TrayekRouteDetail> getRouteOne(long fromWaypointId,
			long toWaypointId) {
		String sql = "select t3.* from (SELECT * FROM TRAYEK_ROUTE_DETAIL where id_waypoint = "
				+ fromWaypointId
				+ ") as t1, (SELECT * FROM TRAYEK_ROUTE_DETAIL where id_waypoint = "
				+ toWaypointId
				+ ") as t2, TRAYEK_ROUTE_DETAIL as t3 where t1.id_rute_trayek = t2.id_rute_trayek and t3.id_rute_trayek = t2.id_rute_trayek and t3.urut >= t1.urut and t3.urut <= t2.urut order by id_rute_trayek, urut";
		return KiriApp.getTrayekRouteDetailDao().loadAllDeepFromCursor(
				KiriApp.getDb().rawQuery(sql, null));
	}

	private static List<TrayekRouteDetail> convertTrayekRouteDetailCursor(
			Cursor c) {
		List<TrayekRouteDetail> list = new ArrayList<TrayekRouteDetail>();
		if (c.moveToFirst()) {
			do {
				// TrayekRouteDetail trayekRouteDetail = new
				// TrayekRouteDetail(c.get, idWaypoint, idRuteTrayek, urut)
				// TrayekWaypoint(id, point)
			} while (c.moveToNext());
		}
		return list;
	}
}
