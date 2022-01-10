package com.roadstar.customerr.app.internationalDelivery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.internationalDelivery.adapter.ViewPagerAdapter;
import com.roadstar.customerr.common.base.BaseActivity;

import static com.roadstar.customerr.app.internationalDelivery.fragments.PostTrip.bottomSheetBehavior;
import static com.roadstar.customerr.app.internationalDelivery.fragments.PostTrip.inProgress;
import static com.roadstar.customerr.app.internationalDelivery.fragments.PostTrip.isSheetOpen;

public class InternationalMainActivity extends AppCompatActivity {

    ViewPager viewPager;
    AppCompatImageView backBtn;
    AppCompatTextView title;
    ProgressBar progressDoalog;
    private ViewPagerAdapter adapter;
    public static InternationalMainActivity internationalMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_international__main);

        internationalMainActivity = this;
        progressDoalog = findViewById(R.id.progressBar);

        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);

        title.setText(R.string.International_delivery_title);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ViewPager viewPager = findViewById(R.id.view_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.getItem(0).onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void onBackPressed() {
        if (isSheetOpen) {
            isSheetOpen = false;
            inProgress.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(adapter);
//
//
//        TabLayout tabLayout = findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(viewPager);
    }
}