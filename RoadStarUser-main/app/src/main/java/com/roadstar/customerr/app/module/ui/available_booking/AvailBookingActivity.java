package com.roadstar.customerr.app.module.ui.available_booking;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.module.ui.available_booking.adapter.MyTripsPagerAdapter;
import com.roadstar.customerr.app.module.ui.main.MainActivity;
import com.roadstar.customerr.app.module.ui.your_package.adapter.ViewPagerAdapter;
import com.roadstar.customerr.common.base.BaseActivity;

public class AvailBookingActivity extends BaseActivity implements View.OnClickListener {
    AppCompatImageView backBtn;
    AppCompatTextView title;
    ProgressBar progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail_booking);
        init();
    }

    void init() {
        progressDoalog = findViewById(R.id.progressBar);

        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);

        title.setText(R.string.Packages);
        backBtn.setOnClickListener(v -> onBackPressed());

        ViewPager viewPager = findViewById(R.id.view_pager);
        MyTripsPagerAdapter adapter = new MyTripsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        //  bindClicklisteners();
    }

    private void bindClicklisteners() {

        findViewById(R.id.btn_submit).setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                startActivityWithNoHistory(this, MainActivity.class);
                break;


        }
    }
}