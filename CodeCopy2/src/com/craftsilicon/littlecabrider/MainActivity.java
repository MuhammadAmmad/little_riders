package com.craftsilicon.littlecabrider;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;

public class MainActivity extends Activity implements OnClickListener {

	/** Called when the activity is first created. */
	private MyFontButton btnSignIn, btnRegister;
	private PreferenceHelper pHelper;
	private boolean isReceiverRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		// Window window = getWindow();
		// window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		// window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// window.setStatusBarColor(getResources().getColor(
		// R.color.color_action_bar_main));
		//
		// }
		// Using StandardExceptionParser to get an Exception description.
		
		
		 /*try {
             String name = null;
             if (name.equals("eric")) {
                  Never comes here as it throws null pointer exception 
             }
         } catch (Exception e) {
             // Tracking exception
             GoogleAnalyticsApp.getInstance().trackException(e);
         }*/
		GoogleAnalyticsApp.getInstance().trackScreenView("MainActivity");
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		pHelper 	   = new PreferenceHelper(this);
		pHelper.putIMEI(telephonyManager.getDeviceId());
		//AndyUtils.log("myimei", telephonyManager.getDeviceId());
		String userid  = pHelper.getUserId();
		String tokenid = pHelper.getDeviceToken();
		
		AppLog.Log("gcmtoken", "tokenid = " + tokenid);
		if (!TextUtils.isEmpty(userid)) {//check user is logged in
			startActivity(new Intent(this, MainDrawerActivity.class));
			this.finish();
			return;
		}
		isReceiverRegister = false;
		 if (Build.VERSION.SDK_INT >= 21) {
	            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	            getWindow().setStatusBarColor(Color.TRANSPARENT);
	        }
		setContentView(R.layout.activity_main);

		btnSignIn = (MyFontButton) findViewById(R.id.btnSignIn);
		btnRegister = (MyFontButton) findViewById(R.id.btnRegister);
		// rlLoginRegisterLayout = (RelativeLayout) view
		// .findViewById(R.id.rlLoginRegisterLayout);
		// tvMainBottomView = (MyFontTextView) view
		// .findViewById(R.id.tvMainBottomView);
		btnSignIn.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		//AppLog.Log("gcmtoken", "tokenid = " + tokenid);
		if (TextUtils.isEmpty(tokenid)) {
			//check if tokenid is not there and register to get it
			isReceiverRegister = true;
			registerGcmReceiver(mHandleMessageReceiver);
		}else{
			openSignin();
		}
	}

	public void registerGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {
			AndyUtils.showCustomProgressDialog(this,getString(R.string.progress_loading), false, null);
			new GCMRegisterHendler(this, mHandleMessageReceiver);

		}
	}

	public void unregisterGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {
			if (mHandleMessageReceiver != null) {
				unregisterReceiver(mHandleMessageReceiver);
			}
		}
	}

	private BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			AndyUtils.removeCustomProgressDialog();
			
			if (intent.getAction().equals(CommonUtilities.DISPLAY_REGISTER_GCM)) {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {
					int resultCode = bundle.getInt(CommonUtilities.RESULT);
					if (resultCode == Activity.RESULT_OK) {

					} else {
						Toast.makeText(MainActivity.this,getString(R.string.register_gcm_failed),Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}		
			
			openSignin();
		}
	};
	
	private void openSignin(){
		Intent startRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
		startRegisterActivity.putExtra("isSignin", true);
		startActivity(startRegisterActivity);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	@Override
	public void onClick(View v) {
		Intent startRegisterActivity = new Intent(MainActivity.this, RegisterActivity.class);
		switch (v.getId()) {
		case R.id.btnSignIn:
			startRegisterActivity.putExtra("isSignin", true);
			break;
		case R.id.btnRegister:
			startRegisterActivity.putExtra("isSignin", false);
			break;
		}
		startActivity(startRegisterActivity);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		finish();
	}

	@Override
	public void onDestroy() {
		if (isReceiverRegister) {
			unregisterGcmReceiver(mHandleMessageReceiver);
			isReceiverRegister = false;
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		openExitDialog();
	}

	public void openExitDialog() {
		final Dialog mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.exit_layout);
		mDialog.setCancelable(false);
		mDialog.findViewById(R.id.tvExitOk).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mDialog.dismiss();
						finish();
						overridePendingTransition(R.anim.slide_in_left,
								R.anim.slide_out_right);

					}
				});
		mDialog.findViewById(R.id.tvExitCancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						mDialog.dismiss();
					}
				});
		mDialog.show();
	}
}