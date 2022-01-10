package com.roadstar.customerr.app.module.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.roadstar.customerr.R;
import com.roadstar.customerr.common.base.BaseActivity;
import com.roadstar.customerr.common.utils.AppUtils;

import java.util.concurrent.TimeUnit;

import static com.roadstar.customerr.MyApplication.sAuth;
import static com.roadstar.customerr.common.utils.ApiConstants.PHONE;

public class VerifyOtpActivity extends BaseActivity implements View.OnClickListener {
    PhoneAuthProvider.OnVerificationStateChangedCallbacks phoneAuthCallback;
    OtpEditText otpEditText;
    private String verificationCode, otp, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        init();
    }

    void init() {
        setActionBar(getString(R.string.verify_otp));
        phoneNumber = getIntent().getStringExtra(PHONE);
        String formattedPhone = String.format(getResources().getString(R.string.str_phone_numb), phoneNumber);
        findTextViewById(R.id.tv_code_numb).setText(formattedPhone);
        bindClicklisteners();

        otpEditText = (OtpEditText) findEditTextById(R.id.et_otp);
        otpEditText.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                //  Toast.makeText(OtpVerificationActivity.this, "Completed " + value, Toast.LENGTH_SHORT).show();
                AppUtils.hideSoftKeyboard(VerifyOtpActivity.this);
            }
        });

//        gotoRegActivity();
        initPhoneAuth();
        AuthPhoneNo();
    }

    private void bindClicklisteners() {
        findViewById(R.id.tv_resend).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_resend:
                AuthPhoneNo();
                break;
            case R.id.btn_next:
                if (isParamValid()) {

                    try {
                        showProgressDialog();
                        otp = otpEditText.getText().toString();
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                        VerifyOtp(credential);
                    } catch (Exception e) {
                        hideProgressDialog();
                        showSnackBar("Incorrect OTP");
                    }

                }

                break;
        }
    }

    private boolean isParamValid() {
        boolean valid = true;
        if (otpEditText.getText().length() != 6) //Check the OTP
        {
            showSnackBar(getString(R.string.error_invalid_otp));
            valid = false;
            return valid;
        }

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    private void initPhoneAuth() {


        phoneAuthCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                hideProgressDialog();
               // showSnackBar(getString(R.string.verif_comp));
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                hideProgressDialog();
                showSnackBar(getString(R.string.verif_fail));
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                hideProgressDialog();
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                showSnackBar(getString(R.string.code_sent));
                //   findViewById(R.id.lay_otp).setVisibility(View.VISIBLE);
                // requestFocus(findViewById(R.id.lay_otp));
            }
        };
    }

    private void AuthPhoneNo() {
        showProgressDialog();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                this,        // Activity (for callback binding)
                phoneAuthCallback);                      // OnVerificationStateChangedCallbacks
    }

    private void VerifyOtp(PhoneAuthCredential credential) {
        sAuth.signInWithCredential(credential)
                .addOnCompleteListener(new com.google.android.gms.tasks.OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            gotoRegActivity();
                        } else {
                            hideProgressDialog();
                            showSnackBar("Incorrect OTP");
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sAuth.signOut();
    }

    void gotoRegActivity(){
        Intent intent = new Intent(VerifyOtpActivity.this, RegistrationActivity.class);
        intent.putExtra(PHONE,phoneNumber);
        startActivity(VerifyOtpActivity.this,intent);
    }
}
