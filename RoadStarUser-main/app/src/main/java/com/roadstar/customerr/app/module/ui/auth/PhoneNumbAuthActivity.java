package com.roadstar.customerr.app.module.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hbb20.CountryCodePicker;
import com.roadstar.customerr.R;
import com.roadstar.customerr.common.base.BaseActivity;
import com.roadstar.customerr.common.utils.AppUtils;

import static com.roadstar.customerr.common.utils.ApiConstants.PHONE;

public class PhoneNumbAuthActivity extends BaseActivity implements View.OnClickListener {

    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_numb_auth);
        init();
    }

    void init() {
        setActionBar(getString(R.string.register));
        ccp = findViewById(R.id.ccp);

        ccp.setCountryForPhoneCode(1);
        ccp.setCountryForNameCode("us");

        bindClicklisteners();
    }

    private void bindClicklisteners() {
        findViewById(R.id.btn_next).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                if (isParamValid()) {

                    goToVerifCodeScreen();
                }
                break;
        }
    }

    private boolean isParamValid() {
        boolean valid = true;

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_phone_numb))) //Check the Mobile Number
        {
            findEditTextById(R.id.et_phone_numb).setError(getString(R.string.error_phone_numb));
            valid = false;
            return valid;
        }
        findEditTextById(R.id.et_phone_numb).setError(null);


        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    void goToVerifCodeScreen() {
        String phone = ccp.getSelectedCountryCodeWithPlus() + "" + AppUtils.getEditTextString(findEditTextById(R.id.et_phone_numb));
        Intent intent = new Intent(PhoneNumbAuthActivity.this, VerifyOtpActivity.class);
        intent.putExtra(PHONE, phone);
        startActivity(PhoneNumbAuthActivity.this, intent);
    }
}