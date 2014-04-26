package com.kiri.hackjak.adapters;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.db.Trayek;
import com.kiri.hackjak.db.TrayekDao;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;

import de.greenrobot.dao.query.QueryBuilder;

public class FormattedResult {
	public long idRuteTrayek;
	public long naik;
	public long turun;

	private String kategoriString;
	private String noTrayekString;
	private String naikString;
	private String turunString;
	
	public void updateStringValues() {
		QueryBuilder<Trayek> qb = KiriApp.getTrayekDao().queryBuilder();
//		qb.where(TrayekDao.Properties._id.eq(idRuteTrayek));
//		Trayek trayek = qb.list().get(0);
//		noTrayekString = trayek.getNoTrayek();
//		kategoriString = trayek.getJenisTrayek().equals("-") ? trayek.getJenisAngkutan() : trayek.getJenisTrayek();

		QueryBuilder<TrayekWaypoint> qb2 = KiriApp.getTrayekWaypointDao().queryBuilder();
		qb2.where(TrayekWaypointDao.Properties.Id.eq(naik));
		naikString = qb2.list().get(0).getPoint();
		QueryBuilder<TrayekWaypoint> qb3 = KiriApp.getTrayekWaypointDao().queryBuilder();
		qb3.where(TrayekWaypointDao.Properties.Id.eq(turun));
		turunString = qb3.list().get(0).getPoint();
	}
	
	public String getTitle() {
		return noTrayekString;
	}
	public String getDetail() {
		return String.format("Take %s %s at %s, then alight at %s", kategoriString, noTrayekString, naikString, turunString);
	}
}
