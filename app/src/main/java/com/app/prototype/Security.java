package com.app.prototype;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by gabri on 9/16/2017.
 */

public class Security {

    private static SecureRandom _random;

    public static SecureRandom GetRandom(){
        if (_random == null)
            _random = new SecureRandom();
        return _random;
    }

    public static byte[] HexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len/2];

        for(int i = 0; i < len; i+=2){
            data[i/2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
        }

        return data;
    }

    final private static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static String ByteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length*2];
        int v;

        for(int j=0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v>>>4];
            hexChars[j*2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] GeneratePassword(int len){
        byte[] password = new byte[len];
        GetRandom().nextBytes(password);
        return password;
    }

    public static String EncodePassword(byte[] password, SecretKey secret)  {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            password = cipher.doFinal(password);
            String passwordStr = Base64.encodeToString(password, Base64.NO_PADDING | Base64.NO_WRAP | Base64.URL_SAFE);//.replace('+', '-').replace('/', '_').replace("==", "");
            return passwordStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
