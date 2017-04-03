package com.craftsilicon.littlecabrider.fragments;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.craftsilicon.littlecabrider.ActionBarBaseActivitiy;
import com.craftsilicon.littlecabrider.MainActivity;
import com.craftsilicon.littlecabrider.MainDrawerActivity;
import com.craftsilicon.littlecabrider.R;
import com.craftsilicon.littlecabrider.RegisterActivity;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextView;
import com.craftsilicon.littlecabrider.component.MyTitleFontTextView;
import com.craftsilicon.littlecabrider.db.DBHelper;
import com.craftsilicon.littlecabrider.models.User;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.parse.ParseContent;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.CountryDetails;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.listeners.OnProfileListener;

/**
 * @author Elluminati elluminati.in
 */
public class SignInFragment extends BaseFragmentRegister implements
		com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks,
		com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

	private MyFontEdittextView etEmail, etPassword, etForgetEmail;
	private MyTitleFontTextView txtGreeting, txtemail, txtpassword;
	private MyFontButton btnSignIn, btnBackSignIn, btnRegister;
	private ImageButton btnGPlus;
	private ImageButton btnFb;

	// Gplus
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private static final int RC_SIGN_IN = 0;
	private boolean mSignInClicked;
	Boolean isetLoginEmail = false, isetLoginPassword = false;

	// FB
	private SimpleFacebook mSimpleFacebook;

	private MyTitleFontTextView btnForgetPassowrd/* , tvRegisterAccount */;
	// private ParseContent pContent;
	// private ArrayList<String> list;
	private Dialog forgotPasswordDialog;
	private PreferenceHelper preferenceHelper;

	/* CountryDetails class variable */
	private CountryDetails country_details;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// activity.actionBar.hide();
		// activity.setStatusBarColor(getResources().getColor(
		// R.color.color_action_bar_main));
		// activity.getSupportActionBar().hide();
		Scope scope = new Scope("https://www.googleapis.com/auth/plus.login");
		// Scope scopePro = new
		// Scope("https://www.googleapis.com/auth/plus.me");
		mGoogleApiClient = new GoogleApiClient.Builder(activity).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, PlusOptions.builder().build()).addScope(scope)
				.build();
		preferenceHelper = new PreferenceHelper(activity);
		if (preferenceHelper.getIsLoggedIn()) {
			startActivity(new Intent(activity, MainDrawerActivity.class));
		}

		/* Instantiate CountryDetails */
		country_details = new CountryDetails();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	private String getGreetings() {
		String greeting = "";
		Calendar c = Calendar.getInstance();
		int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

		if (timeOfDay >= 0 && timeOfDay < 12) {
			greeting = "Good\nMorning!";
		} else if (timeOfDay >= 12 && timeOfDay < 16) {
			greeting = "Good\nAfternoon!";
		} else if (timeOfDay >= 16 && timeOfDay < 21) {
			greeting = "Good\nEvening!";
		} else if (timeOfDay >= 21 && timeOfDay < 24) {
			greeting = "Good\nEvening!";
		}
		if (greeting == null || greeting.length() < 2) {
			greeting = "Good Afternoon!";
		}
		return greeting;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// activity.setTitle(getResources().getString(R.string.text_signin));
		// activity.setIconMenu(R.drawable.taxi);
		View view = inflater.inflate(R.layout.login, container, false);

		etEmail = (MyFontEdittextView) view.findViewById(R.id.etEmail);
		txtemail = (MyTitleFontTextView) view.findViewById(R.id.txtemail);
		txtGreeting = (MyTitleFontTextView) view.findViewById(R.id.txtGreeting);
		LinearLayout llDriverDetail, llLiveFare;

		etPassword = (MyFontEdittextView) view.findViewById(R.id.etPassword);
		txtpassword = (MyTitleFontTextView) view.findViewById(R.id.txtPasword);

		btnSignIn = (MyFontButton) view.findViewById(R.id.btnSignIn);
		// btnGPlus = (ImageButton) view.findViewById(R.id.btnGplus);
		// btnFb = (ImageButton) view.findViewById(R.id.btnFb);
		btnForgetPassowrd = (MyTitleFontTextView) view.findViewById(R.id.btnForgetPassword);
		// tvRegisterAccount = (MyFontTextView) view
		// .findViewById(R.id.tvRegisterAccount);
		btnRegister = (MyFontButton) view.findViewById(R.id.btnRegister);
		// btnRegister = (MyFontButton) view.findViewById(R.id.btnRegister);
		btnForgetPassowrd.setOnClickListener(this);
		btnSignIn.setOnClickListener(this);
		btnSignIn.setOnClickListener(this);
		// btnBackSignIn.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		txtGreeting.setText(getGreetings());
		setTextWatcher(etEmail, txtemail);
		setTextWatcher(etPassword, txtpassword);

		return view;
	}

	private void setTextWatcher(final MyFontEdittextView edt, final TextView txt) {
		txt.setVisibility(View.INVISIBLE);
		edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = edt.getText().toString();
				boolean visibility = false;
				if (edt == etEmail) {
					visibility = isetLoginEmail;
				} else if (edt == etPassword) {
					visibility = isetLoginPassword;
				}
				if (text.length() > 0) {
					if (!visibility) {
						txt.setVisibility(View.VISIBLE);
						Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_top);
						txt.startAnimation(animation);
						visibility = true;
					}
				} else {
					Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_bottom);
					animation.setAnimationListener(new Animation.AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							txt.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}
					});
					txt.startAnimation(animation);
					visibility = false;

				}
				if (edt == etEmail) {
					isetLoginEmail = visibility;
				} else if (edt == etPassword) {
					isetLoginPassword = visibility;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	@Override
	public void onResume() {

		super.onResume();
		activity.actionBar.hide();
		activity.currentFragment = Const.FRAGMENT_SIGNIN;
		activity.actionBar.setTitle(getString(R.string.text_signin_small));
		// mSimpleFacebook = SimpleFacebook.getInstance(activity);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnSignIn:
			if (isValidate()) {
				login();
			}

			break;
		case R.id.btnForgetPassword:
			showForgotPasswordDialog();
			break;

		/*
		 * case R.id.btnRegister: goToRegisterFragment(); break;
		 */

		case R.id.btnRegister:
			goToRegisterFragment();
			break;

		case R.id.tvForgetSubmit:
			if (etForgetEmail.getText().length() == 0) {
				AndyUtils.showToast(getResources().getString(R.string.text_enter_email), activity);
				return;
			} else if (!AndyUtils.eMailValidation(etForgetEmail.getText().toString())) {
				AndyUtils.showToast(getResources().getString(R.string.text_enter_valid_email), activity);
				return;
			} else {
				if (!AndyUtils.isNetworkAvailable(activity)) {
					AndyUtils.showToast(getResources().getString(R.string.dialog_no_inter_message), activity);
					return;
				}
				forgetPassowrd();
			}
			break;
		default:
			break;
		}
	}

	private void getProfile() {
		AndyUtils.showCustomProgressDialog(activity, getString(R.string.text_getting_info), true, null);

		mSimpleFacebook.getProfile(new OnProfileListener() {
			@Override
			public void onComplete(Profile profile) {
				AndyUtils.removeCustomProgressDialog();
				Log.i("Uber", "My profile id = " + profile.getId());
				btnGPlus.setEnabled(false);
				btnFb.setEnabled(false);
				loginSocial(profile.getId(), Const.SOCIAL_FACEBOOK);
			}
		});
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress) {
			mConnectionResult = result;

			if (mSignInClicked) {
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		AndyUtils.removeCustomProgressDialog();
		mSignInClicked = false;
		btnGPlus.setEnabled(false);

		Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

		loginSocial(currentPerson.getId(), Const.SOCIAL_GOOGLE);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	private void resolveSignInError() {

		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				activity.startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN,
						null, 0, 0, 0, Const.FRAGMENT_SIGNIN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RC_SIGN_IN) {

			if (resultCode != Activity.RESULT_OK) {
				mSignInClicked = false;
				AndyUtils.removeCustomProgressDialog();
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else {

			mSimpleFacebook.onActivityResult(activity, requestCode, resultCode, data);
			if (mSimpleFacebook.isLogin()) {
				getProfile();
			} else {
				Toast.makeText(activity, "facebook login failed", Toast.LENGTH_SHORT).show();
			}

			super.onActivityResult(requestCode, resultCode, data);

		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {

	}

	@Override
	protected boolean isValidate() {
		String msg = null;
		if (TextUtils.isEmpty(etEmail.getText().toString())) {
			msg = getResources().getString(R.string.text_enter_email);
		} else if (TextUtils.isEmpty(etEmail.getText().toString())) {

			msg = "Please enter your mobile number";
		}
		if (TextUtils.isEmpty(etPassword.getText().toString())) {
			msg = getResources().getString(R.string.text_enter_password);
		}
		if (msg == null)
			return true;

		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		return false;
	}

	private String formatPhoneNumber(String phone_number) {
		String initialPhoneNumber = etEmail.getText().toString();
		String formattedPhoneNumber = "254";
		if (initialPhoneNumber.startsWith("0")) {
			formattedPhoneNumber = initialPhoneNumber.replaceFirst("0", "254");
		} else {
			return initialPhoneNumber;
		}
		return formattedPhoneNumber;
	}

	private void login() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet), activity);
			return;
		}
		AndyUtils.showCustomProgressDialog(activity, getResources().getString(R.string.text_signing), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		StringBuilder stringBuilder_login = new StringBuilder();
		stringBuilder_login.append("FORMID|LOGIN|");
		stringBuilder_login.append("MOBILENUMBER|" + formatPhoneNumber(etEmail.getText().toString()) + "|");
		stringBuilder_login.append("PIN|" + etPassword.getText().toString() + "|");
		stringBuilder_login.append("DEVICETYPE|" + Const.DEVICE_TYPE_ANDROID + "|");
		stringBuilder_login.append("DEVICETOKEN|" + new PreferenceHelper(activity).getDeviceToken() + "|");
		stringBuilder_login.append("LOGINBY|" + Const.MANUAL + "|");

		String encrypted_login_details = PreferenceHelper.eaes(stringBuilder_login.toString());
		map.put("PLAIN_DATA", stringBuilder_login.toString());
		map.put("DATA", encrypted_login_details);

		AppLog.Log("mylogin", "ecrypted_none::: " + stringBuilder_login.toString());
		AppLog.Log("mylogin", "ecrypted::: " + encrypted_login_details);
		AppLog.Log("mylogin", "decrypted::: " + PreferenceHelper.daes(encrypted_login_details));
		new HttpRequester(activity, map, Const.ServiceCode.LOGIN, this);
	}

	/**
	 * GET PAYMENT ACCOUNTS & CARDS FOR THE USER
	 * 
	 * @param phone_number
	 */
	private void getCards(String phone_number) {
		HashMap<String, String> map = new HashMap<String, String>();
		String unique_id = UUID.randomUUID().toString();
		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		map.put(Const.Params.ID, new PreferenceHelper(getActivity()).getUserId());
		map.put(Const.Params.TOKEN, new PreferenceHelper(getActivity()).getSessionToken());

		StringBuilder sb = new StringBuilder();
		sb.append("FORMID|GETACCOUNTS|");
		sb.append("MOBILENUMBER|" + phone_number + "|");
		sb.append("UNIQUEID|" + unique_id.toString() + "|");
		sb.append("IMEI|" + new PreferenceHelper(getActivity()).getIMEI() + "|");
		String encryptedc1 = PreferenceHelper.eaes(sb.toString());
		map.put("PLAIN_DATA", sb.toString());
		map.put("DATA", encryptedc1);

		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasent:" + sb.toString());
		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasentencrypted:" + encryptedc1);
		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasentdecrypted:" + PreferenceHelper.daes(encryptedc1));
		new HttpRequester(getActivity(), map, Const.ServiceCode.GET_CARD, this);
	}

	private void loginSocial(String id, String loginType) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet), activity);
			return;
		}
		AndyUtils.showCustomProgressDialog(activity, getResources().getString(R.string.text_signin), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.LOGIN);
		map.put(Const.Params.SOCIAL_UNIQUE_ID, id);
		map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
		map.put(Const.Params.DEVICE_TOKEN, new PreferenceHelper(activity).getDeviceToken());
		map.put(Const.Params.LOGIN_BY, loginType);
		new HttpRequester(activity, map, Const.ServiceCode.LOGIN, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.LOGIN, this, this));
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		ParseContent parseContent = new ParseContent(activity);
		AndyUtils.removeCustomProgressDialog();
		super.onTaskCompleted(response, serviceCode);

		switch (serviceCode) {
		case Const.ServiceCode.LOGIN:
			String decrypted_login_response = PreferenceHelper.daes(response);
			AppLog.Log("mylogin", "response::: " + decrypted_login_response);

			/* Process the response from the server */
			AndyUtils.processTags(decrypted_login_response + "|");

			/* Log the login status code from the server */
			String login_status = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"STATUS");

			/* Log the login message from the server */
			String login_message = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"MESSAGE");

			String session_id = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"SESSIONID");

			String full_name = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "NAME");

			String email_address = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"EMAIL");

			String phone_number = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"MOBILENUMBER");

			String picture = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "PICTURE");

			/* if the login is successful */
			if (login_status.equals("000")) {
				/* Save phone number and session_id in the shared preference */
				preferenceHelper.putPhoneNumber(formatPhoneNumber(etEmail.getText().toString()));
				preferenceHelper.putSessionToken(session_id);

				/* Reload cards and bank accounts for the client */
				getCards(preferenceHelper.getPhoneNumber());

				/* Save user password in the SharedPreference */
				new PreferenceHelper(activity).putPassword(etPassword.getText().toString());

				/* Save user details in the database */
				storeUserDetials(full_name, picture, email_address, phone_number);

				if (isAdded()) {
					startActivity(new Intent(activity, MainDrawerActivity.class));
					activity.finish();
				}

				preferenceHelper.putIsLoggedIn(true);

			} else {
				AndyUtils.showToast(login_message.toString(), getActivity());
				preferenceHelper.putIsLoggedIn(false);
			}

			AppLog.Log("mylogin", "response_phone_number::: " + preferenceHelper.getPhoneNumber());
			AppLog.Log("mylogin", "response_status::: " + login_status);
			AppLog.Log("mylogin", "response_message::: " + login_message);
			AppLog.Log("mylogin", "response_session_id::: " + preferenceHelper.getSessionToken());

			break;

		case Const.ServiceCode.FORGET_PASSWORD:
			AppLog.Log("TAG", "forget res:" + response);
			if (new ParseContent(activity).isSuccess(response)) {
				AndyUtils.showToast(getResources().getString(R.string.toast_forget_password_success), activity);
				forgotPasswordDialog.dismiss();
			} else {
				AndyUtils.showToast(getResources().getString(R.string.toast_email_ivalid), activity);
			}
			break;

		case Const.ServiceCode.GET_CARD:
			/**
			 * Logging Response From Elma's Card Service
			 */
			String decrypted_response = PreferenceHelper.daes(response);
			String changed_response;
			if (decrypted_response.startsWith("|")) {
				AppLog.Log("myresponsepipe", "YES");
				changed_response = decrypted_response.substring(1);
				AppLog.Log("myresponsepipe", changed_response);

			} else {
				changed_response = decrypted_response;
			}

			AndyUtils.processTags(changed_response + "|");

			String card_status = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"STATUS");
			String cards = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "CARDS");
			String banks = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "BANKS");

			/* Save Banks To SharedPreference */
			preferenceHelper.putPaymentCards(cards);
			preferenceHelper.putPaymentBanks(banks);
			AppLog.Log("mybanks", preferenceHelper.getPaymentBanks());

		default:
			break;
		}

	}

	/**
	 * Saves user details in the database
	 * 
	 * @param full_name
	 * @param picture
	 * @param email_address
	 * @param phone_number
	 */
	private void storeUserDetials(String full_name, String picture, String email_address, String phone_number) {
		User user = null;
		try {
			user = new User();
			DBHelper dbHelper = new DBHelper(getActivity());
			user.setFname(full_name);
			user.setEmail(email_address);
			user.setPicture(picture);
			user.setContact(phone_number);
			dbHelper.createUser(user);
		} catch (Exception exception) {

		}

	}

	@Override
	public boolean OnBackPressed() {
		// activity.removeAllFragment(new UberMainFragment(), false,
		// Const.FRAGMENT_MAIN);
		activity.finish();
		return false;
	}

	/**
	 * Returns the country based on the country code that precedes phone number
	 * 
	 * @param phone_number
	 */
	private void getCountry(String phone_number) {

		try {
			/* Get a list of all countries */
			int country_code_index = -1;
			phone_number = preferenceHelper.getPhoneNumber();
			String country_code = phone_number.substring(0, 3);
			List<String> country_names = Arrays.asList(getResources().getStringArray(R.array.countries_array));
			List<String> country_codes = Arrays.asList(getResources().getStringArray(R.array.country_codes));

			country_code_index = country_codes.indexOf(country_code);
			String country = country_names.get(country_code_index);

			/* Store rider's country of origin in the shared preference */
			preferenceHelper.putRiderCountry(country);
			AppLog.Log("countrycode", "responseindex:: " + country_code_index);
			AppLog.Log("countrycode", "responsecountry:: " + preferenceHelper.getRiderCountry());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */

	private void showForgotPasswordDialog() {
		forgotPasswordDialog = new Dialog(getActivity());
		forgotPasswordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgotPasswordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		forgotPasswordDialog.setContentView(R.layout.forget_pass_fragment);
		forgotPasswordDialog.setCancelable(true);
		etForgetEmail = (MyFontEdittextView) forgotPasswordDialog.findViewById(R.id.etForgetEmail);
		forgotPasswordDialog.findViewById(R.id.tvForgetSubmit).setOnClickListener(this);
		etForgetEmail.requestFocus();
		activity.showKeyboard(etForgetEmail);
		forgotPasswordDialog.show();
	}

	private void forgetPassowrd() {
		AndyUtils.showCustomProgressDialog(activity, getString(R.string.text_forget_password_loading_msg), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.FORGET_PASSWORD);
		map.put(Const.Params.TYPE, Const.Params.OWNER);
		map.put(Const.Params.EMAIL, etForgetEmail.getText().toString());
		new HttpRequester(activity, map, Const.ServiceCode.FORGET_PASSWORD, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.FORGET_PASSWORD, this, this));
	}

	private void goToRegisterFragment() {
		// AppLog.Log("registerfragment", "RegisterFragment");
		// return new RegisterFragment();

		Intent startRegisterActivity = new Intent(getActivity(), RegisterActivity.class);
		startRegisterActivity.putExtra("isSignin", false);
		startActivity(startRegisterActivity);
		getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

}
