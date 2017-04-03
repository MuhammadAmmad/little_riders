package com.craftsilicon.littlecabrider;

import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class OTPValidationActivity extends ActionBarActivity {
	/* UI Elements*/
	MyFontEdittextView txtFirstPassCode;
	MyFontEdittextView txtSecondPassCode;
	MyFontButton btnSubmit;
	
	/* UI Elements String Variables*/
	String text_first_passcode, text_second_passcode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otpvalidation);
		
		/**
		 * Referencing UI elements
		 */
		txtFirstPassCode = (MyFontEdittextView) findViewById(R.id.edtpasscode1);
		txtSecondPassCode = (MyFontEdittextView) findViewById(R.id.edtpasscode2);
		btnSubmit = (MyFontButton) findViewById(R.id.btn_submit);
		
		/**
		 * Assigning values to string variables
		 */
		text_first_passcode = txtFirstPassCode.getText().toString();
		text_second_passcode = txtSecondPassCode.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.otpvalidation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
