package com.craftsilicon.littlecabrider.utils;

import java.io.ByteArrayOutputStream;

import org.spongycastle.crypto.BufferedBlockCipher;
import org.spongycastle.crypto.CryptoException;
import org.spongycastle.crypto.Digest;
import org.spongycastle.crypto.PBEParametersGenerator;
import org.spongycastle.crypto.digests.SHA1Digest;
import org.spongycastle.crypto.engines.DESEngine;
import org.spongycastle.crypto.generators.PKCS12ParametersGenerator;
import org.spongycastle.crypto.modes.CBCBlockCipher;
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.spongycastle.crypto.params.DESParameters;
import org.spongycastle.crypto.params.KeyParameter;


/*import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.engines.DESEngine;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.DESParameters;
import org.bouncycastle.crypto.params.KeyParameter;*/


public class Ep
{
    private byte [] salt;

    public Ep()
    {
        salt = new byte[] {(byte)0x15,(byte)0x15,(byte)0x15,(byte)0x15,
        (byte)0x15,(byte)0x85,(byte)0x25,(byte)0x15,
        (byte)0x15,(byte)0x75,(byte)0x35,(byte)0x15,
        (byte)0x15,(byte)0x65,(byte)0x55,(byte)0x15,};
    }

    public byte[] Pack(String password,String message) throws Exception
    {
        ByteArrayOutputStream ArrayWriter = new ByteArrayOutputStream();

        byte [] data = DESPasswdEncrypt (message.getBytes(),password.toCharArray());

        ArrayWriter.write(data);

        return ArrayWriter.toByteArray();
    }

    public byte[] UnPack(String Password, byte [] message) throws Exception
    {
        byte [] Output= null;
        try
        {
            Output = DESPasswdDecrypt(message,Password.toCharArray());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return Output;
    }



 public byte [] DESPasswdDecrypt (byte [] toDecrypt, char [] passwd)throws Exception
  {
    DESParameters key = (DESParameters) getDESPasswdKey(passwd);
    BufferedBlockCipher cipher =
             new PaddedBufferedBlockCipher(
               new CBCBlockCipher(new DESEngine()));
    cipher.init(false, key);
    byte[] result = new byte[cipher.getOutputSize(toDecrypt.length)];
    int len = cipher.processBytes(toDecrypt, 0, toDecrypt.length, result, 0);
    try
    {
      cipher.doFinal(result, len);
    }
    catch (CryptoException ce)
    {
      result = (new String("Cipher error")).getBytes();
      ce.printStackTrace();
    }
    return result;
  }

    public byte [] DESPasswdEncrypt (byte [] toEncrypt, char [] passwd)throws Exception
    {
        DESParameters key = (DESParameters)getDESPasswdKey(passwd);
        BufferedBlockCipher cipher =
        new PaddedBufferedBlockCipher(
        new CBCBlockCipher(new DESEngine()));
        cipher.init(true, key);
        byte[] result = new byte[cipher.getOutputSize(toEncrypt.length)];
        int len = cipher.processBytes(toEncrypt, 0, toEncrypt.length, result, 0);
        try
        {
            cipher.doFinal(result, len);
        }
        catch (CryptoException ce)
        {
            result = (new String("Cipher error")).getBytes();
            ce.printStackTrace();
        }
        return result;
    }


    private byte [] generatePasswordDigest(byte[] passwordBytes)
    {
        Digest digest = new SHA1Digest();
        digest.update(passwordBytes, 0, passwordBytes.length);

        byte[] digestValue = new byte[digest.getDigestSize()];

        digest.doFinal(digestValue, 0);
        return digestValue;
    }

    private DESParameters getDESPasswdKey (char [] passwd) throws Exception
    {
        PBEParametersGenerator generator =
        new PKCS12ParametersGenerator(new SHA1Digest());
        generator.init(
        PBEParametersGenerator.PKCS12PasswordToBytes(passwd),salt, 1);
        KeyParameter rawKey =
        (KeyParameter) generator.generateDerivedParameters(64);
        byte [] keyBytes = rawKey.getKey();
        DESParameters.setOddParity(keyBytes);
        DESParameters key = new DESParameters(keyBytes);
        return key;
    }
}
