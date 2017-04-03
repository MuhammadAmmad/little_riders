package com.craftsilicon.littlecabrider.db;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.craftsilicon.littlecabrider.models.DriverLocation;
import com.craftsilicon.littlecabrider.models.DriverLocations;
import com.craftsilicon.littlecabrider.models.User;
import com.craftsilicon.littlecabrider.utils.Const;
import com.google.android.gms.maps.model.LatLng;

public class DBAdapter {

	private static final String TAG = "[ DBAdapter ]";

	private static final String KEY_ROWID = "rowid";
	private static final String KEY_LAT = "latitude";
	private static final String KEY_LON = "longitude";
	private static final String KEY_USER_ID = "user_id";

	private static final String KEY_FULL_NAME = "full_name";
	private static final String KEY_ADDRESS = "address";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_CONTACT = "contact";
	private static final String KEY_BIO = "bio";
	private static final String KEY_PICTURE = "picture";
	private static final String KEY_ZIP_CODE = "zip_code";

	private static final int DATABASE_VERSION = 2;
	public static final String DATABASE_NAME = "LittleCab";

	private static final String TABLE_LOCATION = "table_location";
	// private static final String REQUEST_TABLE = "ClientRequest";
	private static final String USER_TABLE = "User";
	private static final String DRIVER_TABLE = "DriverLocation";
	private static final String TABLE_CREATE_LOCATION = "create table "
			+ TABLE_LOCATION + "( " + KEY_ROWID
			+ " integer primary key autoincrement," + KEY_LAT
			+ " text not null," + KEY_LON + " text not null);";

	private static final String TABLE_CREATE_USER = "create table "
			+ USER_TABLE + "( " + KEY_ROWID
			+ " integer primary key autoincrement," + KEY_USER_ID
			+ " integer not null," + KEY_FULL_NAME + " text not null,"
			+ KEY_EMAIL + " text not null," + KEY_CONTACT + " text not null,"
			+ KEY_PICTURE + " text not null," + KEY_BIO + " text,"
			+ KEY_ADDRESS + " text," + KEY_ZIP_CODE + " text);";
	
	
	
	//driver.setDriverId(jsonArray.getJSONObject(i).getInt(Const.Params.ID));
	//driver.setLatitude(jsonArray.getJSONObject(i).getDouble(Const.Params.LATITUDE));
	//driver.setLongitude(jsonArray.getJSONObject(i).getDouble(Const.Params.LONGITUDE));
	//driver.setBearing(jsonArray.getJSONObject(i).getDouble(Const.Params.BEARING));
	//driver.setVehicleTypeId(jsonArray.getJSONObject(i).getInt("type"));
	private static final String TABLE_CREATE_DRIVER = "create table "
			+ DRIVER_TABLE 
			+ "( " 
			+ Const.Params.ID 			+ " integer primary key autoincrement," 
			+ Const.Params.LATITUDE     + " integer not null," 
			+ Const.Params.LONGITUDE    + " text not null,"
			+ Const.Params.BEARING   	+ " text not null," 
			+ Const.Params.ISACTIVE     + " text not null,"
			+ Const.Params.TYPE 	    + " text not null);";

