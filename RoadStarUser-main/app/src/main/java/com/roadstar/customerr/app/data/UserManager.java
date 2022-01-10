package com.roadstar.customerr.app.data;


import android.content.Context;
import android.provider.Settings;

import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.common.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import static com.roadstar.customerr.app.data.preferences.PreferenceUtils.CARD_ADDED;
import static com.roadstar.customerr.common.utils.ApiConstants.ANDROID;

/**
 * Created by bilal on 11/01/2018.
 */

public class UserManager{

    public static void saveUserData(JSONObject data){

        parseStringAndSave(data, "id", PreferenceUtils.USER_ID);
        parseStringAndSave(data, "referral_code", PreferenceUtils.REFERRAL_CODE);
        parseStringAndSave(data, "payment_mode", PreferenceUtils.PAYMENT_MODE);
        parseBooleanAndSave(data, "mpConnected", PreferenceUtils.MP_CONNECTED);
        parseStringAndSave(data, "mp_token_id", PreferenceUtils.MP_TOKEN_ID);
        parseStringAndSave(data, "user_name", PreferenceUtils.USER_NAME);
        parseStringAndSave(data, "first_name", PreferenceUtils.FIRST_NAME);
        parseStringAndSave(data, "last_name", PreferenceUtils.LAST_NAME);
        parseStringAndSave(data, "dni", PreferenceUtils.DNI);
        parseStringAndSave(data, "email", PreferenceUtils.EMAIL);
        parseStringAndSave(data, "mobile", PreferenceUtils.PHONE);
        parseIntegerAndSave(data, "user_type", PreferenceUtils.USER_TYPE);
        parseStringAndSave(data, "gender", PreferenceUtils.GENDER);
        parseStringAndSave(data, "birth", PreferenceUtils.DOB);
        parseStringAndSave(data, "address", PreferenceUtils.ADDRESS);
        parseStringAndSave(data, "city", PreferenceUtils.CITY);
        parseStringAndSave(data, "country_name", PreferenceUtils.COUNTRY);
        parseStringAndSave(data, "state", PreferenceUtils.STATE);
        parseStringAndSave(data, "postalCode", PreferenceUtils.POSTAL_CODE);
        parseStringAndSave(data, "picture", PreferenceUtils.PICTURE);
        parseStringAndSave(data, "created", PreferenceUtils.ACC_CREATED_DATE);
        parseStringAndSave(data, "device_type", PreferenceUtils.DEVICE_TYPE);
        parseStringAndSave(data, "device_token", PreferenceUtils.DEVICE_TOKEN);

//        parseLongAndSave(data, "avgRating", PreferenceUtils.AVG_RATING);
        parseDoubleAndSave(data, PreferenceUtils.RATING, PreferenceUtils.RATING);
        parseStringAndSave(data, PreferenceUtils.MOVER_TYPE, PreferenceUtils.MOVER_TYPE);
        parseBooleanAndSave(data, PreferenceUtils.SIGNUP_COMPLETED, PreferenceUtils.SIGNUP_COMPLETED);
        parseIntegerAndSave(data, PreferenceUtils.STEP_COMPLETED, PreferenceUtils.STEP_COMPLETED);
    }
    public static void saveAccessToken(JSONObject data){
        parseStringAndSave(data, "access_token", PreferenceUtils.TOKEN);
    }
    public static void saveRefreshToken(JSONObject data){
        parseStringAndSave(data, "refresh_token", PreferenceUtils.REFRESH_TOKEN);
    }
    public static void saveTokenType(JSONObject data){
        parseStringAndSave(data, "token_type", PreferenceUtils.TOKEN_TYPE);
    }

