package com.kiri.hackjak.utils;

import java.util.ArrayList;
import java.util.List;

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
		List<TrayekRouteDetail> list = KiriApp.getTrayekRouteDetailDao()
				.loadAllDeepFromCursor(KiriApp.getDb().rawQuery(sql, null));
		return list;
	}

	public static List<TrayekRouteDetail> getRouteTwoIbun(long fromWaypointId,
			long toWaypointId) {
		// ambil semua trayek yang berangkat dari from
		String sql = "select t3.* from (SELECT * FROM TRAYEK_ROUTE_DETAIL where id_waypoint = "
				+ fromWaypointId
				+ ") as t1, TRAYEK_ROUTE_DETAIL as t3 where t3.id_rute_trayek = t1.id_rute_trayek and t3.urut >= t1.urut order by id_rute_trayek, urut";
		List<TrayekRouteDetail> list = KiriApp.getTrayekRouteDetailDao()
				.loadAllDeepFromCursor(KiriApp.getDb().rawQuery(sql, null));
		for (int i = 0; i < list.size(); i++) {
			// dont check from waypoint
			if (list.get(i).getIdWaypoint() == fromWaypointId)
				continue;

			List<TrayekRouteDetail> listTemp = getRouteOne(list.get(i)
					.getIdWaypoint(), toWaypointId);
			if (listTemp.size() > 0) {
				// add from waypoint to result
				listTemp.add(list.get(0));
				return listTemp;
			}
		}
		return new ArrayList<TrayekRouteDetail>();
	}

}
