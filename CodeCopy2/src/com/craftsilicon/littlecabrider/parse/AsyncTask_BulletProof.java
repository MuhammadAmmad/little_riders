package com.craftsilicon.littlecabrider.parse;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.craftsilicon.littlecabrider.utils.AndyUtils;
import com.craftsilicon.littlecabrider.utils.AppLog;
import com.craftsilicon.littlecabrider.utils.Const;
import com.craftsilicon.littlecabrider.utils.PreferenceHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

@SuppressLint("NewApi")
public class AsyncTask_BulletProof extends AsyncTask<String, String, String> {
	private Context context;
	String fromActivity = "";
	String fromMenu     = "";
	PreferenceHelper am;
	public AsyncTask_BulletProof(Context context) {
		this.context = context;
		am = new PreferenceHelper(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... urls) {
		String QueryString 		 = "";
		String ReturnData  		 = "";
		QueryString 	   		 = urls[1];

		log("myregistrationfromActivity: "+fromActivity);
		log("myregistrationquerystring: "+QueryString);
		AppLog.Log("myregistrationencrpyt::::", "querystring::: "+QueryString);
		
		try{
			fromActivity 	   		 = urls[0];
			fromMenu		   		 = urls[2];
		}catch (Exception e){
			log("varags Exception: "+e);
		}
		log("fromActivity: "+fromActivity);
		log("fromMenu: "+fromMenu);
		String URLCommand 		 = Const.ServiceType.ELMA_ADD_CARD_URL;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httppost 		 = new HttpPost(URLCommand);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		try {
			Random RandomNumber = new Random();
			//String Random_string = String.valueOf(RandomNumber.nextInt());
			//nameValuePairs.add(new BasicNameValuePair("l",Random_string));
			//nameValuePairs.add(new BasicNameValuePair(Random_string,QueryString));
			
			nameValuePairs.add(new BasicNameValuePair("DATA",QueryString));
		} catch (Exception e) {

		}

		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse execute 	= client.execute(httppost);
			InputStream content 	= execute.getEntity().getContent();
			BufferedReader buffer 	= new BufferedReader(new InputStreamReader(content));
			String s 				= "";
			while ((s = buffer.readLine()) != null) {
				ReturnData += s;
			}
		} catch (Exception e) {
		}
		return ReturnData;
	}

	@Override
	protected void onPostExecute(String ReturnData) {
		AppLog.Log("myregistrationreturneddata::: ", "returneddata::: "+ ReturnData);

		if (!ReturnData.equals("")) {
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(new StringReader(ReturnData));
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.TEXT) {
						ReturnData = xpp.getText();
					}
					eventType = xpp.next();
				}
			} catch (Exception e) {
			}

			if (ReturnData.length() < 3) {
			//log("returned: less tha 3"+ret);
			} else {

				try {
					//byte[] url = Base64Utility.decode(ReturnData, Base64Utility.DEFAULT);
					byte[] bytes = PreferenceHelper.daes(ReturnData).getBytes("UTF-8");
					//String text = new String(bytes, "UTF-8");
					ReturnData = new String(bytes, "UTF-8");
					//am.appendLog("reply_gotnow|KEY|" + am.getkv() + "|IV|" + am.getiv(), ReturnData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				AppLog.Log("myreturneddata::: ", "decrypted::: " + ReturnData);
				String howlong[] 	  = ReturnData.split("|");
				String RequestDetails = ReturnData;
				String FieldIDs[] 	  = new String[howlong.length / 2];
				String FieldValues[]  = new String[howlong.length / 2];
				try {
					String Output = "";
					int i = 0, j = 0;

					while (RequestDetails.length() > 0) {
						i 				= RequestDetails.indexOf('|');
						Output 			= RequestDetails.substring(0, i);
						FieldIDs[j] 	= Output;
						RequestDetails  = RequestDetails.substring(i + 1);
						i 				= RequestDetails.indexOf('|');
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
				} catch (Exception e) {

				}
				log("fromActivity:" + fromActivity + ":returned:" + ReturnData);
				if(fromMenu.contains("GETAUTHSMS")){
					String status = FindInArray(FieldIDs, FieldValues, "STATUS");
					if (status.equals("000")) {
						PreferenceHelper ph = new PreferenceHelper(context);
						String SMSCODE = FindInArray(FieldIDs, FieldValues, "SMSCODE");
						PreferenceHelper.savesmsps(SMSCODE);
						log("myfromActivity:" + fromActivity + ":SMSCODE:" + SMSCODE);
						AndyUtils.removeCustomProgressDialog();
						//sendBroadcastBulletProof(status);
					}else{
						sendBroadcastBulletProof(status);
					}
				}else{
				
				}
			}
		}
	}//FROMDATE

	private void sendBroadcastBulletProof(String data) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction("com.craft.little.code");
		intent.putExtra("message", "");
		intent.putExtra("message2", data);
		intent.putExtra("method", fromMenu);
		context.sendBroadcast(intent);
	}
	
	private void sendBroadcast(String message, String message3) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction("com.craft.little.code");
		intent.putExtra("message", message);
		intent.putExtra("message2", fromMenu);
		intent.putExtra("message3", message3);
		context.sendBroadcast(intent);
	}
	public static String FindInArray(String[] ArrayID, String[] ArrayValue, String StringToFind) {
		int i = 0;
		if (StringToFind == "")
			return "";

		StringToFind = StringToFind.toUpperCase();

		for (i = 0; i < ArrayID.length; i++) {
			try {
				if (ArrayID[i].equals(StringToFind)) {
					return ArrayValue[i];
				}
			} catch (Exception e) {
			}

		}
		return "";
	}
	private void dashboardItems(String FieldIDs[], String FieldValues[]){
		String status = FindInArray(FieldIDs, FieldValues, "STATUS");
		if (status.equals("000")) {
			if(fromMenu.contains("DASHBOARDITEMS")){
				String DATA = FindInArray(FieldIDs, FieldValues, "DATA");
				sendBroadcast("_Little_MainActivity", DATA);
			}else{
				String DASHBOARD = FindInArray(FieldIDs, FieldValues, "DASHBOARD");
				sendBroadcast("_Little_MainActivity",DASHBOARD);
			}
		} else if (status.equals("001")) {


		} else {


		}
	}
	private static String[] split(String Data,String Separator)
	{
		if(Data==null)
			return new String[]{};
	
		Vector Chunks = new Vector();
		String Stuff="";
	
		while(Data.indexOf(Separator)!=-1)
	    {
	        Stuff=Data.substring(0,Data.indexOf(Separator)).trim();
	        Chunks.addElement(Stuff);
	        Data=Data.substring(Data.indexOf(Separator) + Separator.length());
	    }
	   // if(!Data.trim().equals(""))
	    	Chunks.addElement(Data.trim());
	
	    String [] Retval=new String[Chunks.size()];
	    Chunks.copyInto(Retval);
	
	    return Retval;
	}
	private void log(String text) {
		Log.d("newasynctask", "-" + text);
		//appendLog("\n\n"+text);
	}
	public void appendLog(String text)
	{
		File logFile = new File("sdcard/log.file");
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
}