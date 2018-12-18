package org.parc.restes.entity;

import java.util.Date;

public class ElasticDateR {
	private Date startTime;
	private Date endTime;
	private String units;
	private int unitsNumber;
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public int getUnitsNumber() {
		return unitsNumber;
	}

	public void setUnitsNumber(int unitsNumber) {
		this.unitsNumber = unitsNumber;
	}

	/**
	 * @param startTime
	 * @param endTime
	 * @param units
	 */
	public ElasticDateR(Date startTime, Date endTime, String units, int unitsNumber) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.units = units;
		this.unitsNumber = unitsNumber;
	}

}
