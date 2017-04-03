package com.craftsilicon.littlecabrider;

import com.craftsilicon.littlecabrider.adapter.PaymentLayoutAdapter;
import com.craftsilicon.littlecabrider.component.SlidingTabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

public class PaymentsActivity extends ActionBarBaseActivitiy {
	/**
	 * Variables
	 */
	SlidingTabLayout slidingTabLayout;
	ViewPager viewPager;
    PaymentLayoutAdapter paymentLayoutAdapter;
    CharSequence Titles[] = {"Card", "Bank Payment"};
    int NumberOfTabs = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payments);
		actionBar.hide();
		/**
		 * Referencing UI elements
		 */
		paymentLayoutAdapter = new PaymentLayoutAdapter(getSupportFragmentManager(), Titles, NumberOfTabs);	
		viewPager = (ViewPager) findViewById(R.id.view_pager_payment);
        viewPager.setAdapter(paymentLayoutAdapter);
        
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout_payment);
        slidingTabLayout.setDistributeEvenly(true);
        
        /**
         * Setting up custom color for the SlidingTabStrip
         */
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        
        /**
         * Setting up SlidingTabLayout with a ViewPager
         */
        slidingTabLayout.setViewPager(viewPager);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isValidate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
}
