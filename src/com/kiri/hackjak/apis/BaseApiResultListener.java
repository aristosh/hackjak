package com.kiri.hackjak.apis;

public interface BaseApiResultListener {
	public void onApiPreCall();

	public void onApiResultError(String errorMessage);
}
