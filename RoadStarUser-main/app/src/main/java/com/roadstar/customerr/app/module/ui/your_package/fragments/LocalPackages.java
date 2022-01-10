package com.roadstar.customerr.app.module.ui.your_package.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.module.ui.chat.ChatActivity;
import com.roadstar.customerr.app.module.ui.support.SupportActivity;
import com.roadstar.customerr.app.module.ui.your_package.PackageStatusHandlingFragment;

public class LocalPackages extends PackageStatusHandlingFragment implements View.OnClickListener {


    private View view;
    private SupportMapFragment mapFragment;
    AppCompatRatingBar ratingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_local_packages, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);;//(SupportMapFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        init();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (view.getId()) {
            case R.id.btn_chat:
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user",bookingStatus.getUser());
                startActivity(intent);
//                getActivity().startActivity(new Intent(getActivity(),SupportActivity.class));
                break;
            case R.id.btn_submit_review:
                callRatingApi();
                break;
            case R.id.btnCancelBooking:
            case R.id.btnCancelBookingSearch:
                animate(R.id.btnCancelBookingSearch);
                findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.VISIBLE);
                break;
            case R.id.btnCall:
                openPhoneDialer();
                break;
            case R.id.btn_cancel:
                animate(R.id.btn_cancel);
                findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.GONE);
                break;
            case R.id.btn_submit_reason:
                TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.et_cancel_reason);

                if(textInputEditText.getText().toString().isEmpty()){
                    textInputEditText.setError("Enter cancel reason here!");
                    textInputEditText.requestFocus();
                    return;
                }
                callCancelRequestsApi();
                break;
        }
    }

    void init() {
        if (mapFragment != null)
            initMap(mapFragment);
        bindClicklisteners();
        apiCalls();
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


    private void bindClicklisteners() {

        view.findViewById(R.id.btn_submit_review).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRatingApi();
            }
        });

        view.findViewById(R.id.btnCancelBooking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((AppCompatButton) view.findViewById(R.id.btnCancelBooking)).getText().equals("Share")) {
                    animate(R.id.btnCancelBookingSearch);
                    findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.VISIBLE);
                }else {

                }
            }
        });
        view.findViewById(R.id.btn_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("user",bookingStatus.getProvider());
                startActivity(intent);
            }
        });
        view.findViewById(R.id.btnCancelBookingSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(R.id.btnCancelBookingSearch);
                findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.btnCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhoneDialer();
            }
        });
        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(R.id.btn_cancel);
                findViewById(R.id.lay_cancel_reason_bsd).setVisibility(View.GONE);
            }
        });
        view.findViewById(R.id.btn_submit_reason).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputEditText textInputEditText = (TextInputEditText) findViewById(R.id.et_cancel_reason);

                if(textInputEditText.getText().toString().isEmpty()){
                    textInputEditText.setError("Enter cancel reason here!");
                    textInputEditText.requestFocus();
                    return;
                }
                callCancelRequestsApi();
            }
        });
        ratingBar = view.findViewById(R.id.rating_bar_user);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating<=0) {
                    findRatingBarByID(R.id.rating_bar_user).setRating(1);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("onDestroy ", "handler onDestroy");
        if (handleCheckStatus == null) return;
        handleCheckStatus.removeCallbacksAndMessages(null);
    }

}