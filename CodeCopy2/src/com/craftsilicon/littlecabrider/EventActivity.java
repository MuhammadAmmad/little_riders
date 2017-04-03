/**
 * 
 */
package com.craftsilicon.littlecabrider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.craftsilicon.littlecabrider.adapter.EventAdapter;
import com.craftsilicon.littlecabrider.models.Event;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.parse.ParseContent;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;

/**
 * @author Elluminati elluminati.in
 * 
 */
public class EventActivity extends ActionBarBaseActivitiy implements
		OnItemClickListener {
	private TreeSet<Integer> mSeparatorsSet = new TreeSet<Integer>();
	private EventAdapter eventAdapter;
	private ArrayList<Event> eventList;
	// private ArrayList<History> historyListOrg;
	private PreferenceHelper preferenceHelper;
	private ParseContent parseContent;
	private ImageView tvNoHistory;
	private ArrayList<Date> dateList = new ArrayList<Date>();
	private TextView fromDateBtn;
	private TextView toDateBtn;
	Calendar cal = Calendar.getInstance();
	int day;
	int month;
	int year;
	DatePickerDialog fromPiker;
	private OnDateSetListener dateset;
	DatePickerDialog toPiker;
	Date fromDate, toDate;
	private String userDate;
	private ParseContent pContent;
	private ListView lvEvents;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		setIconMenu(R.drawable.back);
		setTitle(getString(R.string.text_event));
		btnNotification.setVisibility(View.GONE);
		pContent = new ParseContent(this);
		lvEvents = (ListView) findViewById(R.id.lvEvents);
		lvEvents.setOnItemClickListener(this);
		// fromDateBtn = (TextView) findViewById(R.id.fromDateBtn);
		// toDateBtn = (TextView) findViewById(R.id.toDateBtn);
		// fromDateBtn.setOnClickListener(this);
		// toDateBtn.setOnClickListener(this);
		eventList = new ArrayList<Event>();
		//
		tvNoHistory = (ImageView) findViewById(R.id.ivEmptyView);

		preferenceHelper = new PreferenceHelper(this);
		findViewById(R.id.btnAddEvent).setOnClickListener(this);
		// parseContent = new ParseContent(this);
		// dateList = new ArrayList<Date>();
		// historyListOrg = new ArrayList<History>();
		//
		// // historyAdapter = new HistoryAdapter(this, historyListOrg,
		// // mSeparatorsSet);
		// // lvHistory.setAdapter(historyAdapter);
		//
		// day = cal.get(Calendar.DAY_OF_MONTH);
		// month = cal.get(Calendar.MONTH);
		// year = cal.get(Calendar.YEAR);
		// fromDate = new Date();
		// toDate = new Date();
		//

		//
		// dateset = new OnDateSetListener() {
		//
		// @Override
		// public void onDateSet(DatePicker view, int year, int monthOfYear,
		// int dayOfMonth) {
		// userDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
		//
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		//
		// try {
		// if (view == fromPiker.getDatePicker()) {
		// fromDateBtn.setText(userDate);
		// fromDate = sdf.parse(userDate);
		// } else {
		// toDateBtn.setText(userDate);
		// toDate = sdf.parse(userDate);
		// }
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		//
		// }
		// };
		// fromPiker = new DatePickerDialog(this, dateset, year, month, day);
		// fromPiker.getDatePicker().setMaxDate(System.currentTimeMillis());
		// toPiker = new DatePickerDialog(this, dateset, year, month, day);
		// toPiker.getDatePicker().setMaxDate(System.currentTimeMillis());

	}

	@Override
	public void onTaskCompleted(String response, int serviceCode) {
		switch (serviceCode) {
		case Const.ServiceCode.GET_EVENT:
			AndyUtils.removeCustomProgressDialog();
			AppLog.Log("", "Get Events Response : " + response);
			if (!pContent.isSuccess(response)) {
				return;
			}
			eventList.clear();
			// dateList.clear();
			try {
				pContent.parseEvent(response, eventList);

				if (eventList.size() > 0) {
					lvEvents.setVisibility(View.VISIBLE);
					tvNoHistory.setVisibility(View.GONE);
				} else {
					lvEvents.setVisibility(View.GONE);
					tvNoHistory.setVisibility(View.VISIBLE);
				}
				eventAdapter = new EventAdapter(this, eventList);
				lvEvents.setAdapter(eventAdapter);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void getEvents() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.GET_EVENT);
		map.put(Const.Params.ID, preferenceHelper.getUserId());
		map.put(Const.Params.TOKEN, preferenceHelper.getSessionToken());

		AndyUtils.showCustomProgressDialog(this,
				getString(R.string.text_getting_event), true, null);

		new HttpRequester(this, map, Const.ServiceCode.GET_EVENT, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.GET_EVENT, this, this));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActionMenu:
			onBackPressed();
			break;

		case R.id.btnAddEvent:
			startActivity(new Intent(this, AddEventActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Event selectedEvent = eventList.get(arg2);
		Intent intent = new Intent(this, EventDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("event", selectedEvent);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	protected boolean isValidate() {
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getEvents();
	}
}
