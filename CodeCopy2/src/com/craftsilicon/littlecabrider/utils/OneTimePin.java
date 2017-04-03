package com.craftsilicon.littlecabrider.utils;

import com.craftsilicon.littlecabrider.OTPValidationActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * @author guidovanrossum
 */
public class OneTimePin extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	static String code_message, message;
	String first_pin, second_pin;
	OTPValidationActivity otpValidationActivity;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(SMS_RECEIVED)) {
			Bundle intent_extras = intent.getExtras();
			Object[] pdus = (Object[]) intent_extras.get("pdus");

			if (intent_extras != null) {
				if (pdus.length == 0) {
					return;
				}
			}

			SmsMessage[] sms_message = new SmsMessage[pdus.length];
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < pdus.length; i++) {
				sms_message[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				sb.append(sms_message[i].getMessageBody());
			}

			String message_sender = sms_message[0].getOriginatingAddress();
			try {
				message = sb.toString();
				// message.contains("-")

				if (message_sender.equals("ELMA")) {

					code_message = message.replaceAll("[^\\d]", "");

					char c1, c2, c3, c4, c5, c6;
					c1 = code_message.charAt(0);
					c2 = code_message.charAt(1);
					c3 = code_message.charAt(2);
					c4 = code_message.charAt(3);
					c5 = code_message.charAt(4);
					c6 = code_message.charAt(5);

					first_pin = "" + c1 + c2 + c3;
					second_pin = "" + c4 + c5 + c6;

					if ((first_pin.length() > 0 && first_pin.length() <= 3)
							&& (second_pin.length() > 0 && second_pin.length() <= 3)) {
//						OTPValidation.txtPassCode1.setText(first_pin);
//						OTPValidation.txtPasscode2.setText(second_pin);
//
//						otpValidationActivity = new OTPValidationActivity();
//						otpValidationActivity.validateCredentials();

					}
				}

			} catch (Exception exception) {

			}
		}
	}

}
