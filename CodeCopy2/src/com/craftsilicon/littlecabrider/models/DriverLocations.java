package com.craftsilicon.littlecabrider.models;

import java.io.Serializable;

public class DriverLocations implements Serializable {
	
	private int driverId;

	public int getdriverId() {
		return driverId;
	}

	public void setdriverId(int driverId) {
		this.driverId = driverId;
	}

	public Double getdriverLat() {
		return driverLat;
	}

	public void setdriverLat(Double driverLat) {
		this.driverLat = driverLat;
	}

	public Double getdriverLong() {
		return driverLong;
	}

	public void setdriverLong(Double driverLong) {
		this.driverLong = driverLong;
	}

	public Double getdriverBearing() {
		return driverBearing;
	}

	public void setdriverBearing(Double driverBearing) {
		this.driverBearing = driverBearing;
	}

	public boolean getdriverIsActive() {
		return IsActive;
	}

	public void setIsActive(boolean IsActive) {
		this.IsActive = IsActive;
	}

	public int getDriverType() {
		return DriverType;
	}

	public void setDriverType(int DriverType) {
		this.DriverType = DriverType;
	}


	private int DriverType;
	private Double driverBearing, driverLong, driverLat;
	private boolean IsActive;

}
