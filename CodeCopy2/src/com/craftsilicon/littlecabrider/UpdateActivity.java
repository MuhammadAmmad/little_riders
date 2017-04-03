package com.craftsilicon.littlecabrider;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.craftsilicon.littlecabrider.component.MyTitleFontTextView;
import com.craftsilicon.littlecabrider.db.DBHelper;
import com.craftsilicon.littlecabrider.models.User;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.R;

public class UpdateActivity extends Activity implements OnClickListener {

	ImageView ivProfile;
	private AQuery aQuery;
	private ImageOptions imageOptions;
	private MyTitleFontTextView tvTitle;
	String profileImageData = "";
		
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
		ivProfile 				 = (ImageView) findViewById(R.id.ivProfileProfile);	
		tvTitle 				 = (MyTitleFontTextView) findViewById(R.id.tvTitle);	
		aQuery 					 = new AQuery(this);
		imageOptions 			 = new ImageOptions();
		imageOptions.memCache 	 = true;
		imageOptions.fileCache 	 = true;
		imageOptions.targetWidth = 200;
		imageOptions.fallback 	 = R.drawable.default_user;
		
		DBHelper dbHelper = new DBHelper(getApplicationContext());
		final User user = dbHelper.getUser();
		if (user != null) {
			aQuery.id(ivProfile).progress(R.id.pBar).image(user.getPicture(), imageOptions);
			String title = "Hi there "+user.getFname()+"!\nPlease update your app from Google Play so that we can add better features";
			tvTitle.setText(title);
		}
		if(user !=null){
			aQuery.id(R.id.ivProfileProfile).image(user.getPicture(), true, true,
					200, 0, new BitmapAjaxCallback() {

						@Override
						public void callback(String url, ImageView iv, Bitmap bm,
								AjaxStatus status) {
							if (url != null && aQuery.getCachedFile(url) != null) {
								//AppLog.Log(TAG, "URL FROM AQUERY::" + url);
								profileImageData = aQuery.getCachedFile(url).getPath();
								//AppLog.Log(TAG, "URL path FROM AQUERY::" + url);
								iv.setImageBitmap(bm);
							}
						}
					});	
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignIn:
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
	}
}