package com.kiri.hackjak.sqlite;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table TRAYEK_WAYPOINT.
 */
public class TrayekWaypoint {

	private Long id;
	/** Not-null value. */
	private String point;

	public TrayekWaypoint() {
	}

	public TrayekWaypoint(Long id) {
		this.id = id;
	}

	public TrayekWaypoint(Long id, String point) {
		this.id = id;
		this.point = point;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/** Not-null value. */
	public String getPoint() {
		return point;
	}

	/**
	 * Not-null value; ensure this value is available before it is saved to the
	 * database.
	 */
	public void setPoint(String point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return point;
	}
}