	private DatabaseHelper DBhelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		DBhelper = new DatabaseHelper(ctx);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(TABLE_CREATE_LOCATION);
			db.execSQL(TABLE_CREATE_USER);
			db.execSQL(TABLE_CREATE_DRIVER);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_USER);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_DRIVER);
			
			onCreate(db);
		}
	}

	public DBAdapter open() throws SQLiteException {
		db = DBhelper.getWritableDatabase();
		return this;
	}

	public boolean isCreated() {
		if (db != null) {
			return db.isOpen();
		}

		return false;
	}

	public boolean isOpen() {
		return db.isOpen();
	}

	public void close() {
		DBhelper.close();
		db.close();
	}

	public long addLocation(LatLng latLng) {
		ContentValues values = new ContentValues();
		values.put(KEY_LAT, latLng.latitude);
		values.put(KEY_LON, latLng.longitude);

		return db.insert(TABLE_LOCATION, null, values);
	}

	public long addLocations(ArrayList<LatLng> listLatLang) {
		int count = 0;
		for (int i = 0; i < listLatLang.size(); i++) {
			LatLng latLng = listLatLang.get(i);
			ContentValues values = new ContentValues();
			values.put(KEY_LAT, latLng.latitude);
			values.put(KEY_LON, latLng.longitude);
			long id = db.insert(TABLE_LOCATION, null, values);
			if (id != -1)
				count += 1;
		}
		return count;

	}

	public ArrayList<LatLng> getLocations() {
		ArrayList<LatLng> points = new ArrayList<LatLng>();
		String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;
		try {

			Cursor cursor = db.rawQuery(selectQuery, null);
			if (cursor.moveToFirst()) {
				do {

					LatLng latLng = new LatLng(Double.parseDouble(cursor
							.getString(1)), Double.parseDouble(cursor
							.getString(2)));
					points.add(latLng);
				} while (cursor.moveToNext());
			}
			cursor.close();
			return points;
		} catch (Exception e) {
			Log.d("Error in getting users from DB", e.getMessage());
			return null;
		}
	}

	public int deleteAllLocations() {
		return db.delete(TABLE_LOCATION, null, null);
	}

	public boolean isLocationsExists() {
		String selectQuery = "SELECT * from " + TABLE_LOCATION;
		boolean isExists = false;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
			isExists = true;
			cursor.close();
		}
		return isExists;
	}

	public long createUser(User user) {
		deleteUser();

		ContentValues values = new ContentValues();
		values.put(KEY_USER_ID, user.getUserId());
		values.put(KEY_FULL_NAME, user.getFname());
		values.put(KEY_EMAIL, user.getEmail());
		values.put(KEY_CONTACT, user.getContact());
		values.put(KEY_ADDRESS, user.getAddress());
		values.put(KEY_ZIP_CODE, user.getZipcode());
		values.put(KEY_BIO, user.getBio());
		values.put(KEY_PICTURE, user.getPicture());
		return db.insert(USER_TABLE, null, values);
	}
	
	public long createDriver(DriverLocations driver) {
		deleteDriverLocations();
		String selectQuery = "SELECT * from " + DRIVER_TABLE + " where "+Const.Params.ID+ " = '"+driver.getdriverId()+"'";
		User user = null;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
		ContentValues values = new ContentValues();
		values.put(Const.Params.ID, 		driver.getdriverId());
		values.put(Const.Params.LATITUDE, 	driver.getdriverLat());
		values.put(Const.Params.LONGITUDE, 	driver.getdriverLong());
		values.put(Const.Params.BEARING, 	driver.getdriverBearing());
		values.put(Const.Params.ISACTIVE, 	driver.getdriverIsActive());
		values.put(Const.Params.TYPE, 		driver.getDriverType());
		return db.insert(DRIVER_TABLE, null, values);
		}
		else{
			return db.insert(DRIVER_TABLE, null, null);
		}
	}

	public User getUser() {
		String selectQuery = "SELECT * from " + USER_TABLE;
		User user = null;
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			user = new User();
			user.setUserId(cursor.getInt(1));
			user.setFname(cursor.getString(2));
			user.setEmail(cursor.getString(3));
			user.setContact(cursor.getString(4));
			user.setPicture(cursor.getString(5));
			user.setBio(cursor.getString(6));
			user.setAddress(cursor.getString(7));
			user.setZipcode(cursor.getString(8));
			cursor.close();
		}

		return user;
	}

	public int deleteUser() {
		return db.delete(USER_TABLE, null, null);
	}
	public int deleteDriverLocations() {
		return db.delete(DRIVER_TABLE, null, null);
	}

}
