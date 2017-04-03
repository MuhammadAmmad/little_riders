package com.craftsilicon.littlecabrider.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextViewMedium;
import com.craftsilicon.littlecabrider.parse.AsyncTask_BulletProof;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.parse.MultiPartRequester;
import com.craftsilicon.littlecabrider.parse.ParseContent;
import com.craftsilicon.littlecabrider.parse.ParseContentKamal;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.Plus.PlusOptions;
import com.google.android.gms.plus.model.people.Person;
import com.craftsilicon.littlecabrider.R;
import com.soundcloud.android.crop.Crop;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.entities.Profile;
import com.sromku.simple.fb.entities.Profile.Properties;
import com.sromku.simple.fb.listeners.OnProfileListener;

/**
 * @author Elluminati elluminati.in
 */
public class RegisterFragment extends BaseFragmentRegister implements
		com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks,
		com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener {

	private Button btnNext, btnExit;
	private ImageButton btnGPlush, btnFb;
	private MyFontEdittextView etFullName, etEmail, etPassword, etConfirmPassword, etNumber, etID;
	private ImageView ivProPic, ivPassword;
	private MyFontTextViewMedium tvPassword, tvConfirmPassword;
	private Spinner spinnerIDType;

	// Gplus
	private ConnectionResult mConnectionResult;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private static final int RC_SIGN_IN = 0;
	private boolean mSignInClicked;
	private Uri uri = null;
	private String filePath = null, profileImageFilePath;
	private SimpleFacebook mSimpleFacebook;
	private TextView spCCode;
	private ParseContent pContent;
	private ParseContentKamal pContentKamal;
	private String type = Const.MANUAL;
	private String socialId;
	private String socialUrl;
	ArrayList<String> list;
	private String country;
	private PopupWindow registerInfoPopup;
	private int rotationAngle;
	private Bitmap photoBitmap;
	// private MyFontTextView tvPopupMsg;
	MyFontTextViewMedium txtid, txtNumber;
	ImageView btnClickPhoto, btnPhotoFromGalary;
	Permission[] facebookPermission = new Permission[] { Permission.EMAIL };
	private SimpleFacebookConfiguration facebookConfiguration;
	private MyFontTextViewMedium txtFname, txtEmail;
	Boolean isetLoginEmail = false, isetLoginPassword = false, isetConfirmPassword = false, issetFullName = false,
			isetetID = false, isetetNumber = false;
	PreferenceHelper am;
	Boolean isElmaUser = false;
	boolean testingSecondTime = false;
	/**
	 * OTP Variables
	 */
	public static String first_passcode, second_passcode;
	public static MyFontEdittextView textbox1, textbox2;
	EditText edtRecreateKey;
	Dialog mydestinationDialog;
	TextView txtRecreateKey, txtResendRecreateKey, txtSkipVerification, txtUserMessage;
	String thesmsps = "";

	// private ImageView btnRegisterEmailInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// activity.actionBar.show();

		activity.actionBar.hide();
		Scope scope = new Scope("https://www.googleapis.com/auth/plus.login");
		// Scope scopePro = new
		// Scope("https://www.googleapis.com/auth/plus.me");
		am = new PreferenceHelper(activity);
		mGoogleApiClient = new GoogleApiClient.Builder(activity).addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API, PlusOptions.builder().build()).addScope(scope)
				.build();
		country = Locale.getDefault().getDisplayCountry();

		facebookConfiguration = new SimpleFacebookConfiguration.Builder()
				.setAppId(getResources().getString(R.string.applicationId))
				.setNamespace(getResources().getString(R.string.app_name)).setPermissions(facebookPermission).build();
		SimpleFacebook.setConfiguration(facebookConfiguration);
		listencode();

	}

	private void getViews(View view) {
		spinnerIDType = (Spinner) view.findViewById(R.id.spinnerIdType);
		etPassword = (MyFontEdittextView) view.findViewById(R.id.etRegisterPassword);
		etConfirmPassword = (MyFontEdittextView) view.findViewById(R.id.etConfirmPassword);
		tvPassword = (MyFontTextViewMedium) view.findViewById(R.id.tvRegisterPassword);
		tvConfirmPassword = (MyFontTextViewMedium) view.findViewById(R.id.tvConfirmPassword);
		ivPassword = (ImageView) view.findViewById(R.id.ivPassword);
		etFullName = (MyFontEdittextView) view.findViewById(R.id.etRegisterFName);
		txtFname = (MyFontTextViewMedium) view.findViewById(R.id.txtfname);
		etEmail = (MyFontEdittextView) view.findViewById(R.id.etRegisterEmail);
		txtEmail = (MyFontTextViewMedium) view.findViewById(R.id.txtEmail);
		etNumber = (MyFontEdittextView) view.findViewById(R.id.etRegisterNumber);
		txtNumber = (MyFontTextViewMedium) view.findViewById(R.id.txtRegisterNumber);
		etID = (MyFontEdittextView) view.findViewById(R.id.etIDNumber);
		txtid = (MyFontTextViewMedium) view.findViewById(R.id.txtidNumber);
		// etZipCode = (MyFontEdittextView) view.findViewById(R.id.etZipCode);
		// etAddress = (MyFontEdittextView) view.findViewById(R.id.etAddress);
		ivProPic = (ImageView) view.findViewById(R.id.image_camera);
		spCCode = (TextView) view.findViewById(R.id.etCountryCode);
		btnClickPhoto = (ImageView) view.findViewById(R.id.image_camera);
		// setData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		activity.setTitle(getResources().getString(R.string.text_register));
		activity.setIconMenu(R.drawable.close_payment);
		activity.btnActionMenu.setOnClickListener(this);
		activity.btnNotification.setVisibility(View.GONE);
		View view = inflater.inflate(R.layout.layout_registration, container, false);
		btnNext = (Button) view.findViewById(R.id.button_register);
		btnExit = (Button) view.findViewById(R.id.button_exit);
		getViews(view);
		// btnRegisterEmailInfo.setOnClickListener(this);

		spCCode.setOnClickListener(this);
		btnClickPhoto.setOnClickListener(this);
		// btnPhotoFromGalary.setOnClickListener(this);
		// btnGPlush.setOnClickListener(this);
		// btnFb.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnExit.setOnClickListener(this);
		setTextWatcher(etFullName, txtFname);
		setTextWatcher(etEmail, txtEmail);
		setTextWatcher(etPassword, tvPassword);
		setTextWatcher(etNumber, txtNumber);
		setTextWatcher(etID, txtid);
		setTextWatcher(etConfirmPassword, tvConfirmPassword);

		/**
		 * Spinner Selected Item Listener
		 */
		spinnerIDType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				// national alien military diplomatic passport
				String idtype = spinnerIDType.getSelectedItem().toString();
				if (idtype.contains("National")) {
					// etID.setInputType(InputType.TYPE_CLASS_TEXT |
					// InputType.TYPE_TEXT_VARIATION_NORMAL);
					etID.setInputType(InputType.TYPE_CLASS_NUMBER);
				}
				if (idtype.contains("Alien")) {
					etID.setInputType(InputType.TYPE_CLASS_NUMBER);
				} else if (idtype.contains("Military")) {
					etID.setInputType(InputType.TYPE_CLASS_TEXT);
				} else if (idtype.contains("Diplomatic")) {
					etID.setInputType(InputType.TYPE_CLASS_TEXT);
				} else if (idtype.contains("Passport")) {
					etID.setInputType(InputType.TYPE_CLASS_TEXT);
				}

				txtid.setText(idtype);
				etID.setText("");
				etID.setHint("Enter your " + idtype);
				/*
				 * switch (position) { case 0:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; case 1:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; case 2:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; case 3:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; case 4:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; default:
				 * txtid.setText(spinnerIDType.getSelectedItem().toString());
				 * etID.setHint(spinnerIDType.getSelectedItem().toString());
				 * break; }
				 */

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		/**
		 * Focus Listener
		 */
		etNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					spCCode.setVisibility(View.VISIBLE);
					txtNumber.setVisibility(View.VISIBLE);
					etNumber.setHint("");
					Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_top);
					etNumber.startAnimation(animation);
				} else {
					if (etNumber.getText().length() < 1) {
						spCCode.setVisibility(View.GONE);
						etNumber.setHint("Mobile Number");
						Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_to_bottom);
						animation.setAnimationListener(new Animation.AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {

							}

							@Override
							public void onAnimationRepeat(Animation animation) {

								txtNumber.setVisibility(View.INVISIBLE);

							}

							@Override
							public void onAnimationEnd(Animation animation) {

							}
						});
						txtNumber.startAnimation(animation);
						txtNumber.setVisibility(View.INVISIBLE);
					}

				}
			}
		});

		return view;
	}

	private void enterOTPDialog() {
		asyncExecute("GETAUTHSMS", "GETAUTHSMS", "GETAUTHSMS");
		mydestinationDialog.setContentView(R.layout.activity_verifyaccount);
		edtRecreateKey = (EditText) mydestinationDialog.findViewById(R.id.edtRecreateKey);
		txtRecreateKey = (TextView) mydestinationDialog.findViewById(R.id.txtRecreateKey);
		txtUserMessage = (TextView) mydestinationDialog.findViewById(R.id.txtUserMessage);
		txtResendRecreateKey = (TextView) mydestinationDialog.findViewById(R.id.txtResendRecreateKey);
		txtSkipVerification = (TextView) mydestinationDialog.findViewById(R.id.txtSkipVerification);

		txtUserMessage.setVisibility(View.GONE);
		txtResendRecreateKey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				edtRecreateKey.setText("");
				txtRecreateKey.setText("WAITING..");
				String FORMID = "GETAUTHSMS";
				String FORMNAME = "GETAUTHSMS";
				asyncExecute(FORMID, FORMNAME, "GETAUTHSMS");
			}
		});

		txtSkipVerification.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mydestinationDialog != null) {
					if (mydestinationDialog.isShowing()) {
						mydestinationDialog.dismiss();
						registerWithoutVerification(Const.MANUAL);
					}
				}
			}

		});
		txtRecreateKey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				String rkey = edtRecreateKey.getText().toString();
				if (rkey.length() < 6) {
					AndyUtils.showToast("Please enter a valid key", activity);
				} else if (rkey.equals("bestApp")) {
					if (mydestinationDialog != null) {
						if (mydestinationDialog.isShowing()) {
							mydestinationDialog.dismiss();
							register(type, socialId);
						}
					}
				} else {
					isElmaUser = false;
					verify(isElmaUser);
				}
				/*
				 * edtRecreateKey.setText("");
				 * txtRecreateKey.setText("WAITING.."); String FORMID
				 * ="GETAUTHSMS"; String FORMNAME = "GETAUTHSMS";
				 * asyncExecute(FORMID, FORMNAME, "GETAUTHSMS");
				 */
			}
		});
		mydestinationDialog.show();
	}

	private void asyncExecute(String FORMID, String FORMNAME, String MethodName) {
		if (AndyUtils.isNetworkAvailable(activity)) {

			String imei = new PreferenceHelper(getActivity()).getIMEI();
			if (imei.length() < 1) {
				TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(
						Context.TELEPHONY_SERVICE);
				imei = telephonyManager.getDeviceId();
			}
			String serverString = "";
			serverString = "FORMID|" + FORMID + "|MOBILENUMBER|" + spCCode.getText().toString().trim().replace("+", "")
					+ etNumber.getText().toString()
					// + "|IMEI|5649526526526"
					+ "|IMEI|" + imei + "|CODEBASE|ANDROID" + "|COUNTRY|KENYA|";
			AndyUtils.log("serverString: ", serverString);
			try {
				serverString = PreferenceHelper.eaes(serverString);
				// Base64.encodeBytes(serverString.getBytes());
			} catch (Exception ex) {
				AndyUtils.log("Base64String: ", ex.getMessage().toString());
			}
			AndyUtils.log("todecrypt-encrpyt", "" + serverString);
			AndyUtils.log("todecrypt-decrpyt", "" + PreferenceHelper.daes(serverString));
			// serverString = AndyUtils.urlEncode(serverString);
			new AsyncTask_BulletProof(getActivity()).execute("RegisterFragment", serverString, MethodName);
		} else {
			AndyUtils.showToast("Network not available", activity);
		}
	}

	private void setTextWatcher(final MyFontEdittextView edt, final MyFontTextViewMedium txt) {
		// MyFontEdittextView
		txt.setVisibility(View.INVISIBLE);
		edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String text = edt.getText().toString();
				boolean visibility = false;
				if (edt == etFullName) {
					visibility = issetFullName;
					// } else if (edt == etLName) {
					// visibility = issetLname;
				} else if (edt == etEmail) {
					visibility = isetLoginEmail;
				} else if (edt == etPassword) {
					visibility = isetLoginPassword;
				} else if (edt == etConfirmPassword) {
					visibility = isetConfirmPassword;
				} else if (edt == etID) {
					visibility = isetetID;
				} else if (edt == etNumber) {
					visibility = isetetNumber;
				}
				// etNumber
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
				} else if (edt == etConfirmPassword) {
					isetConfirmPassword = visibility;
				} else if (edt == etFullName) {
					issetFullName = visibility;
				} else if (edt == etID) {
					isetetID = visibility;
				} else if (edt == etNumber) {
					isetetNumber = visibility;
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

	@SuppressWarnings("deprecation")
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		pContent = new ParseContent(activity);
		pContentKamal = new ParseContentKamal(activity);
		AppLog.Log(Const.TAG, country);
		list = pContent.parseCountryCodes();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(country)) {
				spCCode.setText((list.get(i).substring(0, list.get(i).indexOf(" "))));
			}
		}
		if (TextUtils.isEmpty(spCCode.getText())) {
			spCCode.setText((list.get(0).substring(0, list.get(0).indexOf(" "))));
		}
		// popup
		LayoutInflater inflate = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout) inflate.inflate(R.layout.popup_info_window, null);
		// tvPopupMsg = (MyFontTextView) layout.findViewById(R.id.tvPopupMsg);
		registerInfoPopup = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		layout.setOnClickListener(this);
		registerInfoPopup.setBackgroundDrawable(new BitmapDrawable());
		registerInfoPopup.setOutsideTouchable(true);

	}

	@Override
	public void onResume() {
		activity.currentFragment = Const.FRAGMENT_REGISTER;
		super.onResume();
		activity.actionBar.hide();
		// activity.actionBar.setTitle(getString(R.string.text_register_small));
		// mSimpleFacebook = SimpleFacebook.getInstance(activity);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.button_register:

			if (AndyUtils.isNetworkAvailable(activity)) {
				if (isValidate()) {
					// register(type, socialId);
					mydestinationDialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
					if (mydestinationDialog != null) {
						if (!(mydestinationDialog.isShowing())) {
							enterOTPDialog();
						}
					}
				}
			} else {
				AndyUtils.showToast("Network not available", activity);
			}

			break;
		case R.id.button_exit:
			OnBackPressed();
			break;

		case R.id.image_camera:
			// takePhotoFromCamera();
			showPictureDialog();
			break;
		//
		// case R.id.btnPhotoFromGalary:
		// choosePhotoFromGallary();
		// break;

		case R.id.etCountryCode:
			AlertDialog.Builder countryBuilder = new Builder(activity);
			countryBuilder.setTitle("Country codes");

			final String[] array = new String[list.size()];
			list.toArray(array);
			countryBuilder.setItems(array, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					spCCode.setText(array[which].substring(0, array[which].indexOf(" ")));
				}
			}).show();
			break;
		case R.id.btnActionMenu:
			OnBackPressed();
			break;

		default:
			break;
		}
	}

	// private void showPictureDialog() {
	// AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
	// dialog.setTitle(getString(R.string.text_choosepicture));
	// String[] items = { getString(R.string.text_gallary),
	// getString(R.string.text_camera) };
	//
	// dialog.setItems(items, new DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// switch (which) {
	// case 0:
	// choosePhotoFromGallary();
	// break;
	// case 1:
	// takePhotoFromCamera();
	// break;
	//
	// }
	// }
	// });
	// dialog.show();
	// }

	private void resolveSignInError() {

		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				activity.startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(), RC_SIGN_IN,
						null, 0, 0, 0, Const.FRAGMENT_REGISTER);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the
			// user clicks
			// 'sign-in'.

			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all

				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		AndyUtils.removeCustomProgressDialog();
		mSignInClicked = false;
		btnGPlush.setEnabled(false);
		btnFb.setEnabled(false);
		String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
		Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

		String personName = currentPerson.getDisplayName();

		String personPhoto = currentPerson.getImage().toString();
		socialId = currentPerson.getId();
		// etPassword.setEnabled(false);
		etPassword.setVisibility(View.GONE);
		tvPassword.setVisibility(View.GONE);
		ivPassword.setVisibility(View.GONE);
		etEmail.setText(email);
		type = Const.SOCIAL_GOOGLE;
		// etFName.setText(personName);
		if (personName.contains(" ")) {
			String[] split = personName.split(" ");
			etFullName.setText(split[0]);
			// etLName.setText(split[1]);
		} else {
			etFullName.setText(personName);
		}
		if (!TextUtils.isEmpty(personPhoto) || !personPhoto.equalsIgnoreCase("null")) {
			try {
				AppLog.Log("Person photo-----", personPhoto + "-----");
				socialUrl = new JSONObject(personPhoto).getString("url");
				AppLog.Log("Person photo-----", socialUrl + "-----");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			AQuery aQuery = new AQuery(activity);
			aQuery.id(ivProPic).image(socialUrl, getAqueryOption());
		} else {
			socialUrl = null;
		}

	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RC_SIGN_IN:
			if (resultCode != Activity.RESULT_OK) {
				mSignInClicked = false;
				AndyUtils.removeCustomProgressDialog();
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
			break;

		default:
			mSimpleFacebook.onActivityResult(activity, requestCode, resultCode, data);
			if (mSimpleFacebook.isLogin()) {
				getProfile();
			} else {
				Toast.makeText(activity, "facebook login failed", Toast.LENGTH_SHORT).show();
			}

			super.onActivityResult(requestCode, resultCode, data);
			break;
		case Const.CHOOSE_PHOTO:
			if (data != null) {
				Uri uri = data.getData();

				AppLog.Log("RegisterFragment", "Choose photo on activity result");

				profileImageFilePath = getRealPathFromURI(uri);
				filePath = profileImageFilePath;
				try {
					int mobile_width = 480;
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(filePath, options);
					int outWidth = options.outWidth;
					int ratio = (int) ((((float) outWidth) / mobile_width) + 0.5f);

					if (ratio == 0) {
						ratio = 1;
					}
					ExifInterface exif = new ExifInterface(filePath);

					String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
					int orientation = orientString != null ? Integer.parseInt(orientString)
							: ExifInterface.ORIENTATION_NORMAL;

					if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
						rotationAngle = 90;
					if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
						rotationAngle = 180;
					if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
						rotationAngle = 270;

					System.out.println("Rotation : " + rotationAngle);

					options.inJustDecodeBounds = false;
					options.inSampleSize = ratio;

					photoBitmap = BitmapFactory.decodeFile(filePath, options);
					if (photoBitmap != null) {
						Matrix matrix = new Matrix();
						matrix.setRotate(rotationAngle, (float) photoBitmap.getWidth() / 2,
								(float) photoBitmap.getHeight() / 2);
						photoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0, photoBitmap.getWidth(),
								photoBitmap.getHeight(), matrix, true);

						AppLog.Log("RegisterFragment", "Take photo on activity result");
						String path = Images.Media.insertImage(activity.getContentResolver(), photoBitmap, Calendar
								.getInstance().getTimeInMillis() + ".jpg", null);

						beginCrop(Uri.parse(path));

					}
				} catch (OutOfMemoryError e) {
					System.out.println("out of bound");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			else {
				Toast.makeText(activity, getResources().getString(R.string.toast_unable_to_selct_image),
						Toast.LENGTH_LONG).show();
			}
			break;
		case Const.TAKE_PHOTO:
			if (uri != null) {
				String imageFilePath = uri.getPath();
				if (imageFilePath != null && imageFilePath.length() > 0) {
					// File myFile = new File(imageFilePath);
					try {
						// if (bmp != null)
						// bmp.recycle();
						int mobile_width = 480;
						BitmapFactory.Options options = new BitmapFactory.Options();
						// options.inJustDecodeBounds = true;
						// BitmapFactory.decodeFile(imageFilePath, options);
						int outWidth = options.outWidth;
						// int outHeight = options.outHeight;
						int ratio = (int) ((((float) outWidth) / mobile_width) + 0.5f);

						if (ratio == 0) {
							ratio = 1;
						}
						ExifInterface exif = new ExifInterface(imageFilePath);

						String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
						int orientation = orientString != null ? Integer.parseInt(orientString)
								: ExifInterface.ORIENTATION_NORMAL;
						System.out.println("Orientation : " + orientation);
						if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
							rotationAngle = 90;
						if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
							rotationAngle = 180;
						if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
							rotationAngle = 270;

						System.out.println("Rotation : " + rotationAngle);

						options.inJustDecodeBounds = false;
						options.inSampleSize = ratio;

						Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, options);
						File myFile = new File(imageFilePath);
						// bmp = new ImageHelper().decodeFile(myFile);
						FileOutputStream outStream = new FileOutputStream(myFile);
						if (bmp != null) {
							bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
							outStream.flush();
							outStream.close();

							Matrix matrix = new Matrix();
							matrix.setRotate(rotationAngle, (float) bmp.getWidth() / 2, (float) bmp.getHeight() / 2);

							bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

							// ivStuffPicture.setImageBitmap(bmp);

							String path = Images.Media.insertImage(activity.getContentResolver(), bmp, Calendar
									.getInstance().getTimeInMillis() + ".jpg", null);
							beginCrop(Uri.parse(path));
						}
						// AQuery aQuery = new AQuery(this);
						// aQuery.id(ivProfile).image(bmp);
						//
						// String filePath = imageFilePath;
						// rlDescription.setVisibility(View.VISIBLE);
						// llPicture.setVisibility(View.GONE);
					} catch (OutOfMemoryError e) {
						System.out.println("out of bound");
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				Toast.makeText(activity, getResources().getString(R.string.toast_unable_to_selct_image),
						Toast.LENGTH_LONG).show();
			}
			break;
		case Crop.REQUEST_CROP:

			AppLog.Log(Const.TAG, "Crop photo on activity result");
			if (data != null)
				handleCrop(resultCode, data);

			break;
		}

	}

	// private void gotoMyThingFragment(String token, String id) {
	// UberMyThingFragmentRegister fragMything = new
	// UberMyThingFragmentRegister();
	// Bundle bundle = new Bundle();
	// bundle.putString(Const.Params.TOKEN, token);
	// bundle.putString(Const.Params.ID, id);
	// fragMything.setArguments(bundle);
	// activity.addFragment(fragMything, false,
	// Const.FRAGMENT_MYTHING_REGISTER);
	// }

	// private void gotoPaymentFragment(String id, String token) {
	// UberAddPaymentFragmentRegister paymentFragment = new
	// UberAddPaymentFragmentRegister();
	// Bundle bundle = new Bundle();
	// bundle.putString(Const.Params.TOKEN, token);
	// bundle.putString(Const.Params.ID, id);
	// paymentFragment.setArguments(bundle);
	// activity.addFragment(paymentFragment, false,
	// Const.FRAGMENT_PAYMENT_REGISTER);
	// }
	private void goToReffralCodeFragment(String id, String token) {
		ReffralCodeFragment reffralCodeFragment = new ReffralCodeFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Const.Params.TOKEN, token);
		bundle.putString(Const.Params.ID, id);
		reffralCodeFragment.setArguments(bundle);
		activity.addFragment(reffralCodeFragment, false, Const.FRAGMENT_REFFREAL);
	}

	private void setData() {
		etFullName.setText("123456");
		etPassword.setText("123456");
		etConfirmPassword.setText("123456");
		etEmail.setText("ericg.karani@gmail.com");
		etPassword.setText("123456");
		etNumber.setText("720117033");
		etID.setText("720117033");
	}

	@Override
	protected boolean isValidate() {
		String msg = null;
		String name = etFullName.getText().toString();
		String id = etID.getText().toString();
		String email = etEmail.getText().toString();
		String pass1 = etPassword.getText().toString();
		String pass2 = etConfirmPassword.getText().toString();
		String phone = etNumber.getText().toString();
		if (TextUtils.isEmpty(name)) {
			msg = getString(R.string.text_enter_name);
			etFullName.requestFocus();
		} else if (TextUtils.isEmpty(id)) {
			String idtype = spinnerIDType.getSelectedItem().toString();
			msg = "Please enter your " + idtype;
			etID.requestFocus();
		} else if (TextUtils.isEmpty(email)) {
			msg = "Please enter your email";
			etEmail.requestFocus();
		} else if (!AndyUtils.eMailValidation(email)) {
			msg = getString(R.string.text_enter_valid_email);
			etEmail.requestFocus();
		} else if (TextUtils.isEmpty(pass1)) {
			msg = getString(R.string.text_enter_password);
			etPassword.requestFocus();
		} else if (pass1.length() < 6) {
			msg = getString(R.string.text_enter_password_valid);
			etPassword.requestFocus();
		} else if (!(pass1.equals(pass2))) {
			msg = "The passwords do not match";
			etConfirmPassword.requestFocus();
		} else if (phone.length() < 8) {
			msg = "Please enter a valid mobile number";
			etNumber.requestFocus();
		}

		if (msg != null) {
			Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private void getProfile() {
		AndyUtils.showCustomProgressDialog(activity, getString(R.string.text_getting_info), true, null);
		Profile.Properties properties = new Profile.Properties.Builder().add(Properties.ID).add(Properties.FIRST_NAME)
				.add(Properties.GENDER).add(Properties.EMAIL).add(Properties.LAST_NAME).add(Properties.BIRTHDAY)
				.add(Properties.EDUCATION).add(Properties.PICTURE).build();
		mSimpleFacebook.getProfile(properties, new OnProfileListener() {

			@Override
			public void onComplete(Profile profile) {
				AndyUtils.removeCustomProgressDialog();
				Log.i("Uber", "My profile id = " + profile.getId());
				btnGPlush.setEnabled(false);
				btnFb.setEnabled(false);
				etEmail.setText(profile.getEmail());
				etFullName.setText(profile.getFirstName());
				socialId = profile.getId();
				type = Const.SOCIAL_FACEBOOK;
				// etPassword.setEnabled(false);
				tvPassword.setVisibility(View.GONE);
				ivPassword.setVisibility(View.GONE);
				etPassword.setVisibility(View.GONE);

				if (!TextUtils.isEmpty(profile.getPicture()) || !profile.getPicture().equalsIgnoreCase("null")) {
					socialUrl = profile.getPicture();
					AQuery aQuery = new AQuery(activity);
					aQuery.id(ivProPic).image(profile.getPicture(), getAqueryOption());
				} else {
					socialUrl = null;
				}

			}
		});
	}

	private void registerWithoutVerification(String registration_type) {
		AndyUtils.showCustomProgressDialog(getActivity(), "Loading", false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			if (type.equalsIgnoreCase(Const.MANUAL)) {
				map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
				StringBuilder sb = new StringBuilder();
				sb.append("|FORMID|REGISTER|");
				sb.append("|FULLNAME|" + etFullName.getText().toString());
				sb.append("|EMAIL|" + etEmail.getText().toString());
				sb.append("|PASSWORD|" + etPassword.getText().toString());
				sb.append("|IDTYPE|" + spinnerIDType.getSelectedItem().toString());
				sb.append("|IDNUMBER|" + etID.getText().toString());
				sb.append("|PICTURE|" + filePath);
				sb.append("|MOBILENUMBER|" + spCCode.getText().toString().trim() + etNumber.getText().toString());
				sb.append("|DEVICETOKEN|" + activity.phelper.getDeviceToken());
				sb.append("|DEVICETYPE|" + Const.DEVICE_TYPE_ANDROID);
				sb.append("|IMEI|" + new PreferenceHelper(getActivity()).getIMEI());

				String encrypted_register_details = PreferenceHelper.eaes(sb.toString());
				map.put("PLAIN_DATA", sb.toString());
				map.put("DATA", encrypted_register_details);

			}

		} catch (Exception exception) {

		}

		new HttpRequester(activity, map, Const.ServiceCode.REGISTER, this);
	}

	private void register(String type, String id) {

		if (type.equalsIgnoreCase(Const.MANUAL)) {
			HashMap<String, String> map = new HashMap<String, String>();
			StringBuilder sb = new StringBuilder();
			sb.append("|FULLNAME|" + etFullName.getText().toString());
			sb.append("|EMAIL|" + etEmail.getText().toString());
			sb.append("|PASSWORD|" + etPassword.getText().toString());
			sb.append("|IDTYPE|" + spinnerIDType.getSelectedItem().toString());
			sb.append("|IDNUMBER|" + etID.getText().toString());
			sb.append("|PICTURE|" + filePath);
			sb.append("|MOBILENUMBER|" + spCCode.getText().toString().trim() + etNumber.getText().toString());
			sb.append("|DEVICETOKEN|" + activity.phelper.getDeviceToken());
			sb.append("|DEVICETYPE|" + Const.DEVICE_TYPE_ANDROID);
			sb.append("|IMEI|" + new PreferenceHelper(getActivity()).getIMEI());
			AndyUtils.log("myregisteruser", "response::: " + sb.toString());
			asyncExecute2("REGISTER", sb.toString(), "REGISTER");
			AppLog.Log("myregistration::: ", "Entered Execute2::::::");
		} else {
			registerSocial(id, type);
		}

	}

	private void asyncExecute2(String FORMID, String FORMNAME, String MethodName) {
		if (AndyUtils.isNetworkAvailable(activity)) {
			String imei = new PreferenceHelper(getActivity()).getIMEI();
			/*
			 * If IMEI number is not of the appropriate length, get another one
			 * from the device
			 */
			if (imei.length() < 1) {
				TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(
						Context.TELEPHONY_SERVICE);
				imei = telephonyManager.getDeviceId();
			}

			String serverString = "";
			// FORMID|INIT|MOBILENUMBER|xxxx|LATLONG|342,234324|http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=1403812IMEI|xxxxx|COSEBASE|ANDROID|
			serverString = "FORMID|" + FORMID + "|CODEBASE|ANDROID" + FORMNAME;
			AndyUtils.log("myregistrationserverString::: ", serverString);
			try {
				serverString = PreferenceHelper.eaes(serverString);
			} catch (Exception ex) {
				AndyUtils.log("myregistrationexception::: ", ex.getMessage().toString());
			}
			AndyUtils.log("myregistrationencrpyt::: ", "encrypted::: " + serverString);
			AndyUtils.log("myregistrationdecrpyt::: ", "decrypted::: " + PreferenceHelper.daes(serverString));
			// serverString = AndyUtils.urlEncode(serverString);
			new AsyncTask_BulletProof(getActivity()).execute("MapFragment", serverString, MethodName);
		} else {
			AndyUtils.showToast("Network not available", activity);
		}
	}

	private void registerSocial(final String id, final String type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.REGISTER);
		map.put(Const.Params.FULLNAME, etFullName.getText().toString());
		map.put(Const.Params.EMAIL, etEmail.getText().toString());
		map.put(Const.Params.SOCIAL_UNIQUE_ID, id);
		map.put(Const.Params.PICTURE, filePath);
		map.put(Const.Params.PHONE, spCCode.getText().toString().trim() + etNumber.getText().toString());
		map.put(Const.Params.DEVICE_TOKEN, activity.phelper.getDeviceToken());
		map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
		map.put("IMEI", new PreferenceHelper(getActivity()).getIMEI() + "|");
		// map.put(Const.Params.ADDRESS, etAddress.getText().toString());
		// map.put(Const.Params.BIO, etBio.getText().toString());
		// map.put(Const.Params.ZIPCODE, etZipCode.getText().toString());
		map.put(Const.Params.STATE, "");
		map.put(Const.Params.COUNTRY, "");
		map.put(Const.Params.LOGIN_BY, type);
		AndyUtils.showCustomProgressDialog(activity, getString(R.string.text_registering), true, null);
		new MultiPartRequester(activity, map, Const.ServiceCode.REGISTER, this);
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		// System.out.println(response + "<-------");
		switch (serviceCode) {
		case Const.ServiceCode.REGISTER:
			AndyUtils.removeCustomProgressDialog();
			String decrypted_response = PreferenceHelper.daes(response);
			AppLog.Log("myregistration::: ", "noverification::: " + decrypted_response);

			break;

		}
	}

	private void showRegistrationConfirmationDialog() {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout);
		Button okBtn = (Button) dialog.findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void choosePhotoFromGallary() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		activity.startActivityForResult(i, Const.CHOOSE_PHOTO, Const.FRAGMENT_REGISTER);
	}

	private void takePhotoFromCamera() {
		Calendar cal = Calendar.getInstance();
		File file = new File(Environment.getExternalStorageDirectory(), (cal.getTimeInMillis() + ".jpg"));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		uri = Uri.fromFile(file);
		Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(i, Const.TAKE_PHOTO, Const.FRAGMENT_REGISTER);
	}

	private String getRealPathFromURI(Uri contentURI) {
		String result;
		Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);

		if (cursor == null) { // Source is Dropbox or other similar local file
								// path
			result = contentURI.getPath();
		} else {
			cursor.moveToFirst();
			try {
				int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				result = cursor.getString(idx);
			} catch (Exception e) {
				AndyUtils.showToast(getResources().getString(R.string.text_error_get_image), activity);
				result = "";
			}
			cursor.close();
		}
		return result;
	}

	private ImageOptions getAqueryOption() {
		ImageOptions options = new ImageOptions();
		options.targetWidth = 200;
		options.memCache = true;
		options.fallback = R.drawable.default_user;
		options.fileCache = true;
		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.UberBaseFragmentRegister#OnBackPressed()
	 */
	@Override
	public boolean OnBackPressed() {
		// activity.removeAllFragment(new UberMainFragment(), false,
		// Const.FRAGMENT_MAIN);
		activity.goToMainActivity();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */
	private void beginCrop(Uri source) {
		// Uri outputUri = Uri.fromFile(new File(registerActivity.getCacheDir(),
		// "cropped"));
		Uri outputUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), (Calendar.getInstance()
				.getTimeInMillis() + ".jpg")));
		new Crop(source).output(outputUri).asSquare().start(activity);
	}

	@SuppressWarnings("static-access")
	private void handleCrop(int resultCode, Intent result) {
		if (resultCode == activity.RESULT_OK) {
			AppLog.Log(Const.TAG, "Handle crop");
			filePath = getRealPathFromURI(Crop.getOutput(result));
			ivProPic.setImageURI(Crop.getOutput(result));
		} else if (resultCode == Crop.RESULT_ERROR) {
			Toast.makeText(activity, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	private void showPictureDialog() {
		AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
		pictureDialog.setTitle(getResources().getString(R.string.text_choosepicture));
		String[] pictureDialogItems = { getResources().getString(R.string.text_gallary),
				getResources().getString(R.string.text_camera) };

		pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {

				case 0:
					choosePhotoFromGallary();
					break;

				case 1:
					takePhotoFromCamera();
					break;

				}
			}
		});
		pictureDialog.show();
	}

	private void listencode() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.craft.little.code");
		getActivity().registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String messagecode = intent.getStringExtra("message");
				String messagecode2 = intent.getStringExtra("message2");
				String menu = intent.getStringExtra("method");

				AppLog.Log("myregistration::: ", "messagecode::: " + messagecode);
				AppLog.Log("myregistration::: ", "messagecode2::: " + messagecode2);
				AppLog.Log("myregistration::: ", "menu::: " + menu);

				if (messagecode == null) {
					messagecode = "";
				}
				if (messagecode2 == null) {
					messagecode2 = "";
				}
				if (menu == null) {
					menu = "";
				}
				if (menu.equals("GETAUTHSMS")) {
					txtRecreateKey.setText("VERIFYING");
					if (messagecode2.equals("001")) {
						isElmaUser = true;
						verify(isElmaUser);
					}
				} else if (menu.equals("GETCODE")) {
					thesmsps = messagecode + "-" + messagecode2;
					edtRecreateKey.setText(thesmsps);
					txtRecreateKey.setText("VERIFYING");
					isElmaUser = false;
					verify(isElmaUser);
				} else if (menu.equals("REGISTER")) {
					if (pContentKamal.isSuccessStoreUser(messagecode2)) {
						activity.phelper.putPassword(etPassword.getText().toString());
						showRegistrationConfirmationDialog();
						goToReffralCodeFragment(activity.phelper.getUserId(), activity.phelper.getSessionToken());
					}
				}
				// AndyUtils.log("listencodebroadcast","1:"+messagecode+"2:"+messagecode2+":menu:"+menu+":isElmaUser:"+isElmaUser);
			}
		}, intentFilter);
	}

	private void verify(Boolean isElmaUser) {
		new verify(getActivity()).execute();
	}

	public class verify extends AsyncTask<String, String, String> {

		public verify(Context context) {
		}

		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected String doInBackground(String... urls) {
			try {
				Thread.sleep(1200);

			} catch (InterruptedException e) {
			}
			return null;
		}

		/*OnPostExecute of code verification*/
		protected void onPostExecute(String result) {
			verifyCode(isElmaUser);
//			try {
//				AndyUtils.showToast("result", getActivity());
//			} catch (Exception e) {
//			}

		}
	}

	/**
	 * Checks whether the returned code matches the one on SMS
	 * 
	 * @param isElmaUser
	 */
	private void verifyCode(Boolean isElmaUser) {
		if (isElmaUser) {
			if (mydestinationDialog != null) {
				if (mydestinationDialog.isShowing()) {
					mydestinationDialog.dismiss();
					register(type, socialId);
					AndyUtils.showToast("You already have an account. Please login.", getActivity());
					AppLog.Log("myregistration", "response::: " + "I was here once");
				}
			}
		} else {
			PreferenceHelper ph = new PreferenceHelper(getActivity());
			String smsps = ph.getsmsps();
			AndyUtils.log("myregistrationcode", thesmsps + "-" + smsps);
			if (thesmsps.equals(smsps)) {
				if (mydestinationDialog != null) {
					if (mydestinationDialog.isShowing()) {
						mydestinationDialog.dismiss();
						register(type, socialId);
						AppLog.Log("myregistration", "response::: " + "I was here again");
					}
				}
			} else {
				// testingSecondTime = true;
				/*If the message is not read successfully*/
				String toastmessage = "Failed. Press resend to get another code";
				AndyUtils.showToast(toastmessage, getActivity());
			}
		}
	}
}
