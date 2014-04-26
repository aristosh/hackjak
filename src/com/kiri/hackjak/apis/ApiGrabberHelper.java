package com.kiri.hackjak.apis;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.apis.TrayekAllApi.TrayekApiDao;
import com.kiri.hackjak.db.Trayek;
import com.kiri.hackjak.db.TrayekRoute;
import com.kiri.hackjak.db.TrayekRouteDetail;
import com.kiri.hackjak.db.TrayekWaypoint;
import com.kiri.hackjak.db.TrayekWaypointDao;

public class ApiGrabberHelper {
	private Activity mContext;
	ProgressDialog dialog;
	TrayekAllApi trayekAllApi;
	int totalTrayekFromApi = 0;
	int trayekApiPage = 1;

	int totalProcessedData = 0;
	boolean isProcessDbRunning = false;

	Queue<TrayekApiDao> qTrayekData = new LinkedList<TrayekAllApi.TrayekApiDao>();

	public ApiGrabberHelper(Activity ctx) {
		mContext = ctx;
	}

	public void grabDataFromApi() {
		dialog = new ProgressDialog(mContext);
		dialog.setMessage("Preparing data for first time...");
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.setIndeterminate(false);

		// cek, jika masih kosong, ambil dari server
		if (KiriApp.getTrayekDao().count() == 0) {
			// call api
			trayekAllApi = new TrayekAllApi(mContext, trayekAllApiListener);
			trayekAllApi.callApi(trayekApiPage);
		}
	}
	
	private void insertDataToDb() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (!qTrayekData.isEmpty()) {
					// RUTE
					TrayekApiDao trayekApiDao = qTrayekData.poll();
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
							totalProcessedData = totalProcessedData + 1;
							dialog.setProgress(totalProcessedData);
						}
					});
				}

				mContext.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						dialog.cancel();
					}
				});
				isProcessDbRunning = false;
			};
		};
		thread.start();
	}

	TrayekAllApi.ApiResultListener trayekAllApiListener = new TrayekAllApi.ApiResultListener() {

		@Override
		public void onApiResultError(String errorMessage) {
			dialog.cancel();
		}

		@Override
		public void onApiPreCall() {
			if (!isProcessDbRunning)
				dialog.show();
		}

		@Override
		public void onApiResultOk(final int totalCurrent, final int totalAll,
				final List<TrayekApiDao> listTrayek) {
			if (trayekApiPage == 1)
				dialog.setMax(totalAll);

			qTrayekData.addAll(listTrayek);
			if (!isProcessDbRunning) {
				isProcessDbRunning = true;
				insertDataToDb();
			}
			totalTrayekFromApi = totalTrayekFromApi + totalCurrent;

			mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// call next page
					if (totalTrayekFromApi < totalAll) {
						trayekApiPage++;
						trayekAllApi.callApi(trayekApiPage);
					}

				}
			});
		}

	};
}
