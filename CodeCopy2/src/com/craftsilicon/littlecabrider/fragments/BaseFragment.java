package com.craftsilicon.littlecabrider.fragments;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;

import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.MainDrawerActivity;
import com.craftsilicon.littlecabrider.R;
import com.craftsilicon.littlecabrider.parse.AsyncTaskCompleteListener;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;

/**
 * @author Elluminati elluminati.in
 */
@SuppressLint("ValidFragment")
abstract public class BaseFragment extends Fragment implements OnClickListener,
		AsyncTaskCompleteListener {
	MainDrawerActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (MainDrawerActivity) getActivity();
	}

	protected abstract boolean isValidate();

	@Override
	public void onTaskCompleted(final String response, int serviceCode) {

	}

	protected ImageOptions getAqueryOption() {
		ImageOptions options = new ImageOptions();
		options.targetWidth = 200;
		options.memCache = true;
		options.fallback = R.drawable.default_user;
		options.fileCache = true;
		return options;
	}

	protected void login() {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.LOGIN);
		map.put(Const.Params.EMAIL, activity.pHelper.getEmail());
		map.put(Const.Params.PASSWORD, activity.pHelper.getPassword());
		map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
		map.put(Const.Params.DEVICE_TOKEN, activity.pHelper.getDeviceToken());
		map.put(Const.Params.LOGIN_BY, Const.MANUAL);
		new HttpRequester(activity, map, Const.ServiceCode.LOGIN, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.LOGIN, this, this));

	}

	protected void loginSocial(String id, String loginType) {
		if (!AndyUtils.isNetworkAvailable(activity)) {
			AndyUtils.showToast(getResources().getString(R.string.no_internet),
					activity);
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Const.URL, Const.ServiceType.LOGIN);
		map.put(Const.Params.SOCIAL_UNIQUE_ID, id);
		map.put(Const.Params.DEVICE_TYPE, Const.DEVICE_TYPE_ANDROID);
		map.put(Const.Params.DEVICE_TOKEN,
				new PreferenceHelper(activity).getDeviceToken());
		map.put(Const.Params.LOGIN_BY, loginType);
		new HttpRequester(activity, map, Const.ServiceCode.LOGIN, this);
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.LOGIN, this, this));
	}

}
