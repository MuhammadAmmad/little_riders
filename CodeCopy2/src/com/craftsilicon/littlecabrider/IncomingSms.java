package com.craftsilicon.littlecabrider;

import com.craftsilicon.littlecabrider.utils.AndyUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by eric.karani on 11/27/2015.
 */
public class IncomingSms extends BroadcastReceiver {

	Context context;
	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();

	@Override
	public void onReceive(Context context, Intent intent) {

		this.context = context;
		// Retrieves a map of extended menu from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;

					// FROM, TEXT, DATE
					// if(senderNum.equals("ELMA")){
					String senderPremiumnumber = senderNum.replaceAll("[^\\d]",
							"");
					String message = currentMessage.getDisplayMessageBody();
					if (senderPremiumnumber.length() == 0
							|| senderNum.length() < 4) {
						String servermessage = currentMessage
								.getDisplayMessageBody();
						if (senderNum.equals("LITTLECABS")) {
							// if(senderNum.equals("ELMA")) {
							message = message.replaceAll("[^\\d]", "");
							String message1 = message.substring(0, 3);
							String message2 = message.substring(3,
									message.length());
							sendBroadcast(message1, message2, "GETCODE");	
						}
						
						AndyUtils.log("smsreceiver Premium: " + senderNum,
								"- message: " + message);
					} else {
						AndyUtils.log("smsreceiver Normal : " + senderNum,
								"- message: " + message);
					}
					// toast.show();

				}
			}

		} catch (Exception e) {
			AndyUtils.log("SmsReceiver", "Exception smsReceiver" + e);

		}
	}
	
	private void deleteReadMessage(String message_source){
		try{
			
			if (message_source.equals("LITTLECABS")) {
				
			}
			
		}catch (Exception ex){
			
		}
	}

	private void sendBroadcast(String message, String message2, String message3) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
		intent.setAction("com.craft.little.code");
		intent.putExtra("message", message);
		intent.putExtra("message2", message2);
		intent.putExtra("method", message3);
		context.sendBroadcast(intent);
	}
}