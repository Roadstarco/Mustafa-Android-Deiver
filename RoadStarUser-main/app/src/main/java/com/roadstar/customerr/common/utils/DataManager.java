package com.roadstar.customerr.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by USER on 11/7/2017.
 */

public class DataManager {
    private final static String PREF_FILE = "PREFS";
    private static DataManager instance = null;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    private static final String KEY_COOKIE = "cookie";


    // All Shared Preferences Keys
    private static final String IS_LOGIN = null;

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "fname";
    public static final String KEY_LNAME = "lname";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone";

    public static final String KEY_URL = "url";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_DOB = "dob";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_POSTAL = "postal";


    private Context context;

    private DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_FILE, 0);
        editor = sharedPreferences.edit();
        this.context = context;
    }

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            try {
                instance = new DataManager(context);
            } catch (Exception ex) {
                Log.i("Gz", ex.toString());
            }
        }
        return instance;
    }

    /**
     * Create login session
     */
    public void storeUserInfo(String fname, String lname, String email, String cookie, String image, String phone, String gender, String dob, String address, String city, String state, String postalCode) {
        // Storing login value as TRUE
//        editor.putBoolean(IS_LOGIN, true);
//

        editor.putString(KEY_NAME, fname);
        editor.putString(KEY_LNAME, lname);

        editor.putString(KEY_EMAIL, email);

        editor.putString(KEY_URL, image);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_STATE, state);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_POSTAL, postalCode);


        editor.commit();
    }

    public void checkLogin() {

        if (sharedPreferences.getString(KEY_NAME, null) == null) {
//             user is not logged in redirect him to Login Activity
//            Intent i = new Intent(CustomApplication.getAppContext(), LoginActivity.class);
//            // Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring Login Activity
//            context.startActivity(i);

        } else {
            // user is not logged in redirect him to Home
//            Intent i = new Intent(context, NavigationDrawerActivity.class);
////             Closing all the Activities
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            // Add new Flag to start new Activity
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            // Staring home Activity
//            CustomApplication.getAppContext().startActivity(i);
        }


    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));

        user.put(KEY_URL, sharedPreferences.getString(KEY_URL, null));


        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
//
////         After logout redirect user to Loing Activity
//        Intent i = new Intent(context, LoginActivity.class);
//        // Closing all the Activities
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        // Add new Flag to start new Activity
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        // Staring Login Activity
//        context.startActivity(i);
    }


    public boolean isLoggedIn() {
        //get user data from session
        HashMap<String, String> user = getUserDetails();
        return user.get(KEY_NAME) != null;
    }

    public String get_Cookie_from_Pref() {

        String cookie = sharedPreferences.getString(KEY_COOKIE, null);
        return cookie;
    }
}
