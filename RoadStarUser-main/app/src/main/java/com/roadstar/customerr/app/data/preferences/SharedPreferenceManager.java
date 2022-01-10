package com.roadstar.customerr.app.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

final public class SharedPreferenceManager {

    private static SharedPreferenceManager sharedPreferenceManager = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static void setSingletonInstance(Context context) {
        synchronized (SharedPreferenceManager.class) {
            if (sharedPreferenceManager == null)
                sharedPreferenceManager = new SharedPreferenceManager(context);
            else
                throw new IllegalStateException("SharedPreferenceManager instance already exists.");
        }
    }

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PreferenceUtils.PREFERENCE_NAME, PreferenceUtils.PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public static SharedPreferenceManager getInstance() {
        return sharedPreferenceManager;
    }

    public void clearPreferences() {
        editor.clear();
        editor.commit();
    }

    public String read(String valueKey, String valueDefault) {
        return sharedPreferences.getString(valueKey, valueDefault);
    }
    public long readLong(String valueKey, long valueDefault) {
        return sharedPreferences.getLong(valueKey, valueDefault);
    }

    public void save(String valueKey, String value) {
        editor.putString(valueKey, value);
        editor.commit();
    }

    public int read(String valueKey, int valueDefault) {
        return sharedPreferences.getInt(valueKey, valueDefault);
    }

    public void save(String valueKey, int value) {
        editor.putInt(valueKey, value);
        editor.commit();
    }

    public boolean read(String valueKey, boolean valueDefault) {
        return sharedPreferences.getBoolean(valueKey, valueDefault);
    }

    public void save(String valueKey, boolean value) {
        editor.putBoolean(valueKey, value);
        editor.commit();
    }

    public long read(String valueKey, long valueDefault) {
        return sharedPreferences.getLong(valueKey, valueDefault);
    }

    public void save(String valueKey, long value) {
        editor.putLong(valueKey, value);
        editor.commit();
    }

    public float read(String valueKey, float valueDefault) {
        return sharedPreferences.getFloat(valueKey, valueDefault);
    }

    public void save(String valueKey, float value) {
        editor.putFloat(valueKey, value);
        editor.commit();
    }

}
