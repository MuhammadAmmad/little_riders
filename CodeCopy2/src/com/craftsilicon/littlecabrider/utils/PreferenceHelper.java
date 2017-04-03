package com.craftsilicon.littlecabrider.utils;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceHelper {

	private SharedPreferences app_prefs;
	private final String USER_ID = "user_id";
	private final String EMAIL = "email";
	private final String PASSWORD = "password";
	private final String DEVICE_TOKEN = "device_token";
	private final String SESSION_TOKEN = "session_token";
	private final String REQUEST_ID = "request_id";
	private final String REQUEST_TIME = "request_time";
	private final String REQUEST_LATITUDE = "request_latitude";
	private final String REQUEST_LONGITUDE = "request_longitude";
	private final String LOGIN_BY = "login_by";
	private final String SOCIAL_ID = "social_id";
	private final String PAYMENT_MODE = "payment_mode";
	private final String DEFAULT_CARD = "default_card";
	private final String DEFAULT_CARD_NO = "default_card_no";
	private final String DEFAULT_CARD_TYPE = "default_card_type";
	private final String BASE_PRICE = "base_cost";
	private final String DISTANCE_PRICE = "distance_cost";
	private final String TIME_PRICE = "time_cost";
	private final String IS_TRIP_STARTED = "is_trip_started";
	private final String HOME_ADDRESS = "home_address";
	private final String WORK_ADDRESS = "work_address";
	private final String DefaultStations = "Capital FM,Classic,KISS 100,";
	private final String DEST_LAT = "dest_lat";
	private final String DEST_LNG = "dest_lng";

	private final String sDEST_LAT = "sdest_lat";
	private final String sDEST_LNG = "sdest_lng";
	private final String dDEST_LAT = "ddest_lat";
	private final String dDEST_LNG = "ddest_lng";

	private final String PHONE_NUMBER = "phone_number";
	private final String RIDER_SOURCE_ADDRESS = "rider_source_address";
	private final String RIDER_DESTINATION_ADDRESS = "rider_destination_address";
	private final String PAYMENT_MODES_RESPONSE = "payment_modes_response";
	private final String PAYMENT_BANKS = "payment_banks";
	private final String DRIVER_ID = "driver_email";

	private final String REFEREE = "is_referee";
	private final String PROMO_CODE = "promo_code";
	// private Context context;
	private final String TIME_ZONE = "time_zone";
	private final String START_TIME = "start_time";
	private final String CURRENT_TRIP_TIME = "time";

	private final String IS_LOGGED_IN = "logged_in";
	private static Context context = null;

	public PreferenceHelper(Context context) {
		app_prefs = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		PreferenceHelper.context = context;
		// savekv("");
		// saveiv();
	}

	public static String eaes(String text) {
		String data = "";
		try {
			CryptLib _crypt = new CryptLib();
			String key = CryptLib.SHA256("KBSB&er3bflx9%", 32); // 32 bytes =
																// 256 bit
			String iv = "84jfkfndl3ybdfkf";
			data = _crypt.encrypt(text, key, iv); // encrypt
		} catch (Exception e) {

		}
		return data;
	}

	// String IV = "84jfkfndl3ybdfkf", KeyValue = "KBSB&er3bflx9%";

	public static String daes(String text) {
		String data = "";
		try {
			CryptLib _crypt = new CryptLib();
			String key = CryptLib.SHA256("KBSB&er3bflx9%", 32); // 32 bytes =
																// 256 bit
			AppLog.Log("addpayment_kv", "kv:" + "KBSB&er3bflx9%");
			String iv = "84jfkfndl3ybdfkf";
			AppLog.Log("addpayment_iv", "iv:" + iv);
			data = _crypt.decrypt(text, key, iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String getsmsps() {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		return mySharedPreferences.getString("smsps", "");
	}

	public static void savesmsps(String smsps) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("smsps", smsps);
		editor.commit();
	}

	public static String getkv2() {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		return mySharedPreferences.getString("kv", "");
	}

	public static void savekv2(String kv) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("kv", kv);
		editor.commit();
	}

	public static String getiv2() {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		return mySharedPreferences.getString("iv", "");
	}

	public void saveiv2(String iv) {
		SharedPreferences mySharedPreferences = context.getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		editor.putString("iv", iv);
		editor.commit();
	}

	public void putRiderSourceAddress(String rider_source_address) {
		Editor edit = app_prefs.edit();
		edit.putString(RIDER_SOURCE_ADDRESS, rider_source_address);
		edit.commit();
	}

	public void putRiderDestinationAddress(String rider_destination_address) {
		Editor edit = app_prefs.edit();
		edit.putString(RIDER_DESTINATION_ADDRESS, rider_destination_address);
		edit.commit();
	}

	public String getRiderSourceAddress() {

		return app_prefs.getString(RIDER_SOURCE_ADDRESS, null);
	}

	public String getRiderDestinationAddress() {

		return app_prefs.getString(RIDER_DESTINATION_ADDRESS, null);
	}

	public void putDriverId(String driver_email) {
		Editor edit = app_prefs.edit();
		edit.putString(DRIVER_ID, driver_email);
		edit.commit();
	}

	public String getDriverId() {
		return app_prefs.getString(DRIVER_ID, null);
	}

	public void putPaymentBanks(String payment_banks) {
		Editor edit = app_prefs.edit();
		edit.putString(PAYMENT_BANKS, payment_banks);
		edit.commit();
	}

	public String getPaymentBanks() {
		return app_prefs.getString(PAYMENT_BANKS, null);
	}

	public void putPaymentCards(String payment_banks) {
		Editor edit = app_prefs.edit();
		edit.putString("PAYMENT_CARDS", payment_banks);
		edit.commit();
	}

	public String getPaymentCards() {
		return app_prefs.getString("PAYMENT_CARDS", null);
	}

	public void putPaymentModes(String payment_modes) {
		Editor edit = app_prefs.edit();
		edit.putString(PAYMENT_MODES_RESPONSE, payment_modes);
		edit.commit();
	}

	public String getPaymentModes() {
		return app_prefs.getString(PAYMENT_MODES_RESPONSE, null);
	}

	public void putPhoneNumber(String phone_number) {
		Editor edit = app_prefs.edit();
		edit.putString(PHONE_NUMBER, phone_number);
		edit.commit();
	}

	public String getPhoneNumber() {
		return app_prefs.getString(PHONE_NUMBER, null);
	}

	public void putRiderCountry(String country_name) {
		Editor edit = app_prefs.edit();
		edit.putString("RIDER_COUNTRY", country_name);
		edit.commit();
	}

	public String getRiderCountry() {
		return app_prefs.getString("RIDER_COUNTRY", null);
	}

	public void putRiderCountryCode(String country_code) {
		Editor edit = app_prefs.edit();
		edit.putString("RIDER_COUNTRY_CODE", country_code);
		edit.commit();
	}

	public String getRiderCountryCode() {
		return app_prefs.getString("RIDER_COUNTRY_CODE", null);
	}

	public void putSessionID(String session_id) {
		Editor edit = app_prefs.edit();
		edit.putString("SESSION ID", session_id);
		edit.commit();
	}

	public String getSessionID() {
		return app_prefs.getString("SESSION ID", null);
	}

	public void putVehicleSelected(String selected_vehicle) {
		Editor edit = app_prefs.edit();
		edit.putString("SELECTED VEHICLE", selected_vehicle);
		edit.commit();

	}

	public String getVehicleSelected() {

		return app_prefs.getString("SELECTED VEHICLE", null);
	}

	public void putUserId(String userId) {
		Editor edit = app_prefs.edit();
		edit.putString(USER_ID, userId);
		edit.commit();
	}

	public void putIMEI(String IMEI) {
		Editor edit = app_prefs.edit();
		edit.putString("IMEI", IMEI);
		edit.commit();
	}

	public String getIMEI() {
		return app_prefs.getString("IMEI", null);
	}

	public void putEmail(String email) {
		Editor edit = app_prefs.edit();
		edit.putString(EMAIL, email);
		edit.commit();
	}

	public String getEmail() {
		return app_prefs.getString(EMAIL, null);
	}

	public void putPassword(String password) {
		Editor edit = app_prefs.edit();
		edit.putString(PASSWORD, password);
		edit.commit();
	}

	public String getPassword() {
		return app_prefs.getString(PASSWORD, null);
	}

	public void putBasePrice(float price) {
		Editor edit = app_prefs.edit();
		edit.putFloat(BASE_PRICE, price);
		edit.commit();
	}

	public float getBasePrice() {
		return app_prefs.getFloat(BASE_PRICE, 0f);
	}

	public void putMyCountry(String mycountry) {
		Editor edit = app_prefs.edit();
		edit.putString("COUNTRY_CODE", mycountry);
		edit.commit();
	}

	public String getMyCountry() {
		return app_prefs.getString("COUNTRY_CODE", "");
	}

	public void putDistancePrice(float price) {
		Editor edit = app_prefs.edit();
		edit.putFloat(DISTANCE_PRICE, price);
		edit.commit();
	}

	public float getDistancePrice() {
		return app_prefs.getFloat(DISTANCE_PRICE, 0f);
	}

	public void putTimePrice(float price) {
		Editor edit = app_prefs.edit();
		edit.putFloat(TIME_PRICE, price);
		edit.commit();
	}

	public float getTimePrice() {
		return app_prefs.getFloat(TIME_PRICE, 0f);
	}

	public void putSocialId(String id) {
		Editor edit = app_prefs.edit();
		edit.putString(SOCIAL_ID, id);
		edit.commit();
	}

	public String getSocialId() {
		return app_prefs.getString(SOCIAL_ID, null);
	}

	public String getUserId() {
		return app_prefs.getString(USER_ID, null);

	}

	public void putDeviceToken(String deviceToken) {
		Editor edit = app_prefs.edit();
		edit.putString(DEVICE_TOKEN, deviceToken);
		edit.commit();
	}

	public String getDeviceToken() {
		return app_prefs.getString(DEVICE_TOKEN, null);
		// return app_prefs.getString("hardcodedtoken", null);

	}

	public void putSessionToken(String sessionToken) {
		Editor edit = app_prefs.edit();
		edit.putString(SESSION_TOKEN, sessionToken);
		edit.commit();
	}

	public String getSessionToken() {
		return app_prefs.getString(SESSION_TOKEN, null);

	}

	public void putRequestId(int requestId) {
		Editor edit = app_prefs.edit();
		edit.putInt(REQUEST_ID, requestId);
		edit.commit();
	}

	public int getRequestId() {
		return app_prefs.getInt(REQUEST_ID, Const.NO_REQUEST);
	}

	public void putLoginBy(String loginBy) {
		Editor edit = app_prefs.edit();
		edit.putString(LOGIN_BY, loginBy);
		edit.commit();
	}

	public String getLoginBy() {
		return app_prefs.getString(LOGIN_BY, Const.MANUAL);
	}

	public void putRequestTime(long time) {
		Editor edit = app_prefs.edit();
		edit.putLong(REQUEST_TIME, time);
		edit.commit();
	}

	public long getRequestTime() {
		return app_prefs.getLong(REQUEST_TIME, Const.NO_TIME);
	}

	public void putPaymentMode(int mode) {
		Editor edit = app_prefs.edit();
		edit.putInt(PAYMENT_MODE, mode);
		edit.commit();
	}

	public int getPaymentMode() {
		return app_prefs.getInt(PAYMENT_MODE, Const.CASH);
	}

	public void putDefaultCard(int cardId) {
		Editor edit = app_prefs.edit();
		edit.putInt(DEFAULT_CARD, cardId);
		edit.commit();
	}

	public int getDefaultCard() {
		return app_prefs.getInt(DEFAULT_CARD, 0);
	}

	public void putDefaultCardNo(String cardNo) {
		Editor edit = app_prefs.edit();
		edit.putString(DEFAULT_CARD_NO, cardNo);
		edit.commit();
	}

	public String getDefaultCardNo() {
		return app_prefs.getString(DEFAULT_CARD_NO, "");
	}

	public void putDefaultCardType(String cardType) {
		Editor edit = app_prefs.edit();
		edit.putString(DEFAULT_CARD_TYPE, cardType);
		edit.commit();
	}

	public String getDefaultCardType() {
		return app_prefs.getString(DEFAULT_CARD_TYPE, "");
	}

	public boolean getIsTripStarted() {
		return app_prefs.getBoolean(IS_TRIP_STARTED, false);
	}

	public boolean getIsLoggedIn() {
		return app_prefs.getBoolean(IS_LOGGED_IN, false);
	}

	public void putIsLoggedIn(boolean logged_in) {
		Editor edit = app_prefs.edit();
		edit.putBoolean(IS_LOGGED_IN, logged_in);
		edit.commit();
	}

	public void putIsTripStarted(boolean started) {
		Editor edit = app_prefs.edit();
		edit.putBoolean(IS_TRIP_STARTED, started);
		edit.commit();
	}

	public String getHomeAddress() {
		return app_prefs.getString(HOME_ADDRESS, null);
	}

	public void putHomeAddress(String homeAddress) {
		Editor edit = app_prefs.edit();
		edit.putString(HOME_ADDRESS, homeAddress);
		edit.commit();
	}

	public String getWorkAddress() {
		return app_prefs.getString(WORK_ADDRESS, null);
	}

	public void putWorkAddress(String homeAddress) {
		Editor edit = app_prefs.edit();
		edit.putString(WORK_ADDRESS, homeAddress);
		edit.commit();
	}

	public String getMyRating() {
		return app_prefs.getString("MyRating", "0");
	}

	public void saveMyRating(String MyRating) {
		Editor edit = app_prefs.edit();
		edit.putString("MyRating", MyRating);
		edit.commit();
	}

	/*
	 * public String get_taxiPricePerUnitDistance() { return
	 * app_prefs.getString("taxiPricePerUnitDistance", null); } public void
	 * save_taxiPricePerUnitDistance(String taxiPricePerUnitDistance) { Editor
	 * edit = app_prefs.edit(); edit.putString("taxiPricePerUnitDistance",
	 * taxiPricePerUnitDistance); edit.commit(); }
	 */

	public Double get_taxiPricePerUnitDistance() {
		return Double.longBitsToDouble(app_prefs.getLong("taxiPricePerUnitDistance", Double.doubleToLongBits(0)));
	}

	public void save_taxiPricePerUnitDistance(Double taxiPricePerUnitDistance) {
		Editor edit = app_prefs.edit();
		edit.putLong("taxiPricePerUnitDistance", Double.doubleToRawLongBits(taxiPricePerUnitDistance));
		edit.commit();
	}

	/*
	 * public String get_taxiPricePerUnitTime() { return
	 * app_prefs.getString("taxiPricePerUnitTime", null); } public void
	 * save_taxiPricePerUnitTime(String taxiPricePerUnitTime) { Editor edit =
	 * app_prefs.edit(); edit.putString("taxiPricePerUnitTime",
	 * taxiPricePerUnitTime); edit.commit(); }
	 */
	public Double get_taxiPricePerUnitTime() {
		return Double.longBitsToDouble(app_prefs.getLong("taxiPricePerUnitTime", Double.doubleToLongBits(0)));
	}

	public void save_taxiPricePerUnitTime(Double taxiPricePerUnitTime) {
		Editor edit = app_prefs.edit();
		edit.putLong("taxiPricePerUnitTime", Double.doubleToRawLongBits(taxiPricePerUnitTime));
		edit.commit();
	}

	public String get_taxiMinFare() {
		return app_prefs.getString("taxiMinFare", "300");
	}

	public void save_taxiMinFare(String taxiMinFare) {
		Editor edit = app_prefs.edit();
		edit.putString("taxiMinFare", taxiMinFare);
		edit.commit();
	}

	/*
	 * public Long get_TripStartedTime() { return
	 * app_prefs.getLong("TripStartedTime", 0); } public void
	 * save_TripStartedTime(Long TripStartedTime) { Editor edit =
	 * app_prefs.edit(); edit.putLong("TripStartedTime", TripStartedTime);
	 * edit.commit(); }
	 */

	public String get_radioStation() {
		return app_prefs.getString("radioStation", DefaultStations);
	}

	public void save_radioStation(String radioStation) {
		Editor edit = app_prefs.edit();
		edit.putString("radioStation", radioStation);
		edit.commit();
	}

	public boolean getIsCorporate() {
		return app_prefs.getBoolean("IsCorporate", false);
	}

	public void putIsCorporate(boolean IsCorporate) {
		Editor edit = app_prefs.edit();
		edit.putBoolean("IsCorporate", IsCorporate);
		edit.commit();
	}

	public String get_corporatecode() {
		return app_prefs.getString("corporatecode", "0");
	}

	public void save_corporatecode(String corporatecode) {
		Editor edit = app_prefs.edit();
		edit.putString("corporatecode", corporatecode);
		edit.commit();
	}

	public String get_corporatename() {
		return app_prefs.getString("corporatename", "0");
	}

	public void save_corporatename(String corporatename) {
		Editor edit = app_prefs.edit();
		edit.putString("corporatename", corporatename);
		edit.commit();
	}

	public String get_WifiPass() {
		return app_prefs.getString("WifiPass", "NO Wifi");
	}

	public void save_WifiPass(String WifiPass) {
		Editor edit = app_prefs.edit();
		edit.putString("WifiPass", WifiPass);
		edit.commit();
	}

	public String get_taxiMaxSize() {
		return app_prefs.getString("taxiMaxSize", null);
	}

	public void save_taxiMaxSize(String taxiMaxSize) {
		Editor edit = app_prefs.edit();
		edit.putString("taxiMaxSize", taxiMaxSize);
		edit.commit();
	}

	public void save_MyOriginLocation(LatLng latLang) {
		Editor edit = app_prefs.edit();
		// "MyLocation_LATITUDE"
		edit.putString("MyOriginLocation_LATITUDE", String.valueOf(latLang.latitude));
		edit.putString("MyOriginLocation_LONGITUDE", String.valueOf(latLang.longitude));
		edit.commit();
	}

	public LatLng get_MyOriginLocation() {
		LatLng latLng = new LatLng(0.0, 0.0);
		try {
			latLng = new LatLng(Double.parseDouble(app_prefs.getString("MyOriginLocation_LATITUDE", "0.0")),
					Double.parseDouble(app_prefs.getString("MyOriginLocation_LONGITUDE", "0.0")));

		} catch (NumberFormatException nfe) {
			latLng = new LatLng(0.0, 0.0);
		}
		return latLng;
	}

	public void save_MyDestinationLocation(LatLng latLang) {
		Editor edit = app_prefs.edit();
		// "MyLocation_LATITUDE"
		edit.putString("MyLocation_LATITUDE", String.valueOf(latLang.latitude));
		edit.putString("MyLocation_LONGITUDE", String.valueOf(latLang.longitude));
		edit.commit();
	}

	public LatLng get_MyDestinationLocation() {
		LatLng latLng = new LatLng(0.0, 0.0);
		try {
			latLng = new LatLng(Double.parseDouble(app_prefs.getString("MyLocation_LATITUDE", "0.0")),
					Double.parseDouble(app_prefs.getString("MyLocation_LONGITUDE", "0.0")));

		} catch (NumberFormatException nfe) {
			latLng = new LatLng(0.0, 0.0);
		}
		return latLng;
	}

	// /////////////////////////////////////////////////////////////////////////////////

	public void putRequestLocation(LatLng latLang) {
		Editor edit = app_prefs.edit();
		edit.putString(REQUEST_LATITUDE, String.valueOf(latLang.latitude));
		edit.putString(REQUEST_LONGITUDE, String.valueOf(latLang.longitude));
		edit.commit();
	}

	public LatLng getRequestLocation() {
		LatLng latLng = new LatLng(0.0, 0.0);
		try {
			latLng = new LatLng(Double.parseDouble(app_prefs.getString(REQUEST_LATITUDE, "0.0")),
					Double.parseDouble(app_prefs.getString(REQUEST_LONGITUDE, "0.0")));
		} catch (NumberFormatException nfe) {
			latLng = new LatLng(0.0, 0.0);
		}
		return latLng;
	}

	// ########################################################################################################
	public void putClientDestination(LatLng destination) {
		Editor edit = app_prefs.edit();
		if (destination == null) {
			edit.putString(DEST_LAT, null);
			edit.putString(DEST_LNG, null);
		} else {
			edit.putString(DEST_LAT, String.valueOf(destination.latitude));
			edit.putString(DEST_LNG, String.valueOf(destination.longitude));
		}
		edit.commit();
	}

	public void putPathSource(LatLng destination) {
		Editor edit = app_prefs.edit();
		if (destination == null) {
			edit.putString(sDEST_LAT, null);
			edit.putString(sDEST_LNG, null);
		} else {
			edit.putString(sDEST_LAT, String.valueOf(destination.latitude));
			edit.putString(sDEST_LNG, String.valueOf(destination.longitude));
		}
		edit.commit();
	}

	public LatLng getPathSource() {
		try {
			if (app_prefs.getString(DEST_LAT, null) != null) {
				return new LatLng(Double.parseDouble(app_prefs.getString(sDEST_LAT, "0.0")),
						Double.parseDouble(app_prefs.getString(sDEST_LNG, "0.0")));
			} else {
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void putPathDest(LatLng destination) {
		Editor edit = app_prefs.edit();
		if (destination == null) {
			edit.putString(dDEST_LAT, null);
			edit.putString(dDEST_LNG, null);
		} else {
			edit.putString(dDEST_LAT, String.valueOf(destination.latitude));
			edit.putString(dDEST_LNG, String.valueOf(destination.longitude));
		}
		edit.commit();
	}

	public LatLng getPathDest() {
		try {
			if (app_prefs.getString(DEST_LAT, null) != null) {
				return new LatLng(Double.parseDouble(app_prefs.getString(dDEST_LAT, "0.0")),
						Double.parseDouble(app_prefs.getString(dDEST_LNG, "0.0")));
			} else {
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// ######################################################################################################
	public LatLng getClientDestination() {
		try {
			if (app_prefs.getString(DEST_LAT, null) != null) {
				return new LatLng(Double.parseDouble(app_prefs.getString(DEST_LAT, "0.0")),
						Double.parseDouble(app_prefs.getString(DEST_LNG, "0.0")));
			} else {
				return null;
			}
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public void putReferee(int is_referee) {
		Editor edit = app_prefs.edit();
		edit.putInt(REFEREE, is_referee);
		edit.commit();
	}

	public int getReferee() {
		return app_prefs.getInt(REFEREE, 0);
	}

	public void putPromoCode(String promoCode) {
		Editor edit = app_prefs.edit();
		edit.putString(PROMO_CODE, promoCode);
		edit.commit();
	}

	public String getPromoCode() {
		return app_prefs.getString(PROMO_CODE, null);
	}

	public void clearRequestData() {
		putRequestId(Const.NO_REQUEST);
		putRequestTime(Const.NO_TIME);
		putRequestLocation(new LatLng(0.0, 0.0));
		putIsTripStarted(false);
		putClientDestination(null);
		putPromoCode(null);
		// new DBHelper(context).deleteAllLocations();
	}

	public void Logout() {
		clearRequestData();
		// new DBHelper(context).deleteUser();
		putUserId(null);
		putSessionToken(null);
		putSocialId(null);
		putClientDestination(null);
		putLoginBy(Const.MANUAL);
		app_prefs.edit().clear();
	}

	public void putTimeZone(String timeZone) {
		Editor edit = app_prefs.edit();
		edit.putString(TIME_ZONE, timeZone);
		edit.commit();
	}

	public String getTimeZone() {
		return app_prefs.getString(TIME_ZONE, "");
	}

	public void putStartTime(String startTime) {
		Editor edit = app_prefs.edit();
		edit.putString(START_TIME, startTime);
		edit.commit();
	}

	public String getStartTime() {
		return app_prefs.getString(START_TIME, "");
	}

	public void putRequestCreatedTime(String time) {
		Editor edit = app_prefs.edit();
		edit.putString(CURRENT_TRIP_TIME, time);
		edit.commit();
	}

	public String getRequestCreatedTime() {
		return app_prefs.getString(CURRENT_TRIP_TIME, "");
	}
}
