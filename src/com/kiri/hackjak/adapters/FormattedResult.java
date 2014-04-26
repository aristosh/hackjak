package com.kiri.hackjak.adapters;

public class FormattedResult {
	public long idRuteTrayek;
	public String naik;
	public String turun;

	public String getTitle() {
		return "" + idRuteTrayek;
	}
	public String getDetail() {
		return String.format("Take %d at %s, then alight at %s", idRuteTrayek, naik, turun);
	}
}
