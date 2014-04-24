package com.kiri.hackjak.apis;

import com.kiri.hackjak.apis.daos.BuswayHalteDao;
import com.kiri.hackjak.utils.Constants;

import java.util.List;

import android.content.Context;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

public class BuswayAllKoridorApi extends BaseApi {
	private String URL;

	private ApiResultListener resultListener;

	/**
	 * Listener for API result
	 */
	public interface ApiResultListener extends BaseApiResultListener {
		public void onApiResultOk(List<BuswayHalteDao> listHalteDao);
	}

	public BuswayAllKoridorApi(Context ctx, ApiResultListener resListener) {
		super(ctx);
		this.resultListener = resListener;
	}

	public void callApi(String koridorId) {
		URL = String.format(Constants.ApiUrl.BUSWAY_LIST_KORIDOR, koridorId);

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
						resultListener.onApiResultOk(apiDao.getResult());
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
		private List<BuswayHalteDao> result;

		public int getTotal() {
			return total;
		}

		public int getCurrentPageTotal() {
			return currentPageTotal;
		}

		public List<BuswayHalteDao> getResult() {
			return result;
		}

	}

}
