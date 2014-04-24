package com.kiri.hackjak.utils;

public class Constants {

	public final static int CACHE_TIME_HALF_MONTH = 1209600000;
	public final static int CACHE_TIME_6_HOUR = 21600000;
	public final static int CACHE_TIME_15_MIN = 3600000;

	public final static String API_KEY = "IzFJ3nIorHMx6k91oyxFzEPyYpJOpQ86";
	public final static String PATTERN_DATE = "yyyy-MM-dd";

	public class ApiUrl {
		// public final static String BASE_URL = "http://api.tiket.com/";
		public final static String BASE_URL = "http://api.hackjak.bappedajakarta.go.id/";
		public final static String BUSWAY_LIST_KORIDOR = BASE_URL
				+ "busway/koridor/%1$s?apiKey=" + API_KEY;
		public final static String TRAYEK_ALL = BASE_URL + "trayek?apiKey="
				+ API_KEY + "&per_page=250&page=%1$s";
	}

	public class Pref {
	}

}
