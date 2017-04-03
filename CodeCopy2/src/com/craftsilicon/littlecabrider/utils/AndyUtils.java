package com.craftsilicon.littlecabrider.utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.craftsilicon.littlecabrider.db.DBAdapter;
import com.craftsilicon.littlecabrider.interfaces.OnProgressCancelListener;
import com.craftsilicon.littlecabrider.models.Driver;
import com.craftsilicon.littlecabrider.parse.HttpRequester;
import com.craftsilicon.littlecabrider.MainDrawerActivity;
import com.craftsilicon.littlecabrider.R;

/**
 * @author Elluminati elluminati.in
 */
@SuppressLint("NewApi")
public class AndyUtils {
	static float density = 1;
	private static ProgressDialog mProgressDialog;
	public static Dialog mDialog;
	private static OnProgressCancelListener progressCancelListener;
	// private int driverId = 0;
	private static ImageView imageView;
	private static Animation anim;
	static String[] AMFieldValues;
	static String[] AMFieldID;
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		return display.getWidth();
	}
	static public String urlEncode(String sUrl)
	{
		StringBuffer urlOK = new StringBuffer();
		for(int i=0; i<sUrl.length(); i++)
		{
			char ch=sUrl.charAt(i);
			switch(ch)
			{
				case '<': urlOK.append("%3C"); break;
				case '>': urlOK.append("%3E"); break;
				case '/': urlOK.append("%2F"); break;
				case ' ': urlOK.append("%20"); break;
				case ':': urlOK.append("%3A"); break;
				case '-': urlOK.append("%2D"); break;
				case '+': urlOK.append("%2B"); break;
				default: urlOK.append(ch); break;
			}
		}
		return urlOK.toString();
	}

	public static void showSimpleProgressDialog(Context context, String title,
			String msg, boolean isCancelable) {
		try {
			if (mProgressDialog == null) {
				mProgressDialog = ProgressDialog.show(context, title, msg);
				mProgressDialog.setCancelable(isCancelable);
			}

			if (!mProgressDialog.isShowing()) {
				mProgressDialog.show();
			}

		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showSimpleProgressDialog(Context context) {
		showSimpleProgressDialog(context, null, "Loading...", false);
	}

	public static void removeSimpleProgressDialog() {
		try {
			if (mProgressDialog != null) {
				if (mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
					mProgressDialog = null;
				}
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void showNetworkErrorMessage(final Context context) {
		Builder dlg = new AlertDialog.Builder(context);
		dlg.setCancelable(false);
		dlg.setTitle("Error");
		dlg.setMessage("Network error has occured. Please check the network status of your phone and retry");
		dlg.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		dlg.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
				System.exit(0);
			}
		});
		dlg.show();
	}

	public static void showOkDialog(String title, String msg, Activity act) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(act);
		if (title != null) {

			TextView dialogTitle = new TextView(act);
			dialogTitle.setText(title);
			dialogTitle.setPadding(10, 10, 10, 10);
			dialogTitle.setGravity(Gravity.CENTER);
			dialogTitle.setTextColor(Color.WHITE);
			dialogTitle.setTextSize(20);
			dialog.setCustomTitle(dialogTitle);

		}
		if (msg != null) {
			dialog.setMessage(msg);
		}
		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dlg = dialog.show();
		TextView messageText = (TextView) dlg
				.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);

	}

	public static float getDisplayMetricsDensity(Context context) {
		density = context.getResources().getDisplayMetrics().density;

		return density;
	}

	public static int getPixel(Context context, int p) {
		if (density != 1) {
			return (int) (p * density + 0.5);
		}
		return p;
	}

	public static Animation FadeAnimation(float nFromFade, float nToFade) {
		Animation fadeAnimation = new AlphaAnimation(nToFade, nToFade);

		return fadeAnimation;
	}

	public static Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromRight;
	}

	public static Animation inFromLeftAnimation() {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromLeft;
	}

	public static Animation inFromBottomAnimation() {
		Animation inFromBottom = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return inFromBottom;
	}

	public static Animation outToLeftAnimation() {
		Animation outToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToLeft;
	}

	public static Animation outToRightAnimation() {
		Animation outToRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);

		return outToRight;
	}

	public static Animation outToBottomAnimation() {
		Animation outToBottom = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f);

		return outToBottom;
	}

	public static boolean isNetworkAvailable(Activity activity) {
		ConnectivityManager connectivity = (ConnectivityManager) activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean eMailValidation(String emailstring) {
		if (null == emailstring || emailstring.length() == 0) {
			return false;
		}
		Pattern emailPattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
						+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	public static String urlBuilderForGetMethod(Map<String, String> g_map) {

		StringBuilder sbr = new StringBuilder();
		int i = 0;
		if (g_map.containsKey("url")) {
			sbr.append(g_map.get("url"));
			g_map.remove("url");
		}
		for (String key : g_map.keySet()) {
			if (i != 0) {
				sbr.append("&");
			}
			sbr.append(key + "=" + g_map.get(key));
			i++;
		}
		System.out.println("Builder url = " + sbr.toString());
		return sbr.toString();
	}

	public static int isValidate(String... fields) {
		if (fields == null) {
			return 0;
		}

		for (int i = 0; i < fields.length; i++) {
			if (TextUtils.isEmpty(fields[i])) {
				return i;
			}

		}
		return -1;
	}

	public static void showToast(String msg, Context ctx) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	public static String UppercaseFirstLetters(String str) {
		boolean prevWasWhiteSp = true;
		char[] chars = str.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isLetter(chars[i])) {
				if (prevWasWhiteSp) {
					chars[i] = Character.toUpperCase(chars[i]);
				}
				prevWasWhiteSp = false;
			} else {
				prevWasWhiteSp = Character.isWhitespace(chars[i]);
			}
		}
		return new String(chars);
	}

	public static void buttonEffect(ImageButton button, final int alpha) {

		button.setOnTouchListener(new OnTouchListener() {

			@Override
			@SuppressWarnings("deprecation")
			public boolean onTouch(View v, MotionEvent event) {
				ImageButton btn = (ImageButton) v;
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: {

					if (Build.VERSION.SDK_INT > 15) {
						btn.setImageAlpha(alpha);

					} else {
						btn.setAlpha(alpha);
					}

					break;
				}
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:

					if (Build.VERSION.SDK_INT > 15) {

						btn.setImageAlpha(255);
					} else {
						btn.setAlpha(255);
					}

					break;
				}
				return false;
			}
		});

	}

	public static final SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	}

	public static String convertToUTF8(String s) {
		String out = null;
		try {
			out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return out;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}
	

	public static void showCustomProgressDialog(Context context, String title,
			boolean iscancelable,
			OnProgressCancelListener progressCancelListener) {
		if ((mDialog != null && mDialog.isShowing()) || (context == null)|| (title == null))
			return;
		LinearLayout llLoading_bg;
		AndyUtils.progressCancelListener = progressCancelListener;

		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		imageView 	 = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
		llLoading_bg = (LinearLayout) mDialog.findViewById(R.id.llLoading_bg);
		
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
			if(title.contains("location")||title.contains("fare")){
				llLoading_bg.setBackground(null);
			}
		}
		RelativeLayout includeDriver = (RelativeLayout) mDialog
				.findViewById(R.id.includeDriver);
		includeDriver.setVisibility(View.GONE);
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		anim = AnimationUtils.loadAnimation(context, R.anim.scale_updown);
		imageView.startAnimation(anim);
		mDialog.setCancelable(iscancelable);
		mDialog.show();
	}
	
	private void cancleRequest() {
		
		// requestQueue.add(new VolleyHttpRequest(Method.POST, map,
		// Const.ServiceCode.CANCEL_REQUEST, this, this));
	}

	public static void showCustomProgressDialog(Context context, String title,
			boolean iscancelable,
			OnProgressCancelListener progressCancelListener, Driver driver) {
		if (mDialog != null && mDialog.isShowing())
			return;
		AndyUtils.progressCancelListener = progressCancelListener;
		ImageOptions imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.default_user;
		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		imageView = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		RelativeLayout includeDriver = (RelativeLayout) mDialog
				.findViewById(R.id.includeDriver);
		if (driver == null)
			includeDriver.setVisibility(View.GONE);
		else {
			includeDriver.setVisibility(View.VISIBLE);
			ImageView ivDriverPhoto = (ImageView) mDialog
					.findViewById(R.id.ivDriverPhoto);
			TextView tvDriverName = (TextView) mDialog
					.findViewById(R.id.tvDriverName);
			TextView tvTaxiModel = (TextView) mDialog
					.findViewById(R.id.tvTaxiModel);
			TextView tvTaxiNo = (TextView) mDialog.findViewById(R.id.tvTaxiNo);
			TextView tvRateStar = (TextView) mDialog
					.findViewById(R.id.tvRateStar);

			new AQuery(context).id(ivDriverPhoto).progress(R.id.pBar)
					.image(driver.getPicture(), imageOptions);
			tvDriverName.setText(driver.getFirstName() + " "
					+ driver.getLastName());
			tvTaxiModel.setText(driver.getCarModel());
			tvTaxiNo.setText(driver.getCarNumber());
			tvRateStar.setText(MathUtils.getRound((float) driver.getRating())
					+ "");
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		anim = AnimationUtils.loadAnimation(context, R.anim.scale_updown);
		imageView.startAnimation(anim);
		mDialog.setCancelable(iscancelable);
		mDialog.show();
	}

	public static void removeCustomProgressDialog() {
		try {
			AndyUtils.progressCancelListener = null;
			if (mDialog != null && imageView != null) {
				// if (mProgressDialog.isShowing()) {
				imageView.clearAnimation();
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showCustomProgressRequestDialog(Context context,
			String title, boolean iscancelable,
			OnProgressCancelListener progressCancelListener) {
		if (mDialog != null && mDialog.isShowing())
			return;
		AndyUtils.progressCancelListener = progressCancelListener;

		mDialog = new Dialog(context, R.style.MyDialog);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_bar_cancel);

		imageView = (ImageView) mDialog.findViewById(R.id.ivProgressBar);
		if (!TextUtils.isEmpty(title)) {
			TextView tvTitle = (TextView) mDialog.findViewById(R.id.tvTitle);
			tvTitle.setText(title);
		}
		Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
		if (AndyUtils.progressCancelListener == null) {
			btnCancel.setVisibility(View.GONE);
		} else {
			btnCancel.setVisibility(View.VISIBLE);
		}
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		anim = AnimationUtils.loadAnimation(context, R.anim.scale_updown);
		imageView.startAnimation(anim);
		mDialog.setCancelable(iscancelable);
		mDialog.show();
	}

	public static void removeCustomProgressRequestDialog() {
		try {
			AndyUtils.progressCancelListener = null;
			if (mDialog != null && imageView != null) {
				// if (mProgressDialog.isShowing()) {
				imageView.clearAnimation();
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String encodeTobase64(Bitmap image) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
		byte[] bs = outputStream.toByteArray();
		String imageEncoded = Base64.encodeBytes(bs);
		return imageEncoded;
	}

	public static void showErrorToast(int id, Context ctx) {
		String msg = "";
		String error_code = Const.ERROR_CODE_PREFIX + id;
		msg = ctx.getResources().getString(ctx.getResources().getIdentifier(error_code, "string", ctx.getPackageName()));
		if(msg!=null){
			if(msg.length()>0){
				Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();		
			}	
		}
			
		if (msg.contains("not") && msg.contains("valid")&&msg.contains("token")){
			PreferenceHelper pHelper = new PreferenceHelper(ctx);
			pHelper.putIsLoggedIn(false);
		}
	}

	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels,
				displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
				view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	public static void showDriverDetailDialog(Context context, OnProgressCancelListener progressCancelListener, Driver driver) {

		if (context == null)
			return;
		AndyUtils.progressCancelListener = progressCancelListener;

		ImageOptions imageOptions = new ImageOptions();
		imageOptions.fileCache = true;
		imageOptions.memCache = true;
		imageOptions.fallback = R.drawable.default_user;

		mDialog = new Dialog(context);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		mDialog.setContentView(R.layout.progress_driverresponse);
		mDialog.setCancelable(false);
		AppLog.Log("driver", "" + driver);

		ImageView ivDriverPhotoDialog;
		TextView tvDriverNameDialog ;
		if (mDialog != null && mDialog.isShowing()) {
			 ivDriverPhotoDialog = (ImageView) mDialog.findViewById(R.id.ivDriverPhotoDialog);
			 tvDriverNameDialog = (TextView) mDialog.findViewById(R.id.tvDriverNameDialog);

			new AQuery(context).id(ivDriverPhotoDialog).progress(R.id.pBar).image(driver.getPicture(), imageOptions);
			tvDriverNameDialog.setText(driver.getFirstName() + " " + driver.getLastName());
			return;
		} else {
			ivDriverPhotoDialog = (ImageView) mDialog.findViewById(R.id.ivDriverPhotoDialog);
			tvDriverNameDialog = (TextView) mDialog.findViewById(R.id.tvDriverNameDialog);

			new AQuery(context).id(ivDriverPhotoDialog).progress(R.id.pBar).image(driver.getPicture(), imageOptions);
			tvDriverNameDialog.setText(driver.getFirstName() + " "+ driver.getLastName());
		}
		ivDriverPhotoDialog.setVisibility(View.VISIBLE);
		tvDriverNameDialog.setVisibility(View.VISIBLE);
		TextView tvDriverDetailCancel = (TextView) mDialog.findViewById(R.id.tvDriverDetailCancel);

		tvDriverDetailCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AndyUtils.progressCancelListener != null) {
					AndyUtils.progressCancelListener.onProgressCancel();
				}
			}
		});
		mDialog.show();
	}

	public static void log(String activity, String text){
		//Log.d(activity, "-"+text);
		//if(activity.equals("gCoder.getFromLocation")){
			//appendLog(activity, text);
		//}
		if (Const.isLogging) {
			Log.d(activity, "-"+text);
			//appendLog(activity, text);
			//
		}
		
		
	}
	public static void appendLog2(String activity, String text2)
    {
        File logFile = new File("sdcard/unknownlog.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
    		Calendar c  = Calendar.getInstance(); 
    		int seconds = c.get(Calendar.SECOND);
    		int minute  = c.get(Calendar.MINUTE);
    		int hour    = c.get(Calendar.HOUR_OF_DAY);
    		int year 	= c.get(Calendar.YEAR);
    		int month 	= c.get(Calendar.MONTH);
    		int day 	= c.get(Calendar.DAY_OF_MONTH);
    		
        	String text1 = "\n--------------------------------------------\n";
        	String date = " Date:"+day+"-"+(month+1)+"-"+year+" Time:"+hour+":"+minute+":"+seconds+" ";
        	String text = text1+date+activity+text1+"\n"+text2+text1+"\n";
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	public static void appendLog(String activity, String text2)
    {
        File logFile = new File("sdcard/unknown.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        try
        {
    		Calendar c  = Calendar.getInstance(); 
    		int seconds = c.get(Calendar.SECOND);
    		int minute  = c.get(Calendar.MINUTE);
    		int hour    = c.get(Calendar.HOUR_OF_DAY);
    		int year 	= c.get(Calendar.YEAR);
    		int month 	= c.get(Calendar.MONTH);
    		int day 	= c.get(Calendar.DAY_OF_MONTH);
    		
        	String text1 = "\n--------------------------------------------\n";
        	String date = " Date:"+day+"-"+(month+1)+"-"+year+" Time:"+hour+":"+minute+":"+seconds+" ";
        	String text = text1+date+activity+text1+"\n"+text2+text1+"\n";
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
	public static void removeDriverDetailDialog() {
		try {
			AndyUtils.progressCancelListener = null;
			if (mDialog != null) {
				// if (mProgressDialog.isShowing()) {
				mDialog.dismiss();
				mDialog = null;
				// }
			}
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();

		} catch (RuntimeException re) {
			re.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String comaReplaceWithDot(String value) {

		value = value.replace(',', '.');
		return value;
		// DecimalFormat decimalFormat=new DecimalFormat("#.##");
		// DecimalFormatSymbols symbol=new DecimalFormatSymbols();
		// symbol.setDecimalSeparator('.');
		// decimalFormat.setDecimalFormatSymbols(symbol);
		// return decimalFormat.format(value);
	}
	public String[] split(String Data, String Separator) {
        if (Data == null)
            return new String[] {};

        Vector Chunks = new Vector();
        String Stuff  = "";

        while (Data.indexOf(Separator) != -1) {
            Stuff = Data.substring(0, Data.indexOf(Separator)).trim();
            Chunks.addElement(Stuff);
            Data = Data.substring(Data.indexOf(Separator) + Separator.length());
        }
        // if(!Data.trim().equals(""))
        Chunks.addElement(Data.trim());

        String[] Retval = new String[Chunks.size()];
        Chunks.copyInto(Retval);

        return Retval;
    }

    public String[] splitNoEmpty(String Data, String Separator) {
        if (Data == null)
            return new String[] {};

        Vector Chunks = new Vector();
        String Stuff = "";

        while (Data.indexOf(Separator) != -1) {
            Stuff = Data.substring(0, Data.indexOf(Separator)).trim();
            Chunks.addElement(Stuff);
            Data = Data.substring(Data.indexOf(Separator) + Separator.length());
        }
        if (!Data.trim().equals(""))
            Chunks.addElement(Data.trim());

        String[] Retval = new String[Chunks.size()];
        Chunks.copyInto(Retval);

        return Retval;
    }

    public static String FindInArray(String[] ArrayID, String[] ArrayValue,
                              String StringToFind) {
        int i = 0;
        if (StringToFind == "") {
            return "";
        }

        StringToFind = StringToFind.toUpperCase();

        for (i = 0; i < ArrayID.length; i++) {
            try {
                if (ArrayID[i].equals(StringToFind)) {
                    return ArrayValue[i];
                }
            } catch (Exception e) {
                // TODO: handle exception
            }

        }
        return "";
    }

    public static void processTags(String thestring) {
        String RequestDetails = thestring;
        String howlong[] = RequestDetails.split("|");
        String FieldIDs[] = new String[howlong.length / 2];
        String FieldValues[] = new String[howlong.length / 2];

        howlong = null;

        try {

            String Output;
            int i;
            int j = 0;

            while (RequestDetails.length() > 0) {
                i = RequestDetails.indexOf('|');
                Output = RequestDetails.substring(0, i);
                FieldIDs[j] = Output;
                RequestDetails = RequestDetails.substring(i + 1);

                i = RequestDetails.indexOf('|');

                if (i < 0) {
                    Output = RequestDetails;
                    RequestDetails = "";
                } else {
                    Output = RequestDetails.substring(0, i);
                    RequestDetails = RequestDetails.substring(i + 1);
                }
                FieldValues[j] = Output;

                j++;
            }

            AMFieldID = FieldIDs;
            AMFieldValues = FieldValues;
        } catch (Exception e) {

        }

    }

    public static String[] getValuesArray() {
        return AMFieldValues;
    }

    public static String[] getFieldIDArray() {
        return AMFieldID;
    }
	public static void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String dbName = DBAdapter.DATABASE_NAME;
        String currentDBPath = "/data/" + "com.craftsilicon.littlecabrider" + "/databases/"+dbName;
        String backupDBPath = ""+dbName;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.d("dbexported","--");
        } catch (IOException e) {
            Log.d("dbexportedError","-"+e);
        }
    }
}
