/**
 * 
 */
package com.craftsilicon.littlecabrider.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Elluminati elluminati.in
 * 
 */
public class DriverLocation {
	private LatLng latLng;
	private String distance;
	private String time;
	private String unit;
	private String WIFIPass;
	private double bearing;

	public double getBearing() {
		return bearing;
	}

	public void setBearing(double bearing) {
		this.bearing = bearing;
	}

	public String getWIFIPass() {
		return WIFIPass;
	}

	public void setWIFIPass(String WIFIPass) {
		this.WIFIPass = WIFIPass;
	}
	
	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public String getstrTime() {
		return time;
	}

	public void setstrTime(String time) {
		this.time = time;
	}

}
