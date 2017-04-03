package com.craftsilicon.littlecabrider.fragments;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.craftsilicon.littlecabrider.models.Driver;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.craftsilicon.littlecabrider.MainDrawerActivity;
import com.craftsilicon.littlecabrider.PaymentsActivity;
import com.craftsilicon.littlecabrider.R;

/**
 * @author Elluminati elluminati.in
 */
public class FeedbackFragment extends BaseFragment {
	private EditText etComment;
	private RatingBar rtBar;
	private Button btnSubmit;
	private ImageView ivDriverImage;
	private Driver driver;
	private TextView tvDistance, tvTime, tvClientName;

	boolean paymentselected = false;

	// private TextView tvFeedbackAmount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		driver = (Driver) getArguments().getParcelable(Const.DRIVER);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		activity.setTitle(getString(R.string.text_feedback));
		activity.tvTitle.setVisibility(View.VISIBLE);
		View view = inflater.inflate(R.layout.feedback, container, false);
		tvClientName = (TextView) view.findViewById(R.id.tvClientName);
		etComment = (EditText) view.findViewById(R.id.etComment);
		rtBar = (RatingBar) view.findViewById(R.id.ratingBar);
		btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
		ivDriverImage = (ImageView) view.findViewById(R.id.ivDriverImage);
		tvDistance = (TextView) view.findViewById(R.id.tvDistance);
		tvTime = (TextView) view.findViewById(R.id.tvTime);
		// tvFeedbackAmount = (TextView)
		// view.findViewById(R.id.tvFeedbackAmount);
		// tvDistance.setText(driver.getLastDistance());
		tvDistance.setText(driver.getBill().getDistance() + " " + driver.getBill().getUnit());
		tvTime.setText((int) (Double.parseDouble(driver.getBill().getTime())) + " " + getString(R.string.text_mins));
		// tvFeedbackAmount.setText(getString(R.string.text_price_unit)
		// + Double.parseDouble(driver.getBill().getTotal()));
		// tvTime.setText(driver.getLastTime());
		activity.btnNotification.setVisibility(View.GONE);
		btnSubmit.setOnClickListener(this);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (driver != null) {

			if (!TextUtils.isEmpty(driver.getPicture())) {
				new AQuery(activity).id(ivDriverImage).progress(R.id.pBar).image(driver.getPicture());
			} else {
				new AQuery(activity).id(ivDriverImage).progress(R.id.pBar).image(R.drawable.default_user);
			}
			tvClientName.setText(driver.getFirstName() + " " + driver.getLastName());
			activity.showBillDialog(driver.getBill().getTimeCost(), driver.getBill().getTotal(), driver.getBill()
					.getDistanceCost(), driver.getBill().getBasePrice(), driver.getBill().getTime(), driver.getBill()
					.getDistance(), driver.getBill().getPromoBouns(), driver.getBill().getReferralBouns(), driver
					.getBill().getPricePerDistance(), driver.getBill().getPricePerTime(), driver.getBill().getUnit(),
					"");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSubmit:
			if (isValidate()) {
				rating();
			} else
				AndyUtils.showToast(activity.getString(R.string.error_empty_rating), activity);
			break;
		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		if (rtBar.getRating() <= 0)
			return false;
		return true;
	}

	public void rating() {
		AndyUtils.showCustomProgressDialog(activity, getString(R.string.text_rating), false, null);
		HashMap<String, String> map = new HashMap<String, String>();
		PreferenceHelper preferenceHelper = new PreferenceHelper(getActivity());
		StringBuilder stringBuilder_rating = new StringBuilder();
		map.put(Const.URL, Const.ServiceType.RATE_KEVIN);

		stringBuilder_rating.append("FORMID|RATE|");
		stringBuilder_rating.append("TOKEN|" + preferenceHelper.getSessionToken() + "ǀ");
		stringBuilder_rating.append("MOBILENUMBER|" + preferenceHelper.getPhoneNumber() + "|");
		stringBuilder_rating.append("DRIVEREMAIL|" + preferenceHelper.getDriverId() + "|");
		stringBuilder_rating.append("DRIVERMOBILENUMBER|" + "254728591754" + "|");
		stringBuilder_rating.append("COMMENT|" + etComment.getText().toString() + "|");
		stringBuilder_rating.append("RATING|" + String.valueOf(rtBar.getRating()) + "ǀ");
		stringBuilder_rating.append("TRIPID|" + String.valueOf(preferenceHelper.getRequestId()) + "ǀ");

		String encrypted_rating_string = PreferenceHelper.eaes(stringBuilder_rating.toString());
		map.put("PLAIN_DATA", stringBuilder_rating.toString());
		map.put("DATA", encrypted_rating_string);

		/* LOGS */
		AppLog.Log("myrating", "responseplain::: " + stringBuilder_rating);
		AppLog.Log("myrating", "responseencrypted::: " + encrypted_rating_string);

		new HttpRequester(activity, map, Const.ServiceCode.RATING, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.uberorg.fragments.UberBaseFragment#onTaskCompleted(java.lang.String,
	 * int)
	 */
	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		switch (serviceCode) {
		case Const.ServiceCode.RATING:
			AndyUtils.removeCustomProgressDialog();

			String decrypted_response = PreferenceHelper.daes(response);
			AppLog.Log("myrating", "responsedecrypted::: " + decrypted_response);
			/*
			 * if (activity.pContent.isSuccess(response)) {
			 * activity.pHelper.clearRequestData(); JSONObject jsonObject; try {
			 * jsonObject = new JSONObject(response); PreferenceHelper
			 * preference = new PreferenceHelper(activity);
			 * preference.saveMyRating
			 * (jsonObject.getString(Const.Params.MYRATING));
			 * AppLog.Log("myratingsofar",
			 * jsonObject.getString(Const.Params.MYRATING)); } catch
			 * (JSONException e) { e.printStackTrace(); }
			 * 
			 * AndyUtils.showToast(getString(R.string.text_feedback_completed),
			 * activity); activity.gotoMapFragment(); }
			 */
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.fragments.BaseFragmentRegister#OnBackPressed()
	 */
}