    public static String getRefreshToken(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.REFRESH_TOKEN, "");
    }
    public static String getTokenType(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.TOKEN_TYPE, "");
    }
    public static void setIsCardAdded(boolean b) {
        SharedPreferenceManager.getInstance().save(CARD_ADDED, b);
    }
    public static boolean isCardAdded(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.CARD_ADDED, false);
    }

    public static void setCardID(String val) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.CARD_ID, val);
    }
    public static String getCardID() {
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.CARD_ID, "");
    }
    private static void parseLongAndSave(JSONObject data, String key, String prefKey){
        try {
            SharedPreferenceManager.getInstance().save(prefKey, data.has(key) && !data.get(key).equals(null)
                    ? data.getLong(key) :0);
        }catch (JSONException e){
            Logger.caughtException(e);
        }
    }

    private static void parseDoubleAndSave(JSONObject data, String key, String prefKey){
        try {
            SharedPreferenceManager.getInstance().save(prefKey, data.has(key)
                    ? (float) data.getDouble(key) : 0);
        }catch (JSONException e){
            Logger.caughtException(e);
        }
    }

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    public static String getDeviceType(){
        //return SharedPreferenceManager.getInstance().read(PreferenceUtils.DEVICE_TYPE, ANDROID);
        return   ANDROID;
    }
    public static String getDeviceToken(){
      return SharedPreferenceManager.getInstance().read(PreferenceUtils.DEVICE_TOKEN, "");
     //   return  "cBS7Ax3UQYyUoqCqgS9AAA:APA91bFDZ8ymt6ZvEZIAZbZdbBYB7HsQJie4q08cvFXUm_2D3gIGMD24Y9SFdW-2tpxEwtwiKoqsZGBCHjB4FMh6jcmRTkIvpY7QAkFNTP5O6mjb1XjcyFjJNr6Ucn8AcacDXD7E3WCH";
    }

    public static float getUserRating(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.RATING, 0f);
    }
    public static String getUserId(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.USER_ID, "");
    }
    public static String getToken(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.TOKEN, "");
    }
    public static String getUserName(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.USER_NAME, "");
    }

    public static String getFirstname(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.FIRST_NAME, "");
    }

    public static String getLastName(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.LAST_NAME, "");
    }
    public static String getCompanyName(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.COMPANY_NAME, "");
    }

    public static String getEmail(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.EMAIL, "");
    }

    public static String getPhone(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.PHONE, "");
    }
   public static String getSSN(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.DNI, "");
    }

    public static int getType(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.USER_TYPE, 0);
    }

    public static String getGender(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.GENDER, "");
    }

    public static String getDob(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.DOB, "");
    }

    public static String getAddress(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.ADDRESS, "");
    }

    public static String getCity(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.CITY, "");
    }
   public static String getCountry(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.COUNTRY, "");
    }

    public static String getState(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.STATE, "");
    }
    public static String getAccCreatedDate( ) {
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.ACC_CREATED_DATE, "");
    }
    public static String getPostalCode(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.POSTAL_CODE, "");
    }
    public static String getMpTokenId(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.MP_TOKEN_ID, "");
    }
    public static String getImage(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.PICTURE, "");
    }
    public static boolean isMpConnected(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.MP_CONNECTED, false);
    }
    public static boolean isSignUpCompleted(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.SIGNUP_COMPLETED, false);
    }

    public static int stepsCompleted(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.STEP_COMPLETED, 1);
    }

    public static int getRating(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.RATING, 5);
    }

    public static String getMoverType(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.MOVER_TYPE, "");
    }

    private static void parseStringAndSave(JSONObject data, String key, String prefKey){
        try {
            SharedPreferenceManager.getInstance().save(prefKey, data.has(key) && !data.get(key).equals(null)
             ? data.getString(key) : "");
        }catch (JSONException e){
            Logger.caughtException(e);
        }
    }

    private static void parseIntegerAndSave(JSONObject data, String key, String prefKey){
        try {
            SharedPreferenceManager.getInstance().save(prefKey, data.has(key) && !data.get(key).equals(null)
                    ? data.getInt(key) : 0);
        }catch (JSONException e){
            Logger.caughtException(e);
        }
    }

    private static void parseBooleanAndSave(JSONObject data, String key, String prefKey){
        try {
            SharedPreferenceManager.getInstance().save(prefKey, (data.has(key) && !data.get(key).equals(null)) && data.getBoolean(key));
        }catch (JSONException e){
            Logger.caughtException(e);
        }
    }

    public static boolean isUserLoggedIn(){
        return !SharedPreferenceManager.getInstance().
                read(PreferenceUtils.COOKIE, "").equalsIgnoreCase("") &&
                SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_LOGGED_IN, false);
    }

    public static int getNotificationCount() {
        return 0;
    }

    public static void setStepCompleted(int i) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.STEP_COMPLETED, i);
    }

    public static int getStepCompleted() {
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.STEP_COMPLETED, 1);
    }

    public static void setIsSignUpCompleted(boolean b) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.SIGNUP_COMPLETED, b);
    }

    public static void setIsOnline(boolean status) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.IS_ONLINE, status);
    }

    public static void setIsUserLogin(boolean status) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.IS_LOGGED_IN, status);
    }

    public static boolean isOnline() {
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_ONLINE, false);
    }

    public static void setIsTrial(boolean status) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.IS_FREE_TRIAL, status);
    }
    public static boolean getIsTrial(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_FREE_TRIAL, false);
    }
    public static boolean getNotifSetting(){
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.NOTIF_SETTING, false);
    }
    public static void setNotifSetting(boolean val) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.NOTIF_SETTING, val);
    }

    public static void setRideInprogress(boolean val) {
        SharedPreferenceManager.getInstance().save(PreferenceUtils.RIDE_INPROGRESS, val);
    }
    public static boolean getRideInprogress() {
        return SharedPreferenceManager.getInstance().read(PreferenceUtils.RIDE_INPROGRESS, false);
    }
}
