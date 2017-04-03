package com.craftsilicon.littlecabrider;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Toast;

import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.fragments.RegisterFragment;
import com.craftsilicon.littlecabrider.fragments.SignInFragment;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

public class XMainActivity extends ActionBarBaseActivitiy implements OnClickListener {

	/** Called when the activity is first created. */
	private MyFontButton btnSignIn, btnRegister;
	private boolean isReceiverRegister;
	
	Permission[] permissions = new Permission[] { Permission.EMAIL };
	SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
			.setPermissions(permissions).build();
	public PreferenceHelper pHelper;

	// private int oldOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pHelper = new PreferenceHelper(this);
		String userid = pHelper.getUserId();
		String tokenid = pHelper.getDeviceToken();

		if (!TextUtils.isEmpty(userid)) {// check user is logged in
			startActivity(new Intent(this, MainDrawerActivity.class));
			this.finish();
			return;
		}

		isReceiverRegister = false;
		setContentView(R.layout.activity_main);

		btnSignIn = (MyFontButton) findViewById(R.id.btnSignIn);
		btnRegister = (MyFontButton) findViewById(R.id.btnRegister);
		btnSignIn.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		if (TextUtils.isEmpty(tokenid)) {
			// check if tokenid is not there and register to get it
			isReceiverRegister = true;
			registerGcmReceiver(mHandleMessageReceiver);
		}
	}
	@Override
	protected boolean isValidate(){
		return false;
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
						Toast.makeText(XMainActivity.this,
								getString(R.string.register_gcm_failed),
								Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		// Refactor all these
		switch (v.getId()) {
		case R.id.btnSignIn:
		// Call SignIn Fragment
			gotSignInFragment();
			break;
		case R.id.btnRegister:
		// Call Register Fragment
			goToRegisterFragment();
			break;
		}
//		startActivity(startRegisterActivity);
//		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//		finish();
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

	@Override
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
	
	private void gotSignInFragment() {
		SignInFragment signInFrag = new SignInFragment();
		clearBackStack();
		addFragment(signInFrag, false, Const.FRAGMENT_SIGNIN);
	}
	
	private void goToRegisterFragment() {
		RegisterFragment regFrag = new RegisterFragment();
		clearBackStack();
		addFragment(regFrag, false, Const.FRAGMENT_REGISTER);
	}
	

	public void showKeyboard(View v) {
		InputMethodManager inputManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		// View view = activity.getCurrentFocus();
		// if (view != null) {
		inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
		// }
	}

	
}