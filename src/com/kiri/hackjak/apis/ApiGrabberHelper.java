package com.kiri.hackjak.apis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.apis.TrayekAllApi.TrayekApiDao;
import com.kiri.hackjak.grdaos.Trayek;
import com.kiri.hackjak.grdaos.TrayekRoute;
import com.kiri.hackjak.grdaos.TrayekRouteDetail;
import com.kiri.hackjak.grdaos.TrayekWaypoint;
import com.kiri.hackjak.grdaos.TrayekWaypointDao;

public class ApiGrabberHelper {
	private Activity mContext;
	ProgressDialog dialog;
	TrayekAllApi trayekAllApi;
	int totalTrayekFromApi = 0;
	int trayekApiPage = 1;

	public ApiGrabberHelper(Activity ctx) {
		mContext = ctx;
	}

	public void grabDataFromApi() {
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("Loading");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setIndeterminate(false);

		// cek, jika masih kosong, ambil dari server
		if (KiriApp.getTrayekDao().count() == 0) {
			// call api
			trayekAllApi = new TrayekAllApi(mContext, trayekAllApiListener);
			trayekAllApi.callApi(trayekApiPage);
			dialog.show();
		}
	}

	int totalData = 0;
	TrayekAllApi.ApiResultListener trayekAllApiListener = new TrayekAllApi.ApiResultListener() {

		@Override
		public void onApiResultError(String errorMessage) {
			dialog.cancel();
		}

		@Override
		public void onApiPreCall() {

		}

		@Override
		public void onApiResultOk(final int totalCurrent, final int totalAll,
				final List<TrayekApiDao> listTrayek) {
			if (trayekApiPage == 1)
				dialog.setMax(totalAll);

			Thread thread = new Thread() {
				@Override
				public void run() {

					for (int i = 0; i < listTrayek.size(); i++) {
						TrayekApiDao trayekApiDao = listTrayek.get(i);
						Trayek trayek = new Trayek(null, trayekApiDao.getId(),
								trayekApiDao.getJenisAngkutan(),
								trayekApiDao.getJenisTrayek(),
								trayekApiDao.getNoTrayek(),
								trayekApiDao.getNamaTrayek(),
								trayekApiDao.getTerminal(),
								trayekApiDao.getKodeWilayah(),
								trayekApiDao.getWilayah(),
								trayekApiDao.getSukuDinas());
						long idTrayek = KiriApp.getTrayekDao().insert(trayek);

						List<List<String>> arrRute = new ArrayList<List<String>>();
						arrRute.add(trayekApiDao.getRuteBerangkat());
						arrRute.add(trayekApiDao.getRuteKembali());
						// loop ruteberangkat & rutekembali
						for (int j = 0; j < arrRute.size(); j++) {

							// TRAYEK ROUTE
							String routeName;
							if (j == 0) {
								routeName = "ruteBerangkat";
							} else {
								routeName = "ruteKembali";
							}
							TrayekRoute trayekRoute = new TrayekRoute(null,
									routeName, idTrayek);
							long trayekRouteId = KiriApp.getTrayekRouteDao()
									.insert(trayekRoute);

							for (int k = 0; k < arrRute.get(j).size(); k++) {

								// TRAYEK WAYPOINT
								String point = arrRute.get(j).get(k);
								TrayekWaypoint trayekWaypoint = new TrayekWaypoint(
										null, point);
								// cek apakah sudah ada
								TrayekWaypoint searchDao = KiriApp
										.getTrayekWaypointDao()
										.queryBuilder()
										.where(TrayekWaypointDao.Properties.Point
												.eq(point)).unique();
								long waypointId;
								if (searchDao != null) {
									waypointId = searchDao.getId();
								} else {
									waypointId = KiriApp.getTrayekWaypointDao()
											.insert(trayekWaypoint);
								}

								// TRACK DETAIL
								TrayekRouteDetail trayekRouteDetail = new TrayekRouteDetail(
										null, waypointId, trayekRouteId, k + 1);
								KiriApp.getTrayekRouteDetailDao().insert(
										trayekRouteDetail);
							}
						}

						mContext.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								totalData = totalData + 1;
								dialog.setProgress(totalData);
							}
						});
					}

					totalTrayekFromApi = totalTrayekFromApi + totalCurrent;

					mContext.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// call next page
							if (totalTrayekFromApi < totalAll) {
								trayekApiPage++;
								trayekAllApi.callApi(trayekApiPage);
							} else {
								dialog.cancel();
							}

						}
					});
				};
			};
			thread.start();
		}

	};
}
