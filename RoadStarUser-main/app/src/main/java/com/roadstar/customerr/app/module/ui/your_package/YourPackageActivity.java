package com.roadstar.customerr.app.module.ui.your_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.module.ui.your_package.adapter.ViewPagerAdapter;
import com.roadstar.customerr.common.Services.Config;
import com.roadstar.customerr.common.base.BaseActivity;

public class YourPackageActivity extends BaseActivity {

    AppCompatImageView backBtn;
    AppCompatTextView title;
    ProgressBar progressDoalog;
    BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_your_package);

        progressDoalog = findViewById(R.id.progressBar);

        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);

        title.setText(R.string.Packages);
        backBtn.setOnClickListener(v -> onBackPressed());

        ViewPager viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //   displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");
                    String body = intent.getStringExtra("body");
                    Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                }
            }
        };

        /* setContentView(R.layout.activity_your_package);
        init();*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


   /* void init() {
        setActionBar("");
        initMap();
        bindClicklisteners();
        apiCalls();
    }

    private void bindClicklisteners() {

        findViewById(R.id.btn_submit_review).setOnClickListener(this);
        findViewById(R.id.btnCancelBooking).setOnClickListener(this);
        findViewById(R.id.btnCancelBookingSearch).setOnClickListener(this);
        findViewById(R.id.btnCall).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_submit_reason).setOnClickListener(this);

        findRatingBarByID(R.id.rating_bar_user).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating<=0) {
                    findRatingBarByID(R.id.rating_bar_user).setRating(1);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_chat:
                startActivity(this, SupportActivity.class);
                break;
            case R.id.btn_submit_review:
                callRatingApi();
                break;
            case R.id.btnCancelBooking:
            case R.id.btnCancelBookingSearch:
                animate();
                findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.VISIBLE);
                break;
            case R.id.btnCall:
                openPhoneDialer();
                break;
            case R.id.btn_cancel:
                animate();
               findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.GONE);
                break;
            case R.id.btn_submit_reason:
                callCancelRequestsApi();
                break;
        }
    }

    Handler handleCheckStatus;

    private void apiCalls() {
         handleCheckStatus = new Handler();
        //check status every 3 sec
        handleCheckStatus.postDelayed(new Runnable() {
            @Override
            public void run() {
                callCheckStatusApi();
                handleCheckStatus.postDelayed(this, 3000);


            }
        }, 3000);

        callCheckStatusApi();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("handler ", "handler onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onStop ", "handler onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy ", "handler onDestroy");
        if (handleCheckStatus == null) return;
        handleCheckStatus.removeCallbacksAndMessages(null);
    }*/

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }
}