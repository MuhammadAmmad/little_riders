package com.craftsilicon.littlecabrider.fragments;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.GoogleAnalyticsApp;
import com.craftsilicon.littlecabrider.MainActivity;
import com.craftsilicon.littlecabrider.MainDrawerActivity;
import com.craftsilicon.littlecabrider.R;
import com.craftsilicon.littlecabrider.RegisterActivity;
import com.craftsilicon.littlecabrider.UpdateActivity;
import com.craftsilicon.littlecabrider.adapter.FancyCoverAdapter;
import com.craftsilicon.littlecabrider.adapter.PlacesAutoCompleteAdapter;
import com.craftsilicon.littlecabrider.adapter.PlacesAutoCompleteAdapter2;
import com.craftsilicon.littlecabrider.adapter.VehiclesAdapter;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextView;
import com.craftsilicon.littlecabrider.db.DBHelper;
import com.craftsilicon.littlecabrider.interfaces.OnProgressCancelListener;
import com.craftsilicon.littlecabrider.models.Booking;
import com.craftsilicon.littlecabrider.models.Driver;
import com.craftsilicon.littlecabrider.models.Route;
import com.craftsilicon.littlecabrider.models.Step;
import com.craftsilicon.littlecabrider.models.User;
import com.craftsilicon.littlecabrider.models.VehicalType;
import com.craftsilicon.littlecabrider.parse.AsyncTask_BulletProof;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.parse.ParseContent;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Base64Utility;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.LocationHelper;
import com.craftsilicon.littlecabrider.utils.LocationHelper.OnLocationReceived;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.devsmart.android.ui.HorizontalListView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mikhaellopez.circularimageview.CircleImageView;

//import android.animation.ValueAnimator;

/**
 * @author Elluminati elluminati.in
 */

