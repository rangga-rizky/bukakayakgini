package com.example.ranggarizky.bukakayakgini.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ranggarizky on 5/6/2016.
 */
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "sessions";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_TOKEN = "token";
    private static final String KEY_IS_USERNAME = "username";
    private static final String KEY_IS_UID = "uid";
    private static final String KEY_IS_LOKASI= "lokasi";
    private static final String KEY_IS_EMAIL = "email";
    private static final String KEY_IS_FIREBASETOKEN = "firebasetoken";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
    }

    public void setFirebaseToken(String id) {

        editor.putString(KEY_IS_FIREBASETOKEN, id);
        editor.commit();

    }

    public void setEmail(String email) {

        editor.putString(KEY_IS_EMAIL, email);
        editor.commit();
    }

    public void setUsername(String username) {

        editor.putString(KEY_IS_USERNAME, username);
        editor.commit();
    }

    public void setUid(String uid) {
        editor.putString(KEY_IS_UID, uid);
        editor.commit();
    }

    public void setLokasi(String lokasi) {
        editor.putString(KEY_IS_LOKASI, lokasi);
        editor.commit();
    }


    public void setTOken(String token) {
        editor.putString(KEY_IS_TOKEN, token);
        editor.commit();
    }


    public boolean isLogin(){
        return pref.getBoolean(KEY_IS_LOGGEDIN,false);
    }

    public String getUsername(){
        return pref.getString(KEY_IS_USERNAME,null);
    }

    public String getUid(){
        return pref.getString(KEY_IS_UID,null);
    }

    public String getLokasi(){
        return pref.getString(KEY_IS_LOKASI,null);
    }

    public String getToken(){
        return pref.getString(KEY_IS_TOKEN,null);
    }

    public String getEmail(){
        return pref.getString(KEY_IS_EMAIL,null);
    }

    public String getFirebaseToken(){
        return pref.getString(KEY_IS_FIREBASETOKEN,null);
    }

}
