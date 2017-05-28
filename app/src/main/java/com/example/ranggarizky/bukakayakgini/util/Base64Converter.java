package com.example.ranggarizky.bukakayakgini.util;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by RanggaRizky on 5/13/2017.
 */
public class Base64Converter {
    

    public String encodeString(String s) {
        byte[] encodeValue = Base64.encode(s.getBytes(),Base64.NO_WRAP);
        String base64Encoded = new String(encodeValue);
        return base64Encoded;

    }

    private String decodeString(String encoded) {
        byte[] dataDec = Base64.decode(encoded, Base64.DEFAULT);
        String decodedString = "";
        try {

            decodedString = new String(dataDec, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        } finally {

            return decodedString;
        }
    }
}
