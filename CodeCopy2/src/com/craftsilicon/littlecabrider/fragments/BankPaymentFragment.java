package com.craftsilicon.littlecabrider.fragments;

import com.craftsilicon.littlecabrider.R;
import com.craftsilicon.littlecabrider.component.MyFontButton;
import com.craftsilicon.littlecabrider.component.MyFontEdittextView;
import com.craftsilicon.littlecabrider.component.MyFontTextViewMedium;
import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class BankPaymentFragment extends Fragment implements OnClickListener{
	/**
	 * UI Elements
	 */
	ImageButton floatingActionButton;
	View layout_bank_payment;
	LinearLayout layout_list_view;
	LinearLayout layout_bottom;
	RelativeLayout layout_include_new_bank_details;
	RelativeLayout layout_include_edit_bank_details;
	MyFontButton button_skip_step;
	MyFontButton button_save_bank;
	MyFontButton button_finish;
	MyFontEdittextView txtBankName;
	MyFontEdittextView txtBankNickName;
	MyFontEdittextView txtBankAccountNumber;
	MyFontButton button_update_bank;
	MyFontEdittextView txtBankNameEdit;
	MyFontEdittextView txtBankNickNameEdit;
	MyFontEdittextView txtBankAccountNumberEdit;
	MyFontTextViewMedium txtNoBanks;
	ListView listview_bank_payments;

	/**
	 * Shared Preference
	 */
	PreferenceHelper preference_helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout_bank_payment = inflater.inflate(
				R.layout.layout_bank_payment, container, false);

		/**
		 * Instantiate PreferenceHelper
		 */
		preference_helper = new PreferenceHelper(getActivity());

		/**
		 * Referencing UI elements
		 */
		listview_bank_payments = (ListView) layout_bank_payment
				.findViewById(R.id.listView_bank_payment);
		txtNoBanks = (MyFontTextViewMedium)layout_bank_payment.findViewById(R.id.textNoBanks);
		button_finish = (MyFontButton) layout_bank_payment
				.findViewById(R.id.button_banks_finish);
		layout_list_view = (LinearLayout) layout_bank_payment
				.findViewById(R.id.layoutListViewBank);
		layout_bottom = (LinearLayout) layout_bank_payment
				.findViewById(R.id.layoutBottomBank);
		/* Load Bank Records From ELMA */
		getBankAccounts();
		
		AppLog.Log("mybankaccounts", preference_helper.getPaymentBanks());

		/**
		 * Set OnClickListener
		 */
		button_finish.setOnClickListener(this);

		/**
		 * UnSet focus on EditText boxes
		 */
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		return layout_bank_payment;
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		 if (v.getId() == R.id.button_banks_finish) {
			getActivity().onBackPressed();
		}
	}
	
	private void getBankAccounts(){
		String payment_modes = preference_helper.getPaymentBanks();
		String[] split_banks = payment_modes.split(",");
		AppLog.Log("mybankaccounts", payment_modes);
		
		if (payment_modes.isEmpty()) {
			listview_bank_payments.setVisibility(View.GONE);
			txtNoBanks.setVisibility(View.VISIBLE);
		}else{
			listview_bank_payments.setVisibility(View.VISIBLE);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_bank_payments_list, R.id.txtBankType, split_banks);
			listview_bank_payments.setAdapter(arrayAdapter);
		}
		
	}

}
