package com.roadstar.customerr.app.module.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.auth.WelcomeActivity;
import com.roadstar.customerr.app.module.ui.main.MainActivity;
import com.roadstar.customerr.common.base.BaseActivity;


public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

    }

    @Override
    protected void onStart() {
        super.onStart();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                checkLoginStatus();
//            }
//        }, 300);
    }

    void checkLoginStatus() {
        if (SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_LOGGED_IN, false))
            gotoMainActivity();
        else
            gotoLandingActivity();
    }

    void gotoLandingActivity() {
        startActivityWithNoHistory(SplashActivity.this, WelcomeActivity.class);
    }

    void gotoMainActivity() {
        startActivityWithNoHistory(SplashActivity.this, MainActivity.class);
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        finish();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

        new Thread(){
            @Override
            public void run(){

            }
        }.start();
    }

    @Override
    protected void onResume() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (SharedPreferenceManager.getInstance().read(PreferenceUtils.IS_LOGGED_IN, false)){
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gotoMainActivity();
                    }
                }, 300);
            } else {
                showGPSDisabledAlertToUser();
            }
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoLandingActivity();
                }
            }, 300);
        }
        super.onResume();
    }

}
