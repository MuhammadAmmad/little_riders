package com.craftsilicon.littlecabrider;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.craftsilicon.littlecabrider.fragments.FeedbackFragment;
import com.craftsilicon.littlecabrider.fragments.RegisterFragment;
import com.craftsilicon.littlecabrider.fragments.SignInFragment;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

/**
 * @author Elluminati elluminati.in
 */
public class RegisterActivity extends ActionBarBaseActivitiy {

	Permission[] permissions = new Permission[] { Permission.EMAIL };
	SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
			.setPermissions(permissions).build();
	public PreferenceHelper phelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		actionBar.hide();
		
		setContentView(R.layout.register_activity);
		SimpleFacebook.setConfiguration(configuration);
		phelper = new PreferenceHelper(this);
		// actionBar.setDisplayShowHomeEnabled(true);
		// actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle("");
		setIcon(R.drawable.back);
		
		
		if (getIntent().getBooleanExtra("isSignin", false)) {
			gotSignInFragment();
		} else {
			goToRegisterFragment();
		}
		// UberMainFragment mainFrag = new UberMainFragment();
		// addFragment(mainFrag, false, Const.FRAGMENT_MAIN);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActionNotification:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	public void registerGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {
			AndyUtils.showCustomProgressDialog(this,
					getString(R.string.progress_loading), false, null);
			new GCMRegisterHendler(RegisterActivity.this,mHandleMessageReceiver);

		}
	}

	public void unregisterGcmReceiver(BroadcastReceiver mHandleMessageReceiver) {
		if (mHandleMessageReceiver != null) {

			if (mHandleMessageReceiver != null) {
				unregisterReceiver(mHandleMessageReceiver);
			}
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.uberorg.ActionBarBaseActivitiy#isValidate()
	 */
	@Override
	protected boolean isValidate() {
		return false;
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