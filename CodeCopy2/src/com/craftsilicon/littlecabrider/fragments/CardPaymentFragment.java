package com.craftsilicon.littlecabrider.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.craftsilicon.littlecabrider.PaymentsActivity;
import com.craftsilicon.littlecabrider.R;
import com.craftsilicon.littlecabrider.adapter.VehiclesAdapter;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextViewMedium;
import com.craftsilicon.littlecabrider.db.DBAdapter;
import com.craftsilicon.littlecabrider.db.DBHelper;
import com.craftsilicon.littlecabrider.models.CardPayment;
import com.craftsilicon.littlecabrider.parse.AsyncTaskCompleteListener;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.stripe.android.util.TextUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CardPaymentFragment extends Fragment implements OnClickListener,
		OnItemClickListener, AsyncTaskCompleteListener {
	ImageButton floatingActionButton;
	ImageView imageview_visa_mastercard;
	View layout_card_payment;
	LinearLayout layout_bottom;
	RelativeLayout layout_include_new_card_details;
	RelativeLayout layout_include_edit_card_details;
	MyFontButton button_skip_step;
	MyFontButton button_save_card;
	MyFontButton button_finish;
	MyFontEdittextView txtCardName;
	MyFontEdittextView txtCardNickName;
	MyFontEdittextView txtCardNumber;
	MyFontEdittextView txtCardExpiryDate;
	MyFontTextViewMedium txtNoCards;

	MyFontButton button_update_card;
	MyFontEdittextView txtCardNameEdit;
	MyFontEdittextView txtCardNickNameEdit;
	MyFontEdittextView txtCardNumberEdit;
	MyFontEdittextView txtCardExpiryDateEdit;
	ListView list_view_card_payments;
	RelativeLayout layout;

	/**
	 * Database CardPayment Variables
	 */
	String card_holder_name_edit;
	String card_nickname_edit;
	String card_number_edit;
	String card_expiry_date_edit;
	String card_id;

	/**
	 * String variables
	 */
	String card_name;
	String card_nickname;
	String card_number;
	String card_expiry_date;

	/**
	 * Array Adapter
	 */
	SimpleCursorAdapter arrayAdapter_card_payments;

	/**
	 * Database
	 */
	DBHelper database_helper;
	CardPayment cardPayment;
	DBAdapter database_adapter;
	Cursor cursor_card_payments;
	int cardnumbers = 0;
	/**
	 * Shared Preference
	 */
	PreferenceHelper preference_helper;
	ArrayList<Integer> images;

	/**
	 * Textatcher Lock
	 */
	private boolean lock;

	/**
	 * PATTERN MATCHER
	 */
	static final Pattern CODE_PATTERN = Pattern
			.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
	// private LinearLayout llPaymentList;
	public static final String[] PREFIXES_AMERICAN_EXPRESS = { "34", "37" };
	public static final String[] PREFIXES_DISCOVER = { "60", "62", "64", "65" };
	public static final String[] PREFIXES_JCB = { "35" };
	public static final String[] PREFIXES_DINERS_CLUB = { "300", "301", "302",
			"303", "304", "305", "309", "36", "38", "37", "39" };
	public static final String[] PREFIXES_VISA = { "4" };
	public static final String[] PREFIXES_MASTERCARD = { "50", "51", "52",
			"53", "54", "55" };
	public static final String AMERICAN_EXPRESS = "American Express";
	public static final String DISCOVER = "Discover";
	public static final String JCB = "JCB";
	public static final String DINERS_CLUB = "Diners Club";
	public static final String VISA = "Visa";
	public static final String MASTERCARD = "MasterCard";
	public static final String UNKNOWN = "Unknown";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout_card_payment = inflater.inflate(R.layout.layout_card_payment,
				container, false);

		/**
		 * Instantiate Database Adapter & Bank Payment model
		 */
		database_helper = new DBHelper(getActivity());
		database_adapter = new DBAdapter(getActivity());
		cardPayment = new CardPayment();

		/**
		 * Instantiate PreferenceHelper
		 */
		preference_helper = new PreferenceHelper(getActivity());

		/**
		 * Referencing UI elements
		 */
		layout_include_new_card_details = (RelativeLayout) layout_card_payment
				.findViewById(R.id.layout_include_new_card_details);
		button_finish = (MyFontButton) layout_card_payment
				.findViewById(R.id.button_card_payment_finish);
		layout_include_edit_card_details = (RelativeLayout) layout_card_payment
				.findViewById(R.id.layout_include_edit_card_details);
		button_skip_step = (MyFontButton) layout_include_new_card_details
				.findViewById(R.id.button_skip_step);
		button_save_card = (MyFontButton) layout_include_new_card_details
				.findViewById(R.id.button_card_save);
		txtCardName = (MyFontEdittextView) layout_include_new_card_details
				.findViewById(R.id.txtCardHolderName);
		txtCardNickName = (MyFontEdittextView) layout_include_new_card_details
				.findViewById(R.id.txtCardNickName);
		txtCardNumber = (MyFontEdittextView) layout_include_new_card_details
				.findViewById(R.id.txtCardNumber);
		imageview_visa_mastercard = (ImageView) layout_include_new_card_details
				.findViewById(R.id.image_visa_mastercard);
		txtCardExpiryDate = (MyFontEdittextView) layout_include_new_card_details
				.findViewById(R.id.txtCardExpiryDate);
		button_update_card = (MyFontButton) layout_include_edit_card_details
				.findViewById(R.id.button_card_update);
		txtCardNameEdit = (MyFontEdittextView) layout_include_edit_card_details
				.findViewById(R.id.txtCardHolderNameEdit);
		txtCardNickNameEdit = (MyFontEdittextView) layout_include_edit_card_details
				.findViewById(R.id.txtCardNickNameEdit);
		txtCardNumberEdit = (MyFontEdittextView) layout_include_edit_card_details
				.findViewById(R.id.txtCardNumberEdit);
		txtCardExpiryDateEdit = (MyFontEdittextView) layout_include_edit_card_details
				.findViewById(R.id.txtCardExpiryDateEdit);
		txtNoCards = (MyFontTextViewMedium) layout_card_payment
				.findViewById(R.id.textNoCards);
		list_view_card_payments = (ListView) layout_card_payment
				.findViewById(R.id.listview_card_payments);
		layout_bottom = (LinearLayout) layout_card_payment
				.findViewById(R.id.layoutBottom1);
		floatingActionButton = (ImageButton) layout_card_payment
				.findViewById(R.id.floatingActionButton);

		images = new ArrayList<Integer>();

		/**
		 * Card Expiry Date
		 */
		txtCardExpiryDate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String date = s.toString();
				count = date.length();
				if (count == 2) {
					date = date + "/";
					txtCardExpiryDate.setText(date);
				}

				if (date.length() == 5) {
					button_save_card.requestFocus();
				}

				cardnumbers = count;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		/**
		 * Card Number TextWatcher
		 */
		txtCardNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String type = getType(s.toString());
				switch (type) {
				case VISA:
					imageview_visa_mastercard.setImageDrawable(getResources()
							.getDrawable(R.drawable.visa));
					break;

				case MASTERCARD:
					imageview_visa_mastercard.setImageDrawable(getResources()
							.getDrawable(R.drawable.mastercard));
					break;

				default:
					imageview_visa_mastercard.setImageDrawable(getResources()
							.getDrawable(R.drawable.number));
					break;
				}

				if (s.length() == 19) {
					txtCardExpiryDate.requestFocus();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
					String input = s.toString();
					String numbersOnly = keepNumbersOnly(input);
					String code = formatNumbersAsCode(numbersOnly);
					txtCardNumber.removeTextChangedListener(this);
					txtCardNumber.setText(code);
					txtCardNumber.setSelection(code.length());
					txtCardNumber.addTextChangedListener(this);
				}

			}
		});

		/* Test API Call */
		getCards();
		
		/* Get Cards From SharedPreference*/
		if (preference_helper.getPaymentCards().isEmpty()) {
			getCards();
		}else{
			list_view_card_payments.setVisibility(View.VISIBLE);
			txtNoCards.setVisibility(View.GONE);
			String cards = preference_helper.getPaymentCards();
			String[] cards_list = cards.split(",");
			ArrayAdapter<String> cards_adapter = null;
			AndyUtils.log("listnamecards", "" + cards);
			cards_adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.layout_card_payments_list, R.id.txtCardType2,
					cards_list);
			list_view_card_payments.setAdapter(cards_adapter);
			list_view_card_payments.invalidate();
		}

		/**
		 * Set OnClickListener
		 */
		floatingActionButton.setOnClickListener(this);
		button_skip_step.setOnClickListener(this);
		button_save_card.setOnClickListener(this);
		list_view_card_payments.setOnItemClickListener(this);
		button_update_card.setOnClickListener(this);
		button_finish.setOnClickListener(this);

		/**
		 * UnSet focus on EditText boxes
		 */
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return layout_card_payment;
	}

	private String keepNumbersOnly(CharSequence s) {
		return s.toString().replaceAll("[^0-9]", "");
	}

	private String formatNumbersAsCode(CharSequence s) {
		int groupDigits = 0;
		String tmp = "";
		for (int i = 0; i < s.length(); ++i) {
			tmp += s.charAt(i);
			++groupDigits;
			if (groupDigits == 4) {
				tmp += "-";
				groupDigits = 0;
			}
		}
		return tmp;
	}

	public String getType(String number) {
		if (!TextUtils.isBlank(number)) {
			if (TextUtils.hasAnyPrefix(number, PREFIXES_AMERICAN_EXPRESS)) {
				return AMERICAN_EXPRESS;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_DISCOVER)) {
				return DISCOVER;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_JCB)) {
				return JCB;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_DINERS_CLUB)) {
				return DINERS_CLUB;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_VISA)) {
				return VISA;
			} else if (TextUtils.hasAnyPrefix(number, PREFIXES_MASTERCARD)) {
				return MASTERCARD;
			} else {
				return UNKNOWN;
			}
		}
		return UNKNOWN;
	}

	/* Reset TextViews after saving a card record in the database */
	private void reset() {
		txtCardName.setText("");
		txtCardNickName.setText("");
		txtCardNumber.setText("");
		txtCardExpiryDate.setText("");
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.floatingActionButton) {
			list_view_card_payments.setVisibility(View.INVISIBLE);
			layout_bottom.setVisibility(View.INVISIBLE);
			floatingActionButton.setVisibility(View.INVISIBLE);
			layout_include_new_card_details.setVisibility(View.VISIBLE);
			txtNoCards.setVisibility(View.INVISIBLE);

		} else if (v.getId() == R.id.button_skip_step) {
			/* Reload Cards */
			getCards();

			layout_bottom.setVisibility(View.VISIBLE);
			floatingActionButton.setVisibility(View.VISIBLE);
			layout_include_new_card_details.setVisibility(View.INVISIBLE);
		} else if (v.getId() == R.id.button_card_save) {

			/* Create string variables from EditTextViews */
			card_name = txtCardName.getText().toString();
			card_nickname = txtCardNickName.getText().toString();
			card_number = txtCardNumber.getText().toString();
			card_expiry_date = txtCardExpiryDate.getText().toString();

			if (card_name.length() == 0) {
				txtCardName.setError("Please enter your card holder name");
			} else if (card_nickname.length() == 0) {
				txtCardNickName.setError("Please enter your card nickname");
			} else if (card_number.length() == 0) {
				txtCardNumber.setError("Please enter your card number");
			} else if (card_number.length() < 19) {
				txtCardNumber.setError("Your card number is not complete");
			} else if (card_expiry_date.length() < 5) {
				txtCardExpiryDate
						.setError("Your card expiry date is not complte");
			} else if (card_expiry_date.length() == 0) {
				txtCardExpiryDate
						.setError("Please enter your card expiry date");
			} else {
				/* Split the expiry date into month and year */
				String[] separated_date = card_expiry_date.split("/");
				String card_month = separated_date[0];
				String card_year = separated_date[1];
				/* Save Card Data */
				addNewCard(card_name, card_nickname, card_number, card_year,
						card_month,
						new PreferenceHelper(getActivity()).getPhoneNumber());

				/* Reset EditTextViews */
				reset();

				/* Reload Cards */
				// getCards();

				layout_include_edit_card_details.setVisibility(View.INVISIBLE);
				layout_include_new_card_details.setVisibility(View.INVISIBLE);
				list_view_card_payments.setVisibility(View.VISIBLE);
				layout_bottom.setVisibility(View.VISIBLE);
				floatingActionButton.setVisibility(View.VISIBLE);
			}

		} else if (v.getId() == R.id.button_card_update) {

		} else if (v.getId() == R.id.button_card_payment_finish) {
			getActivity().onBackPressed();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final MyFontTextViewMedium card_alias = (MyFontTextViewMedium) view
				.findViewById(R.id.txtCardType2);

		AlertDialog.Builder deleteCardDialog = new AlertDialog.Builder(
				getActivity());
		deleteCardDialog.setTitle("Delete Card");
		deleteCardDialog
				.setMessage("Are you sure you want to delete this card?");
		deleteCardDialog.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});

		deleteCardDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						deleteCard(card_alias.getText().toString());

						/* Reload the card list */
						// getCards();
					}
				});

		Dialog deleteDialog = deleteCardDialog.create();
		deleteDialog.show();

	}

	/* Method for listing available cards */
	private void getCards() {
		AndyUtils.showCustomProgressDialog(getActivity(),
				"Loading Payment Modes", false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		String unique_id = UUID.randomUUID().toString();
		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		map.put(Const.Params.ID,
				new PreferenceHelper(getActivity()).getUserId());
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(getActivity()).getSessionToken());

		StringBuilder sb = new StringBuilder();
		sb.append("FORMID|GETACCOUNTS|");
		sb.append("MOBILENUMBER|"
				+ new PreferenceHelper(getActivity()).getPhoneNumber() + "|");
		sb.append("UNIQUEID|" + unique_id.toString() + "|");
		sb.append("IMEI|" + new PreferenceHelper(getActivity()).getIMEI() + "|");

		String encryptedc1 = PreferenceHelper.eaes(sb.toString());
		map.put("PLAIN_DATA", sb.toString());
		map.put("DATA", encryptedc1);

		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasent:" + sb.toString());
		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasentencrypted:"
				+ encryptedc1);
		AppLog.Log("CARD PAYMENT GETACCOUNTS", "datasentdecrypted:"
				+ PreferenceHelper.daes(encryptedc1));
		new HttpRequester(getActivity(), map, Const.ServiceCode.GET_CARD, this);
	}

	/* Method for deleting a card */
	private void deleteCard(String card_alias) {
		AndyUtils.showCustomProgressDialog(getActivity(), "Deleting Card",
				false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		String unique_id = UUID.randomUUID().toString();
		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		map.put(Const.Params.ID,
				new PreferenceHelper(getActivity()).getUserId());
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(getActivity()).getSessionToken());

		StringBuilder sb = new StringBuilder();
		sb.append("FORMID|DELETECARD|");
		sb.append("MOBILENUMBER|"
				+ new PreferenceHelper(getActivity()).getPhoneNumber() + "|");
		sb.append("CARDALIAS|" + card_alias + "|");
		sb.append("UNIQUEID|" + unique_id.toString() + "|");
		sb.append("CODEBASE|" + "ANDROID|");
		sb.append("IMEI|" + new PreferenceHelper(getActivity()).getIMEI() + "|");

		String encryptedc1 = PreferenceHelper.eaes(sb.toString());
		map.put("PLAIN_DATA", sb.toString());
		map.put("DATA", encryptedc1);

		AppLog.Log("carddelete", "datasent:" + sb.toString());
		AppLog.Log("carddelete", "datasentencrypted:" + encryptedc1);
		AppLog.Log("carddelete",
				"datasentdecrypted:" + PreferenceHelper.daes(encryptedc1));
		new HttpRequester(getActivity(), map, Const.ServiceCode.DELETE_CARD,
				this);
	}

	/* Method for adding a new card */
	private void addNewCard(String card_name, String card_alias,
			String card_number, String card_year, String card_month,
			String phone_number) {
		HashMap<String, String> map = new HashMap<String, String>();
		String unique_id = UUID.randomUUID().toString();
		map.put(Const.URL, Const.ServiceType.ELMA_ADD_CARD_URL);
		map.put(Const.Params.ID,
				new PreferenceHelper(getActivity()).getUserId());
		map.put(Const.Params.TOKEN,
				new PreferenceHelper(getActivity()).getSessionToken());

		StringBuilder sb = new StringBuilder();
		sb.append("FORMID|ADDCARD|");
		sb.append("CARDNAME|" + card_name + "|");
		sb.append("CARDNUM|" + card_number.trim().replaceAll("\\s+|-", "")
				+ "|");
		sb.append("CARDALIAS|" + card_alias + "|");
		sb.append("UNIQUEID|" + unique_id.toString() + "|");
		sb.append("CARDMONTH|" + card_month + "|");
		sb.append("CARDYEAR|" + card_year + "|");
		sb.append("MOBILENUMBER|" + phone_number + "|");
		sb.append("CODEBASE|" + "ANDROID|");
		sb.append("IMEI|" + new PreferenceHelper(getActivity()).getIMEI() + "|");

		String encryptedc1 = PreferenceHelper.eaes(sb.toString());
		map.put("PLAIN_DATA", sb.toString());
		map.put("DATA", encryptedc1);

		AppLog.Log("mycardsresponse", "datasent:" + sb.toString());
		AppLog.Log("mycardsresponse", "datasentencrypted:" + encryptedc1);
		AppLog.Log("mycards",
				"datasentdecrypted:" + PreferenceHelper.daes(encryptedc1));
		new HttpRequester(getActivity(), map, Const.ServiceCode.ADD_CARD, this);
	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {

		switch (serviceCode) {
		case Const.ServiceCode.ADD_CARD:
			getCards();
			String res = PreferenceHelper.daes(response);
			AppLog.Log("CARD PAYMENT RESPONSE", "response:" + res);
			AndyUtils.processTags(res + "|");
			String status = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(),
					AndyUtils.getValuesArray(), "STATUS");
			if (status == "000") {

				AndyUtils.showToast("You have successfully added a new card",
						getActivity());
			}

			break;
		case Const.ServiceCode.GET_CARD:
			AndyUtils.removeCustomProgressDialog();
			String decrypted_response = PreferenceHelper.daes(response);
			AppLog.Log("myresponse", "response:" + decrypted_response);
			String changed_response;
			if (decrypted_response.startsWith("|")) {
				AppLog.Log("myresponsepipe", "YES");
				changed_response = decrypted_response.substring(1);
				AppLog.Log("myresponsepipe", changed_response);

			} else {
				changed_response = decrypted_response;
			}

			AndyUtils.processTags(changed_response + "|");

			String card_status = AndyUtils.FindInArray(
					AndyUtils.getFieldIDArray(), AndyUtils.getValuesArray(),
					"STATUS");
			String cards = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(),
					AndyUtils.getValuesArray(), "CARDS");
			String banks = AndyUtils.FindInArray(AndyUtils.getFieldIDArray(),
					AndyUtils.getValuesArray(), "BANKS");
			
			/* Save Banks To SharedPreference */
			preference_helper.putPaymentCards(cards);
			preference_helper.putPaymentBanks(banks);
			AppLog.Log("mybanks", preference_helper.getPaymentBanks());

			if (cards.isEmpty()) {
				txtNoCards.setVisibility(View.VISIBLE);
				list_view_card_payments.setVisibility(View.GONE);
			} else {
				list_view_card_payments.setVisibility(View.VISIBLE);
				txtNoCards.setVisibility(View.GONE);
				String[] cards_list = cards.split(",");
				ArrayAdapter<String> cards_adapter = null;
				AndyUtils.log("listnamecards", "" + cards);
				cards_adapter = new ArrayAdapter<String>(getActivity(),
						R.layout.layout_card_payments_list, R.id.txtCardType2,
						cards_list);
				list_view_card_payments.setAdapter(cards_adapter);
				list_view_card_payments.invalidate();
			}

			AppLog.Log("mycardresponse", "response:" + changed_response);

			break;
		case Const.ServiceCode.DELETE_CARD:
			getCards();
			String delete_card_response = PreferenceHelper.daes(response);
			AppLog.Log("carddelete", "response:" + delete_card_response);
			AndyUtils.removeCustomProgressDialog();

			AndyUtils.showToast("You card has been successfully deleted",
					getActivity());
			break;

		default:
			break;
		}

	}

}
