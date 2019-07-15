package com.hyundaimobil.jadwalkajian.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hyundaimobil.jadwalkajian.MainActivity;

import java.util.HashMap;

/**
 * Created by User HMI on 9/21/2017.
 */

@SuppressLint("CommitPrefEdits")
public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // nama sharepreference
    private static final String PREF_NAME = "Sesi";

    // All Shared Preferences Keys
    private static final String IS_LOGIN            = "IsLoggedIn";
    public static final String KEY_USERCODE         = "kdUsers";
    public static final String KEY_NAMAUSER         = "nama";
    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String kdUsers, String nama){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERCODE, kdUsers);
        editor.putString(KEY_NAMAUSER, nama);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            //((Activity)_context).finish();
        }
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERCODE, pref.getString(KEY_USERCODE, null));
        user.put(KEY_NAMAUSER, pref.getString(KEY_NAMAUSER, null));
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}