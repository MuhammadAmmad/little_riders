package com.craftsilicon.littlecabrider.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.os.Build;

public class Sc {
	//static String key="DulLk9fkHajiB674wjla96NZDeXJy/7O";
	String url;
	static String result;
	//static String iv ="D+z2EqbxoJJNZhwxn9fzWuEu1IDI+mmy";
	public static String getKey(){
		String key="yHC5+CoXkXJjWLj5AjvyBt7pTgD8BNN7";
		key = Sc.DecryptData(key, "CHATAPP7");
		return key;
	}
	public static String getIV(){		
		String iv ="0B0GCdL3pzpKK0XUGhANFX+OtlsyQnm9";
		iv  = Sc.DecryptData(iv, "CHATAPP7");
		return iv;
	}
	public static String encrypt(String input) {
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(getKey().getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey,new IvParameterSpec(getIV().getBytes()));
			crypted = cipher.doFinal(input.getBytes());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		String result= new String(B6U.encodeToString(crypted,B6U.DEFAULT));
		result=result.replace("+", "-");
		result=result.replace("\n", "");
		return result;
	}

	public static String decrypt(String input) {
		input = input.replace("-", "+");
		byte[] output = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(getKey().getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey,new IvParameterSpec(getIV().getBytes()));
			
			byte[] url = B6U.decode(input, B6U.DEFAULT);
			output = cipher.doFinal(url);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return new String(output);
	}
	public static  String EncryptData(String DataToEncrypt, String Key) {

		byte[] EncryptedData = null;
		Ep EncryptionEngine = new Ep();

		try {
			EncryptedData = EncryptionEngine.DESPasswdEncrypt(
					DataToEncrypt.getBytes(), Key.toCharArray());
			
			if (Build.VERSION.SDK_INT >= 8)
			{
				DataToEncrypt = android.util.Base64.encodeToString(
						EncryptedData,
						android.util.Base64.DEFAULT);
			} else {
				DataToEncrypt = B7.encodeToString(
						EncryptedData,
						B7.DEFAULT);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return DataToEncrypt;
	}
	public static String DecryptData(String DataToEncrypt, String Key)
	{
		byte[] EncryptedData = null;

		Ep EncryptionEngine = new Ep();

		try
		{
			EncryptedData = B7.decode(DataToEncrypt, B7.DEFAULT);
			//EncryptedData=Base64Utility.decode(DataToEncrypt, Base64Utility.DEFAULT);
			//EncryptedData = Base64.Base64Decode(DataToEncrypt.toCharArray());
			EncryptedData = EncryptionEngine.DESPasswdDecrypt(EncryptedData, Key.toCharArray());
			DataToEncrypt = (new String(EncryptedData));
			DataToEncrypt = DataToEncrypt.replace('\0', ' ');
			DataToEncrypt = DataToEncrypt.trim();
		}
		catch (Exception ex)
		{
			//ex.printStackTrace();
			try
			{
				//EncryptedData=Base64Utility.decode(DataToEncrypt, Base64Utility.DEFAULT);
				EncryptedData = B7.decode(DataToEncrypt, B7.DEFAULT);
				DataToEncrypt = (new String(EncryptedData));
				DataToEncrypt = DataToEncrypt.replace('\0', ' ');
				DataToEncrypt = DataToEncrypt.trim();
			}
			catch (Exception exx)
			{
				exx.printStackTrace();
			}
			return DataToEncrypt;
		}
		return DataToEncrypt;
	}

	
}
