package com.kiri.hackjak.apis;

import com.kiri.hackjak.utils.Constants;

import java.util.List;

import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class TrayekAllApi extends BaseApi {
	private String URL;

	private ApiResultListener resultListener;

	/**
	 * Listener for API result
	 */
	public interface ApiResultListener extends BaseApiResultListener {
		public void onApiResultOk(int totalCurrent, int totalAll,
				List<TrayekApiDao> listTrayek);
	}

	public TrayekAllApi(Context ctx, ApiResultListener resListener) {
		super(ctx);
		this.resultListener = resListener;
	}

	public void callApi(int page) {
		URL = String.format(Constants.ApiUrl.TRAYEK_ALL, page);

		resultListener.onApiPreCall();
		// cache 15 menit
		aq.ajaxCancel();
		try {
			aq.ajax(URL, String.class, Constants.CACHE_TIME_15_MIN, callback);
		} catch (Exception e) {
			resultListener.onApiResultError(e.getMessage());
		}
	}

	private AjaxCallback<String> callback = new AjaxCallback<String>() {
		public void callback(String url, String result, AjaxStatus status) {
			if (result != null) {
				ApiDao apiDao = null;
				try {
					apiDao = gson.fromJson((result).toString(), ApiDao.class);

					if (apiDao.getErrorMessage() == null) {
						resultListener.onApiResultOk(
								apiDao.getCurrentPageTotal(),
								apiDao.getTotal(), apiDao.getResult());
					} else {
						resultListener.onApiResultError(apiDao
								.getErrorMessage());
					}
				} catch (Exception e) {
					resultListener.onApiResultError(e.getMessage());
				}
			} else {
				resultListener.onApiResultError(status.getMessage());
			}
		};
	};

	/**
	 * Class for catch the result
	 */
	public class ApiDao extends BaseApiDao {
		private int total;
		private int currentPageTotal;
		private List<TrayekApiDao> result;

		public int getTotal() {
			return total;
		}

		public int getCurrentPageTotal() {
			return currentPageTotal;
		}

		public List<TrayekApiDao> getResult() {
			return result;
		}

	}

	public class TrayekApiDao extends BaseApiDao {
		private String id;
		private String jenisAngkutan;
		private String jenisTrayek;
		private String noTrayek;
		private String namaTrayek;
		private String terminal;
		private String kodeWilayah;
		private String wilayah;
		private String sukuDinas;
		private List<String> ruteBerangkat;
		private List<String> ruteKembali;

		public String getId() {
			return id;
		}

		public String getJenisAngkutan() {
			return jenisAngkutan;
		}

		public String getJenisTrayek() {
			return jenisTrayek;
		}

		public String getNoTrayek() {
			return noTrayek;
		}

		public String getNamaTrayek() {
			return namaTrayek;
		}

		public String getTerminal() {
			return terminal;
		}

		public String getKodeWilayah() {
			return kodeWilayah;
		}

		public String getWilayah() {
			return wilayah;
		}

		public String getSukuDinas() {
			return sukuDinas;
		}

		public List<String> getRuteBerangkat() {
			return ruteBerangkat;
		}

		public List<String> getRuteKembali() {
			return ruteKembali;
		}

	}

}
