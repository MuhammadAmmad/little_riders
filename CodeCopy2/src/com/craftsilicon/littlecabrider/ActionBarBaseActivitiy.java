package com.craftsilicon.littlecabrider;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextView;
import com.craftsilicon.littlecabrider.component.MyFontTextViewMedium;
import com.craftsilicon.littlecabrider.fragments.BaseFragmentRegister;
import com.craftsilicon.littlecabrider.interfaces.OnProgressCancelListener;
import com.craftsilicon.littlecabrider.parse.AsyncTaskCompleteListener;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.parse.ParseContent;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.craftsilicon.littlecabrider.LatLngInterpolator.Linear;

/**
 * @author Elluminati elluminati.in
 */
@SuppressLint("NewApi")
abstract public class ActionBarBaseActivitiy extends ActionBarActivity implements OnClickListener,
		AsyncTaskCompleteListener {

	public boolean isTip = false;
	/**
	 * Payment Variables
	 */
	public String PAYMENT_MODE = "";
	public String CARD_ALIAS = "";
	public String REFERRALBONUS = "";
	public String TIP = "0";
	public String BANKNAME = "";
	public String BANKPIN = "";
	public String CARDCVV = "";
	public String TIMESTAMP = "";
	public String DRIVEREMAIL = "";
	public ProgressDialog progressDialog;
	public String PAYMENT_STATUS;
	public ParseContent parseContent;

	public ActionBar actionBar;
	private int mFragmentId = 0;
	private String mFragmentTag = null;
	public ImageButton btnNotification, btnActionMenu, btnEditProfile;
	public MyFontTextView tvTitle;
	public AutoCompleteTextView etEnterSource;
	public String currentFragment = null, totalTmp, distCostTmp, timeCostTmp, basePriceTmp, resetTotal;

	private Spinner spnPaymentType, spnSelectBank;
	public LinearLayout layoutDestination;
	public PreferenceHelper preferenceHelper;
	public Dialog mDialog;
	public static Dialog mDialog2;

	// public ImageButton imgClearDst;

	public boolean billAccepted = false;

	protected abstract boolean isValidate();

	public boolean isPaymentSelected = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			actionBar = getSupportActionBar();
		} catch (Exception e) {

		}

		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		// Window window = getWindow();
		// window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		// window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// window.setStatusBarColor(getResources().getColor(
		// R.color.color_action_bar));
		//
		// }

		// setStatusBarColor((getResources().getColor(R.color.color_action_bar)));
		// Custom Action Bar
		preferenceHelper = new PreferenceHelper(getApplicationContext());
		parseContent = new ParseContent(this);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		LayoutInflater inflater = (LayoutInflater) actionBar.getThemedContext().getSystemService(
				LAYOUT_INFLATER_SERVICE);
		View customActionBarView = inflater.inflate(R.layout.custom_action_bar, null);
		layoutDestination = (LinearLayout) customActionBarView.findViewById(R.id.layoutDestination);
		btnNotification = (ImageButton) customActionBarView.findViewById(R.id.btnActionNotification);
		btnNotification.setOnClickListener(this);

		// imgClearDst = (ImageButton) customActionBarView
		// .findViewById(R.id.imgClearDst);

		tvTitle = (MyFontTextView) customActionBarView.findViewById(R.id.tvTitle);
		tvTitle.setOnClickListener(this);

		btnEditProfile = (ImageButton) customActionBarView.findViewById(R.id.btnEditProfile);

		etEnterSource = (AutoCompleteTextView) customActionBarView.findViewById(R.id.etEnterSource);

		btnActionMenu = (ImageButton) customActionBarView.findViewById(R.id.btnActionMenu);
		btnActionMenu.setOnClickListener(this);
		try {
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM
					| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
			actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void setStatusBarColor(int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(color);
		}
	}

	public void setFbTag(String tag) {
		mFragmentId = 0;
		mFragmentTag = tag;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Fragment fragment = null;

		if (mFragmentId > 0) {
			fragment = getSupportFragmentManager().findFragmentById(mFragmentId);
		} else if (mFragmentTag != null && !mFragmentTag.equalsIgnoreCase("")) {
			fragment = getSupportFragmentManager().findFragmentByTag(mFragmentTag);
		}
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void startActivityForResult(Intent intent, int requestCode, int fragmentId) {
		mFragmentId = fragmentId;
		mFragmentTag = null;
		super.startActivityForResult(intent, requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode, String fragmentTag) {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startActivityForResult(intent, requestCode);
	}

	public void startActivityForResult(Intent intent, int requestCode, int fragmentId, Bundle options) {

		mFragmentId = fragmentId;
		mFragmentTag = null;
		super.startActivityForResult(intent, requestCode, options);
	}

	public void startActivityForResult(Intent intent, int requestCode, String fragmentTag, Bundle options) {

		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startActivityForResult(intent, requestCode, options);
	}

	public void startIntentSenderForResult(Intent intent, int requestCode, String fragmentTag, Bundle options) {

		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startActivityForResult(intent, requestCode, options);
	}

	@Override
	@Deprecated
	public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
			int flagsValues, int extraFlags) throws SendIntentException {
		super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
	}

	public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
			int flagsValues, int extraFlags, String fragmentTag) throws SendIntentException {

		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
	}

	@Override
	@Deprecated
	public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
			int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
		super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
	}

	public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask,
			int flagsValues, int extraFlags, Bundle options, String fragmentTag) throws SendIntentException {
		mFragmentTag = fragmentTag;
		mFragmentId = 0;
		super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
		super.startActivityForResult(intent, requestCode, options);
	}

	public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
		try {
			FragmentManager manager = getSupportFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
					R.anim.slide_out_right);
			if (addToBackStack) {
				ft.addToBackStack(tag);
			}
			ft.replace(R.id.content_frame, fragment, tag);
			ft.commitAllowingStateLoss();
		} catch (Exception e) {

		}
	}

	public void addFragment(Fragment fragment, boolean addToBackStack, boolean isAnimate, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();
		if (isAnimate)
			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
					R.anim.slide_out_right);
		if (addToBackStack) {
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.content_frame, fragment, tag);
		ft.commitAllowingStateLoss();
	}

	public void addFragmentWithStateLoss(Fragment fragment, boolean addToBackStack, String tag) {

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		if (addToBackStack) {
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.content_frame, fragment, tag);
		ft.commitAllowingStateLoss();
	}

	public void removeAllFragment(Fragment replaceFragment, boolean addToBackStack, String tag) {
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction ft = manager.beginTransaction();

		manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		if (addToBackStack) {
			ft.addToBackStack(tag);
		}
		ft.replace(R.id.content_frame, replaceFragment);
		ft.commit();
	}

	public void clearBackStackImmidiate() {

		FragmentManager manager = getSupportFragmentManager();

		manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

	}

	public void clearBackStack() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {

		if (!TextUtils.isEmpty(currentFragment)) {
			FragmentManager manager = getSupportFragmentManager();
			BaseFragmentRegister frag = ((BaseFragmentRegister) manager.findFragmentByTag(currentFragment));

			if (frag != null && frag.isVisible())
				frag.OnBackPressed();
			else
				super.onBackPressed();
		} else {
			super.onBackPressed();

		}
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		default:
			break;
		}
		return true;
	}

	protected ImageOptions getAqueryOption() {
		ImageOptions options = new ImageOptions();
		options.targetWidth = 200;
		options.memCache = true;
		options.fallback = R.drawable.default_user;
		options.fileCache = true;
		return options;
	}

	public void openExitDialog() {
		final Dialog mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.exit_layout);
		mDialog.setCancelable(false);
		mDialog.findViewById(R.id.tvExitOk).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			}
		});
		mDialog.findViewById(R.id.tvExitCancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	public static String getUserCountry(Context context) {
		try {
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) { // SIM country
																	// code is
																	// available
				return simCountry.toLowerCase(Locale.US);
			} else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device
																				// is
																				// not
																				// 3G
																				// (would
																				// be
																				// unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2) { // network
																				// country
																				// code
																				// is
																				// available
					return networkCountry.toLowerCase(Locale.US);
				}
			}
		} catch (Exception e) {
		}
		return null;
	}

	public void showBillDialog(String timeCost, String total, String distCost, String basePrice, String time,
			String distance, String promoBouns, String referralBouns, String pricePerDistance, String pricePerTime,
			String unit, String btnTitle) {
		mDialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawableResource(R.color.white);
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setContentView(R.layout.layout_receipt);
		REFERRALBONUS = referralBouns;

		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					return false;
				}
				return false;
			}
		});
		/**
		 * Bill Dialog UI Variables
		 */
		final MyFontButton button_pay_cash;
		final MyFontButton button_pay_mpesa;
		final MyFontButton button_pay_bank;
		final MyFontButton button_pay_card;
		final MyFontEdittextView text_card_cvv;
		final MyFontTextViewMedium txtNoCardsAvailable, txtOverallPaymentMessage, textSelectedVehicleType;
		final MyFontButton button_tip_fifty, button_tip_hundred, button_round_off_tip, button_pay_coupon, button_no_tip;

		/* Referencing UI elements */

		final LinearLayout layout_bottom = (LinearLayout) mDialog.findViewById(R.id.layout_bottom);
		final RelativeLayout layout_messages = (RelativeLayout) layout_bottom.findViewById(R.id.layoutPaymentMessages);
		final LinearLayout spinner_layout = (LinearLayout) layout_bottom.findViewById(R.id.spinner_layout);
		final LinearLayout layout_tip_driver = (LinearLayout) mDialog.findViewById(R.id.layout_tip_driver);
		final LinearLayout layout_card_payment_details = (LinearLayout) layout_bottom
				.findViewById(R.id.layout_payment_type_details);
		final LinearLayout layout_cost_breakup = (LinearLayout) layout_tip_driver
				.findViewById(R.id.layout_cost_breakdown);

		textSelectedVehicleType = (MyFontTextViewMedium) mDialog.findViewById(R.id.tvSelectedVehicleType);
		txtOverallPaymentMessage = (MyFontTextViewMedium) layout_messages.findViewById(R.id.txtOverallPaymentMessage);
		txtNoCardsAvailable = (MyFontTextViewMedium) layout_messages.findViewById(R.id.txtNoCardsAvailable);
		button_no_tip = (MyFontButton) layout_tip_driver.findViewById(R.id.text_no_tip);
		button_pay_coupon = (MyFontButton) layout_bottom.findViewById(R.id.button_pay_coupon);
		button_pay_card = (MyFontButton) layout_bottom.findViewById(R.id.button_pay_card);
		button_pay_bank = (MyFontButton) layout_bottom.findViewById(R.id.button_pay_bank);
		button_pay_cash = (MyFontButton) layout_bottom.findViewById(R.id.button_pay_cash);
		button_pay_mpesa = (MyFontButton) layout_bottom.findViewById(R.id.button_pay_mpesa);
		text_card_cvv = (MyFontEdittextView) layout_bottom.findViewById(R.id.txtCardCVV);
		button_tip_fifty = (MyFontButton) layout_tip_driver.findViewById(R.id.text_tip_fifty);
		button_tip_hundred = (MyFontButton) layout_tip_driver.findViewById(R.id.text_tip_hundred);
		button_round_off_tip = (MyFontButton) layout_tip_driver.findViewById(R.id.button_round_off_tip);

		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		DecimalFormat perHourFormat = new DecimalFormat("0.0");
		try {
			resetTotal = String.valueOf(decimalFormat.format(Double.parseDouble(total)));
			totalTmp = String.valueOf(decimalFormat.format(Double.parseDouble(total)));
			distCostTmp = String.valueOf(decimalFormat.format(Double.parseDouble(distCost)));
			timeCostTmp = String.valueOf(decimalFormat.format(Double.parseDouble(timeCost)));
			basePriceTmp = String.valueOf(decimalFormat.format(Double.parseDouble(basePrice)));

		} catch (NumberFormatException e) {
			total = AndyUtils.comaReplaceWithDot(total);
			distCost = AndyUtils.comaReplaceWithDot(distCost);
			timeCost = AndyUtils.comaReplaceWithDot(timeCost);
			basePrice = AndyUtils.comaReplaceWithDot(basePrice);

			resetTotal = String.valueOf(decimalFormat.format(Double.parseDouble(total)));
			totalTmp = String.valueOf(decimalFormat.format(Double.parseDouble(total)));
			distCostTmp = String.valueOf(decimalFormat.format(Double.parseDouble(distCost)));
			timeCostTmp = String.valueOf(decimalFormat.format(Double.parseDouble(timeCost)));
			basePriceTmp = String.valueOf(decimalFormat.format(Double.parseDouble(basePrice)));

		}
		AppLog.Log("Distacne", distance);
		AppLog.Log("Time", time);

		((TextView) mDialog.findViewById(R.id.tvBasePrice)).setText("Kes " + basePriceTmp);

		((TextView) mDialog.findViewById(R.id.tvBillDistancePerMile)).setText(distance + "kms" + " * Kes "
				+ pricePerDistance + "/" + unit);

		((TextView) mDialog.findViewById(R.id.tvBillTimePerHour)).setText(time + "mins" + " * Kes " + pricePerTime
				+ "/min");

		spnPaymentType = (Spinner) spinner_layout.findViewById(R.id.spnSelectPayment);
		setSpinner(spnPaymentType);

		((TextView) mDialog.findViewById(R.id.lable_rider_pickup_address)).setText(preferenceHelper
				.getRiderSourceAddress());
		((TextView) mDialog.findViewById(R.id.lable_rider_destination_address)).setText(preferenceHelper
				.getRiderDestinationAddress());
		((TextView) mDialog.findViewById(R.id.tvDis1)).setText("Kes " + distCostTmp);

		((TextView) mDialog.findViewById(R.id.tvTime1)).setText("Kes " + timeCostTmp);

		((TextView) mDialog.findViewById(R.id.tvTotal1)).setText("Kes " + totalTmp);
		// ((TextView) mDialog.findViewById(R.id.tvPromoBonus))
		// .setText(decimalFormat.format(Double.parseDouble(promoBouns)));
		((TextView) mDialog.findViewById(R.id.tvReferralBonus)).setText("Kes "
				+ decimalFormat.format(Double.parseDouble(referralBouns)));

		/**
		 * Decision making process for payment buttons
		 */
		button_no_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isTip = true;
				TIP = "0";

				((TextView) mDialog.findViewById(R.id.tvTotal1)).setText("Kes " + resetTotal);

				button_no_tip.setBackground(getResources().getDrawable(R.drawable.drawable_tip_payment_clicked));
				button_no_tip.setTextColor(getResources().getColor(android.R.color.white));

				button_round_off_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_round_off_tip.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_fifty.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_fifty.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_hundred.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_hundred.setTextColor(getResources().getColor(R.color.darkgray));

			}
		});

		button_round_off_tip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isTip = true;
				totalTmp = resetTotal;
				button_round_off_tip.setBackground(getResources().getDrawable(R.drawable.drawable_tip_payment_clicked));
				button_round_off_tip.setTextColor(getResources().getColor(android.R.color.white));

				button_no_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_no_tip.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_fifty.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_fifty.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_hundred.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_hundred.setTextColor(getResources().getColor(R.color.darkgray));

				double new_total = Math.ceil(Double.parseDouble(totalTmp + 49) / 50) * 50;
				double aggregate_tip = new_total - Double.parseDouble(totalTmp);
				TIP = String.valueOf(aggregate_tip);

				((TextView) mDialog.findViewById(R.id.tvTotal1)).setText("Kes " + new_total);
			}
		});
		button_tip_hundred.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				totalTmp = resetTotal;
				isTip = true;
				button_tip_hundred.setBackground(getResources().getDrawable(R.drawable.drawable_tip_payment_clicked));
				button_tip_hundred.setTextColor(getResources().getColor(android.R.color.white));

				button_no_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_no_tip.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_fifty.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_fifty.setTextColor(getResources().getColor(R.color.darkgray));

				button_round_off_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_round_off_tip.setTextColor(getResources().getColor(R.color.darkgray));

				TIP = button_tip_hundred.getText().toString();
				double new_total = Double.parseDouble(totalTmp) + Double.parseDouble(TIP);
				((TextView) mDialog.findViewById(R.id.tvTotal1)).setText("Kes " + new_total);
			}
		});

		button_tip_fifty.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				totalTmp = resetTotal;
				isTip = true;
				button_tip_fifty.setBackground(getResources().getDrawable(R.drawable.drawable_tip_payment_clicked));
				button_tip_fifty.setTextColor(getResources().getColor(android.R.color.white));

				button_no_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_no_tip.setTextColor(getResources().getColor(R.color.darkgray));

				button_round_off_tip.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_round_off_tip.setTextColor(getResources().getColor(R.color.darkgray));

				button_tip_hundred.setBackground(getResources().getDrawable(R.drawable.drawable_circle_button));
				button_tip_hundred.setTextColor(getResources().getColor(R.color.darkgray));

				TIP = button_tip_fifty.getText().toString();
				double new_total = Double.parseDouble(totalTmp) + Double.parseDouble(TIP);
				((TextView) mDialog.findViewById(R.id.tvTotal1)).setText("Kes " + new_total);
			}
		});

		button_pay_coupon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isPaymentSelected = true;
				button_pay_coupon.setBackground(getResources().getDrawable(R.drawable.drawable_payment_button_clicked));
				button_pay_coupon.setTextColor(getResources().getColor(android.R.color.white));

				layout_card_payment_details.setVisibility(View.GONE);
				layout_messages.setVisibility(View.VISIBLE);
				// txtOverallPaymentMessage.setVisibility(View.GONE);
				txtNoCardsAvailable.setText("Your payment will be done via coupons.");

				PAYMENT_MODE = "COUPONS";

				/* Reset all other buttons */
				button_pay_bank.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_bank.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_cash.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_cash.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_mpesa.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_mpesa.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_card.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_card.setTextColor(getResources().getColor(R.color.darkgray));

			}
		});

		button_pay_card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isPaymentSelected = true;
				button_pay_card.setBackground(getResources().getDrawable(R.drawable.drawable_payment_button_clicked));
				button_pay_card.setTextColor(getResources().getColor(android.R.color.white));

				String payment_cards = preferenceHelper.getPaymentCards();
				String[] cards_list = payment_cards.split(",");

				// layout_card_payment_details.setVisibility(View.VISIBLE);
				if (cards_list.length == 0) {
					layout_card_payment_details.setVisibility(View.GONE);
					txtNoCardsAvailable.setText("You have not added any cards yet.");
				} else {
					layout_card_payment_details.setVisibility(View.VISIBLE);
					layout_messages.setVisibility(View.GONE);
				}

				PAYMENT_MODE = "CARDS";

				/* Reset all other buttons */
				button_pay_bank.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_bank.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_cash.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_cash.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_mpesa.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_mpesa.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_coupon.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_coupon.setTextColor(getResources().getColor(R.color.darkgray));
			}
		});

		button_pay_bank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isPaymentSelected = true;
				button_pay_bank.setBackground(getResources().getDrawable(R.drawable.drawable_payment_button_clicked));
				button_pay_bank.setTextColor(getResources().getColor(android.R.color.white));

				layout_card_payment_details.setVisibility(View.GONE);
				layout_messages.setVisibility(View.VISIBLE);
				// txtOverallPaymentMessage.setVisibility(View.GONE);
				txtNoCardsAvailable.setText("You have no available bank accounts");

				PAYMENT_MODE = "BANKS";

				/* Reset all other buttons */
				button_pay_cash.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_cash.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_mpesa.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_mpesa.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_card.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_card.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_coupon.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_coupon.setTextColor(getResources().getColor(R.color.darkgray));
			}
		});

		button_pay_cash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isPaymentSelected = true;
				button_pay_cash.setBackground(getResources().getDrawable(R.drawable.drawable_payment_button_clicked));
				button_pay_cash.setTextColor(getResources().getColor(android.R.color.white));

				layout_card_payment_details.setVisibility(View.GONE);

				layout_messages.setVisibility(View.VISIBLE);
				// txtOverallPaymentMessage.setVisibility(View.GONE);
				txtNoCardsAvailable.setText("Your payment will be made via cash.");

				PAYMENT_MODE = "CASH";

				/* Reset all other buttons */
				button_pay_bank.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_bank.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_mpesa.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_mpesa.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_card.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_card.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_coupon.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_coupon.setTextColor(getResources().getColor(R.color.darkgray));
			}
		});

		button_pay_mpesa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isPaymentSelected = true;
				button_pay_mpesa.setBackground(getResources().getDrawable(R.drawable.drawable_payment_button_clicked));
				button_pay_mpesa.setTextColor(getResources().getColor(android.R.color.white));

				// AppLog.Log("mycountry", "response:" +
				// getUserCountry(getApplicationContext()));
				layout_card_payment_details.setVisibility(View.GONE);

				layout_messages.setVisibility(View.VISIBLE);
				// txtOverallPaymentMessage.setVisibility(View.GONE);
				txtNoCardsAvailable.setText("Your payment will be made via MPESA.");

				PAYMENT_MODE = "MPESA";

				/* Reset all other buttons */
				button_pay_bank.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_bank.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_cash.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_cash.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_card.setBackground(getResources()
						.getDrawable(R.drawable.drawable_payment_buttons_background));
				button_pay_card.setTextColor(getResources().getColor(R.color.darkgray));

				button_pay_coupon.setBackground(getResources().getDrawable(
						R.drawable.drawable_payment_buttons_background));
				button_pay_coupon.setTextColor(getResources().getColor(R.color.darkgray));
			}
		});

		/* MyFontTextViewMedium With An Arrow-Down Drawable */
		final MyFontTextViewMedium label_cost_breakup_down = (MyFontTextViewMedium) mDialog
				.findViewById(R.id.label_cost_breakup_down);
		label_cost_breakup_down.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * layout_tip_driver.setVisibility(View.VISIBLE);
				 * label_cost_breakup_down.setVisibility(View.INVISIBLE);
				 * layout_cost_breakup.setVisibility(View.INVISIBLE);
				 * label_cost_breakup_down.setVisibility(View.INVISIBLE);
				 */
			}
		});
		/* MyFontTextViewMedium With An Arrow-Up Drawable */
		MyFontTextViewMedium label_cost_breakup = (MyFontTextViewMedium) mDialog.findViewById(R.id.label_cost_breakup);
		label_cost_breakup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * layout_tip_driver.setVisibility(View.INVISIBLE);
				 * label_cost_breakup_down.setVisibility(View.VISIBLE);
				 * layout_cost_breakup.setVisibility(View.VISIBLE);
				 * label_cost_breakup_down.setVisibility(View.VISIBLE);
				 */
			}
		});
		Button btnConfirm = (Button) mDialog.findViewById(R.id.btnBillDialogClose);
		if (!TextUtils.isEmpty(btnTitle)) {
			btnConfirm.setText(btnTitle);
		}
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * Payment Logic
				 */
				// if (isTip==false) {
				// TIP="0";
				// }
				if (isPaymentSelected == true) {

					CARDCVV = text_card_cvv.getText().toString();
					if (layout_card_payment_details.getVisibility() == View.VISIBLE && CARDCVV.isEmpty()) {
						text_card_cvv.setError("Please enter your card's CVV.");
					} else {
						confirmTrip();
					}
					// TODO

					if (mDialog != null) {
						mDialog.dismiss();
					}
					showCustomProgressDialog("Making Payment");
				} else {
					AndyUtils.showToast("Please select a payment option.", getApplicationContext());
				}

			}
		});

		mDialog.setCancelable(true);
		mDialog.show();
	}

	public void showCustomProgressDialog(String title) {
		LinearLayout llLoading_bg;

		mDialog2 = new Dialog(this, R.style.MyDialog);
		mDialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog2.setContentView(R.layout.progress_bar_cancel);

		ImageView imageView = (ImageView) mDialog2.findViewById(R.id.ivProgressBar);
		llLoading_bg = (LinearLayout) mDialog2.findViewById(R.id.llLoading_bg);

		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog2.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
			if (title.contains("location") || title.contains("fare")) {
				llLoading_bg.setBackground(null);
			}
		}
		RelativeLayout includeDriver = (RelativeLayout) mDialog2.findViewById(R.id.includeDriver);
		includeDriver.setVisibility(View.GONE);
		Button btnCancel = (Button) mDialog2.findViewById(R.id.btnCancel);
		btnCancel.setVisibility(View.GONE);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_updown);
		imageView.startAnimation(anim);
		mDialog2.setCancelable(true);

		mDialog2.show();
	}

	private void setSpinner(Spinner spn) {
		final ArrayAdapter<String> adapter;
		String payment_cards = preferenceHelper.getPaymentCards();
		String[] cards_list = payment_cards.split(",");

		AppLog.Log("mypaymentcards", "responsepaymentcards:" + cards_list);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cards_list);
		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spn.setAdapter(adapter);

		spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@SuppressWarnings("unused")
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				if (adapter != null) {
					CARD_ALIAS = adapter.getItem(position);
				} else {
					AndyUtils.showToast("You have not added any cards yet", getApplicationContext());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});
	}

	/**
	 * Confirm Payment
	 */
	private void confirmPayment() {
		String customer_payment_status = "";
		if (PAYMENT_STATUS == "0") {
			customer_payment_status = "0";
		} else {
			customer_payment_status = "1";
		}

		HashMap<String, String> map = new HashMap<String, String>();
		StringBuilder stringBuilder_confirm_payment = new StringBuilder();
		String unique_id = UUID.randomUUID().toString();
		map.put(Const.URL, Const.ServiceType.CONFIRMPAYMENT);
		
		stringBuilder_confirm_payment.append("FORMID|CONFIRMPAYMENT|");
		stringBuilder_confirm_payment.append("TOKEN|" + preferenceHelper.getSessionToken() + "|");
		stringBuilder_confirm_payment.append("UNIQUEID|" + unique_id.toString() + "|");
		stringBuilder_confirm_payment.append("MOBILENUMBER|" + preferenceHelper.getPhoneNumber() + "|");
		stringBuilder_confirm_payment.append("TRIPID|" + String.valueOf(preferenceHelper.getRequestId()) + "|");
		stringBuilder_confirm_payment.append("PAYMENTAMOUNT|" + totalTmp + "|");
		stringBuilder_confirm_payment.append("TIP|" + TIP + "|");
		stringBuilder_confirm_payment.append("PAYMENTMODE|" + PAYMENT_MODE + "|");
		stringBuilder_confirm_payment.append("PAYMENTSTATUS|" + customer_payment_status + "|");

		String encrypted_confirm_payment_string = PreferenceHelper.eaes(stringBuilder_confirm_payment.toString());
		map.put("PLAIN_DATA", stringBuilder_confirm_payment.toString());
		map.put("DATA", encrypted_confirm_payment_string);

		/*LOGS*/
		AppLog.Log("userrequestid", String.valueOf(preferenceHelper.getRequestId()));
		AppLog.Log("userpaymentamount", totalTmp);
		AppLog.Log("userpaymentstatus", customer_payment_status);
		AppLog.Log("userid", preferenceHelper.getUserId());
		AppLog.Log("usertoken", preferenceHelper.getSessionToken());
		new HttpRequester(this, map, Const.ServiceCode.CONFIRMPAYMENT, this);
	}

	/**
	 * MAKE PAYMENT
	 */
	public void confirmTrip() {
		HashMap<String, String> map = new HashMap<String, String>();
		String unique_id = UUID.randomUUID().toString();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		TIMESTAMP = simpleDateFormat.format(new Date());

		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		map.put(Const.Params.ID, preferenceHelper.getUserId());
		map.put(Const.Params.TOKEN, preferenceHelper.getSessionToken());

		StringBuilder sb = new StringBuilder();
		sb.append("FORMID|CONFIRMTRIP|");
		sb.append("UNIQUEID|" + unique_id.toString() + "|");
		sb.append("MOBILENUMBER|" + preferenceHelper.getPhoneNumber() + "|");
		sb.append("COUNTRY|" + preferenceHelper.getRiderCountry() + "|");
		sb.append("PICKUP|" + preferenceHelper.getRiderSourceAddress() + "|");
		sb.append("DROPOFF|" + preferenceHelper.getRiderDestinationAddress() + "|");
		sb.append("DATE|" + TIMESTAMP + "|");
		sb.append("DRIVEREMAIL|" + preferenceHelper.getDriverId() + "|");
		sb.append("PAYMENTMODE|" + PAYMENT_MODE + "|");
		sb.append("CARDALIAS|" + CARD_ALIAS + "|");
		sb.append("DISTANCEFARE|" + distCostTmp + "|");
		sb.append("DURATIONFARE|" + timeCostTmp + "|");
		sb.append("REFERRALBONUS|" + REFERRALBONUS + "|");
		sb.append("TIP|" + TIP + "|");
		sb.append("TOTALFARE|" + totalTmp + "|");
		sb.append("CARDCVV|" + CARDCVV + "|");
		sb.append("BANKPIN|" + BANKPIN + "|");
		sb.append("BANKNAME|" + BANKNAME + "|");
		sb.append("CODEBASE|" + "ANDROID|");
		sb.append("IMEI|" + preferenceHelper.getIMEI() + "|");

		String encryptedc1 = PreferenceHelper.eaes(sb.toString());
		map.put("PLAIN_DATA", sb.toString());
		map.put("DATA", encryptedc1);

		AppLog.Log("myconfirmtripresponse", "datasent:" + sb.toString());
		AppLog.Log("myconfirmtripsresponse", "datasentencrypted:" + encryptedc1);
		AppLog.Log("myconfirmtrip", "datasentdecrypted:" + PreferenceHelper.daes(encryptedc1));
		new HttpRequester(this, map, Const.ServiceCode.CONFIRM_TRIP, this);
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		switch (serviceCode) {
		case Const.ServiceCode.CONFIRM_TRIP:
			String confirm_trip_response = PreferenceHelper.daes(response);
			String payment_status1 = "0";
			AppLog.Log("confirmtripresponse", "response:" + confirm_trip_response);
			AppLog.Log("confirmtripresponse", "encrypted response:" + response);

			AndyUtils.processTags(confirm_trip_response + "|");
			String status = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "STATUS");
			String message = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(), "MESSAGE");

			if (status.equals("000")) {
				PAYMENT_STATUS = "1";
				TIP = "0";
				isPaymentSelected = false;
				// isTip = false;
				confirmPayment();
				mDialog2.dismiss();
				mDialog.dismiss();
				billAccepted = true;
			} else if (status.equals("091")) {
				PAYMENT_STATUS = "0";
				confirmPayment();
				mDialog2.dismiss();
				mDialog.show();
				AndyUtils.showToast("Your transaction was unsuccessful. Please select another payment mode",
						getApplicationContext());
			}

			break;

		case Const.ServiceCode.CONFIRMPAYMENT:
			AppLog.Log("confirmpayment", "reponse: " + response);
		default:
			break;
		}

	}

	public void setTitle(String str) {
		tvTitle.setText(str);
	}

	public void setIconMenu(int img) {
		btnActionMenu.setImageResource(img);
	}

	public void setIcon(int img) {
		btnNotification.setImageResource(img);
	}

	public void goToMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
		finish();
	}

	public void clearAll() {
		NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nMgr.cancelAll();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_pay_card:
			AndyUtils.showToast("OK", getApplicationContext());
			break;

		default:
			break;
		}
	}

}
