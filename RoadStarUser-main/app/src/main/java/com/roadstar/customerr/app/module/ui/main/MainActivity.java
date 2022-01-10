package com.roadstar.customerr.app.module.ui.main;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.internationalDelivery.InternationalMainActivity;
import com.roadstar.customerr.app.module.ui.auth.WelcomeActivity;
import com.roadstar.customerr.app.module.ui.available_booking.AvailBookingActivity;
import com.roadstar.customerr.app.module.ui.booking_activity.BookingActivity;
import com.roadstar.customerr.app.module.ui.booking_history.BookingHistoryActivity;
import com.roadstar.customerr.app.module.ui.chat.ChatActivity;
import com.roadstar.customerr.app.module.ui.chat.MessagesActivity;
import com.roadstar.customerr.app.module.ui.claim.ClaimActivity;
import com.roadstar.customerr.app.module.ui.dialog.LogoutConfrmDialog;
import com.roadstar.customerr.app.module.ui.payment_method.PaymentMethodActivity;
import com.roadstar.customerr.app.module.ui.profile.MyProfileActivity;
import com.roadstar.customerr.app.module.ui.support.SupportActivity;
import com.roadstar.customerr.app.module.ui.your_package.MapHandlerActivity;
import com.roadstar.customerr.app.module.ui.your_package.YourPackageActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.Services.BackgroundService;
import com.roadstar.customerr.common.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, LogoutConfrmDialog.OnInputListener {

    private int selectedItemId = 0;
    private boolean isItemSelected = false;
    public static final int TAG_CODE_PERMISSION_LOCATION = 1;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        BackgroundService.timeCount = 0;

        if(!isMyServiceRunning(this,BackgroundService.class))
            startService(new Intent(getApplicationContext(), BackgroundService.class));

        init();
        Log.d("Firebase", "token " + FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeaderData();
    }

    void init() {
        setActionBar(getString(R.string.road_star));
        populateDrawerView();
        setDrawerToggle();
        bindClickListners();
//        UserManager.getToken();
    }

    private void bindClickListners() {

        findViewById(R.id.tv_view_profile).setOnClickListener(MainActivity.this);
        findViewById(R.id.img_loc_delv).setOnClickListener(MainActivity.this);
        findViewById(R.id.img_intr_delv).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_home).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_Messages).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_post_trip).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_my_trip).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_your_package).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_booking_history).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_available_booking).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_claim).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_payment_method).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_support).setOnClickListener(MainActivity.this);
        findViewById(R.id.ll_logout).setOnClickListener(MainActivity.this);

    }


    private void populateDrawerView() {
        mDrawerLayout = findViewById(R.id.layout_main);
        NavigationView mNavigationView = findViewById(R.id.nav_drawer_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setCheckedItem(R.id.home);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_drawer_view);
        mNavigationView.setCheckedItem(item.getItemId());
        selectedItemId = item.getItemId();
        isItemSelected = true;
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onDrawerClosed() {
        super.onDrawerClosed();
        if (!isItemSelected)
            return;
        isItemSelected = false;
        Class targetActivity = null;
        if (selectedItemId == R.id.tv_view_profile)
            targetActivity = MyProfileActivity.class;
        else if (selectedItemId == R.id.ll_home)
            closeDrawer();
        else if (selectedItemId == R.id.ll_your_package)
            targetActivity = YourPackageActivity.class;
        else if (selectedItemId == R.id.ll_post_trip)
            targetActivity = InternationalMainActivity.class;
        else if (selectedItemId == R.id.ll_booking_history)
            targetActivity = BookingHistoryActivity.class;
        else if (selectedItemId == R.id.ll_my_trip)
            targetActivity = AvailBookingActivity.class;
        else if (selectedItemId == R.id.ll_claim)
            targetActivity = ClaimActivity.class;
        else if (selectedItemId == R.id.ll_payment_method)
            targetActivity = PaymentMethodActivity.class;
        else if (selectedItemId == R.id.ll_support)
            targetActivity = SupportActivity.class;
        else if (selectedItemId == R.id.ll_logout)
            showLogoutDialog();
        else if (selectedItemId == R.id.ll_Messages)
            targetActivity = MessagesActivity.class;


        ScrollView scrollView = findViewById(R.id.scroll_drawer);
        scrollView.scrollTo(0, 0);


        /*if (selectedItemId == R.id.ll_your_package && !UserManager.getRideInprogress()) {
            showToast("No booking in progress");
            return;
        } else */if (targetActivity != null) {
            startActivity(MainActivity.this, targetActivity);
        }

    }

    @Override
    public void onClick(View view) {
        selectedItemId = view.getId();

        if (selectedItemId == R.id.img_loc_delv ) {
            //if (UserManager.getRideInprogress()) {
                //showToast("Booking already in progress");
            //} else {
            setMapCurrentLoc();
//                startActivity(this, BookingActivity.class);
            //}

        }

        else if (selectedItemId == R.id.img_intr_delv) {
            startActivity(this, InternationalMainActivity.class);
        }

        else {
            isItemSelected = true;
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }


    }

    void logoutApp() {
        SharedPreferenceManager.getInstance().clearPreferences();
        startActivity(new Intent(MainActivity.this, WelcomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();

    }

    @Override
    public void sendResponse(Boolean policyAccept) {

        if (policyAccept)
            logoutApp();
    }

    private void showLogoutDialog() {
        LogoutConfrmDialog logoutConfrmDialog = new LogoutConfrmDialog();
        logoutConfrmDialog.show(getSupportFragmentManager(), "LogoutConfrmDialog");
        logoutConfrmDialog.setCancelable(false);

    }

    private void setMapCurrentLoc( ) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            startActivity(this, BookingActivity.class);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == TAG_CODE_PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                /*All Permission Allow and Do Your Code here*/
                startActivity(this, BookingActivity.class);
            }

        }

    }

    public static void callReCreateApi(String id) {
        /*Create handle for the RetrofitInstance interface*/
        try {

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.recreateReq(HEADER_BEARER + UserManager.getToken(),id);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);
                    if (response.code() == 200) {
                        BackgroundService.timeCount = 0;
//                        finish();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    Toast.makeText(mainActivity, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void askDialog(String id){
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(mainActivity);
        alertDialog.setTitle("Submit Request")
                .setMessage("Do you want to send request to other available drivers?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Perform Action & Dismiss dialog
                                callReCreateApi(id);
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        BackgroundService.timeCount = 0;
                    }
                })
                .setCancelable(false)
                .create()
                .show();
//        AlertDialog alertDialog1 = alertDialog.create();
//        alertDialog1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        alertDialog1.show();
    }

    public static void askDialogN(String id){
        AlertDialog.Builder alertDialog =new AlertDialog.Builder(mainActivity);
        alertDialog.setTitle("Request Cancel")
            .setMessage("Sorry no driver available!")
            .setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Perform Action & Dismiss dialog
//                                callReCreateApi(id);
                            dialog.dismiss();
                        }
                    })
            .setCancelable(false)
            .create()
            .show();
    }


    public static boolean isMyServiceRunning(Activity activity, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