public class MapFragment extends BaseFragment implements
		OnProgressCancelListener, OnSeekBarChangeListener, OnItemClickListener,
		OnLocationReceived, OnItemSelectedListener {

	/**
	 * FARE ESTIMATION VARIABLES
	 */
	private AutoCompleteTextView destinationTextView;
	private MyFontTextView riderSourceTextView;
	private MyFontTextView textPriceUnitTime;
	private MyFontTextView totalDistanceToDestination;
	private String textTotalCharge2;
	private MyFontTextView textTotalCharge;
	private MyFontTextView textBaseFare;
	private MyFontTextView textChargePerKm;
	private ProgressDialog progressDialog;
	private MyFontButton buttonCancel;
	private CircleImageView imageProfile;
	private LinearLayout layout_destination;
	private LinearLayout layout_source;
	private Spinner spnMusic;
	private LatLng destlanglong;
	ProgressDialog progressDialog2;
	private PlacesAutoCompleteAdapter adapter;
	public static boolean isMapTouched = false;
	private float currentZoom = -1;
	private GoogleMap map;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private LatLng curretLatLng;
	private String strAddress = null, selectedDate = "", selectedTime = "",
			startTime = "", appliedPromoCode = "";
	private boolean isContinueRequest;
	private Timer timer;
	private ImageOptions imageOptions, imageOptions2;
	private WalkerStatusReceiver walkerReceiver;
	private ImageButton btnMyLocation;
	private FrameLayout mapFrameLayout;
	// private GridView listViewType;
	private ArrayList<VehicalType> listType;
	// private VehicalTypeListAdapter typeAdapter;
	private int selectedPostion = -1, selectedHour, selectedMinute, reqId;
	private boolean isGettingVehicalType = true;
	boolean isLocationFound;
	TymeHorizontaliconsAdapter horizontalAdapter;
	// private MyFontButton btnSelectService;
	private SlidingDrawer drawer;
	private LinearLayout layoutDestination;
	// private LinearLayout llCash, llCard;
	private int paymentMode;
	// private LinearLayout llServiceText;
	private TextView tvEstimatedTime, tvTripPickupAddress, txtLoadingVehicles;
	// private TextView tvCash, tvCard;
	// private TextView tvPickupAddress;
	// private ProgressBar pBar;
	// private RelativeLayout layoutDuration;
	// private AutoCompleteTextView etDestination;
	private PlacesAutoCompleteAdapter adapterDestination;
	private PlacesAutoCompleteAdapter2 adapterDestination2;
	private Marker markerDestination, markerSource;
	private Route route;
	private ArrayList<LatLng> points;
	private PolylineOptions lineOptions;
	private boolean isSource, isAddDestination;
	private Polyline polyLine;
	private View layoutBubble;
	MarkerOptions mo2;
	public String myCountry = "";
	MyFontEdittextView edtcorporate_code;
	Boolean isCorporateSelected = false, isRidingAsCorporate = false;
	private Spinner spnCorporateName;
	// private Button btnSendRequestDesti;
	// private View layoutRgService;
	// private LinearLayout linearPointer;
	// private SeekBar sb;
	// private int value;
	int start;
	// private ValueAnimator anim;
	int pointer;
	// private LinearLayout layoutMarker;
	// private TextView tvNo;
	private PreferenceHelper preference;
	// private LinearLayout layoutDestination;
	// private LinearLayout layoutCardDetails;
	// private TextView tvDurationUnit;
	// private ImageView ivCard;
	// private BroadcastReceiver mReceiver;
	// private LinearLayout layoutFareQuote;
	// private TextView txtTotalFareQuate;
	private Dialog quoteDialog, confirmFutureRequest, dialog, referralDialog,
			cancellationDialog;
	private View view;
	private MapView mMapView;
	private Bundle mBundle;
	private MyFontTextView tvVehicleType, txtMaxPersons, txtMinFare,
			txtFareSummary;
	// private LinearLayout linearPointer2;
	private TextView tvMaxSize, tvMinFare, tvETA, tvGetFareEst, tvTotalFare,
			tvHomeAddress, tvWorkAddress, tvRateVehicleTypeName,
			tvRateBasePrice, tvRateDistanceCost, tvRateTimeCost;
	private ImageView cancelVehicleDetail, selectedCard, selectedCash;
	private String estimatedTimeTxt, estimatedTimeTxt2;
	private AutoCompleteTextView etSource, etDestination, etPopupDestination,
			etHomeAddress, etWorkAddress;
	private PlacesAutoCompleteAdapter adapterPopUpDestination,
			adapterHomeAddress, adapterWorkAddress;
	private Dialog destinationDialog, mydestinationDialog, scheduleDialog;
	private ProgressBar pbMinFare;
	// private TextView tvLblMinFare;
	private LinearLayout layoutHomeText, layoutHomeEdit, layoutWorkText,
			layoutWorkEdit, linearPickupAddress, vehicleLayout, llErrorMsg;
	ImageView imgDrawer;
	public LinearLayout sendReqLayout;
	private ListView nearByList;
	private ArrayAdapter<String> nearByAd;
	private Address address;
	private ProgressBar pbNearby, pbar;
	private ArrayList<Driver> listDriver = new ArrayList<Driver>();
	private HashMap<Integer, Marker> nearDriverMarker;
	private Timer timerProvidersLocation;
	private final int LOCATION_SCHEDULE = 7 * 1000;
	private ArrayList<Driver> listUpdatedDriver;
	// private Location loc;
	private EditText etRefCode;
	private int is_skip = 0;
	private LocationHelper locHelper;
	private Location myLocation;
	// private TextView tvRateCard;
	private FancyCoverFlow fancyCoverFlow;
	private Button btnRideNow, btnRideLater, scheduleBtn, cancel, ok;
	// private EditText editPromo;
	public Driver driverInfoTemp = new Driver();

	public Booking booking;
	private String pickupAddrs;
	private String mypickupAddrs;
	private String mydropAddrs;

	Double toserverlongitute, toserverlatitude;
	HorizontalListView listView;
	ArrayList<VehicalType> types;
	ImageView imgProfile;

	String strMaxSize = "";
	String strMinFare = "";
	Double strPricePerUnitTime = 0.0;
	Double strPricePerUnitDistance = 0.0;
	int selectedType = 0;
	ImageButton btnMyFavouriteLocations;
	String cartypename = "";

	boolean isDestinationDialogOpen;
	String myMusic = "Any Radio Station", passengerType = "Individual",
			corporatename = "", corporatecode = "";
	// txtMaxPersons.setText(types.get(position).getMaxSize()+" PERSONS");
	// txtMinFare.setText("MIN FARE KES "+types.get(position).getMinFare());
	// txtFareSummary.setText(
	// "Base Fare KES "+types.get(position).getMinFare()+
	// " +KES"+types.get(position).getPricePerUnitTime()+"/MIN"+
	// " +KES"+types.get(position).getPricePerUnitDistance()+"/KM")

	// PopupWindow window;
	MyFontTextView txtGetFareEstimate;
	ArrayList<VehiclesAdapter> arraylist;
	Switch myCorporateSwitch;
	LinearLayout llCorporates;
	String corporate_reference = "";
	boolean isPausedTwice = false;

	public static MapFragment newInstance() {
		MapFragment mapFragment = new MapFragment();
		return mapFragment;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_map_bullet, container, false);
		mydestinationDialog = new Dialog(getActivity(),
				R.style.CustomDialogTheme2);
		imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.ic_launcher;

		imageOptions2 = new ImageOptions();
		imageOptions2.fileCache = true;
		imageOptions2.memCache = true;
		imageOptions2.fallback = R.drawable.default_car;
		try {
			MapsInitializer.initialize(getActivity());
		} catch (Exception e) {
		}
		isLocationFound = false;
		types = new ArrayList<VehicalType>();

		/*
		 * String name = null; if(name.equals("")){
		 * 
		 * }
		 */

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// layoutMarker = (LinearLayout) view.findViewById(R.id.layoutMarker);
		view.findViewById(R.id.markerBubblePickMeUp).setOnClickListener(this);
		layoutBubble = view.findViewById(R.id.layoutBubble);
		// llServiceText = (LinearLayout) view.findViewById(R.id.llServiceText);
		// linearPointer = (LinearLayout) view.findViewById(R.id.linearPointer);
		// linearPointer2 = (LinearLayout)
		// view.findViewById(R.id.linearPointer2);
		// layoutRgService = view.findViewById(R.id.layoutRgService);
		myCorporateSwitch = (Switch) view.findViewById(R.id.myCorporateSwitch);
		llCorporates = (LinearLayout) view.findViewById(R.id.llCorporates);
		tvEstimatedTime = (TextView) view.findViewById(R.id.tvEstimatedTime);
		txtLoadingVehicles = (TextView) view
				.findViewById(R.id.txtLoadingVehicles);
		// tvDurationUnit = (TextView) view.findViewById(R.id.tvDurationUnit);

		// tvNo = (TextView) view.findViewById(R.id.tvNo);
		// ivCard = (ImageView) view.findViewById(R.id.ivCard);
		// layoutCardDetails = (LinearLayout)
		// view.findViewById(R.id.layoutCardDetails);
		// layoutCardDetails.setOnClickListener(this);

		// layoutFareQuote = (LinearLayout) view
		// .findViewById(R.id.layoutFareQuote);
		// view.findViewById(R.id.tvFareQuote).setOnClickListener(this);
		// view.findViewById(R.id.btnFareInfo).setOnClickListener(this);

		tvTripPickupAddress = (TextView) view
				.findViewById(R.id.tvTripPickupAddress);

		//
		tvVehicleType = (MyFontTextView) view.findViewById(R.id.tvVehicleType);
		txtMaxPersons = (MyFontTextView) view.findViewById(R.id.txtMaxPersons);
		txtFareSummary = (MyFontTextView) view
				.findViewById(R.id.txtFareSummary);
		txtMinFare = (MyFontTextView) view.findViewById(R.id.txtMinFare);
		layoutDestination = (LinearLayout) view
				.findViewById(R.id.layoutDestination);
		sendReqLayout = (LinearLayout) view.findViewById(R.id.sendReqLayout);
		vehicleLayout = (LinearLayout) view.findViewById(R.id.vehicleLayout);
		spnCorporateName = (Spinner) view.findViewById(R.id.spnCorporateName);
		setCorpSpinner(spnCorporateName);
		imgDrawer = (ImageView) view.findViewById(R.id.imgDrawer);
		txtGetFareEstimate = (MyFontTextView) view
				.findViewById(R.id.txtGetFareEstimate);
		linearPickupAddress = (LinearLayout) view
				.findViewById(R.id.linearLayoutPickup);
		// tvCash = (TextView) view.findViewById(R.id.tvCash);
		// tvCard = (TextView) view.findViewById(R.id.tvCardNo);
		// tvCash.setOnClickListener(this);
		// tvCard.setOnClickListener(this);
		txtGetFareEstimate.setOnClickListener(this);
		selectedCard = (ImageView) view.findViewById(R.id.imgSelectedCard);
		selectedCash = (ImageView) view.findViewById(R.id.imgSelectedCash);
		// editPromo = (EditText) view.findViewById(R.id.ibApplyPromo);
		// editPromo.setOnClickListener(this);
		btnRideNow = (Button) view.findViewById(R.id.btnRideNow);
		btnRideLater = (Button) view.findViewById(R.id.btnRideLater);
		btnRideNow.setOnClickListener(this);
		btnRideLater.setOnClickListener(this);
		imgDrawer.setOnClickListener(this);

		/*
		 * String name = null;
		 * 
		 * 
		 * if(name.equals("")){
		 * 
		 * }
		 */
		if (activity.pHelper.getReferee() == 0) {
			showReferralDialog();
		}

		// sb = (SeekBar) view.findViewById(R.id.seekBar);
		// sb.setOnSeekBarChangeListener(this);
		// pBar = (ProgressBar) view.findViewById(R.id.pBar);
		// layoutDuration = (RelativeLayout) view
		// .findViewById(R.id.layoutDuration);

		etSource = (AutoCompleteTextView) view.findViewById(R.id.etEnterSouce);
		imgProfile = (ImageView) view.findViewById(R.id.imgProfile);
		imgProfile.setOnClickListener(this);
		// MapsActionBarBaseActivity map = new
		// MapsActionBarBaseActivity(getActivity());
		// etSource = MapsActionBarBaseActivity.getAT();
		AndyUtils.log("todecrypt-encrpyt", "-2");
		etDestination = (AutoCompleteTextView) view
				.findViewById(R.id.etEnterDestination);
		// layoutDestination = (LinearLayout) view
		// .findViewById(R.id.layoutDestination);
		// btnSendRequestDesti = (Button) view
		// .findViewById(R.id.btnSendRequestDesti);
		// btnSendRequestDesti.setOnClickListener(this);
		// view.findViewById(R.id.imgClearSource).setOnClickListener(this);
		// view.findViewById(R.id.imgClearDst).setOnClickListener(this);

		selectedPostion = 0;
		// listViewType = (GridView) view.findViewById(R.id.gvTypes);

		mapFrameLayout = (FrameLayout) view.findViewById(R.id.mapFrameLayout);

		mapFrameLayout.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// AndyUtils.log("motioneventmap", "Touch sdf"+event);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN | MotionEvent.ACTION_MOVE:
					MapFragment.isMapTouched = true;
					// layoutMarker.setVisibility(View.GONE);
					/*
					 * LatLng latLang = new
					 * LatLng(myLocation.getLatitude(),myLocation
					 * .getLongitude()); animateCameraToMarker(latLang, true);
					 * //
					 * AndyUtils.log("newlatlong","lat:"+myLocation.getLatitude
					 * ()+":long:"+myLocation.getLongitude());
					 * getAddressFromLocation(latLang, etSource);
					 */

					break;

				case MotionEvent.ACTION_UP:
					MapFragment.isMapTouched = false;
					Log.e("Map", "NoTouch sdf -------------------------");
					break;
				}
				return true;

			}
		});

		btnMyLocation = (ImageButton) view.findViewById(R.id.btnMyLocation);
		btnMyFavouriteLocations = (ImageButton) view
				.findViewById(R.id.btnMyFavouriteLocations);
		// btnSelectService = (MyFontButton) view
		// .findViewById(R.id.btnSelectService);
		// btnSelectService.setOnClickListener(this);
		//
		drawer = (SlidingDrawer) view.findViewById(R.id.drawer_layout);

		mMapView = (MapView) view.findViewById(R.id.map);
		mMapView.onCreate(mBundle);
		setUpMapIfNeeded();
		preference = new PreferenceHelper(activity);

		tvVehicleType = (MyFontTextView) view.findViewById(R.id.tvVehicleType);
		datePicker = new DatePicker(activity);
		timePicker = new TimePicker(activity);
		//
		fancyCoverFlow = (FancyCoverFlow) view
				.findViewById(R.id.fancyCoverFlow);
		// fancyCoverFlow.setUnselectedAlpha(1);
		fancyCoverFlow.setUnselectedSaturation(0f);
		fancyCoverFlow.setUnselectedScale(1f);
		fancyCoverFlow.setUnselectedAlpha(1f);
		fancyCoverFlow.setOnItemSelectedListener(this);

		listView = (HorizontalListView) view.findViewById(R.id.lst_cartypes);

		// testConfirmPayment();
		// fancyCoverFlow.setSpacing(20);
		// sendReqLayout.setVisibility(View.GONE);
		/*
		 * editPromo.setOnKeyListener(new OnKeyListener() {
		 * 
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
		 * if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode ==
		 * KeyEvent.KEYCODE_ENTER)) { if
		 * ((!(editPromo.getText().toString().equals(getActivity()
		 * .getResources().getString( R.string.text_enter_promo_code)))) &&
		 * (!(editPromo.getText().toString().equals("")))) { appliedPromoCode =
		 * editPromo.getText().toString(); new PreferenceHelper(activity)
		 * .putPromoCode(appliedPromoCode); } } // Returning false allows other
		 * listeners to react to the press.
		 * 
		 * if (event.getAction() == KeyEvent.ACTION_UP && keyCode ==
		 * KeyEvent.KEYCODE_BACK) { if (sendReqLayout.getVisibility() ==
		 * View.VISIBLE) { cancelConfirmation(); } else { return false; } return
		 * true; } return false; } });
		 */
		// fancyCoverFlow.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// if (position != selectedPostion) {
		// return;
		// }
		// if (TextUtils.isEmpty(tvPickupAddress.getText().toString())
		// || tvPickupAddress
		// .getText()
		// .toString()
		// .equalsIgnoreCase(
		// getResources()
		// .getString(
		// R.string.text_waiting_for_address))) {
		// AndyUtils.showToast(
		// getActivity().getString(
		// R.string.text_waiting_for_address),
		// getActivity());
		// } else if (TextUtils.isEmpty(estimatedTimeTxt)) {
		// AndyUtils.showToast(
		// getActivity().getString(
		// R.string.text_waiting_for_eta),
		// getActivity());
		// } else {
		// showDestinationPopup();
		// }
		// }
		// });
		checkCorporateStatus();

		// checkIsCorporate();
		return view;
	}

	private void checkIsCorporate() {
		if (preference.getIsCorporate()) {
			myCorporateSwitch.setVisibility(View.VISIBLE);
			llCorporates.setVisibility(View.VISIBLE);
			myCorporateSwitch
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (isChecked) {
								// edtcorporate_code.setText(preference.get_corporatecode());
								// edtcorporate_code.setVisibility(View.VISIBLE);
								isCorporateSelected = true;
								isRidingAsCorporate = true;
								passengerType = "Corporate";
								AndyUtils.log("mapfragmentcorporate:isChecked"
										+ isCorporateSelected, "code:"
										+ corporatecode + ":name:"
										+ corporatename);

							} else {
								// /edtcorporate_code.setText("0");
								// edtcorporate_code.setVisibility(View.GONE);
								passengerType = "Individual";
								isCorporateSelected = false;
								isRidingAsCorporate = false;
								AndyUtils.log("mapfragmentcorporate:isChecked"
										+ isCorporateSelected, "code:"
										+ corporatecode + ":name:"
										+ corporatename);

							}
						}

					});
		} else {
			myCorporateSwitch.setVisibility(View.GONE);
			llCorporates.setVisibility(View.GONE);
			isCorporateSelected = false;
			isRidingAsCorporate = false;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (activity != null && activity.actionBar != null) {
			activity.actionBar.hide();
		}

		mBundle = savedInstanceState;
		IntentFilter filter = new IntentFilter(Const.INTENT_WALKER_STATUS);
		walkerReceiver = new WalkerStatusReceiver();
		LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
				walkerReceiver, filter);

		paymentMode = Const.CASH;
		new PreferenceHelper(getActivity()).putPaymentMode(paymentMode);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// activity.layoutDestination.setVisibility(View.VISIBLE);
		// activity.tvTitle.setVisibility(View.GONE);

		etSource = (AutoCompleteTextView) view.findViewById(R.id.etEnterSouce);

		activity.btnNotification.setVisibility(View.VISIBLE);
		activity.tvTitle.setVisibility(View.VISIBLE);
		activity.setIcon(R.drawable.fare_info);
		activity.setTitle(getActivity().getResources().getString(
				R.string.text_make_request));
		activity.btnNotification.setOnClickListener(this);
		// etSource = activity.etSource;
		// activity.imgClearDst.setOnClickListener(this);
		adapter = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);

		adapterDestination = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);

		adapterDestination2 = new PlacesAutoCompleteAdapter2(activity,
				R.layout.autocomplete_list_text);

		etSource.setAdapter(adapter);
		locHelper = new LocationHelper(activity);
		locHelper.setLocationReceivedLister(this);

		etDestination.setAdapter(adapterDestination);
		etSource.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				etSource.setText("");
			}
		});
		etSource.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedMyNewSelectedPlace = adapter.getItem(arg2);
				AndyUtils.log("todecrypt-encrpyt", "-1");
				new GetSourceLocationFromAddress()
						.execute(selectedMyNewSelectedPlace);
				/*
				 * final String selectedDestPlace = adapter.getItem(arg2);
				 * 
				 * new Thread(new Runnable() {
				 * 
				 * @Override public void run() { final LatLng latlng =
				 * getLocationFromAddress(selectedDestPlace);
				 * getActivity().runOnUiThread(new Runnable() {
				 * 
				 * @Override public void run() { isMapTouched = true;
				 * curretLatLng = latlng; isSource = true;
				 * setMarker(curretLatLng, isSource);
				 * setMarkerOnRoad(curretLatLng, curretLatLng);
				 * animateCameraToMarker(curretLatLng, true);
				 * stopUpdateProvidersLoaction(); getAllProviders(curretLatLng);
				 * } }); } }).start();
				 */
			}
		});

		// locHelper.setLocationReceivedLister(new OnLocationReceived() {
		// @Override
		// public void onLocationReceived(LatLng latlong) {
		//
		//
		// }
		// });

		locHelper.onStart();

		etDestination.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final String selectedDestPlace = adapterDestination
						.getItem(arg2);
				etDestination.setText(selectedDestPlace);
				etDestination.clearFocus();
				new Thread(new Runnable() {
					@Override
					public void run() {
						final LatLng latlng = getLocationFromAddress(selectedDestPlace);
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								isSource = false;
								setMarker(latlng, isSource);
								setMarkerOnRoad(latlng, latlng);
							}
						});
					}
				}).start();
			}
		});
		listType = new ArrayList<VehicalType>();
		getVehicalTypes();
		setData(imgProfile);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.map_fragment_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume() {
		super.onResume();
		geoFence(true);
		activity.tvTitle.setText(getActivity().getResources().getString(
				R.string.text_make_request));
		// activity.btnNotification.setVisibility(View.INVISIBLE);
		// etSource.setVisibility(View.VISIBLE);
		if (sendReqLayout.isShown()) {
			sendReqLayout.setVisibility(View.GONE);
		}
		mMapView.onResume();
		setData(imgProfile);
		startCheckingStatusUpdate();
		startUpdateProvidersLocation();
		// registerUsePromoReceiver();
		getView().setFocusableInTouchMode(true);
		getView().requestFocus();
		getView().setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				/*
				 * if (event.getAction() == KeyEvent.ACTION_UP && keyCode ==
				 * KeyEvent.KEYCODE_BACK) { if (sendReqLayout.getVisibility() ==
				 * View.VISIBLE) { cancelConfirmation(); } else { return false;
				 * } return true; }
				 */
				if (keyCode == KeyEvent.KEYCODE_BACK && sendReqLayout.isShown()) {
					sendReqLayout.setVisibility(View.GONE);
					return true;
				}
				return false;
			}
		});
	}

	private void cancelConfirmation() {
		if (markerSource != null) {
			markerSource.remove();
			markerSource = null;
		}
		if (markerDestination != null) {
			markerDestination.remove();
			markerDestination = null;
		}
		if (polyLine != null)
			polyLine.remove();

		appliedPromoCode = "";
		new PreferenceHelper(activity).putPromoCode(appliedPromoCode);
		isAddDestination = false;
		isSource = true;
		etDestination.setText("");
		// vehicleLayout.setVisibility(View.GONE);
		// tvVehicleType.setVisibility(View.GONE);
		layoutDestination.setVisibility(View.GONE);
		// sendReqLayout.setVisibility(View.GONE);
		// confirmLayout.setVisibility(View.GONE);
		layoutBubble.setVisibility(View.VISIBLE);
		// vehicleLayout.setVisibility(View.GONE);
		linearPickupAddress.setVisibility(View.VISIBLE);
		btnMyLocation.setVisibility(View.VISIBLE);
	}

	private void showReferralDialog() {
		referralDialog = new Dialog(getActivity(), R.style.MyDialog);
		referralDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		referralDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		referralDialog.setContentView(R.layout.dialog_referral);
		referralDialog.setCancelable(false);
		etRefCode = (EditText) referralDialog.findViewById(R.id.etRefCode);
		llErrorMsg = (LinearLayout) referralDialog
				.findViewById(R.id.llErrorMsg);
		referralDialog.findViewById(R.id.btnRefSubmit).setOnClickListener(this);
		referralDialog.findViewById(R.id.btnSkip).setOnClickListener(this);
		referralDialog.show();

	}

	private void checkCorporateStatus() {
		AndyUtils.showCustomProgressDialog(activity, "Checking location",
				false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.ISCORPORATE);
		map.put(Const.Params.ID, activity.pHelper.getUserId());
		map.put(Const.Params.TOKEN, activity.pHelper.getSessionToken());
		new HttpRequester(activity, map, Const.ServiceCode.ISCORPORATE, this);
	}

	private void applyReffralCode(boolean isShowLoader) {
		if (isShowLoader)
			AndyUtils.showCustomProgressDialog(activity, getActivity()
					.getResources().getString(R.string.progress_loading),
					false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.APPLY_REFFRAL_CODE);
		map.put(Const.Params.REFERRAL_CODE, etRefCode.getText().toString());
		map.put(Const.Params.ID, activity.pHelper.getUserId());
		map.put(Const.Params.TOKEN, activity.pHelper.getSessionToken());
		map.put(Const.Params.IS_SKIP, String.valueOf(is_skip));
		new HttpRequester(activity, map, Const.ServiceCode.APPLY_REFFRAL_CODE,
				this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.APPLY_REFFRAL_CODE, this, this));

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (map == null) {
			// map = ((SupportMapFragment) activity.getSupportFragmentManager()
			// .findFragmentById(R.id.map)).getMap();
			map = ((MapView) view.findViewById(R.id.map)).getMap();
			map.setMyLocationEnabled(false);
			map.getUiSettings().setMyLocationButtonEnabled(false);
			map.getUiSettings().setZoomControlsEnabled(false);
			map.getUiSettings().setRotateGesturesEnabled(false);
			map.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location loc) {

				}
			});

			btnMyLocation.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// Location loc = map.getMyLocation();
					if (myLocation != null) {
						LatLng latLang = new LatLng(myLocation.getLatitude(),
								myLocation.getLongitude());
						animateCameraToMarker(latLang, true);
						getAddressFromLocation(latLang, etSource);
					}
				}
			});
			btnMyFavouriteLocations
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO my favourites
							enterFavouritesDialog();
						}
					});

			map.setOnCameraChangeListener(new OnCameraChangeListener() {

				public void onCameraChange(CameraPosition camPos) {
					if (currentZoom == -1) {
						currentZoom = 16;
					} else if (camPos.zoom != currentZoom) {
						currentZoom = 16;
						return;
					}

					if (!isMapTouched) {
						curretLatLng = camPos.target;
						if (!isAddDestination) {
							// layoutMarker.setVisibility(LinearLayout.VISIBLE);
							if (listType.size() > 0) {
								stopUpdateProvidersLoaction();
								getAllProviders(curretLatLng);
							}
							getAddressFromLocation(camPos.target, etSource);
						}
					}
					isMapTouched = false;
					// setMarker(camPos.target);
				}

			});
			if (map != null) {
				// Log.i("Map", "Map Fragment");
			}
		}

	}

	@Override
	public void onPause() {
		stopCheckingStatusUpdate();
		stopUpdateProvidersLoaction();
		geoFence(false);
		// AndyUtils.log("mapfragmentonpause", "onPause");
		super.onPause();
		if (isPausedTwice) {
			// getActivity().finish();
		}
		isPausedTwice = true;
		mMapView.onPause();

	}

	@Override
	public void onDestroyView() {
		// AndyUtils.log("mapfragmentonpause", "onDestroyView");
		SupportMapFragment f = (SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map);
		if (f != null) {
			try {
				getFragmentManager().beginTransaction().remove(f).commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		map = null;
		// getActivity().finish();
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// AndyUtils.log("mapfragmentonpause", "onDestroy");
		mMapView.onDestroy();
		super.onDestroy();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				walkerReceiver);
		// activity.tvTitle.setVisibility(View.VISIBLE);
		// etSource.setVisibility(View.GONE);
		etSource.setText("");
		// getActivity().finish();
		// dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.etEnterSouce:
			etSource.setText(" ");
			break;

		case R.id.markerBubblePickMeUp:
			/*
			 * if (isValidate()) { // showPaymentOptionDialog();
			 * requestConfirmation(null); }
			 */
			break;

		case R.id.txtGetFareEstimate:
			String mySource2 = etSource.getText().toString();
			if (!(mySource2.contains("Waiting") || mySource2.length() < 1)) {
				if (!(mydestinationDialog.isShowing())) {
					isDestinationDialogOpen = true;
					enterDestinationDialog(Const.NEW_DESTINATION_ACTION_TYPE);
					// AndyUtils.showToast("mydestination:"+mySource,activity);
				} else {
					AndyUtils.showToast("opening..", activity);
				}

			} else {
				AndyUtils.showToast("retrieving an accurate location..",
						activity);
				LatLng latLang = new LatLng(myLocation.getLatitude(),
						myLocation.getLongitude());
				animateCameraToMarker(latLang, true);
			}

			break;

		case R.id.btnRideNow:
			String mySource = etSource.getText().toString();
			if (!(mySource.contains("Waiting") || mySource.length() < 1)) {
				if (!(mydestinationDialog.isShowing())) {
					enterDestinationDialog(Const.CONFIRM_RIDE_ACTION_TYPE);
					// AndyUtils.showToast("mydestination:"+mySource,activity);
				} else {
					AndyUtils.showToast("opening..", activity);
				}
			} else {
				AndyUtils.showToast("retrieving an accurate location..",
						activity);
				LatLng latLang = new LatLng(myLocation.getLatitude(),
						myLocation.getLongitude());
				animateCameraToMarker(latLang, true);
			}

			break;
		case R.id.btnRideLater:
			showDateTimePicker();
			break;

		case R.id.btnActionNotification:
			showVehicleDetails();
			break;

		case R.id.imgProfile:
			MainDrawerActivity.drawerLayout
					.openDrawer(MainDrawerActivity.listDrawer);
			break;

		case R.id.imgDrawer:
			MainDrawerActivity.drawerLayout
					.openDrawer(MainDrawerActivity.listDrawer);
			break;

		// case R.id.btnAddDestination:
		// if (!isAddDestination) {
		// isAddDestination = true;
		// isSource = true;
		// setMarkerOnRoad(curretLatLng, curretLatLng);
		// layoutDestination.setVisibility(View.VISIBLE);
		// btnSendRequestDesti.setVisibility(View.VISIBLE);
		// layoutFareQuote.setVisibility(View.VISIBLE);
		// layoutBubble.setVisibility(View.GONE);
		// llServiceText.setVisibility(View.GONE);
		// layoutRgService.setVisibility(View.GONE);
		// } else {
		// if (markerSource != null) {
		// markerSource.remove();
		// markerSource = null;
		// }
		// if (markerDestination != null) {
		// markerDestination.remove();
		// markerDestination = null;
		// }
		// if (polyLine != null)
		// polyLine.remove();
		//
		// isAddDestination = false;
		// isSource = true;
		// layoutDestination.setVisibility(View.GONE);
		// layoutBubble.setVisibility(View.VISIBLE);
		// btnSendRequestDesti.setVisibility(View.GONE);
		// layoutFareQuote.setVisibility(View.GONE);
		// llServiceText.setVisibility(View.VISIBLE);
		// layoutRgService.setVisibility(View.VISIBLE);
		// }
		// break;
		// case R.id.btnSendRequestDesti:
		// showPaymentOptionDialog();
		// break;
		// case R.id.btnSelectService:
		// if (drawer.isOpened()) {
		// drawer.animateClose();
		// drawer.unlock();
		// } else {
		// drawer.animateOpen();
		// drawer.lock();
		// }
		// break;
		// case R.id.layoutCardDetails:
		// startActivity(new Intent(getActivity(),
		// UberViewPaymentActivity.class));
		// break;
		// case R.id.imgClearSource:
		// etSource.setText("");
		// break;
		// case R.id.imgClearDst:
		// etSource.setText("");
		// break;
		// case R.id.tvFareQuote:
		// showFareQuote();
		// break;
		// case R.id.btnFareInfo:
		// showVehicleDetails();
		// break;

		case R.id.tvCardNo:
			if (preference.getDefaultCard() == 0) {
				AndyUtils.showToast(
						activity.getResources().getString(R.string.no_card),
						activity);
				return;
			}
			if (selectedCash.getVisibility() == View.VISIBLE)
				selectedCash.setVisibility(View.GONE);

			selectedCard.setVisibility(View.VISIBLE);
			// tvCard.setSelected(true);
			// tvCash.setSelected(false);
			paymentMode = Const.CREDIT;
			new PreferenceHelper(getActivity()).putPaymentMode(paymentMode);
			break;

		case R.id.tvCash:
			if (selectedCard.getVisibility() == View.VISIBLE) {
				selectedCard.setVisibility(View.GONE);
			}
			selectedCash.setVisibility(View.VISIBLE);
			// tvCard.setSelected(false);
			// tvCash.setSelected(true);
			paymentMode = Const.CASH;
			new PreferenceHelper(getActivity()).putPaymentMode(paymentMode);
			break;

		// case R.id.ibApplyPromo:
		// editPromo.setRawInputType(InputType.TYPE_CLASS_TEXT);
		// editPromo.setTextIsSelectable(true);
		// if((!(editPromo.getText().toString().equals(getResources().getString(R.string.text_enter_promo_code))))
		// &&
		// (!(editPromo.getText().toString().equals("")))){
		// appliedPromoCode = editPromo.getText().toString();
		// new PreferenceHelper(activity).putPromoCode(appliedPromoCode);
		// }
		// break;

		case R.id.btnOKFareQuote:
			quoteDialog.dismiss();
			break;
		case R.id.tvGetFareEst:
			if (address != null) {
				showDestinationPopup();
			} else {
				AndyUtils.showToast(
						activity.getResources().getString(
								R.string.text_enter_pickup_location), activity);
			}
			break;
		case R.id.btnEditHome:
			layoutHomeEdit.setVisibility(View.VISIBLE);
			layoutHomeText.setVisibility(View.GONE);
			break;
		case R.id.btnEditWork:
			layoutWorkEdit.setVisibility(View.VISIBLE);
			layoutWorkText.setVisibility(View.GONE);
			break;
		case R.id.layoutHomeText:
			if (preference.getWorkAddress() != null)
				sendQuoteRequest(preference.getHomeAddress());
			break;
		case R.id.layoutWorkText:
			if (preference.getWorkAddress() != null)
				sendQuoteRequest(preference.getWorkAddress());
			break;
		case R.id.imgClearDest:
			destinationDialog.dismiss();
			// etPopupDestination.setText("");
			break;
		case R.id.imgClearHome:
			etHomeAddress.setText("");
			break;
		case R.id.imgClearWork:
			etWorkAddress.setText("");
			break;
		case R.id.btnRefSubmit:
			if (etRefCode.getText().length() == 0) {
				AndyUtils.showToast(
						activity.getResources().getString(
								R.string.text_blank_ref_code), activity);
				return;
			} else {
				if (!AndyUtils.isNetworkAvailable(activity)) {
					AndyUtils
							.showToast(
									activity.getResources().getString(
											R.string.dialog_no_inter_message),
									activity);
					return;
				}
				is_skip = 0;
				applyReffralCode(true);
			}
			break;
		case R.id.btnSkip:
			is_skip = 1;
			applyReffralCode(false);
			activity.onBackPressed();
			break;
		// case R.id.tvRateCard:
		// AppLog.Log("", "clicked");
		// showRateCardDialog();
		// break;
		case R.id.cancelVehicleDetail:
			dialog.dismiss();
			break;

		case R.id.btn_cancel_request:
			showCancellationtDialog();
			break;

		case R.id.btnOk:
			confirmFutureRequest.dismiss();
			AndyUtils.showToast("Request submitted successfully.", activity);
			break;

		default:
			break;
		}
	}

	private void showCancellationtDialog() {
		cancellationDialog = new Dialog(activity);
		cancellationDialog.setTitle("Tell us why");
		cancellationDialog.setContentView(R.layout.dilog_cancel_future_request);
		ok = (Button) cancellationDialog.findViewById(R.id.btnOk);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				cancellationDialog.dismiss();
				AndyUtils.showCustomProgressDialog(
						activity,
						activity.getResources().getString(
								R.string.progress_canceling_trip), false, null);

				HashMap<String, String> hmap = new HashMap<String, String>();
				hmap.put(Const.URL, Const.ServiceType.DELETE_FUTURE_REQUEST);
				hmap.put(Const.Params.TOKEN,
						new PreferenceHelper(activity).getSessionToken());
				hmap.put(Const.Params.ID,
						new PreferenceHelper(activity).getUserId());
				hmap.put(Const.Params.REQUEST_ID, String.valueOf(reqId));

				new HttpRequester(activity, hmap,
						Const.ServiceCode.DELETE_FUTURE_REQUEST,
						MapFragment.this);
				// requestQueue.add(new VolleyHttpRequest(Method.POST, hmap,
				// Const.ServiceCode.DELETE_FUTURE_REQUEST,
				// MapFragment.this, MapFragment.this));
			}
		});
		cancellationDialog.show();
	}

	@SuppressWarnings("unused")
	private void requestConfirmation(String destAddr) {
		isAddDestination = true;
		isSource = true;
		tvTripPickupAddress.setText(etSource.getText().toString());
		setMarker(curretLatLng, isSource);
		// tvVehicleType.setVisibility(View.GONE); // Remove this
		// layoutDestination.setVisibility(View.VISIBLE);
		sendReqLayout.setVisibility(View.VISIBLE); // Layout for sending
													// requests appears
		// confirmLay out.setVisibility(View.VISIBLE);
		// layoutBubble.setVisibility(View.GONE); // Remove this
		// vehicleLayout.setVisibility(View.GONE); // Remove this
		linearPickupAddress.setVisibility(View.GONE);
		btnMyLocation.setVisibility(View.GONE);
		if (preference.getPaymentMode() == Const.CREDIT)
			selectedCard.setVisibility(View.VISIBLE);
		else
			selectedCash.setVisibility(View.VISIBLE);
		if (destAddr != null) {
			LatLng destLatLng = getLocationFromAddress(destAddr);
			addDestination(destAddr, destLatLng);
		}
	}

	/*
	 * @Override public void onDetach() { //super.onDetach();
	 * //AndyUtils.log("fragcheck", "onDetach"); }
	 */
	private void addDestination(String destAddr, LatLng destLatLng) {
		isSource = false;
		setMarker(destLatLng, isSource);
		setMarkerOnRoad(destLatLng, destLatLng);
		etDestination.setText(destAddr);
		etDestination.dismissDropDown();
	}

	private void showDateTimePicker() {
		final Calendar c = Calendar.getInstance();
		final int year = c.get(Calendar.YEAR);
		final int month = c.get(Calendar.MONTH);
		final int date = c.get(Calendar.DAY_OF_MONTH);
		final int hour = c.get(Calendar.HOUR_OF_DAY);
		final int minute = c.get(Calendar.MINUTE);

		scheduleDialog = new Dialog(activity);
		scheduleDialog.setContentView(R.layout.picker_dialog);
		scheduleDialog.setTitle(getActivity().getResources().getString(
				R.string.text_schedule_trip));
		datePicker = (DatePicker) scheduleDialog.findViewById(R.id.date_picker);
		timePicker = (TimePicker) scheduleDialog.findViewById(R.id.time_picker);
		scheduleBtn = (Button) scheduleDialog
				.findViewById(R.id.confirm_schedule);
		datePicker.setMinDate(System.currentTimeMillis() - 1000);
		datePicker.setMaxDate((System.currentTimeMillis())
				+ (2 * 24 * 60 * 60 * 1000));

		selectedDate = String.valueOf(year) + "-" + String.valueOf(month + 1)
				+ "-" + String.valueOf(date);

		datePicker.init(year, month, date, new OnDateChangedListener() {
			@Override
			public void onDateChanged(DatePicker v, int selectedYear,
					int selectedMonth, int selectedDay) {

				selectedDate = String.valueOf(datePicker.getYear()) + "-"
						+ String.valueOf((datePicker.getMonth()) + 1) + "-"
						+ String.valueOf(datePicker.getDayOfMonth());

			}

		});
		timePicker.setIs24HourView(true);
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		selectedTime = String.valueOf(hour) + ":" + String.valueOf(minute);
		selectedHour = hour;
		selectedMinute = minute;

		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int min) {
				selectedHour = hourOfDay;
				selectedMinute = min;
				selectedTime = String.valueOf(hourOfDay) + ":"
						+ String.valueOf(min);
			}
		});

		scheduleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if ((datePicker.getDayOfMonth() == c.get(Calendar.DAY_OF_MONTH))
						&& (selectedHour == c.get(Calendar.HOUR_OF_DAY))
						&& (((selectedMinute - c.get(Calendar.MINUTE)) >= 0) && (((selectedMinute - c
								.get(Calendar.MINUTE)) <= 30)))) {

					AndyUtils.showCustomProgressDialog(
							activity,
							activity.getResources().getString(
									R.string.text_contacting), false, null);
					HashMap<String, String> map = new HashMap<String, String>();

					map.put(Const.URL, Const.ServiceType.CREATE_REQUEST);
					map.put(Const.Params.TOKEN,
							new PreferenceHelper(activity).getSessionToken());
					map.put(Const.Params.ID,
							new PreferenceHelper(activity).getUserId());
					map.put(Const.Params.LATITUDE,
							String.valueOf(curretLatLng.latitude));
					map.put(Const.Params.LONGITUDE,
							String.valueOf(curretLatLng.longitude));
					map.put(Const.Params.PAYMENT_OPT, String
							.valueOf(new PreferenceHelper(activity)
									.getPaymentMode()));
					map.put(Const.Params.TYPE, String.valueOf(listType.get(
							selectedPostion).getId()));
					map.put(Const.Params.CARD_ID, String
							.valueOf(new PreferenceHelper(activity)
									.getDefaultCard()));
					map.put(Const.Params.DISTANCE, "1");

					map.put(Const.Params.PROMO_CODE, appliedPromoCode);
					map.put(Const.Params.SOURCE_ADDRESS, tvTripPickupAddress
							.getText().toString());
					if (markerDestination != null) {
						final LatLng dest = markerDestination.getPosition();
						map.put(Const.Params.DESTI_LATITUDE,
								String.valueOf(dest.latitude));
						map.put(Const.Params.DESTI_LONGITUDE,
								String.valueOf(dest.longitude));
					}

					new HttpRequester(activity, map,
							Const.ServiceCode.CREATE_REQUEST, MapFragment.this);
					// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
					// Const.ServiceCode.CREATE_REQUEST, MapFragment.this,
					// MapFragment.this));
				}
				//

				else if ((datePicker.getDayOfMonth() == c
						.get(Calendar.DAY_OF_MONTH))
						&& (selectedHour == c.get(Calendar.HOUR_OF_DAY))
						&& ((selectedMinute - c.get(Calendar.MINUTE)) < 0)) {
					AndyUtils.showToast(
							"You can create trip for onward time only.",
							activity);
				}

				else if ((datePicker.getDayOfMonth() == c
						.get(Calendar.DAY_OF_MONTH))
						&& ((selectedHour - c.get(Calendar.HOUR_OF_DAY)) < 0)) {
					AndyUtils.showToast(
							"You can create trip for onward time only.",
							activity);
				}

				else if ((datePicker.getDayOfMonth() == c
						.get(Calendar.DAY_OF_MONTH))
						&& ((selectedHour - c.get(Calendar.HOUR_OF_DAY)) < 0)) {
					AndyUtils.showToast(
							"You can create trip for onward time only.",
							activity);
				}

				else if ((datePicker.getDayOfMonth() == ((c
						.get(Calendar.DAY_OF_MONTH)) + 2))
						&& ((selectedHour > c.get(Calendar.HOUR_OF_DAY)))) {
					AndyUtils.showToast(
							"You can create request upto 48 hours only.",
							activity);
				} else if ((datePicker.getDayOfMonth() == ((c
						.get(Calendar.DAY_OF_MONTH)) + 2))
						&& ((selectedHour == c.get(Calendar.HOUR_OF_DAY)))
						&& ((selectedMinute - c.get(Calendar.MINUTE)) > 0)) {

					// if((selectedMinute - c.get(Calendar.MINUTE)) > 0)
					AndyUtils.showToast(
							"You can create request upto 48 hours only.",
							activity);
				}

				else if ((datePicker.getMonth() == ((c.get(Calendar.MONTH)) + 1))
						&& (datePicker.getDayOfMonth() == 2)
						&& (selectedHour > c.get(Calendar.HOUR_OF_DAY))) {

					AndyUtils.showToast(
							"You can create request upto 48 hours only.",
							activity);
				} else if ((datePicker.getMonth() == ((c.get(Calendar.MONTH)) + 1))
						&& (datePicker.getDayOfMonth() == 2)
						&& (selectedHour == c.get(Calendar.HOUR_OF_DAY))
						&& ((selectedMinute - c.get(Calendar.MINUTE)) > 0)) {

					AndyUtils.showToast(
							"You can create request upto 48 hours only.",
							activity);
				}

				else {
					startTime = selectedDate + " " + selectedTime;
					activity.pHelper.putStartTime(startTime);
					Calendar cal = Calendar.getInstance();
					TimeZone timeZone = cal.getTimeZone();
					activity.pHelper.putTimeZone(timeZone.getID());
					scheduleTrip();
				}
			}
		});
		scheduleDialog.show();
	}

	private void scheduleTrip() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(
					getResources().getString(R.string.dialog_no_inter_message),
					activity);
			return;
		}
		AndyUtils.showCustomProgressRequestDialog(activity,
				getString(R.string.text_creating_request), true, null);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put(Const.URL, Const.ServiceType.CREATE_FUTURE_REQUEST);
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(activity).getSessionToken());
		map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
		map.put(Const.Params.LATITUDE, String.valueOf(curretLatLng.latitude));
		map.put(Const.Params.LONGITUDE, String.valueOf(curretLatLng.longitude));
		map.put(Const.Params.PAYMENT_MODE,
				String.valueOf(new PreferenceHelper(activity).getPaymentMode()));
		map.put(Const.Params.TYPE,
				String.valueOf(listType.get(selectedPostion).getId()));
		map.put(Const.Params.PROMO_CODE, "");
		map.put(Const.Params.SRC_ADDRESS, mypickupAddrs);

		map.put(Const.Params.TIME_ZONE,
				new PreferenceHelper(activity).getTimeZone());
		map.put(Const.Params.START_TIME,
				new PreferenceHelper(activity).getStartTime());
		if (markerDestination != null) {
			final LatLng dest = markerDestination.getPosition();
			map.put(Const.Params.DEST_ADDRESS, mydropAddrs);
			map.put(Const.Params.D_LATITUDE, String.valueOf(dest.latitude));
			map.put(Const.Params.D_LONGITUDE, String.valueOf(dest.longitude));
		}
		new HttpRequester(activity, map,
				Const.ServiceCode.CREATE_FUTURE_REQUEST, MapFragment.this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.CREATE_FUTURE_REQUEST, this, this));
	}

	private class GetAddressFromLocation2 extends
			AsyncTask<LatLng, Void, String> {
		@Override
		protected void onPreExecute() {
			/*
			 * progressDialog2 = new ProgressDialog(getActivity());
			 * progressDialog2.setMessage("Retrieving accurate location..");
			 * if(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 * 
			 * 
			 * if(!progressDialog2.isShowing()){ if (!(AndyUtils.mDialog != null
			 * && AndyUtils.mDialog.isShowing())){ progressDialog2.show(); } }
			 */

		}

		@Override
		protected String doInBackground(LatLng... params) {
			StringBuilder stringBuilder = new StringBuilder();
			try {
				String theurl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
						+ params[0].latitude + "," + params[0].longitude;
				HttpPost httpGet = new HttpPost(theurl);
				AndyUtils.log("googlemapsurl", "" + theurl);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}
			;
			//

			// JSONObject long_name;
			String formatted_address = null;
			// JSONObject jsonObject = new JSONObject();
			try {
				// jsonObject = new JSONObject(stringBuilder.toString());
				// AndyUtils.log("googlemapsurl:stringBuilder", "" +
				// stringBuilder.toString());
				/*
				 * long_name = ((JSONArray)
				 * jsonObject.get("results")).getJSONObject
				 * (0).getJSONObject("formatted_address");
				 * AndyUtils.log("googlemapsurl:long_name", "" + long_name);
				 * formatted_address = long_name.getString("formatted_address");
				 * AndyUtils.log("googlemapsurl:formatted_address", "" +
				 * formatted_address);
				 */

				JSONObject json = new JSONObject(stringBuilder.toString());
				AndyUtils.log("googlemapsurl:json", "" + json.toString());
				JSONArray jArray = json.getJSONArray("results");
				AndyUtils.log("googlemapsurl:jArray", "" + jArray.toString());
				formatted_address = jArray.getJSONObject(0).getString(
						"formatted_address");

			} catch (JSONException e) {
				/*
				 * AndyUtils.log("googlemapsurl:JSONException", "" +
				 * e.getMessage().toString()); if(progressDialog2.isShowing()){
				 * progressDialog2.dismiss(); }
				 */
				// e.printStackTrace();
			}

			return formatted_address;
		}

		@Override
		protected void onPostExecute(String result) {
			/*
			 * AndyUtils.log("googlemapsurl:result", "" + result);
			 * if(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 * if(progressDialog2.isShowing()){ progressDialog2.cancel(); }
			 */
			etSource.setText(result);

		}

	}

	private class GetAddressFromLocation extends
			AsyncTask<LatLng, Void, String> {
		@Override
		protected void onPreExecute() {
			/*
			 * progressDialog2 = new ProgressDialog(getActivity());
			 * progressDialog2.setMessage("Retrieving accurate location..");
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 * 
			 * if(!progressDialog2.isShowing()){ if (!(AndyUtils.mDialog != null
			 * && AndyUtils.mDialog.isShowing())){ progressDialog2.show(); } }
			 */
			AndyUtils.showCustomProgressDialog(getActivity(),
					"Getting current location", false, null);

		}

		@Override
		protected String doInBackground(LatLng... params) {
			StringBuilder stringBuilder = new StringBuilder();
			try {
				String theurl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
						+ params[0].latitude + "," + params[0].longitude;
				HttpPost httpGet = new HttpPost(theurl);
				AndyUtils.log("googlemapsurl", "" + theurl);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}
			;
			//

			// JSONObject long_name;
			String formatted_address = null;
			// JSONObject jsonObject = new JSONObject();
			try {
				// jsonObject = new JSONObject(stringBuilder.toString());
				// AndyUtils.log("googlemapsurl:stringBuilder", "" +
				// stringBuilder.toString());
				/*
				 * long_name = ((JSONArray)
				 * jsonObject.get("results")).getJSONObject
				 * (0).getJSONObject("formatted_address");
				 * AndyUtils.log("googlemapsurl:long_name", "" + long_name);
				 * formatted_address = long_name.getString("formatted_address");
				 * AndyUtils.log("googlemapsurl:formatted_address", "" +
				 * formatted_address);
				 */

				JSONObject json = new JSONObject(stringBuilder.toString());
				// AndyUtils.log("googlemapsurl:json", "" + json.toString());
				JSONArray jArray = json.getJSONArray("results");
				AndyUtils.log("googlemapsurl:jArray", "" + jArray.toString());
				formatted_address = jArray.getJSONObject(0).getString(
						"formatted_address");

			} catch (JSONException e) {
				/*
				 * AndyUtils.log("googlemapsurl:JSONException", "" +
				 * e.getMessage().toString()); if(progressDialog2.isShowing()){
				 * progressDialog2.dismiss(); }
				 */
				// e.printStackTrace();
			}

			return formatted_address;
		}

		@Override
		protected void onPostExecute(String result) {
			AndyUtils.log("googlemapsurl:result", "" + result);
			/*
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 */

			AndyUtils.removeCustomProgressDialog();
			// geoFence(true);
			etSource.setText(result);

		}

	}

	private void getAddressFromLocation(final LatLng latlng, final EditText et) {
		if (latlng.latitude != 0 || latlng.longitude != 0) {
			new GetAddressFromLocation().execute(latlng);
		}

	}

	private void getAddressFromLocation2(final LatLng latlng, final EditText et) {
		if (latlng.latitude != 0 || latlng.longitude != 0) {
			new GetAddressFromLocation2().execute(latlng);
		}

	}

	// getAddressFromLocation(markerSource.getPosition(), etSource);
	/*
	 * private void getAddressFromLocation(final LatLng latlng, final EditText
	 * et) { // et.setText("Waiting for Address"); et.setTextColor(Color.GRAY);
	 * // AndyUtils.log("gCoder.getFromLocation", ":1:"); new Thread(new
	 * Runnable() {
	 * 
	 * @Override public void run() { // Geocoder gCoder = new
	 * Geocoder(getActivity()); //ISSUE //
	 * AndyUtils.log("gCoder.getFromLocation", ":2:"); try { //
	 * AndyUtils.log("gCoder.getFromLocation", ":3:"); Geocoder gCoder = new
	 * Geocoder(getActivity()); // AndyUtils.log("gCoder.getFromLocation", //
	 * ":lati:"+latlng.latitude+":longi:"+latlng.longitude); final List<Address>
	 * list;
	 * 
	 * if(latlng.latitude ==0.0 ){ list =
	 * gCoder.getFromLocation(latlng.latitude, latlng.longitude, 1); }else{
	 * 
	 * list = gCoder.getFromLocation(latlng.latitude, latlng.longitude, 1); // }
	 * 
	 * if (list != null && list.size() > 0) { //
	 * AndyUtils.log("gCoder.getFromLocation", ":4:"); address = list.get(0);
	 * StringBuilder sb = new StringBuilder(); if (address.getAddressLine(0) !=
	 * null) { // AndyUtils.log("gCoder.getFromLocation", ":5:"); if
	 * (address.getMaxAddressLineIndex() > 0) { //
	 * AndyUtils.log("gCoder.getFromLocation", // ":6:"); for (int i = 0; i <
	 * address .getMaxAddressLineIndex(); i++) { //
	 * AndyUtils.log("gCoder.getFromLocation", // ":7:");
	 * sb.append(address.getAddressLine(i)) .append("\n"); } sb.append(",");
	 * sb.append(address.getCountryName()); } else { //
	 * AndyUtils.log("gCoder.getFromLocation", // ":8:");
	 * sb.append(address.getAddressLine(0)); } // 973882 //1200 } strAddress =
	 * sb.toString(); strAddress = strAddress.replace(",null", ""); strAddress =
	 * strAddress.replace("null", ""); strAddress =
	 * strAddress.replace("Unnamed", ""); } // if (list != null && list.size() >
	 * 0) { // address = list.get(0); // StringBuilder sb = new StringBuilder();
	 * // if (address.getAddressLine(0) != null) { // for (int i = 0; i <
	 * address // .getMaxAddressLineIndex(); i++) { //
	 * sb.append(address.getAddressLine(i)).append( // "\n"); // } // } //
	 * strAddress = sb.toString(); // strAddress = strAddress.replace(",null",
	 * ""); // strAddress = strAddress.replace("null", ""); // strAddress =
	 * strAddress.replace("Unnamed", ""); // } if (getActivity() == null)
	 * return;
	 * 
	 * getActivity().runOnUiThread(new Runnable() {
	 * 
	 * @Override public void run() { // AndyUtils.log("gCoder.getFromLocation",
	 * ":9:");
	 * 
	 * try { // AndyUtils.log("gCoder.getFromLocation", // ":10:"); if
	 * (!TextUtils.isEmpty(strAddress)) { et.setFocusable(false);
	 * et.setFocusableInTouchMode(false); et.setText(strAddress);
	 * et.setTextColor(activity.getResources()
	 * .getColor(android.R.color.black)); et.setFocusable(true);
	 * et.setFocusableInTouchMode(true); } else { et.setText("");
	 * et.setTextColor(getResources().getColor( android.R.color.black)); }
	 * etSource.setEnabled(true); } catch (Exception e) {
	 * 
	 * } } }); } catch (Exception exc) { //
	 * AndyUtils.log("gCoder.getFromLocation", //
	 * ":11:"+exc.getMessage().toString()); exc.printStackTrace(); } }
	 * }).start(); }
	 */

	private void animateCameraToMarker(LatLng latLng, boolean isAnimate) {
		try {
			etSource.setFocusable(false);
			etSource.setFocusableInTouchMode(false);
			CameraUpdate cameraUpdate = null;

			cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
			if (cameraUpdate != null && map != null) {
				if (isAnimate)
					map.animateCamera(cameraUpdate);
				else
					map.moveCamera(cameraUpdate);
			}
			etSource.setFocusable(true);
			etSource.setFocusableInTouchMode(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// perfect function...
	private void animateMarker(final Marker marker, final LatLng toPosition,
			final Location toLocation, final boolean hideMarker) {
		if (map == null || !this.isVisible() || marker == null
				|| marker.getPosition() == null) {
			return;
		}
		// int MYID = 1;
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		Projection proj = map.getProjection();
		// int id = nearDriverMarker.get(marker.getId());
		Point startPoint = proj.toScreenLocation(marker.getPosition());
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final double startRotation = marker.getRotation();
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);
				double lng = t * toPosition.longitude + (1 - t)
						* startLatLng.longitude;
				double lat = t * toPosition.latitude + (1 - t)
						* startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				float rotation = (float) (t * toLocation.getBearing() + (1 - t)
						* startRotation);
				if (rotation != 0) {
					marker.setRotation(rotation);
				}
				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				} else {
					if (hideMarker) {
						marker.setVisible(false);
					} else {
						marker.setVisible(true);
					}
				}
			}
		});
	}

	private LatLng getLocationFromAddress(final String place) {
		LatLng loc = null;
		Geocoder gCoder = new Geocoder(getActivity());
		try {
			final List<Address> list = gCoder.getFromLocationName(place, 1);
			if (list != null && list.size() > 0) {
				loc = new LatLng(list.get(0).getLatitude(), list.get(0)
						.getLongitude());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return loc;
	}

	@Override
	public void onProgressCancel() {
		stopCheckingStatusUpdate();
		cancleRequest();

		// stopCheckingStatusUpdate();
	}

	// Validating Location
	@Override
	protected boolean isValidate() {
		String msg = null;
		if (curretLatLng == null) {
			msg = getString(R.string.text_location_not_found);
		} else if (selectedPostion == -1) {
			msg = getString(R.string.text_select_type);
		} else if (TextUtils.isEmpty(etSource.getText().toString())
				|| etSource.getText().toString()
						.equalsIgnoreCase("Waiting for Address")) {
			msg = getString(R.string.text_waiting_for_address);
		}
		pickupAddrs = etSource.getText().toString().trim();
		if (msg == null)
			return true;
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		return false;
	}

	private void pickMeUp(String destination) {
		final HashMap<String, String> create_request_map = new HashMap<String, String>();
		final StringBuilder string_builder_create_request = new StringBuilder();
		try{
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		} else if (preference.getDefaultCard() == 0
				&& paymentMode == Const.CREDIT) {
			AndyUtils.showToast(getResources().getString(R.string.no_card),
					activity);
			return;
		}
		AndyUtils.showCustomProgressRequestDialog(activity,getString(R.string.text_creating_request), true, null);
		create_request_map.put(Const.URL, Const.ServiceType.CREATE_REQUEST);
		create_request_map.put(Const.Params.TOKEN,new PreferenceHelper(activity).getSessionToken());

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d hh:mm a");
		String currentDate = sdf.format(c.getTime());
		TimeZone timezone = TimeZone.getDefault();
		
		string_builder_create_request.append("FORMID|CREATEREQUEST");
		string_builder_create_request.append("ID|" + new PreferenceHelper(activity).getUserId() + "|");
		string_builder_create_request.append("TOKEN|" + new PreferenceHelper(activity).getSessionToken() + "|");
		string_builder_create_request.append("LATITUDE|" + String.valueOf(curretLatLng.latitude) + "|");
		string_builder_create_request.append("LONGITUDE|" + String.valueOf(curretLatLng.longitude) + "|");
		string_builder_create_request.append("PAYMENT_MODE|" + String.valueOf(new PreferenceHelper(activity).getPaymentMode()) + "|");
		string_builder_create_request.append("TYPE|" + String.valueOf(listType.get(selectedPostion).getId()) + "|");
		string_builder_create_request.append("PROMO_CODE|" + appliedPromoCode+ "|");
		string_builder_create_request.append("SRC_ADDRESS|" + mypickupAddrs + "|");
		string_builder_create_request.append("CURRENT_DATE|" + currentDate + "|");
		string_builder_create_request.append("MY_MUSIC|" + myMusic + "|");
		string_builder_create_request.append("PASSENGERTYPE|" + passengerType + "|");
		string_builder_create_request.append("TIME_ZONE|" + timezone.getID() + "|");
		
		if (isRidingAsCorporate) {
			string_builder_create_request.append("CORPORATECODE|" + corporatecode + "|");
			string_builder_create_request.append("CORPORATENAME|" + corporatename + "|");
		}
		if (markerDestination != null) {
			final LatLng dest = markerDestination.getPosition();
			string_builder_create_request.append("DEST_ADDRESS|" + mydropAddrs+ "|");
			string_builder_create_request.append("D_LATITUDE|" + String.valueOf(dest.latitude)+ "|");
			string_builder_create_request.append("D_LONGITUDE|" + String.valueOf(dest.latitude)+ "|");
		}else{
			string_builder_create_request.append("DEST_ADDRESS|" + mydropAddrs + "|");
			string_builder_create_request.append("D_LATITUDE|" + String.valueOf(destlanglong.latitude) + "|");
			string_builder_create_request.append("D_LONGITUDE|" +String.valueOf(destlanglong.longitude) + "|");
		}
		if (isRidingAsCorporate) {
			string_builder_create_request.append("corporate_reference|" +corporate_reference + "|");
		} else {
			string_builder_create_request.append("corporate_reference|" + "N/A" + "|");
		}
		}catch(Exception ex){
			AppLog.Log("createrequestexception", ex.getMessage());
		}	
 
		AppLog.Log("mycreaterequest", "response:::" + string_builder_create_request);
		AndyUtils.log("mapfragmentcorporate:isRidingAsCorporate"+ isRidingAsCorporate, "code:" + corporatecode + ":name:"+ corporatename);

		new HttpRequester(activity, create_request_map, Const.ServiceCode.CREATE_REQUEST, this);
		AppLog.Log(Const.TAG, "RequestCreated:");
	}

	@Override
	public void onTaskCompleted(final String response, int serviceCode) {
		super.onTaskCompleted(response, serviceCode);

		if (!this.isVisible())
			return;
		if (!(isAdded()))
			return;
		if (!(activity.pHelper.getIsLoggedIn())) {
			AppLog.Log("opendialog", "2");
			if (!(activity.isdialogopen())) {
				AppLog.Log("opendialog", "3");
				activity.openForceLogoutDialog();
				stopUpdateProvidersLoaction();
				stopCheckingStatusUpdate();
			}

		}
		switch (serviceCode) {
		case Const.ServiceCode.CREATE_REQUEST:
			AppLog.Log("mycreaterequest", "response::::" + response);
			
			AndyUtils.removeCustomProgressRequestDialog();
			if (activity.pContent.isSuccess(response)) {
				activity.pHelper.putRequestLocation(curretLatLng);
				activity.pHelper.putRequestId(activity.pContent
						.getRequestId(response));
				Driver driverInfo = activity.pContent.getDriverDetail(response);
				preference.putDriverId(driverInfo.getEmail());
				AndyUtils.showDriverDetailDialog(activity, this, driverInfo);
				strMaxSize = types.get(selectedType).getMaxSize();
				strMinFare = types.get(selectedType).getMinFare();
				strPricePerUnitTime = types.get(selectedType)
						.getPricePerUnitTime();
				strPricePerUnitDistance = types.get(selectedType)
						.getPricePerUnitDistance();
				stopUpdateProvidersLoaction();
				startCheckingStatusUpdate();
			}

			break;

		case Const.ServiceCode.CREATE_FUTURE_REQUEST:
			// toast("IS_WALKER_STARTED: -"+activity.pContent.checkRequestStatus(response));

			// AppLog.Log("MapFragment", "Create future request Response : " +
			// response);
			if (activity.pContent.isSuccess(response)) {
				activity.pHelper.putRequestLocation(curretLatLng);
				scheduleDialog.dismiss();
				if (!(TextUtils.isEmpty(response))) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONArray jArray = jsonObject
								.getJSONArray("all_scheduled_requests");
						if (jsonObject.getBoolean("success")) {
							JSONObject jObj = jArray.getJSONObject(0);
							reqId = jObj.getInt(Const.Params.REQ_ID);
							// isAddDestination = false;
							// isSource = true;
							// etDestination.setText("");
							// tvVehicleType.setVisibility(View.VISIBLE);
							// layoutDestination.setVisibility(View.GONE);
							// sendReqLayout.setVisibility(View.GONE);
							// confirmLayout.setVisibility(View.GONE);
							// layoutBubble.setVisibility(View.VISIBLE);
							// vehicleLayout.setVisibility(View.VISIBLE);
							// linearPickupAddress.setVisibility(View.VISIBLE);
							// view.findViewById(R.id.markerBubblePickMeUp)
							// .setVisibility(View.GONE);
							cancelConfirmation();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				AndyUtils.removeCustomProgressRequestDialog();
				showConfirmationDialog();
			} else {
				AndyUtils.removeCustomProgressRequestDialog();
				AndyUtils.showToast("Error.", activity);
				// AppLog.Log("MapFragment",
				// "Create future request Response : " + response);
			}
			break;

		case Const.ServiceCode.CONFIRMPAYMENT:
			AppLog.Log(Const.TAG, "confirmpaymentResponse::" + response);
			break;

		case Const.ServiceCode.GET_REQUEST_STATUS:
			AppLog.Log(Const.TAG, "Get RequestStatusResponse::::::" + response);
			if (activity.pContent.isSuccess(response)) {
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:
				case Const.IS_WALKER_ARRIVED:
				case Const.IS_COMPLETED:
				case Const.IS_WALKER_STARTED:
					AndyUtils.removeCustomProgressRequestDialog();
					// view.findViewById(R.id.markerBubblePickMeUp).setVisibility(
					// View.GONE); // Changes
					stopCheckingStatusUpdate();
					stopUpdateProvidersLoaction();
					Driver driver = activity.pContent.getDriverDetail(response);

					if (this.isVisible())
						removeThisFragment();
					activity.gotoTripFragment(driver);
					tvVehicleType.setVisibility(View.GONE);
					layoutDestination.setVisibility(View.GONE);
					// sendReqLayout.setVisibility(View.GONE);
					layoutBubble.setVisibility(View.VISIBLE);
					// vehicleLayout.setVisibility(View.GONE);
					linearPickupAddress.setVisibility(View.VISIBLE);

					if (mo2 == null) {
						mo2 = new MarkerOptions();
						mo2.flat(true);
						mo2.anchor(0.5f, 0.5f);
						mo2.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.driver_icon));
						mo2.title(getString(R.string.text_drive_location));
						mo2.position(new LatLng(driver.getLatitude(), driver
								.getLongitude()));
						setDriverMarker(mo2, new LatLng(driver.getLatitude(),
								driver.getLongitude()), driver.getBearing());
					}
					mo2.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.driver_icon));
					mo2.title("Driver Location");
					mo2.position(new LatLng(driver.getLatitude(), driver
							.getLongitude()));
					// markerSource.setDraggable(true);

					break;
				case Const.IS_WALKER_RATED:
					stopCheckingStatusUpdate();
					if (this.isVisible())
						removeThisFragment();
					// activity.gotoRateFragment(activity.pContent.getDriverDetail(response));
					break;

				case Const.IS_REQEUST_CREATED:
					if (activity.pHelper.getRequestId() != Const.NO_REQUEST) {
						stopUpdateProvidersLoaction();
					}
					isContinueRequest = true;
					break;
				case Const.NO_REQUEST:
					if (!isGettingVehicalType) {
						AndyUtils.removeDriverDetailDialog();
						startUpdateProvidersLocation();
					}
					stopCheckingStatusUpdate();
					break;
				default:
					isContinueRequest = false;
					break;
				}

			} else if (activity.pContent.getErrorCode(response) == Const.REQUEST_ID_NOT_FOUND) {
				AndyUtils.removeCustomProgressDialog();
				activity.pHelper.clearRequestData();
				isContinueRequest = false;
			} else if (activity.pContent.getErrorCode(response) == Const.INVALID_TOKEN) {
				if (activity.pHelper.getLoginBy()
						.equalsIgnoreCase(Const.MANUAL))
					login();
				else
					loginSocial(activity.pHelper.getUserId(),
							activity.pHelper.getLoginBy());
			} else {
				isContinueRequest = true;
			}
			break;
		case Const.ServiceCode.CANCEL_REQUEST:
			AndyUtils.log("cancelling1", "response received " + response);
			activity.pHelper.clearRequestData();
			AndyUtils.removeDriverDetailDialog();
			if (activity.pContent.isSuccess(response)) {
				activity.pHelper.clearRequestData();
				AndyUtils.removeDriverDetailDialog();
				AndyUtils
						.showToast("Request cancelled successfully.", activity);
			}

			break;

		case Const.ServiceCode.GET_VEHICAL_TYPES:
			AndyUtils.log("GET_VEHICAL_TYPES", "" + response);
			if (activity.pContent.isSuccess(response)) {
				VehicalType vehicalType = new VehicalType();

				listType.clear();
				activity.pContent.parseTypes(response, listType);
				// AndyUtils.log("vehicletyperesponse", response);
				pointer = listType.size();

				// ListAdapter listAdapter = new SimpleAdapter(getActivity(),
				// (List<? extends Map<String, ?>>) listType,
				// R.layout.activity_car_types, new
				// String[]{vehicalType.getIcon(), vehicalType.getName()}, new
				// int[]{R.id.img_cartype, R.id.txt_cartype});
				//
				// listView.setAdapter(listAdapter);

				fancyCoverFlow.setAdapter(new FancyCoverAdapter(activity,
						listType));
				if (listType.size() > 2) {
					selectedPostion = 1;
					fancyCoverFlow.setSelection(1);

				} else {
					selectedPostion = 0;
				}

				arraylist = new ArrayList<VehiclesAdapter>();
				arraylist.clear();

				// int [] vehicleCountByType = null;
				// int [] vehiclecounter = null;
				for (int i = 0; i < listType.size(); i++) {
					String name = listType.get(i).getName();
					String icon = listType.get(i).getIcon();
					VehiclesAdapter participant = new VehiclesAdapter(
							"" + name, "" + icon, "", false);
					// AndyUtils.log("vehicletype", "vehiclename:" + name+
					// ":vehicleicon:" + icon);
					/*
					 * for (int j = 0; j < listDriver.size(); j++) { Driver
					 * driver = listDriver.get(i); if (driver.getVehicleTypeId()
					 * == i) { ++ vehiclecounter [i]; //vehicleCountByType [0] =
					 * vehiclecounter [0]+1; } }
					 */
					arraylist.add(participant);
				}
				try {
					WindowManager mWinMgr = (WindowManager) getActivity()
							.getSystemService(Context.WINDOW_SERVICE);
					int displayWidth = mWinMgr.getDefaultDisplay().getWidth();
					// toast(Integer.toString(displayWidth));
					int dps = 80 * listType.size();
					final float scale = getActivity().getResources()
							.getDisplayMetrics().density;
					int pixels = (int) (dps * scale + 0.5f);
					if (pixels > displayWidth) {// set width of horizontallist
												// not larger than phone
						pixels = displayWidth - 5;
					}
					listView.getLayoutParams().width = pixels;
					horizontalAdapter = new TymeHorizontaliconsAdapter(
							getActivity(), arraylist);
					listView.setAdapter(horizontalAdapter);
					listView.invalidate();
				} catch (Exception e) {

				}

				// if (pointer <= 1 && pointer > 4)
				// sb.setEnabled(false);
				isGettingVehicalType = false;
				// if (listType.size() > 0) {
				// if (listType != null && listType.get(0) != null)
				// listType.get(0).isSelected = true;
				// typeAdapter.notifyDataSetChanged();
				// }

				String name = "", icon = "";
				HashMap<String, String> type_response = new HashMap<String, String>();
				ArrayList<HashMap<String, String>> lst_types = new ArrayList<HashMap<String, String>>();

				listType.clear();
				types = activity.pContent.parseTypes(response, listType);
				pointer = listType.size();

				/*
				 * for(int i=0; i<types.size(); i++){ name =
				 * types.get(i).getName(); icon = types.get(i).getIcon();
				 * 
				 * type_response.put(vehicalType.getIcon(), icon);
				 * type_response.put(vehicalType.getName(), name);
				 * 
				 * lst_types.add(type_response); }
				 * 
				 * Log.e("types_response", ""+name);
				 * 
				 * ListAdapter listAdapter = new SimpleAdapter(getActivity(),
				 * lst_types, R.layout.activity_car_types, new
				 * String[]{vehicalType.getIcon(), vehicalType.getName()}, new
				 * int[]{R.id.img_cartype, R.id.txt_cartype});
				 * 
				 * listView.setAdapter(listAdapter);
				 */

			}
			AndyUtils.removeCustomProgressDialog();
			break;
		case Const.ServiceCode.GET_PROVIDERS:
			try {
				map.getUiSettings().setScrollGesturesEnabled(true);
				AppLog.Log("kkkkkkkkk", "Provider Response : " + response);
				if (new JSONObject(response).getBoolean("success")) {
					new Thread(new Runnable() {
						@Override
						public void run() {

							listDriver = new ArrayList<Driver>();
							listDriver = activity.pContent.parseNearestDrivers(
									response, txtLoadingVehicles);
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// AndyUtils.log("kkkkk3:getproviders",
									// "getDriverId");
									setProvirderOnMap();
								}
							});
						}
					}).start();
				} else {
					map.clear();
				}
			} catch (Exception e) {
			}
			break;
		case Const.ServiceCode.GET_DURATION:
			AppLog.Log("durationresponse", "Duration Response : " + response);
			// pBar.setVisibility(View.GONE);
			// layoutDuration.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(response)) {
				try {
					estimatedTimeTxt = activity.pContent
							.parseNearestDriverDurationString(response);
					if (estimatedTimeTxt != null) {
						if (!(estimatedTimeTxt.equals(estimatedTimeTxt2))) {
							tvEstimatedTime.setText(estimatedTimeTxt);
							horizontalAdapter.notifyDataSetChanged();
							estimatedTimeTxt2 = estimatedTimeTxt;
						}

					} else {
						tvEstimatedTime.setText("N/A");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// tvDurationUnit.setText(durationArr[1]);
			}
			break;

		// case Const.ServiceCode.GET_DURATION:
		// AppLog.Log("MaPFragment", "DistanceMatrix : " + response);
		// //pBar.setVisibility(View.GONE);
		// //layoutDuration.setVisibility(View.VISIBLE);
		// if (!TextUtils.isEmpty(response)) {
		// estimatedTimeTxt = activity.pContent
		// .parseNearestDriverDurationString(response);
		// String[] durationArr = estimatedTimeTxt.split(" ");
		// tvEstimatedTime.setText(durationArr[0] + durationArr[1]);
		// //tvDurationUnit.setText(durationArr[1]);
		// }
		// break;

		case Const.ServiceCode.DRAW_PATH_ROAD:
			if (!TextUtils.isEmpty(response)) {
				route = new Route();
				activity.pContent.parseRoute(response, route);

				final ArrayList<Step> step = route.getListStep();
				System.out.println("step size=====> " + step.size());
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				for (int i = 0; i < step.size(); i++) {
					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					points.addAll(path);
				}
				if (points != null && points.size() > 0) {
					setMarker(new LatLng(points.get(0).latitude,
							points.get(0).longitude), isSource);
					if (isSource) {
						getAddressFromLocation(
								new LatLng(points.get(0).latitude,
										points.get(0).longitude), etSource);
					}
					// else {
					// getAddressFromLocation(
					// new LatLng(points.get(0).latitude,
					// points.get(0).longitude), etDestination);
					// }
					if (markerSource != null && markerDestination != null) {
						showDirection(markerSource.getPosition(),
								markerDestination.getPosition());
					}
				}
			}
			break;
		case Const.ServiceCode.DRAW_PATH:
			if (!TextUtils.isEmpty(response)) {
				route = new Route();
				activity.pContent.parseRoute(response, route);

				final ArrayList<Step> step = route.getListStep();
				System.out.println("step size=====> " + step.size());
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				for (int i = 0; i < step.size(); i++) {

					List<LatLng> path = step.get(i).getListPoints();
					System.out.println("step =====> " + i + " and "
							+ path.size());
					points.addAll(path);

				}

				if (polyLine != null)
					polyLine.remove();
				lineOptions.addAll(points);
				lineOptions.width(15);
				lineOptions.color(getResources().getColor(R.color.color_text)); // #00008B
																				// rgb(0,0,139)

				if (lineOptions != null && map != null) {
					polyLine = map.addPolyline(lineOptions);
					LatLngBounds.Builder bld = new LatLngBounds.Builder();
					bld.include(markerSource.getPosition());
					bld.include(markerDestination.getPosition());
					LatLngBounds latLngBounds = bld.build();
					map.moveCamera(CameraUpdateFactory.newLatLngBounds(
							latLngBounds, 30));
				}
			}
			break;
		// case Const.ServiceCode.GET_QUOTE:
		// if (!TextUtils.isEmpty(response)) {
		// try {
		// quoteDialog = new Dialog(getActivity());
		// quoteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// quoteDialog.setContentView(R.layout.farequote_popup);
		// quoteDialog.getWindow().setLayout(
		// LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT);
		//
		// txtTotalFareQuate = (TextView) quoteDialog
		// .findViewById(R.id.txtTotalFareQuate);
		// quoteDialog.findViewById(R.id.btnOKFareQuote)
		// .setOnClickListener(this);
		//
		// JSONArray jsonArray = new JSONObject(response)
		// .getJSONArray("routes");
		// JSONArray jArrSub = jsonArray.getJSONObject(0)
		// .getJSONArray("legs");
		// JSONObject legObj = jArrSub.getJSONObject(0);
		//
		// JSONObject durationObj = legObj.getJSONObject("duration");
		// JSONObject distanceObj = legObj.getJSONObject("distance");
		//
		// double minute = durationObj.getDouble("value") / 60;
		// double kms = distanceObj.getDouble("value") / 1000;
		//
		// AppLog.Log("TAG",
		// "Duration Seconds: " + durationObj.getLong("value"));
		// AppLog.Log("TAG",
		// "Distance meter: " + distanceObj.getLong("value"));
		//
		// AppLog.Log("TAG", "Duration kms: " + kms);
		// AppLog.Log("TAG", "Distance minute: " + minute);
		//
		// // String totalQuote = MathUtils.getRound(preference
		// // .getBasePrice()
		// // + (preference.getDistancePrice() * kms)
		// // + (preference.getTimePrice() * minute));
		// // AppLog.Log("TAG", "totalQuote: " + totalQuote);
		// txtTotalFareQuate.setText(activity
		// .getString(R.string.payment_unit)
		// + getFareCalculation(kms));
		//
		// AndyUtils.removeCustomProgressDialog();
		// quoteDialog.show();
		// } catch (Exception e) {
		// AndyUtils.removeCustomProgressDialog();
		// AppLog.Log("UberMapFragment", "" + e);
		// }
		// }
		// break;
		case Const.ServiceCode.GET_FARE_QUOTE:
			if (!TextUtils.isEmpty(response)) {
				try {
					JSONArray jsonArray = new JSONObject(response)
							.getJSONArray("routes");
					JSONArray jArrSub = jsonArray.getJSONObject(0)
							.getJSONArray("legs");
					JSONObject legObj = jArrSub.getJSONObject(0);

					JSONObject durationObj = legObj.getJSONObject("duration");
					JSONObject distanceObj = legObj.getJSONObject("distance");

					double minute = durationObj.getDouble("value") / 60;
					double kms = distanceObj.getDouble("value") / 1000;

					// AppLog.Log("TAG", "Duration Seconds: " +
					// durationObj.getLong("value"));
					// AppLog.Log("TAG", "Distance meter: " +
					// distanceObj.getLong("value"));

					// AppLog.Log("TAG", "Duration kms:  " + kms);
					// AppLog.Log("TAG", "Distance minute: " + minute);
					pbMinFare.setVisibility(View.GONE);
					tvTotalFare.setVisibility(View.VISIBLE);
					tvTotalFare.setText(getString(R.string.payment_unit)
							+ getFareCalculation(kms));
				} catch (Exception e) {
					// AppLog.Log("UberMapFragment=====",
					// "GET_FARE_QUOTE Response: " + e);
				}
			}
			break;
		case Const.ServiceCode.GET_NEAR_BY:
			AppLog.Log("TAG", "Near by : " + response);
			pbNearby.setVisibility(View.GONE);
			nearByList.setVisibility(View.VISIBLE);
			ArrayList<String> resultList = new ArrayList<String>();
			activity.pContent.parseNearByPlaces(response, resultList);
			nearByAd = new ArrayAdapter<String>(getActivity(),
					R.layout.autocomplete_list_text, R.id.tvPlace, resultList);
			nearByList.setAdapter(nearByAd);
			break;
		case Const.ServiceCode.UPDATE_PROVIDERS:
			AppLog.Log("providerlistresponse", "providerlist : " + response);
			try {
				boolean successful = new JSONObject(response)
						.getBoolean("success");
				String error = new JSONObject(response).getString("error");
				String error_code = new JSONObject(response)
						.getString("error_code");
				if (successful) {
					AppLog.Log("providerlistsuccess=true",
							"providerlist success=true: " + response);
					new Thread(new Runnable() {
						@Override
						public void run() {
							listUpdatedDriver = new ArrayList<Driver>();
							listUpdatedDriver = activity.pContent
									.parseNearestDrivers(response,
											txtLoadingVehicles);
							activity.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									updateProviderOnMap();
								}
							});
						}
					}).start();
					LatLng latLang = new LatLng(myLocation.getLatitude(),
							myLocation.getLongitude());
					animateCameraToMarker(latLang, true);
					getAddressFromLocation2(latLang, etSource);

				} else if (error.equals("81") && error_code.equals("411")) {
					AppLog.Log("providerlist",
							"providerlist success=false elseif: " + response);
					map.clear();
				} else {
					AppLog.Log("providerlist",
							"providerlist success=false else: " + response);
					// map.clear();
				}
			} catch (Exception e) {
			}
			break;

		case Const.ServiceCode.ISCORPORATE:
			AndyUtils.log("iscorporate", "" + response);
			if (new ParseContent(activity).isSuccess(response)) {

				JSONObject jsonObject;
				boolean iscorporate = false, force_update = false, ride_later = false;
				String corporatecode = "", corporatename = "", radiostations = "";
				int playstoreVersion = 0;
				try {
					// STATUS|000|SMSCODE|027-299
					jsonObject = new JSONObject(response);
					iscorporate = jsonObject.getBoolean("iscorporate");
					force_update = jsonObject.getBoolean("force_update");
					ride_later = jsonObject.getBoolean("ride_later");
					playstoreVersion = jsonObject.getInt("version_code");
					corporatecode = jsonObject.optString("corporate_code");
					corporatename = jsonObject.optString("corporate_name");
					radiostations = jsonObject.optString("radio_stations");
				} catch (JSONException e) {
				}
				// btnRideLater
				preference.putIsCorporate(iscorporate);
				preference.save_corporatecode(corporatecode);
				preference.save_radioStation(radiostations);
				checkIsCorporate();
				PackageInfo pInfo = null;
				int currentVersion = 0;

				// FORCE A DISMISS FOR RIDE LATER
				if (ride_later) {
					btnRideLater.setVisibility(View.VISIBLE);
				} else {
					btnRideLater.setVisibility(View.GONE);
				}

				// FORCE AN UPDATE
				if (force_update) {
					try {
						pInfo = activity.getPackageManager().getPackageInfo(
								activity.getPackageName(), 0);
						currentVersion = pInfo.versionCode;
					} catch (NameNotFoundException e) {
					}
					if (currentVersion < playstoreVersion) {
						Intent startUpdateActivity = new Intent(activity,
								UpdateActivity.class);
						activity.startActivity(startUpdateActivity);
					}
				}

				AndyUtils.log("iscorporate:boolean", "" + iscorporate);
				AndyUtils.log("iscorporate:corporatecode", "" + corporatecode);
				AndyUtils.log("iscorporate:radiostations", "" + radiostations);
				AndyUtils.log("versions", "" + currentVersion + ":"
						+ playstoreVersion);
			} else {

			}

			break;

		case Const.ServiceCode.APPLY_REFFRAL_CODE:
			AndyUtils.removeCustomProgressDialog();
			// AppLog.Log(Const.TAG, "Referral Response: " + response);
			if (new ParseContent(activity).isSuccess(response)) {
				new PreferenceHelper(activity).putReferee(1);
				referralDialog.dismiss();
				// activity.startActivity(new Intent(activity,
				// MainDrawerActivity.class));
			} else {
				llErrorMsg.setVisibility(View.VISIBLE);
				etRefCode.requestFocus();
			}
			break;

		case Const.ServiceCode.DELETE_FUTURE_REQUEST:
			if (activity.pContent.isSuccess(response)) {
				// AppLog.Log("DELETE_FUTURE_REQUEST response", response);
				confirmFutureRequest.dismiss();
				AndyUtils.showToast("Your request cancelled successfully.",
						activity);
				AndyUtils.removeCustomProgressDialog();
				appliedPromoCode = "";
				new PreferenceHelper(activity).putPromoCode(appliedPromoCode);
				// editPromo.setText(getString(R.string.text_enter_promo_code));
				isAddDestination = false;
				isSource = true;
				etDestination.setText("");
				tvVehicleType.setVisibility(View.GONE);
				layoutDestination.setVisibility(View.GONE);
				// sendReqLayout.setVisibility(View.GONE);
				// confirmLayout.setVisibility(View.GONE);
				layoutBubble.setVisibility(View.VISIBLE);
				// vehicleLayout.setVisibility(View.GONE);
				linearPickupAddress.setVisibility(View.VISIBLE);

			} else {
				AndyUtils.removeCustomProgressDialog();
				AndyUtils.showToast("Error", activity);
			}
			break;

		}
	}

	private void showConfirmationDialog() {
		confirmFutureRequest = new Dialog(activity);
		confirmFutureRequest.requestWindowFeature(Window.FEATURE_NO_TITLE);
		confirmFutureRequest
				.setContentView(R.layout.dialog_confirm_future_request);
		cancel = (Button) confirmFutureRequest
				.findViewById(R.id.btn_cancel_request);
		ok = (Button) confirmFutureRequest.findViewById(R.id.btnOk);
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
		confirmFutureRequest.show();
	}

	private void geoFence(boolean open) {
		// AndyUtils.log("todecrypt-encrpyt", "1");
		if (curretLatLng != null) {
			if (open) {
				asyncExecute("INIT", "1", "INIT");
			} else {
				asyncExecute("INIT", "0", "INIT");
			}

		}

	}

	private void asyncExecute(String FORMID, String FORMNAME, String MethodName) {
		if (AndyUtils.isNetworkAvailable(activity)) {
			String imei = new PreferenceHelper(getActivity()).getIMEI();
			if (imei.length() < 1) {
				TelephonyManager telephonyManager = (TelephonyManager) getActivity()
						.getSystemService(Context.TELEPHONY_SERVICE);
				imei = telephonyManager.getDeviceId();
			}
			String serverString = "";
			// FORMID|INIT|MOBILENUMBER|xxxx|LATLONG|342,234324|http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=1403812IMEI|xxxxx|COSEBASE|ANDROID|
			serverString = "FORMID|" + FORMID
					+ "|MOBILENUMBER|"
					+ preference.getPhoneNumber()
					// + "|IMEI|"+imei
					// + "|CODEBASE|ANDROID"
					+ "|STATUS|" + FORMNAME + "|LATLONG|"
					+ curretLatLng.latitude + "," + curretLatLng.longitude
			// + "|COUNTRY|KENYA|"
			;
			AndyUtils.log("serverString: ", serverString);
			try {
				serverString = PreferenceHelper.eaes(serverString);
				// Base64.encodeBytes(serverString.getBytes());
			} catch (Exception ex) {
				AndyUtils.log("Base64String: ", ex.getMessage().toString());
			}
			AndyUtils.log("todecrypt-encrpyt", "" + serverString);
			AndyUtils.log("todecrypt-decrpyt",
					"" + PreferenceHelper.daes(serverString));
			// serverString = AndyUtils.urlEncode(serverString);
			new AsyncTask_BulletProof(getActivity()).execute(
					"RegisterFragment", serverString, MethodName);
		} else {
			AndyUtils.showToast("Network not available", activity);
		}
	}

	private class TimerRequestStatus extends TimerTask {
		@Override
		public void run() {
			if (isContinueRequest) {
				isContinueRequest = false;
				getRequestStatus(String
						.valueOf(activity.pHelper.getRequestId()));
			}
		}
	}

	private void getRequestStatus(String requestId) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				Const.ServiceType.GET_REQUEST_STATUS + Const.Params.ID + "="
						+ new PreferenceHelper(activity).getUserId() + "&"
						+ Const.Params.TOKEN + "="
						+ new PreferenceHelper(activity).getSessionToken()
						+ "&" + Const.Params.REQUEST_ID + "=" + requestId);

		new HttpRequester(activity, map, Const.ServiceCode.GET_REQUEST_STATUS,
				true, this);
		AndyUtils.log("GET_REQUEST_STATUS", "Create Request GetStatus");

		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.GET_REQUEST_STATUS, this, this));
	}

	private void startCheckingStatusUpdate() {
		stopCheckingStatusUpdate();
		if (activity.pHelper.getRequestId() != Const.NO_REQUEST) {
			isContinueRequest = true;
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerRequestStatus(), Const.DELAY,
					Const.TIME_SCHEDULE);
		}
	}

	private void stopCheckingStatusUpdate() {
		isContinueRequest = false;
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private void cancleRequest() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(
					getActivity().getResources()
							.getString(R.string.no_internet), activity);
			return;
		}
		AndyUtils.removeCustomProgressRequestDialog();
		AndyUtils.showCustomProgressRequestDialog(activity, activity
				.getResources().getString(R.string.text_canceling_request),
				true, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.CANCEL_REQUEST);
		map.put(Const.Params.ID, String.valueOf(activity.pHelper.getUserId()));
		map.put(Const.Params.TOKEN,
				String.valueOf(activity.pHelper.getSessionToken()));
		map.put(Const.Params.REQUEST_ID,
				String.valueOf(activity.pHelper.getRequestId()));

		AndyUtils.log("cancelling1", "waiting response");
		AppLog.Log(
				"cancelling2variables",
				Const.Params.ID + ":" + activity.pHelper.getUserId() + ":"
						+ Const.Params.TOKEN + ":"
						+ activity.pHelper.getSessionToken() + ":"
						+ Const.Params.REQUEST_ID + ":"
						+ activity.pHelper.getRequestId() + ":"
						+ Const.Params.ID + ":" + activity.pHelper.getUserId()

		);

		new HttpRequester(activity, map, Const.ServiceCode.CANCEL_REQUEST, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.CANCEL_REQUEST, this, this));
	}

	class WalkerStatusReceiver extends BroadcastReceiver implements
			OnProgressCancelListener {

		@Override
		public void onReceive(Context context, Intent intent) {
			String response = intent.getStringExtra(Const.EXTRA_WALKER_STATUS);
			// AppLog.Log("Response from walkerstatusReceiver------>>>>",
			// response);
			if (TextUtils.isEmpty(response))
				return;
			stopCheckingStatusUpdate();

			if (activity.pContent.isSuccess(response)) {

				Driver driverInfo = activity.pContent.getDriverInfo(response);
				AndyUtils.removeDriverDetailDialog();
				//
				AndyUtils.showDriverDetailDialog(activity, this, driverInfo);
				switch (activity.pContent.checkRequestStatus(response)) {
				case Const.IS_WALK_STARTED:
					break;
				case Const.IS_WALKER_ARRIVED:
					break;
				case Const.IS_COMPLETED:
					break;
				case Const.IS_WALKER_STARTED:
					AndyUtils.removeDriverDetailDialog();
					/*
					 * Driver driver =
					 * activity.pContent.getDriverDetail(response); if
					 * (MapFragment.this.isVisible())
					 * activity.gotoTripFragment(driver);
					 */
					removeThisFragment();
					view.findViewById(R.id.markerBubblePickMeUp).setVisibility(
							View.GONE);
					AndyUtils.removeCustomProgressRequestDialog();
					break;
				case Const.IS_WALKER_RATED:
					stopCheckingStatusUpdate();
					/*
					 * if (MapFragment.this.isVisible())
					 * activity.gotoRateFragment(activity.pContent
					 * .getDriverDetail(response));
					 */
					AndyUtils.removeDriverDetailDialog();
					break;

				case Const.IS_REQEUST_CREATED:
					view.findViewById(R.id.markerBubblePickMeUp).setVisibility(
							View.GONE);
					/*
					 * Driver driverData =
					 * activity.pContent.getDriverDetail(response);
					 * AndyUtils.showCustomProgressDialog(activity,
					 * getString(R.string.text_contacting), false,
					 * MapFragment.this, driverData);
					 */
					startCheckingStatusUpdate();
					isContinueRequest = true;
					break;
				default:
					isContinueRequest = false;
					break;
				}

			} else if (activity.pContent.getErrorCode(response) == Const.REQUEST_ID_NOT_FOUND) {
				AndyUtils.removeDriverDetailDialog();
				activity.pHelper.clearRequestData();
				isContinueRequest = false;
			} else if (activity.pContent.getErrorCode(response) == Const.INVALID_TOKEN) {
				if (activity.pHelper.getLoginBy()
						.equalsIgnoreCase(Const.MANUAL))
					login();
				else
					loginSocial(activity.pHelper.getUserId(),
							activity.pHelper.getLoginBy());
			}

			else {
				isContinueRequest = true;
				startCheckingStatusUpdate();
			}
			// startCheckingStatusUpdate();
		}

		@Override
		public void onProgressCancel() {
			stopCheckingStatusUpdate();
			// AppLog.Log("cancleRequest1", "cancleRequest : ");
			cancleRequest();
		}
	}

	private void removeThisFragment() {
		try {
			getActivity().getSupportFragmentManager().beginTransaction()
					.remove(this).commitAllowingStateLoss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getVehicalTypes() {
		AndyUtils.showCustomProgressDialog(activity, "loading", false, null);
		isGettingVehicalType = true;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.GET_VEHICAL_TYPES);
		AppLog.Log(Const.TAG, Const.URL);
		new HttpRequester(activity, map, Const.ServiceCode.GET_VEHICAL_TYPES,
				true, this);
		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.GET_VEHICAL_TYPES, this, this));
	}

	public void onItemClick(int pos) {
		selectedPostion = pos;
	}

	private void getAllProviders(LatLng latlang) {
		try {
			estimatedTimeTxt = "";
			if (!AndyUtils.isNetworkAvailable(activity)) {
				AndyUtils
						.showToast(
								activity.getResources().getString(
										R.string.no_internet), activity);
				return;
			} else if (latlang == null) {
				Toast.makeText(
						activity,
						getActivity().getResources().getString(
								R.string.text_location_not_found),
						Toast.LENGTH_SHORT).show();
				return;
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.GET_PROVIDERS);
			map.put(Const.Params.ID,
					String.valueOf(activity.pHelper.getUserId()));
			map.put(Const.Params.TOKEN,
					String.valueOf(activity.pHelper.getSessionToken()));
			map.put(Const.Params.USER_LATITUDE,
					String.valueOf(latlang.latitude));
			map.put(Const.Params.USER_LONGITUDE,
					String.valueOf(latlang.longitude));
			new HttpRequester(activity, map, Const.ServiceCode.GET_PROVIDERS,
					this);

			AppLog.Log(
					"getallprovider",
					Const.Params.ID
							+ String.valueOf(activity.pHelper.getUserId())
							+ Const.Params.TOKEN
							+ String.valueOf(activity.pHelper.getSessionToken())
							+ Const.Params.USER_LATITUDE
							+ String.valueOf(latlang.latitude)
							+ Const.Params.USER_LONGITUDE
							+ String.valueOf(latlang.longitude));
		} catch (Exception e) {
			AppLog.Log("TAG", "getAllProviderException : " + e);
		}
	}

	private void updateAllProviders(LatLng latlang) {
		try {
			if (!AndyUtils.isNetworkAvailable(activity)) {
				return;
			} else if (latlang == null) {
				return;
			}
			// AppLog.Log("TAG", "Update Provider lat : " + latlang.latitude
			// + " Long :" + latlang.longitude);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Const.URL, Const.ServiceType.GET_PROVIDERS);
			map.put(Const.Params.ID,
					String.valueOf(activity.pHelper.getUserId()));
			map.put(Const.Params.TOKEN,
					String.valueOf(activity.pHelper.getSessionToken()));
			map.put(Const.Params.USER_LATITUDE,
					String.valueOf(latlang.latitude));
			map.put(Const.Params.USER_LONGITUDE,
					String.valueOf(latlang.longitude));
			new HttpRequester(activity, map,
					Const.ServiceCode.UPDATE_PROVIDERS, this);
		} catch (Exception e) {
			AppLog.Log("TAG", "updateAllProviderException : " + e);
			// id,token,usr_lat,userlong,
		}
	}

	private void getDuration(LatLng origin, String lat, String lng) {

		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(
					activity.getResources().getString(R.string.no_internet),
					activity);
			return;
		} else if (origin == null) {
			return;
		}
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;
		String str_dest = "destination=" + lat + "," + lng;
		String sensor = "sensor=false";

		String parameters = str_origin + "&" + str_dest + "&" + sensor;
		String output = "json";

		StringBuilder sb1 = new StringBuilder();
		sb1.append("https://maps.googleapis.com/maps/api/directions/");
		sb1.append(output + "?");
		sb1.append(parameters + "&key=" + Const.PLACES_AUTOCOMPLETE_API_KEY);

		/*
		 * String url = "https://maps.googleapis.com/maps/api/directions/" +
		 * output + "?" + parameters + "&key=" +
		 * Const.PLACES_AUTOCOMPLETE_API_KEY;
		 */

		// AppLog.Log("MapFragment", "Url : " + sb1.toString());
		AppLog.Log("duration:url", "Duration url : " + sb1.toString());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, sb1.toString());
		new HttpRequester(activity, map, Const.ServiceCode.GET_DURATION, true,
				this);

		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.GET_DURATION, this, this));
	}

	// private void getDuration(LatLng origin, String lat, String lng) {
	// if (!AndyUtils.isNetworkAvailable(activity)) {
	// return;
	// } else if (origin == null) {
	// return;
	// }
	// String str_origin = "origins=" + origin.latitude + ","
	// + origin.longitude;
	// String str_dest = "destinations=" + lat + "," + lng;
	// String parameters = str_origin + "&" + str_dest + "&key="
	// + Const.PLACES_AUTOCOMPLETE_API_KEY;
	// String output = "json";
	// String url = "https://maps.googleapis.com/maps/api/distancematrix/"
	// + output + "?" + parameters;
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put(Const.URL, url);
	// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
	// Const.ServiceCode.GET_DURATION, this, this));
	// }

	// private void getCommonFareQuote(String source, String destination) {
	// AndyUtils.showCustomProgressDialog(activity,
	// getString(R.string.text_please_wait), false, this);
	// if (!AndyUtils.isNetworkAvailable(activity)) {
	// AndyUtils.showToast(getResources().getString(R.string.no_internet),
	// activity);
	// } else {
	// LatLng origin = getLocationFromAddress(source);
	// LatLng dest = getLocationFromAddress(destination);
	//
	// String str_origin = "origin=" + origin.latitude + ","
	// + origin.longitude;
	// String str_dest = "destination=" + dest.latitude + ","
	// + dest.longitude;
	// String sensor = "sensor=false";
	// String parameters = str_origin + "&" + str_dest + "&" + sensor;
	// String output = "json";
	// String url = "https://maps.googleapis.com/maps/api/directions/"
	// + output + "?" + parameters;
	// AppLog.Log("MapFragment", "Url : " + url);
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put(Const.URL, url);
	// // new HttpRequester(activity, map, Const.ServiceCode.GET_QUOTE,
	// // this);
	// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
	// Const.ServiceCode.GET_ROUTE, this, this));
	// }
	// }

	private void getFareQuote(LatLng origin, String destination) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(
					activity.getResources().getString(R.string.no_internet),
					activity);
		} else {
			try {
				LatLng dest = getLocationFromAddress(destination);

				String str_origin = "origin=" + origin.latitude + ","
						+ origin.longitude;
				String str_dest = "destination=" + dest.latitude + ","
						+ dest.longitude;
				String sensor = "sensor=false";
				String parameters = str_origin + "&" + str_dest + "&" + sensor;
				String output = "json";
				String url = "https://maps.googleapis.com/maps/api/directions/"
						+ output + "?" + parameters + "&key="
						+ Const.PLACES_AUTOCOMPLETE_API_KEY;
				// AppLog.Log("MapFragment", "Url : " + url);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(Const.URL, url);
				new HttpRequester(activity, map,
						Const.ServiceCode.GET_FARE_QUOTE, this);
				// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
				// Const.ServiceCode.GET_FARE_QUOTE, this, this));
			} catch (Exception e) {

			}
		}
	}

	// Client Marker
	private void setMarker(LatLng latLng, boolean isSource) {
		if (!MapFragment.this.isVisible())
			return;
		if (getActivity() != null && getActivity().getCurrentFocus() != null) {
			// inputMethodManager.hideSoftInputFromWindow(getActivity()
			// .getCurrentFocus().getWindowToken(), 0);
			activity.hideKeyboard();
		}

		if (latLng != null && map != null) {
			if (isSource) {
				if (markerSource == null) {
					MarkerOptions me = new MarkerOptions()
							.position(
									new LatLng(latLng.latitude,
											latLng.longitude))
							.title(getActivity().getResources().getString(
									R.string.text_source_pin_title))
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.pin_client_org));
					markerSource = map.addMarker(me);
					// markerSource.setDraggable(true);
				} else {
					markerSource.setPosition(latLng);
				}
				CameraUpdateFactory.newLatLng(latLng);
				// getAddressFromLocation(markerSource.getPosition(), etSource);

			} else {
				if (markerDestination == null) {
					MarkerOptions opt = new MarkerOptions();
					opt.position(latLng);
					opt.title(getActivity().getResources().getString(
							R.string.text_destination_pin_title));
					opt.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.destination_pin));
					markerDestination = map.addMarker(opt);

					markerDestination.setDraggable(true);

					if (markerSource != null) {
						LatLngBounds.Builder bld = new LatLngBounds.Builder();

						bld.include(new LatLng(
								markerSource.getPosition().latitude,
								markerSource.getPosition().longitude));
						bld.include(new LatLng(
								markerDestination.getPosition().latitude,
								markerDestination.getPosition().longitude));
						LatLngBounds latLngBounds = bld.build();
						map.moveCamera(CameraUpdateFactory.newLatLngBounds(
								latLngBounds, 30));
					} else {
						CameraUpdateFactory.newLatLng(latLng);
					}

				} else {
					markerDestination.setPosition(latLng);
				}
			}

		} else {
			Toast.makeText(getActivity(), "Unable to get location",
					Toast.LENGTH_LONG).show();
		}
	}

	private void setMarkerOnRoad(LatLng source, LatLng destination) {
		String msg = null;
		if (source == null) {
			msg = "Unable to get source location, please try again";
		} else if (destination == null) {
			msg = "Unable to get destination location, please try again";
		}
		if (msg != null) {
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				"http://maps.googleapis.com/maps/api/directions/json?origin="
						+ source.latitude + "," + source.longitude
						+ "&destination=" + destination.latitude + ","
						+ destination.longitude + "&sensor=false");

		new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH_ROAD,
				true, this);
		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.DRAW_PATH_ROAD, this, this));
	}

	private void showDirection(LatLng source, LatLng destination) {
		if (source == null || destination == null) {
			return;
		}

		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL,
				"http://maps.googleapis.com/maps/api/directions/json?origin="
						+ source.latitude + "," + source.longitude
						+ "&destination=" + destination.latitude + ","
						+ destination.longitude + "&sensor=false");
		new HttpRequester(activity, map, Const.ServiceCode.DRAW_PATH, true,
				this);
		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.DRAW_PATH, this, this));
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		start = seekBar.getProgress();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// if (start == seekBar.getProgress()
		// || (start <= seekBar.getProgress() + 3 && start >= seekBar
		// .getProgress() - 3)) {
		// seekBar.setProgress(start);
		// if (TextUtils.isEmpty(etSource.getText().toString())
		// || etSource.getText().toString()
		// .equalsIgnoreCase("Waiting for Address")) {
		// AndyUtils.showToast(
		// getActivity().getString(
		// R.string.text_waiting_for_address),
		// getActivity());
		// } else if (TextUtils.isEmpty(estimatedTimeTxt)) {
		// AndyUtils.showToast(
		// getActivity().getString(R.string.text_waiting_for_eta),
		// getActivity());
		// } else {
		// showVehicleDetails();
		// }
		// return;
		// }
		// if (pointer == 4) {
		// fourPointer();
		// } else if (pointer == 3) {
		// threePointer();
		// } else if (pointer == 2) {
		// twoPointer();
		// }
		// AppLog.Log("Mapfragment", "Selected Service : " + selectedPostion);
		// if (listDriver.size() > 0) {
		// activity.runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		// setProvirderOnMap();
		// }
		// });
		// } else {
		// pBar.setVisibility(View.VISIBLE);
		// }
	}

	// private void twoPointer() {
	// value = sb.getProgress();
	//
	// if (value >= 50) {
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 100);
	// } else if (value < 50) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }
	//
	// private void threePointer() {
	// value = sb.getProgress();
	// if (value < 25) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// } else if (value >= 25 && value < 75) {
	// // Log.e("value", "" + value);
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 50);
	// } else if (value >= 75) {
	// selectedPostion = 2;
	// anim = ValueAnimator.ofInt(value, 100);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }
	//
	// private void fourPointer() {
	// value = sb.getProgress();
	//
	// if (value <= 17) {
	// selectedPostion = 0;
	// anim = ValueAnimator.ofInt(value, 0);
	// } else if (value > 17 && value <= 50) {
	// selectedPostion = 1;
	// anim = ValueAnimator.ofInt(value, 33);
	// } else if (value > 50 && value <= 83) {
	// selectedPostion = 2;
	// anim = ValueAnimator.ofInt(value, 66);
	// } else if (value >= 83) {
	// selectedPostion = 3;
	// anim = ValueAnimator.ofInt(value, 100);
	// }
	// anim.setDuration(300);
	// anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	// @Override
	// public void onAnimationUpdate(ValueAnimator animation) {
	// value = (Integer) animation.getAnimatedValue();
	// sb.setProgress(value);
	// }
	// });
	// anim.start();
	// }

	private void setProvirderOnMap() {
		VehicalType vehicle = null;

		if (listType != null && listType.size() > selectedPostion) {
			vehicle = listType.get(selectedPostion);
		}
		if (vehicle == null) {
			return;
		}
		if (map != null) {
			map.clear();
		}
		// AndyUtils.log("kkkkk3:", "getDriverId");

		nearDriverMarker = new HashMap<Integer, Marker>();
		for (int i = 0; i < listDriver.size(); i++) {
			Driver driver = listDriver.get(i);
			if (vehicle.getId() == driver.getVehicleTypeId()) {
				mo2 = new MarkerOptions();
				mo2.flat(true);
				mo2.anchor(0.5f, 0.5f);
				mo2.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.driver_icon));
				mo2.title(getActivity().getResources().getString(
						R.string.text_drive_location));
				mo2.position(new LatLng(driver.getLatitude(), driver
						.getLongitude()));
				nearDriverMarker.put(driver.getDriverId(), map.addMarker(mo2));
			}
		}

		boolean isGetProvider = false;
		for (int i = 0; i < listDriver.size(); i++) {
			Driver driver = listDriver.get(i);
			if (vehicle.getId() == driver.getVehicleTypeId()) {
				isGetProvider = true;
				getDuration(curretLatLng, String.valueOf(driver.getLatitude()),
						String.valueOf(driver.getLongitude()));
				break;
			}
		}
		if (!isGetProvider) {
			// layoutDuration.setVisibility(View.GONE);
			// pBar.setVisibility(View.VISIBLE);
		}
		startUpdateProvidersLocation();
	}

	private class GetSourceLocationFromAddress extends
			AsyncTask<String, Void, LatLng> {

		@Override
		protected void onPreExecute() {
			/*
			 * progressDialog2 = new ProgressDialog(getActivity());
			 * progressDialog2.setMessage("Getting Current Location");
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 * progressDialog2.show();
			 */
			AndyUtils.showCustomProgressDialog(getActivity(),
					"Getting current location", false, null);
			// AndyUtils.removeCustomProgressDialog();
		}

		@Override
		protected LatLng doInBackground(String... destination_address) {
			StringBuilder stringBuilder = new StringBuilder();

			try {
				destination_address[0] = destination_address[0].replaceAll(" ",
						"%20");
				HttpPost httpGet = new HttpPost(
						"http://maps.googleapis.com/maps/api/geocode/json?address="
								+ destination_address[0] + "&sensor=false");
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;

				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);

				}
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			}
			;

			Double latitude = new Double(0);
			Double longitude = new Double(0);
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject = new JSONObject(stringBuilder.toString());

				latitude = ((JSONArray) jsonObject.get("results"))
						.getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lat");

				longitude = ((JSONArray) jsonObject.get("results"))
						.getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lng");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new LatLng(latitude, longitude);
		}

		@Override
		protected void onPostExecute(LatLng loc) {
			// currentLatLng = loc;
			/*
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 */
			AndyUtils.removeCustomProgressDialog();
			isMapTouched = true;
			curretLatLng = loc;
			AndyUtils.log("todecrypt-encrpyt", "0");
			geoFence(true);
			isSource = true;
			setMarker(curretLatLng, isSource);
			// setMarkerOnRoad(curretLatLng, curretLatLng);
			animateCameraToMarker(curretLatLng, true);
			// stopUpdateProvidersLoaction();
			// getAllProviders(curretLatLng);

		}

	}

	private void updateProviderOnMap() {

		try {
			VehicalType vehicle = listType.get(selectedPostion);
			for (int i = 0; i < listDriver.size(); i++) {
				Driver driver = listDriver.get(i);
				if (vehicle.getId() == driver.getVehicleTypeId()) {
					for (int j = 0; j < listUpdatedDriver.size(); j++) {
						Driver updatedDriver = listUpdatedDriver.get(j);
						map.clear();
						if (driver.getDriverId() == updatedDriver.getDriverId()) {
							Location driverLocation = new Location("");
							driverLocation.setLatitude(updatedDriver
									.getLatitude());
							driverLocation.setLongitude(updatedDriver
									.getLongitude());
							driverLocation.setBearing((float) updatedDriver
									.getBearing());
							/*
							 * animateMarker( nearDriverMarker.get(i), new
							 * LatLng(updatedDriver.getLatitude(),updatedDriver.
							 * getLongitude()), driverLocation, false);
							 */

							setAnimatedDriverMarker(nearDriverMarker.get(i),
									updatedDriver.getLatitude(),
									updatedDriver.getLongitude(),
									updatedDriver.getBearing());
							break;
						}
					}
				}
			}

			boolean isGetProvider = false;
			for (int i = 0; i < listUpdatedDriver.size(); i++) {
				Driver driver = listUpdatedDriver.get(i);
				if (vehicle.getId() == driver.getVehicleTypeId()) {
					isGetProvider = true;
					getDuration(curretLatLng,
							String.valueOf(driver.getLatitude()),
							String.valueOf(driver.getLongitude()));
					break;
				}
			}
			listDriver.clear();
			listDriver.addAll(listUpdatedDriver);
			if (!isGetProvider) {
				// layoutDuration.setVisibility(View.GONE);
				// pBar.setVisibility(View.VISIBLE);
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private void showFareQuote() {
	// if (etSource.getText().toString().trim().length() > 0) {
	// if (etDestination.getText().toString().trim().length() > 0)
	// getCommonFareQuote(etSource.getText().toString(), etDestination
	// .getText().toString());
	// else
	// AndyUtils.showToast("Please set destination address",
	// getActivity());
	// } else
	// AndyUtils.showToast("Please set source address", getActivity());
	// }

	private void showVehicleDetails() {
		if (listType != null && listType.size() > 0) {
			VehicalType vehicle = listType.get(selectedPostion);
			// AppLog.Log("", "MAX:" + vehicle.getMaxSize());
			dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.vehicle_details);
			dialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.MATCH_PARENT);

			tvMaxSize = (TextView) dialog.findViewById(R.id.tvMaxSize);
			tvMinFare = (TextView) dialog.findViewById(R.id.tvMinFare);
			// tvLblMinFare = (TextView) dialog.findViewById(R.id.tvLblMinFare);
			tvETA = (TextView) dialog.findViewById(R.id.tvETA);
			pbar = (ProgressBar) dialog.findViewById(R.id.pbar);
			tvGetFareEst = (TextView) dialog.findViewById(R.id.tvGetFareEst);
			pbMinFare = (ProgressBar) dialog.findViewById(R.id.pbMinFare);
			tvTotalFare = (TextView) dialog.findViewById(R.id.tvTotalFare);
			cancelVehicleDetail = (ImageView) dialog
					.findViewById(R.id.cancelVehicleDetail);

			tvRateVehicleTypeName = (TextView) dialog
					.findViewById(R.id.tvRateVehicleTypeName);
			tvRateBasePrice = (TextView) dialog
					.findViewById(R.id.tvRateBasePrice);
			tvRateDistanceCost = (TextView) dialog
					.findViewById(R.id.tvRateDistanceCost);
			tvRateTimeCost = (TextView) dialog
					.findViewById(R.id.tvRateTimeCost);
			// tvRateCard = (TextView) dialog.findViewById(R.id.tvRateCard);
			// tvRateCard.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// showRateCardDialog();
			// }
			// });
			tvGetFareEst.setOnClickListener(this);
			tvETA.setText(estimatedTimeTxt);
			tvETA.setVisibility(View.VISIBLE);
			pbar.setVisibility(View.GONE);
			tvMaxSize.setText(vehicle.getMaxSize() + " Person");
			tvMinFare.setText(activity.getResources().getString(
					R.string.payment_unit)
					+ vehicle.getMinFare());

			tvRateVehicleTypeName.setText(vehicle.getName());

			tvRateBasePrice.setText(getActivity().getResources().getString(
					R.string.payment_unit)
					+ vehicle.getBasePrice()
					+ " "
					+ getActivity().getResources().getString(R.string.text_for)
					+ " " + vehicle.getBaseDistance() + vehicle.getUnit());
			tvRateDistanceCost.setText(getActivity().getResources().getString(
					R.string.payment_unit)
					+ vehicle.getPricePerUnitDistance()
					+ getString(R.string.text_per) + vehicle.getUnit());
			tvRateTimeCost
					.setText(getString(R.string.payment_unit)
							+ vehicle.getPricePerUnitTime()
							+ getActivity().getResources().getString(
									R.string.text_per)
							+ getActivity().getResources().getString(
									R.string.text_min));

			cancelVehicleDetail.setOnClickListener(this);
			dialog.show();
		}
	}

	// private void showRateCardDialog() {
	// VehicalType vehicle = (VehicalType) listType.get(selectedPostion);
	// rateCardDialog = new Dialog(getActivity());
	// rateCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	// rateCardDialog.setContentView(R.layout.dialog_rate_card);
	// rateCardDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT,
	// LayoutParams.WRAP_CONTENT);
	// // TextView tvRateVehicleTypeName, tvRateBasePrice, tvRateDistanceCost,
	// // tvRateTimeCost;
	// tvRateVehicleTypeName = (TextView) rateCardDialog
	// .findViewById(R.id.tvRateVehicleTypeName);
	// tvRateBasePrice = (TextView) rateCardDialog
	// .findViewById(R.id.tvRateBasePrice);
	// tvRateDistanceCost = (TextView) rateCardDialog
	// .findViewById(R.id.tvRateDistanceCost);
	// tvRateTimeCost = (TextView) rateCardDialog
	// .findViewById(R.id.tvRateTimeCost);
	// tvRateVehicleTypeName.setText(vehicle.getName());
	//
	// tvRateBasePrice.setText(getString(R.string.payment_unit)
	// + (int) vehicle.getBasePrice() + " "
	// + getString(R.string.text_for) + " "
	// + vehicle.getBaseDistance() + vehicle.getUnit());
	// tvRateDistanceCost.setText(getString(R.string.payment_unit)
	// + (int) vehicle.getPricePerUnitDistance()
	// + getString(R.string.text_per) + vehicle.getUnit());
	// tvRateTimeCost.setText(getString(R.string.payment_unit)
	// + (int) vehicle.getPricePerUnitTime()
	// + getString(R.string.text_per) + getString(R.string.text_min));
	//
	// // tvRateDistanceCost.setText(getString(R.string.payment_unit)
	// // + preference.getDistancePrice() + "/"
	// // + getString(R.string.text_miles));
	// // tvRateTimeCost.setText(getString(R.string.payment_unit)
	// // + preference.getTimePrice() + "/"
	// // + getString(R.string.text_mins));
	// rateCardDialog.show();
	// }

	private void showDestinationPopup() {
		destinationDialog = new Dialog(getActivity());
		destinationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		destinationDialog.setContentView(R.layout.destination_popup);
		etPopupDestination = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etPopupDestination);
		etHomeAddress = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etHomeAddress);
		etWorkAddress = (AutoCompleteTextView) destinationDialog
				.findViewById(R.id.etWorkAddress);
		tvHomeAddress = (TextView) destinationDialog
				.findViewById(R.id.tvHomeAddress);
		tvWorkAddress = (TextView) destinationDialog
				.findViewById(R.id.tvWorkAddress);
		tvHomeAddress.setText(preference.getHomeAddress());
		tvWorkAddress.setText(preference.getWorkAddress());
		etHomeAddress.setText(preference.getHomeAddress());
		etWorkAddress.setText(preference.getWorkAddress());

		layoutHomeText = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutHomeText);
		layoutHomeEdit = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutHomeEdit);
		layoutWorkText = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutWorkText);
		layoutWorkEdit = (LinearLayout) destinationDialog
				.findViewById(R.id.layoutWorkEdit);
		layoutHomeText.setOnClickListener(this);
		layoutWorkText.setOnClickListener(this);
		destinationDialog.findViewById(R.id.imgClearDest).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.imgClearHome).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.imgClearWork).setOnClickListener(
				this);

		destinationDialog.findViewById(R.id.btnEditHome).setOnClickListener(
				this);
		destinationDialog.findViewById(R.id.btnEditWork).setOnClickListener(
				this);
		nearByList = (ListView) destinationDialog.findViewById(R.id.nearByList);
		pbNearby = (ProgressBar) destinationDialog.findViewById(R.id.pbNearby);

		adapterPopUpDestination = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etPopupDestination.setAdapter(adapterPopUpDestination);
		adapterHomeAddress = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etHomeAddress.setAdapter(adapterHomeAddress);
		adapterWorkAddress = new PlacesAutoCompleteAdapter(activity,
				R.layout.autocomplete_list_text);
		etWorkAddress.setAdapter(adapterWorkAddress);
		destinationDialog.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		etPopupDestination.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				sendQuoteRequest(adapterPopUpDestination.getItem(arg2));
			}
		});
		etHomeAddress.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedPlace = adapterHomeAddress.getItem(arg2);
				preference.putHomeAddress(selectedPlace);
				tvHomeAddress.setText(selectedPlace);
				layoutHomeEdit.setVisibility(View.GONE);
				layoutHomeText.setVisibility(View.VISIBLE);
			}
		});
		etWorkAddress.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedPlace = adapterWorkAddress.getItem(arg2);
				preference.putWorkAddress(selectedPlace);
				tvWorkAddress.setText(selectedPlace);
				layoutWorkEdit.setVisibility(View.GONE);
				layoutWorkText.setVisibility(View.VISIBLE);
			}
		});
		nearByLocations();
		nearByList.setOnItemClickListener(this);
		destinationDialog.show();
	}

	private void sendQuoteRequest(String destination) {
		pbMinFare.setVisibility(View.VISIBLE);
		tvMinFare.setVisibility(View.GONE);
		// tvLblMinFare.setVisibility(View.GONE);
		tvTotalFare.setVisibility(View.GONE);
		tvGetFareEst.setText(destination);
		getFareQuote(curretLatLng, destination);
		destinationDialog.dismiss();
	}

	private void nearByLocations() {
		StringBuilder sb = new StringBuilder(Const.PLACES_API_BASE
				+ Const.TYPE_NEAR_BY + Const.OUT_JSON);
		sb.append("?sensor=true&key=" + Const.PLACES_AUTOCOMPLETE_API_KEY);
		sb.append("&location=" + curretLatLng.latitude + ","
				+ curretLatLng.longitude);
		sb.append("&radius=500");
		AppLog.Log("", "Near location Url : " + sb.toString());
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, sb.toString());
		new HttpRequester(activity, map, Const.ServiceCode.GET_NEAR_BY, true,
				this);
		// requestQueue.add(new VolleyHttpRequest(Method.GET, map,
		// Const.ServiceCode.GET_NEAR_BY, this, this));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (address != null) {
			sendQuoteRequest(nearByAd.getItem(arg2) + ", "
					+ address.getLocality() + ", " + address.getAdminArea()
					+ ", " + address.getCountryName());
		}

	}

	private void startUpdateProvidersLocation() {
		timerProvidersLocation = new Timer();
		timerProvidersLocation.scheduleAtFixedRate(new TrackLocation(), 1000,
				LOCATION_SCHEDULE);
	}

	private void stopUpdateProvidersLoaction() {
		if (timerProvidersLocation != null) {
			timerProvidersLocation.cancel();
			timerProvidersLocation = null;
		}
	}

	class TrackLocation extends TimerTask {
		@Override
		public void run() {
			// if (isContinueDriverRequest) {
			// isContinueDriverRequest = false;
			// getDriverLocation();
			// }
			updateAllProviders(curretLatLng);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.automated.taxinow.utils.LocationHelper.OnLocationReceived#
	 * onLocationReceived(com.google.android.gms.maps.model.LatLng)
	 */
	@Override
	public void onLocationReceived(LatLng latlong) {

	}

	/*
	 * (non-Javadoc) onLocationReceived(android.location.Location)
	 */
	@Override
	public void onLocationReceived(Location location) {

		if (location != null) {
			// drawTrip(latlong);
			myLocation = location;
			if (myLocation != null) {
				LatLng latLang = new LatLng(myLocation.getLatitude(),
						myLocation.getLongitude());
				// animateCameraToMarker(latLang, true);
				// getAllProviders(latLang);
				// animateCameraToMarker(latLang, false);

			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.automated.taxinow.utils.LocationHelper.OnLocationReceived#onConntected
	 * (android.os.Bundle)
	 */
	@Override
	public void onConntected(Bundle bundle) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.automated.taxinow.utils.LocationHelper.OnLocationReceived#onConntected
	 * (android.location.Location)
	 */
	@Override
	public void onConntected(Location location) {

		if (location != null) {

			myLocation = location;

			// isLocationEnable = true;
			LatLng latLang = new LatLng(location.getLatitude(),
					location.getLongitude());
			curretLatLng = latLang;
			getAllProviders(latLang);
			animateCameraToMarker(latLang, false);
		} else {
			activity.showLocationOffDialog();
		}
	}

	private String getFareCalculation(double distanceInKm) {
		VehicalType vehicle = listType.get(selectedPostion);
		double basePrice = vehicle.getBasePrice();
		int baseDistance = vehicle.getBaseDistance();
		double distanceCost = vehicle.getPricePerUnitDistance();
		// double timeCost = Double.parseDouble(vehicle.getPricePerUnitTime());

		double fare = (distanceInKm - baseDistance) * distanceCost + basePrice;
		DecimalFormat format = new DecimalFormat("0.00");
		String finalFare = format.format(fare);

		return finalFare;
	}

	private void setselectedList(int position) {
		selectedPostion = position;
		tvVehicleType.setText(listType.get(position).getName());
		// AppLog.Log("Mapfragment", "Selected Service : " + selectedPostion);
		if (listDriver.size() > 0) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// AndyUtils.log("kkkkk3:setselected", "getDriverId");
					setProvirderOnMap();
				}
			});
		} else {
			// pBar.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		selectedPostion = position;
		tvVehicleType.setText(listType.get(position).getName());
		// AppLog.Log("Mapfragment", "Selected Service : " + selectedPostion);
		if (listDriver.size() > 0) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// AndyUtils.log("kkkkk3:itemselected", "getDriverId");
					setProvirderOnMap();
				}
			});
		} else {
			// pBar.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	private class TymeHorizontaliconsAdapter extends BaseAdapter {

		Context mContext;
		LayoutInflater inflater;
		public List<VehiclesAdapter> contactList = null;
		private ArrayList<VehiclesAdapter> arraylist;

		public TymeHorizontaliconsAdapter(Context context,
				List<VehiclesAdapter> contactList) {
			mContext = context;
			this.contactList = contactList;
			inflater = LayoutInflater.from(mContext);
			this.arraylist = new ArrayList<VehiclesAdapter>();
			this.arraylist.addAll(contactList);
		}

		public class ViewHolder {
			ImageView imgVehicle;
			TextView txtVehicleName, txtVehicleETA;
		}

		@Override
		public int getCount() {
			return contactList.size();
		}

		public void clearthis() {
			arraylist.clear();
		}

		@Override
		public VehiclesAdapter getItem(int position) {
			return contactList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {

			final ViewHolder holder;
			if (view == null) {
				holder = new ViewHolder();
				view = inflater.inflate(R.layout.vehicle_item_, parent, false);
				holder.imgVehicle = (ImageView) view
						.findViewById(R.id.imgVehicle);
				holder.txtVehicleName = (TextView) view
						.findViewById(R.id.txtVehicleName);
				holder.txtVehicleETA = (TextView) view
						.findViewById(R.id.txtVehicleETA);

			} else {
				holder = (ViewHolder) view.getTag();
			}

			String icon = contactList.get(position).getv_image();
			String name = contactList.get(position).getv_name();

			boolean selected = contactList.get(position).isSelected();

			View.OnClickListener selectCar = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					sendReqLayout.setVisibility(View.VISIBLE);
					if (estimatedTimeTxt.length() < 1) {
						holder.txtVehicleETA.setText("checking");
					} else {
						holder.txtVehicleETA.setText("" + estimatedTimeTxt);
					}
					cartypename = "LITTLE "
							+ contactList.get(position).getv_name() + "";
					fancyCoverFlow.setSelection(position);
					setselectedList(position);

					selectedType = position;
					txtMaxPersons.setText("Max "
							+ types.get(position).getMaxSize() + " Persons");
					txtMinFare.setText("Min Fare Kes "
							+ types.get(position).getMinFare());
					txtFareSummary.setText("KES Base Fare "
							+ types.get(position).getMinFare() + " +"
							+ types.get(position).getPricePerUnitTime()
							+ "/MIN" + " +"
							+ types.get(position).getPricePerUnitDistance()
							+ "/KM");

					// txtMaxPersons.setText(types.get(position).getMaxSize()+" PERSONS");
					// txtMinFare.setText("MIN FARE KES "+types.get(position).getMinFare());
					// txtFareSummary.setText(
					// "Base Fare KES "+types.get(position).getMinFare()+
					// " +KES"+types.get(position).getPricePerUnitTime()+"/MIN"+
					// " +KES"+types.get(position).getPricePerUnitDistance()+"/KM")

					for (int i = 0; i < contactList.size(); i++) {
						contactList.get(i).setSelected(false);
					}
					contactList.get(position).setSelected(true);
					if (contactList.get(position).isSelected()) {
						if (sendReqLayout.isShown()) {
							// sendReqLayout.setVisibility(View.GONE);
						}

						// holder.txtVehicleName.setTextColor(R.color.col)
						if (contactList.get(position).getv_name()
								.contains("Basic")) {
							holder.imgVehicle
									.setImageResource(R.drawable.basic_selected);
						} else if (contactList.get(position).getv_name()
								.contains("Comfort")) {
							holder.imgVehicle
									.setImageResource(R.drawable.comfort_selected);
						} else if (contactList.get(position).getv_name()
								.contains("Lady")) {
							holder.imgVehicle
									.setImageResource(R.drawable.lady_bug_selected);
						} else if (contactList.get(position).getv_name()
								.contains("uber")) {
							holder.imgVehicle
									.setImageResource(R.drawable.uber_selected);
						} else {
							holder.imgVehicle
									.setBackgroundResource(R.drawable.defaultcar_selected);
						}
					} else {
						if (!(sendReqLayout.isShown())) {
							// sendReqLayout.setVisibility(View.VISIBLE);
						}
						if (contactList.get(position).getv_name()
								.contains("uber")) {
							holder.imgVehicle.setImageResource(R.drawable.uber);
						}
						holder.imgVehicle
								.setBackgroundResource(R.drawable.round_image);
					}
					/* Set adapter for AutoCompleteTextView in dialog view */
					strMaxSize = types.get(selectedType).getMaxSize();
					strMinFare = types.get(selectedType).getMinFare();
					strPricePerUnitTime = types.get(selectedType)
							.getPricePerUnitTime();
					strPricePerUnitDistance = types.get(selectedType)
							.getPricePerUnitDistance();
					// PreferenceHelper preferenceHelper = new
					// PreferenceHelper(getActivity());
					// preferenceHelper.save_taxiMaxSize(strMaxSize);
					// preferenceHelper.save_taxiMinFare(strMinFare);
					// preferenceHelper.save_taxiPricePerUnitTime(strPricePerUnitTime);
					// preferenceHelper.save_taxiPricePerUnitDistance(strPricePerUnitDistance);
					horizontalAdapter.notifyDataSetChanged();
				}
			};

			View.OnLongClickListener onlongclick = new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {

					return false;
				}
			};
			if (name.contains("uber")) {
				holder.imgVehicle.setImageResource(R.drawable.uber);
			} else {
				new AQuery(view).id(holder.imgVehicle).image(
						listType.get(position).getIcon(), imageOptions2);
			}
			holder.txtVehicleName.setText("" + name);
			holder.txtVehicleETA.setText("" + estimatedTimeTxt);
			// holder.imgVehicle.setImageBitmap(getBitmapFromString(icon));
			holder.imgVehicle.setOnClickListener(selectCar);
			holder.txtVehicleName.setOnClickListener(selectCar);

			if (selected) {
				if (sendReqLayout.isShown()) {
					// sendReqLayout.setVisibility(View.GONE);
				}
				if (name.contains("Basic")) {
					holder.imgVehicle
							.setImageResource(R.drawable.basic_selected);
				} else if (name.contains("Comfort")) {
					holder.imgVehicle
							.setImageResource(R.drawable.comfort_selected);
				} else if (name.contains("Lady")) {
					holder.imgVehicle
							.setImageResource(R.drawable.lady_bug_selected);
				} else if (name.contains("uber")) {
					holder.imgVehicle
							.setImageResource(R.drawable.uber_selected);
				} else {
					holder.imgVehicle
							.setBackgroundResource(R.drawable.roundimageselected);
				}
				// new
				// AQuery(view).id(holder.imgVehicle).image(listType.get(position).getIconSelected(),
				// imageOptions2);
			} else {
				if (!(sendReqLayout.isShown())) {
					// sendReqLayout.setVisibility(View.VISIBLE);
				}

				// new
				// AQuery(view).id(holder.imgVehicle).image(listType.get(position).getIcon(),
				// imageOptions2);
				// holder.imgVehicle.setBackgroundResource(R.drawable.roundimage);
			}
			txtLoadingVehicles.setVisibility(View.GONE);
			return view;
		}
	}

	private Bitmap getBitmapFromString(String base64) {
		byte[] url = Base64Utility.decode(base64, Base64Utility.DEFAULT);
		Bitmap photo = BitmapFactory.decodeByteArray(url, 0, url.length);
		return photo;
	}

	private void toast(String text) {
		AndyUtils.showToast(text, activity);
		Log.d("gcmpush", "" + text);
	}

	private void setData(ImageView image) {
		AQuery aQuery = new AQuery(getActivity());
		imageOptions = new ImageOptions();
		imageOptions.memCache = true;
		imageOptions.fileCache = true;
		// imageOptions.targetWidth = 200;
		imageOptions.fallback = R.drawable.default_user;

		DBHelper dbHelper = new DBHelper(getActivity());
		final User user = dbHelper.getUser();
		if (user != null) {
			aQuery.id(image).progress(R.id.pBar)
					.image(user.getPicture(), imageOptions);
			// tvProfileFName.setText(user.getFname());
			// etProfileFname.setText(user.getFname());
			// etProfileEmail.setText(user.getEmail());
			// etProfileNumber.setText(user.getContact());
		}
	}

	/**
	 * Calculates fare estimation
	 * 
	 * @author guidovanrossum
	 * 
	 */

	private void calculateEstimatedTotalFare(double destination_distance,
			double destination_eta) {
		double $price_per_distance = strPricePerUnitDistance;
		double $base_fare = 0.0;
		try {
			$base_fare = Double.valueOf(strMinFare);
		} catch (Exception e) {
			try {
				$base_fare = Double.valueOf(preference.get_taxiMinFare());
			} catch (Exception e2) {
				try {
					$base_fare = Double.valueOf(types.get(selectedType)
							.getMinFare());
				} catch (Exception e3) {

				}
			}
		}

		double $price_per_minute = strPricePerUnitTime;
		/* Calculate fare estimation */
		double total_charge = ($price_per_distance * destination_distance)
				+ (destination_eta * $price_per_minute);

		if (total_charge < $base_fare) {
			/* Calculate inflated fare */
			double inflation_rate = 0.3;
			double inflated_fare = (inflation_rate * $base_fare) + $base_fare;
			double rounded_inflated_fare = Math
					.round((inflated_fare + 49) / 50) * 50;
			textTotalCharge2 = "KES " + Double.toString(rounded_inflated_fare);
			textTotalCharge.setText("KES " + Double.toString($base_fare)
					+ "  -  " + textTotalCharge2);
		} else {
			/* Calculate inflated fare */
			double inflation_rate = 0.3;
			double inflated_fare = (inflation_rate * total_charge)
					+ total_charge;
			double rounded_inflated_fare = Math
					.round((inflated_fare + 49) / 50) * 50;
			textTotalCharge2 = "KES " + Double.toString(rounded_inflated_fare);
			textTotalCharge
					.setText("KES "
							+ Double.toString(Math
									.round((total_charge + 49) / 50) * 50)
							+ "  -  " + textTotalCharge2);

		}
	}

	/**
	 * 
	 * GET COORDINATES FOR THE DESTINATION
	 * 
	 * @author guidovanrossum
	 */
	private class GetLocationFromAddress extends
			AsyncTask<String, Void, LatLng> {

		@Override
		protected void onPreExecute() {
			/*
			 * progressDialog2 = new ProgressDialog(getActivity());
			 * progressDialog2.setMessage("Resolving Distance..");
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 * 
			 * 
			 * if(!progressDialog2.isShowing()){ if (!(AndyUtils.mDialog != null
			 * && AndyUtils.mDialog.isShowing())){ progressDialog2.show(); } }
			 */
			AndyUtils.showCustomProgressDialog(getActivity(),
					"Resolving distance", false, null);
		}

		@Override
		protected LatLng doInBackground(String... destination_address) {
			StringBuilder stringBuilder = new StringBuilder();

			try {
				destination_address[0] = destination_address[0].replaceAll(" ","%20");
				HttpPost httpGet = new HttpPost(
						"https://maps.googleapis.com/maps/api/geocode/json?address="
								+ destination_address[0] + "&sensor=false");
				HttpClient client = new DefaultHttpClient();
				HttpResponse response;
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
					//AppLog.Log("mydest::", stringBuilder.toString());

				}
			} catch (ClientProtocolException e) {
			} catch (IOException e) {
			};

			Double latitude = new Double(0);
			Double longitude = new Double(0);
			JSONObject jsonObject = new JSONObject();
			
			try {
				AppLog.Log("mydest::", stringBuilder.toString());
				jsonObject = new JSONObject(stringBuilder.toString());
				AppLog.Log("mydest::", jsonObject.toString());
				latitude = ((JSONArray) jsonObject.get("results"))
						.getJSONObject(1).getJSONObject("locations")
						.getJSONObject("latlng").getDouble("lat");
				longitude = ((JSONArray) jsonObject.get("results"))
						.getJSONObject(0).getJSONObject("locations")
						.getJSONObject("latlng").getDouble("lng");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			destlanglong = new LatLng(latitude, longitude);
			AppLog.Log("COORDINATES:: ", "LAT:: " + Double.toString(latitude)
					+ "  LNG:: " + Double.toString(longitude));
			return destlanglong;
		}

		@Override
		protected void onPostExecute(LatLng loc) {

			/*
			 * while(progressDialog2.isShowing()){ progressDialog2.dismiss(); }
			 */
			AndyUtils.removeCustomProgressDialog();
			new GetFareEstimate().execute(destinationTextView.getText()
					.toString());

		}

	}

	/**
	 * AsyncTask subclass for calculating the distance between rider source and
	 * rider destination
	 * 
	 * @author guidovanrossum
	 */
	private class GetFareEstimate extends
			AsyncTask<String, Void, ArrayList<Integer>> {

		@Override
		protected void onPreExecute() {
			/*
			 * progressDialog = new ProgressDialog(getActivity());
			 * progressDialog
			 * .setMessage("Calculating Your Fare Estimate. Please Wait...");
			 * progressDialog.setCancelable(false);
			 * while(progressDialog.isShowing()){ progressDialog.dismiss(); }
			 * progressDialog.show();
			 */

			AndyUtils.showCustomProgressDialog(getActivity(),
					"Calculating Your Fare Estimate. Please Wait..", false,
					null);
		}

		@SuppressWarnings("static-access")
		@Override
		protected ArrayList<Integer> doInBackground(String... params) {
			// Subclass variables
			Integer $parsedDistance;
			Integer $parsedDuration;
			ArrayList<Integer> result = new ArrayList<Integer>();
			try {
				/* USE GOOGLE API TO REVERSE GEOCODE */
				URL url = new URL(
						"http://maps.googleapis.com/maps/api/directions/json?origin="
								+ curretLatLng.latitude + ","
								+ curretLatLng.longitude + "&destination="
								+ destlanglong.latitude + ","
								+ destlanglong.longitude
								+ "&sensor=false&units=metric&mode=driving");
				final HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("POST");
				InputStream in = new BufferedInputStream(conn.getInputStream());
				String response = org.apache.commons.io.IOUtils.toString(in,
						"UTF-8");

				JSONObject jsonObject = new JSONObject(response);
				JSONArray array = jsonObject.getJSONArray("routes");
				JSONObject routes = array.getJSONObject(0);
				JSONArray legs = routes.getJSONArray("legs");
				JSONObject steps = legs.getJSONObject(0);
				JSONObject distance = steps.getJSONObject("distance");
				JSONObject duration = steps.getJSONObject("duration");

				$parsedDistance = distance.getInt("value");
				$parsedDuration = duration.getInt("value");

				result.add($parsedDistance);
				result.add($parsedDuration);

			} catch (Exception e) {
				AndyUtils.removeCustomProgressDialog();
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<Integer> result) {
			/*
			 * while(progressDialog.isShowing()){ progressDialog.dismiss(); }
			 */
			AndyUtils.removeCustomProgressDialog();
			if (result.size() == 0) {
				AndyUtils.showToast("You Internet connection is not stable",
						getActivity());
			} else {
				double distance_in_km = result.get(0) / 1000;
				double destination_duration = TimeUnit.MILLISECONDS
						.toMinutes(result.get(1));

				/* Calculating fare estimate using the returned results */
				calculateEstimatedTotalFare(distance_in_km,
						destination_duration);
			}

		}

	}

	private void enterFavouritesDialog() {
		mydestinationDialog.setContentView(R.layout.layout_favourites);
		// spnMusic = (Spinner)
		// mydestinationDialog.findViewById(R.id.spinnerMusic);
		buttonCancel = (MyFontButton) mydestinationDialog
				.findViewById(R.id.button_cancel);
		imageProfile = (CircleImageView) mydestinationDialog
				.findViewById(R.id.imageProfile);
		setData(imageProfile);
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mydestinationDialog.dismiss();
			}
		});
		mydestinationDialog.show();
	}

	// Dialog box for prompting the user to enter destination
	private void enterDestinationDialog(final String ACTION_TYPE) {
		mydestinationDialog
				.setContentView(R.layout.layout_enter_destination_new);
		layout_destination = (LinearLayout) mydestinationDialog
				.findViewById(R.id.layout_dest);
		layout_source = (LinearLayout) mydestinationDialog
				.findViewById(R.id.layout_source);
		textChargePerKm = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.txtFareEstimatePerKilometer);
		spnMusic = (Spinner) mydestinationDialog
				.findViewById(R.id.spinnerMusic);
		final Spinner spnPassengerType = (Spinner) mydestinationDialog
				.findViewById(R.id.spnPassengerType);
		textBaseFare = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.txtFareEstimateBaseFare);
		MyFontTextView labelCabType = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.labelCabType);
		textTotalCharge = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.txtTotalFareSummary);
		textPriceUnitTime = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.txtFareEstimatePerMinute);
		final MyFontButton buttonCheckNewDestination = (MyFontButton) mydestinationDialog
				.findViewById(R.id.button_check_new_destination);
		buttonCancel = (MyFontButton) mydestinationDialog
				.findViewById(R.id.button_cancel);
		imageProfile = (CircleImageView) mydestinationDialog
				.findViewById(R.id.imageProfile);
		riderSourceTextView = (MyFontTextView) layout_source
				.findViewById(R.id.txtFareEstimationRiderSource);
		destinationTextView = (AutoCompleteTextView) layout_destination
				.findViewById(R.id.txtFareEstimationRiderDestination);
		final PlacesAutoCompleteAdapter adapterDestination2 = new PlacesAutoCompleteAdapter(
				activity, R.layout.autocomplete_list_text);
		final MyFontTextView textActionType = (MyFontTextView) mydestinationDialog
				.findViewById(R.id.textActionType);

		Spinner spnPaymentType = (Spinner) mydestinationDialog
				.findViewById(R.id.spnPassengerType);
		edtcorporate_code = (MyFontEdittextView) mydestinationDialog
				.findViewById(R.id.edtcorporate_code);
		setSpinnerData(spnMusic, spnPassengerType, spnPaymentType);

		/* Set User Data */
		setData(imageProfile);
		labelCabType.setText(cartypename);

		/* Set Action Type */
		buttonCheckNewDestination.setText(ACTION_TYPE);

		/* Set Music Spinner Visibility */
		if (ACTION_TYPE == Const.NEW_DESTINATION_ACTION_TYPE) {
			spnMusic.setVisibility(View.INVISIBLE);
			textActionType.setText("Fare Estimate");
		} else {
			spnMusic.setVisibility(View.VISIBLE);
			textActionType.setText("Ride Now");
		}

		/* Set values from server */
		textBaseFare.setText("KES " + strMinFare);
		textChargePerKm.setText(String
				.valueOf("KES " + strPricePerUnitDistance));
		textPriceUnitTime.setText(String.valueOf("KES " + strPricePerUnitTime));

		/* Set adapter for destination autocomplete */
		destinationTextView.setAdapter(adapterDestination2);
		destinationTextView.setThreshold(1);
		riderSourceTextView.setText(etSource.getText().toString());

		/* OnItem click listener for @destinationTextView */
		/*
		 * mydestinationDialog.setOnKeyListener(new Dialog.OnKeyListener() {
		 * 
		 * @Override public boolean onKey(DialogInterface arg0, int keyCode,
		 * KeyEvent event) { if (keyCode == KeyEvent.KEYCODE_BACK) {
		 * AndyUtils.showToast("Use Cancel Button" + "", getActivity()); }
		 * return true; } });
		 */
		destinationTextView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final String selectedDestination = adapterDestination2
						.getItem(position);
				mypickupAddrs = riderSourceTextView.getText().toString();
				destinationTextView.setText(selectedDestination);
				InputMethodManager imm = (InputMethodManager) getActivity()
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						destinationTextView.getWindowToken(), 0);
				/*
				 * Save rider source and destination addresses to the
				 * SharedPreference
				 */
				preference.putRiderSourceAddress(riderSourceTextView.getText()
						.toString());
				preference.putRiderDestinationAddress(destinationTextView
						.getText().toString());

				corporate_reference = edtcorporate_code.getText().toString();
				new GetLocationFromAddress().execute(destinationTextView
						.getText().toString());
				AndyUtils.removeSimpleProgressDialog();
			}
		});
		if (preference.getIsCorporate()) {
			edtcorporate_code.setVisibility(View.VISIBLE);
			if (isRidingAsCorporate) {
				edtcorporate_code.setVisibility(View.VISIBLE);
			} else {
				edtcorporate_code.setVisibility(View.GONE);
			}
		} else {
			edtcorporate_code.setVisibility(View.GONE);
		}

		/* OnClick listner for @buttonCancel */
		buttonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mydestinationDialog.dismiss();
			}
		});

		buttonCheckNewDestination.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ACTION_TYPE == Const.NEW_DESTINATION_ACTION_TYPE) {
					// LatLng loc =
					// getAddressLatLang(destinationTextView.getText().toString());
					// AndyUtils.showToast(Double.toString(loc.latitude) +
					// Double.toString(loc.longitude), getActivity());
					destinationTextView.setText("");
					textTotalCharge.setText("KES 0  -  KES 0");
				} else {
					mydropAddrs = destinationTextView.getText().toString();
					myMusic = spnMusic.getSelectedItem().toString();
					Geocoder geocoder = new Geocoder(getActivity());
					List<Address> locationSource = null, locationDestination = null;
					try {
						locationSource = geocoder.getFromLocationName(
								riderSourceTextView.getText().toString(), 1);
						locationDestination = geocoder.getFromLocationName(
								destinationTextView.getText().toString(), 1);

					} catch (IOException e) {
						e.printStackTrace();
					}

					if (locationSource != null && locationSource.size() > 0) {
						LatLng originLatLng = new LatLng(locationSource.get(0)
								.getLatitude(), locationSource.get(0)
								.getLongitude());
						PreferenceHelper preferenceHelper = new PreferenceHelper(
								getActivity());
						preferenceHelper.save_MyOriginLocation(originLatLng);
						// preferenceHelper.save_TripStartedTime(System.currentTimeMillis());
					}

					// Get coordinates for the destination location
					if (locationDestination != null
							&& locationDestination.size() > 0) {
						LatLng destinationLatLng = new LatLng(
								locationDestination.get(0).getLatitude(),
								locationDestination.get(0).getLongitude());
						PreferenceHelper preferenceHelper = new PreferenceHelper(
								getActivity());
						preferenceHelper
								.save_MyDestinationLocation(destinationLatLng);
					}
					if (isRidingAsCorporate) {
						passengerType = "Corporate";
					} else {
						passengerType = "Individual";
					}

					corporatecode = edtcorporate_code.getText().toString();
					corporatename = preference.get_corporatename();
					if (TextUtils.isEmpty(mydropAddrs)) {
						AndyUtils.showToast("Please Enter Your Destination.",
								getActivity());
					}/*
					 * else if((passengerType.equalsIgnoreCase("corporate"))&&
					 * corporatecode .length()<1){ AndyUtils.showToast(
					 * "Please enter your Corporate code to continue",
					 * getActivity()); }
					 */else {
						// PreferenceHelper preferenceHelper = new
						// PreferenceHelper(getActivity());

						preference.save_taxiMaxSize(strMaxSize);
						preference.save_taxiMinFare(strMinFare);
						preference
								.save_taxiPricePerUnitTime(strPricePerUnitTime);
						preference
								.save_taxiPricePerUnitDistance(strPricePerUnitDistance);
						mydestinationDialog.dismiss();

						pickMeUp(mydropAddrs);
					}

				}

			}
		});
		mydestinationDialog.show();

	}

	private void setAnimatedDriverMarker(Marker moDriver, Double lati,
			Double longi, double bearing) {
		if (map != null && this.isVisible()) {
			// Marker markerDriver = map.addMarker(moDriver);
			if (moDriver == null) {
				Location driverLocation = new Location("");
				driverLocation.setLatitude(lati);
				driverLocation.setLongitude(longi);
				driverLocation.setBearing((float) bearing);
				LatLng latLng = new LatLng(lati, longi);
				animateMarker(moDriver, latLng, driverLocation, false);
			} else {
				Location driverLocation = new Location("");
				driverLocation.setLatitude(lati);
				driverLocation.setLongitude(longi);
				driverLocation.setBearing((float) bearing);
				LatLng latLng = new LatLng(lati, longi);
				animateMarker(moDriver, latLng, driverLocation, false);
			}
		}
	}

	private void setDriverMarker(MarkerOptions moDriver, LatLng latLng,
			double bearing) {
		if (latLng != null) {
			if (map != null && this.isVisible()) {
				Marker markerDriver = map.addMarker(moDriver);
				if (moDriver == null) {
					Location driverLocation = new Location("");
					driverLocation.setLatitude(latLng.latitude);
					driverLocation.setLongitude(latLng.longitude);
					driverLocation.setBearing((float) bearing);
					animateMarker(markerDriver, latLng, driverLocation, false);
					/*
					 * if (isCameraZoom) { animateCameraToMarker(latLng); }
					 */
				} else {
					Location driverLocation = new Location("");
					driverLocation.setLatitude(latLng.latitude);
					driverLocation.setLongitude(latLng.longitude);
					driverLocation.setBearing((float) bearing);
					animateMarker(markerDriver, latLng, driverLocation, false);
					/*
					 * if (isCameraZoom) { animateCameraToMarker(latLng); }
					 */
				}
				/*
				 * if (myMarker != null && myMarker.getPosition() != null)
				 * getDirectionsUrl(latLng, myMarker.getPosition());
				 */
			}
		}
	}

	private void setCorpSpinner(Spinner spn) {
		List<String> spinnerArray = new ArrayList<String>();
		spinnerArray.add("Craft");
		spinnerArray.add("BIDCO");
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, spinnerArray);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spn.setAdapter(adapter2);
	}

	// spnCorporateName

	private void setSpinnerData(Spinner spnMusic,
			final Spinner spnPassngerType, Spinner spnPaymentType) {
		List<String> spinnerArray2 = new ArrayList<String>();
		List<String> spinnerArrayPassenger = new ArrayList<String>();
		List<String> spinnerArrayPaymentType = new ArrayList<String>();

		spinnerArrayPassenger.add("Individual");
		spinnerArrayPassenger.add("Corporate / BIDCO");
		/*
		 * spinnerArray2.add("Kiss 100.3"); spinnerArray2.add("Classic FM");
		 * spinnerArray2.add("Homeboys 103.5");
		 * spinnerArray2.add("Ghetto Radio 89.5");
		 */

		PreferenceHelper preferenceHelper = new PreferenceHelper(activity);
		String[] availableStations = preferenceHelper.get_radioStation().split(
				",");
		spinnerArray2.clear();
		spinnerArray2.add("Any Radio Station");
		for (int i = 0; i < availableStations.length; i++) {
			spinnerArray2.add(availableStations[i]);
		}

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, spinnerArray2);
		adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spnMusic.setAdapter(adapter2);

		/*
		 * if (!(preference.get_corporatename().equals("0"))) {
		 * spnPassngerType.setVisibility(View.VISIBLE); ArrayAdapter<String>
		 * adapterPassenger = new ArrayAdapter<String>( getActivity(),
		 * android.R.layout.simple_spinner_item, spinnerArrayPassenger);
		 * adapterPassenger
		 * .setDropDownViewResource(R.layout.spinner_dropdown_item);
		 * spnPassngerType.setAdapter(adapterPassenger);
		 * 
		 * spnPassngerType.setOnItemSelectedListener(new
		 * AdapterView.OnItemSelectedListener() {
		 * 
		 * @Override public void onItemSelected(AdapterView<?> adapterView, View
		 * view, int position, long l) { if
		 * (spnPassngerType.getSelectedItem().toString
		 * ().equalsIgnoreCase("corporate")) {
		 * edtcorporate_code.setText(preference.get_corporatecode()); //
		 * edtcorporate_code.setVisibility(View.VISIBLE); isCorporateSelected =
		 * true; } else { edtcorporate_code.setText("0"); //
		 * edtcorporate_code.setVisibility(View.GONE); isCorporateSelected =
		 * false; } }
		 * 
		 * @Override public void onNothingSelected(AdapterView<?> adapterView) {
		 * 
		 * } }); } else { passengerType = "Individual";
		 * spnPassngerType.setVisibility(View.GONE); }
		 */
	}

	public static JSONObject getLocationInfo(String address) {
		StringBuilder stringBuilder = new StringBuilder();
		try {

			address = address.replaceAll(" ", "%20");

			HttpPost httppost = new HttpPost(
					"http://maps.google.com/maps/api/geocode/json?address="
							+ address + "&sensor=false");
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			stringBuilder = new StringBuilder();

			response = client.execute(httppost);
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			int b;
			while ((b = stream.read()) != -1) {
				stringBuilder.append((char) b);
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		}

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = new JSONObject(stringBuilder.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	public boolean getLatLong(JSONObject jsonObject) {

		try {

			toserverlongitute = ((JSONArray) jsonObject.get("results"))
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lng");

			toserverlatitude = ((JSONArray) jsonObject.get("results"))
					.getJSONObject(0).getJSONObject("geometry")
					.getJSONObject("location").getDouble("lat");

		} catch (JSONException e) {
			return false;

		}

		return true;
	}

}