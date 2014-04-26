package com.kiri.hackjak.sqliteasset;

import android.content.Context;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "hackjack_db";
	private static final int DATABASE_VERSION = 3;

	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
}