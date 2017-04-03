package com.craftsilicon.littlecabrider.adapter;

import com.craftsilicon.littlecabrider.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BanksCustomAdapter extends ArrayAdapter<String> {
	private final String[] ACCOUNT_TYPES;
	private Activity CONTEXT;

	public BanksCustomAdapter(Activity context, String[] card_types) {
		super(context, R.layout.layout_bank_payments_list, card_types);
		// TODO Auto-generated constructor stub
		this.ACCOUNT_TYPES = card_types;
		this.CONTEXT = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = CONTEXT.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.layout_bank_payments_list,
				null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txtBankType);

		txtTitle.setText(ACCOUNT_TYPES[position]);

		return rowView;
	}

}
