package com.kiri.hackjak;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

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
	private String jenisAngkutanString;
	private String jenisTrayekString;
	private List<String> pointString;
	
	private Resources resources;
	
	public FormattedResult(Context context) {
		pointId = new ArrayList<Long>();
		pointString = new ArrayList<String>();
		resources = context.getResources();
	}

	private void constructTexts() {
		QueryBuilder<TrayekRoute> trqb = KiriApp.getTrayekRouteDao().queryBuilder();
		trqb.where(TrayekRouteDao.Properties.Id.eq(idRuteTrayek));
		long idTrayek = trqb.list().get(0).getIdTrayek();
		QueryBuilder<Trayek> tqb = KiriApp.getTrayekDao().queryBuilder();
		tqb.where(TrayekDao.Properties._id.eq(idTrayek));
		noTrayekString = tqb.list().get(0).getNoTrayek();
		jenisAngkutanString = tqb.list().get(0).getJenisAngkutan();
		jenisTrayekString = tqb.list().get(0).getJenisTrayek();
		
		for (int i = 0; i <pointId.size(); i++) {
			QueryBuilder<TrayekWaypoint> qb = KiriApp.getTrayekWaypointDao().queryBuilder();
			qb.where(TrayekWaypointDao.Properties.Id.eq(pointId.get(i)));
			pointString.add(qb.list().get(0).getPoint());
		}
	}
	
	public String getTitle() {
		if (noTrayekString == null) {
			constructTexts();
		}
		return noTrayekString;
	}
	
	public String getDetail() {
		if (noTrayekString == null) {
			constructTexts();
		}
		
		if(type == Type.BOARD) {
			return String.format(resources.getString(R.string.board_at), jenisTrayekString.equals("-") ? jenisAngkutanString : jenisTrayekString, noTrayekString, pointString.get(0));
		} else if(type == Type.ALIGHT) {
			return String.format(resources.getString(R.string.alight_from), jenisTrayekString.equals("-") ? jenisAngkutanString : jenisTrayekString, noTrayekString, pointString.get(0));
		} else {
			String detail = String.format(resources.getString(R.string.keep_riding_through) + " ", noTrayekString);
			if(pointId.size() == 1) {
				detail += (" " +pointString.get(0));
			} else {
				for(int i = 0; i < pointId.size() - 1; i++) {
					detail += String.format("%s, ", pointString.get(i));
				}
				detail += String.format(resources.getString(R.string.and), pointString.get(pointString.size() - 1));
			}
			return detail;
		}
	}

	public String getJenisAngkutanString() {
		return jenisAngkutanString;
	}

	public String getJenisTrayekString() {
		return jenisTrayekString;
	}
	public String getNoTrayekString() {
		return noTrayekString;
	}
}
