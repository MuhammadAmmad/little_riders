package com.craftsilicon.littlecabrider.adapter;

import com.craftsilicon.littlecabrider.fragments.BankPaymentFragment;
import com.craftsilicon.littlecabrider.fragments.CardPaymentFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author guidovanrossum
 */
public class PaymentLayoutAdapter extends FragmentStatePagerAdapter {
	CharSequence tab_titles[];
	int number_of_tabs;

	/**
	 * Adapter constructor
	 * 
	 * @param fragmentManager
	 * @param Tab_titles
	 * @param Number_of_tabs
	 */
	public PaymentLayoutAdapter(FragmentManager fragmentManager,
			CharSequence Tab_titles[], int Number_of_tabs) {
		super(fragmentManager);
		this.tab_titles = Tab_titles;
		this.number_of_tabs = Number_of_tabs;
	}

	/**
	 * Returns the fragment at the respective position
	 */
	@Override
	public Fragment getItem(int position) {
		if (position == 0) {
			return new CardPaymentFragment();
		} else {
			return new BankPaymentFragment();
		}
	}

	/**
	 * Returns a page title for the tab at the respective position
	 */
	@Override
	public CharSequence getPageTitle(int position) {
		return tab_titles[position];
	}

	/**
	 * Returns the total count of the tabs
	 */
	@Override
	public int getCount() {
		return number_of_tabs;
	}

}
