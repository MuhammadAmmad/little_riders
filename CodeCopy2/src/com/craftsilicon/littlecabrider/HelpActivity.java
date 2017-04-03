package com.craftsilicon.littlecabrider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HelpActivity extends ActionBarBaseActivitiy {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setIcon(0);
		setIconMenu(R.drawable.back);
		setTitle("Help");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnActionMenu:
			onBackPressed();
			break;

		default:
			break;
		}
	}

	@Override
	protected boolean isValidate() {
		return false;
	}

	public void showKeyboard(View v) {
		InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		// View view = activity.getCurrentFocus();
		// if (view != null) {
		inputManager.showSoftInput(v, InputMethodManager.SHOW_FORCED);
		// }
	}

}