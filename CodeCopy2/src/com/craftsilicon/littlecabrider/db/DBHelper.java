package com.craftsilicon.littlecabrider.db;

import com.craftsilicon.littlecabrider.models.DriverLocations;
import com.craftsilicon.littlecabrider.models.User;

import android.content.Context;

public class DBHelper {

	private DBAdapter dbAdapter;

	public DBHelper(Context context) {
		dbAdapter = new DBAdapter(context);
	}

//	public void addLocation(LatLng latLng) {
//		dbAdapter.open();
//
//		dbAdapter.addLocation(latLng);
//
//		dbAdapter.close();
//	}

//	public ArrayList<LatLng> getLocations() {
//		dbAdapter.open();
//		ArrayList<LatLng> points = dbAdapter.getLocations();
//		dbAdapter.close();
//		return points;
//	}

//	public int deleteAllLocations() {
//		int count = 0;
//		dbAdapter.open();
//		count = dbAdapter.deleteAllLocations();
//		dbAdapter.close();
//		return count;
//
//	}

//	public boolean isLocationsExists() {
//		boolean isExists = false;
//		dbAdapter.open();
//		isExists = dbAdapter.isLocationsExists();
//		dbAdapter.close();
//		return isExists;
//	}
	
	public long createDriverLocation(DriverLocations driver) {
		long count = 0;
		dbAdapter.open();
		count = dbAdapter.createDriver(driver);
		dbAdapter.close();
		return count;

	}

	public long createUser(User user) {
		long count = 0;
		dbAdapter.open();
		count = dbAdapter.createUser(user);
		dbAdapter.close();
		return count;

	}

	public User getUser() {
		User user = null;
		dbAdapter.open();
		user = dbAdapter.getUser();
		dbAdapter.close();
		return user;
	}
	
	public int deleteDriverDetails() {
		int count = 0;
		dbAdapter.open();
		count = dbAdapter.deleteDriverLocations();
		dbAdapter.close();
		return count;
	}

	public int deleteUser() {
		int count = 0;
		dbAdapter.open();
		count = dbAdapter.deleteUser();
		dbAdapter.close();
		return count;
	}

}
