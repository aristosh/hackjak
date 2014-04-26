package com.kiri.hackjak.apis;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.app.ProgressDialog;

import com.kiri.hackjak.KiriApp;
import com.kiri.hackjak.apis.TrayekAllApi.TrayekApiDao;
import com.kiri.hackjak.sqlite.Trayek;
import com.kiri.hackjak.sqlite.TrayekWaypoint;
import com.kiri.hackjak.sqlite.TrayekWaypointDao;

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
		dialog.setMessage("Loading");
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
					for (int j = 0; j < 2; j++) {
						// RUTE
						TrayekApiDao trayekApiDao = qTrayekData.poll();
						String kategori = (trayekApiDao.getJenisTrayek()
								.equals("-")) ? trayekApiDao.getJenisAngkutan()
								: trayekApiDao.getJenisTrayek();
						List<String> rute;
						if (j == 0) {
							rute = trayekApiDao.getRuteBerangkat();
						} else {
							rute = trayekApiDao.getRuteKembali();
						}
						String namaSingkat = String.format("%s %s (arah %s)",
								trayekApiDao.getNoTrayek(),
								trayekApiDao.getNamaTrayek(),
								rute.get(rute.size() - 1));
						String ruteStr = KiriApp.implodeArray(rute, ",");
						Trayek trayek = new Trayek(null, trayekApiDao.getId(),
								trayekApiDao.getNoTrayek(), kategori,
								namaSingkat, ruteStr);
						long idTrayek = KiriApp.getTrayekDao().insert(trayek);

						// TRAYEK WAYPOINT
						for (int k = 0; k < rute.size(); k++) {
							String point = rute.get(k);
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
						}
						mContext.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								totalProcessedData = totalProcessedData + 1;
								dialog.setProgress(totalProcessedData);
							}
						});
					}

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
