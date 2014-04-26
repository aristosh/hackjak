package com.kiri.hackjak.adapters;

import java.util.ArrayList;
import java.util.List;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.db.Trayek;
import com.kiri.hackjak.db.TrayekDao;
import com.kiri.hackjak.db.TrayekRoute;
import com.kiri.hackjak.db.TrayekRouteDao;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;

import de.greenrobot.dao.query.QueryBuilder;

public class FormattedResult {
	public enum Type {
		BOARD, OTW, ALIGHT 
	}
	
	public long idRuteTrayek;
	public Type type;
	public List<Long> pointId;
	private String noTrayekString;
	
	public FormattedResult() {
		pointId = new ArrayList<Long>();
	}
	
	public String getTitle() {
		return noTrayekString;
	}
	
	public String getDetail() {
		QueryBuilder<TrayekRoute> trqb = KiriApp.getTrayekRouteDao().queryBuilder();
		trqb.where(TrayekRouteDao.Properties.Id.eq(idRuteTrayek));
		long idTrayek = trqb.list().get(0).getIdTrayek();
		
		QueryBuilder<Trayek> tqb = KiriApp.getTrayekDao().queryBuilder();
		tqb.where(TrayekDao.Properties._id.eq(idTrayek));
		String noTrayek = tqb.list().get(0).getNoTrayek();
		
		
		if(type == Type.BOARD) {
			QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao().queryBuilder();
			qb.where(TrayekWaypointDao.Properties.Id.eq(pointId.get(0)));
			String str = qb.list().get(0).getPoint();
			return String.format("Board on %s at %s", noTrayek, str);
		} else if(type == Type.ALIGHT) {
			QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao().queryBuilder();
			qb.where(TrayekWaypointDao.Properties.Id.eq(pointId.get(0)));
			String str = qb.list().get(0).getPoint();
			return String.format("Alight from %s at %s", noTrayek, str);
		} else {
			String detail = "Keep riding at ";
			for(int i = 0; i < pointId.size() - 1; i++) {
				QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao().queryBuilder();
				qb.where(TrayekWaypointDao.Properties.Id.eq(pointId.get(i)));
				String str = qb.list().get(0).getPoint();
				detail += String.format("%s, ", str);
			}
			QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao().queryBuilder();
			qb.where(TrayekWaypointDao.Properties.Id.eq(pointId.get(pointId.size() - 1)));
			String str = qb.list().get(0).getPoint();
			detail += String.format("%s", str);
			
			return detail;
		}
	}

}
