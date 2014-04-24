package com.kiri.hackjak.apis;

import android.content.Context;

import com.androidquery.AQuery;
import com.google.myjson.Gson;

public class BaseApi {
	protected AQuery aq;
	protected Gson gson;
	protected Context context;

	public BaseApi(Context ctx) {
		this.context = ctx;
		gson = new Gson();
		aq = new AQuery(context);
	}
}
