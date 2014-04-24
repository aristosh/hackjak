package com.kiri.hackjak.apis;

public class BaseApiDao {
	private String error;

	public BaseApiDao() {
	}

	public BaseApiDao(String error, String errorMessage) {
		this.error = errorMessage;
	}

	/**
	 * @return the description
	 */
	public String getErrorMessage() {
		if (error != null)
			return error;
		else
			return null;

	}

	/**
	 * @param errorMsg
	 *            the description to set
	 */
	public void setErrorMessage(String errorMsg) {
		this.error = errorMsg;
	}
}
