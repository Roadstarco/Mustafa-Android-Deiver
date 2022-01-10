package com.roadstar.customerr.app.module.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.broooapps.otpedittext2.OnCompleteListener;
import com.broooapps.otpedittext2.OtpEditText;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.BaseActivity;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.roadstar.customerr.common.utils.ApiConstants.FORGET_PASSWORD_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CONFIRM_PASS;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_EMAIL;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ERROR;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_PASSWORD;
import static com.roadstar.customerr.common.utils.ApiConstants.RESET_PASSWORD_URL;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    ScrollView simpleLayout ,verifyLayout;
    EditText new_password , confirm_password ;

    OtpEditText otpEditText;

    private String sendOTP = "";
    private String ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        init();
    }

    void init() {
        setActionBar(getString(R.string.forget_password));

        simpleLayout = findViewById(R.id.simpleLayout);
        verifyLayout = findViewById(R.id.verifyLayout);

        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);

        otpEditText = (OtpEditText) findEditTextById(R.id.et_otp);
        otpEditText.setOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(String value) {
                //  Toast.makeText(OtpVerificationActivity.this, "Completed " + value, Toast.LENGTH_SHORT).show();
                AppUtils.hideSoftKeyboard(ForgetPasswordActivity.this);
            }
        });


        bindClicklisteners();
    }

    private boolean isOTPParamValid() {
        boolean valid = true;
        if (otpEditText.getText().length() != 6 || !otpEditText.getText().toString().equals(sendOTP)) //Check the OTP
        {
            showSnackBar(getString(R.string.error_invalid_otp));
            valid = false;
            return valid;
        }

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    private void bindClicklisteners() {

        findViewById(R.id.btn_submit).setOnClickListener(this);
        findViewById(R.id.verifyOtp).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_submit:
                if(isParamValid())
                    callForgetPassApi();
                break;

            case R.id.verifyOtp:

                if (isOTPParamValid() && isVerifyParamsValid()){

                    callResetPassword();

                }

                break;

        }
    }



    private boolean isParamValid() {
        boolean valid = true;
        if (!AppUtils.isEmailValid(findEditTextById(R.id.et_email).getText().toString())) //Check the User Name
        {
            findTextInputLayout(R.id.lay_email).setError(getString(R.string.error_invalid_email));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_email).setError(null);

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    private boolean isVerifyParamsValid() {
        boolean valid = true;
        if (new_password.getText().toString().length() <= 6) //Check the User Name
        {
            new_password.setError("password length must be greater then 6");
            valid = false;
            return valid;
        }
        new_password.setError(null);

        if (!confirm_password.getText().toString().equals(new_password.getText().toString())) //Check the User Name
        {
            confirm_password.setError("confirm password mis-match");
            valid = false;
            return valid;
        }
        confirm_password.setError(null);

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }


    //Forget Password
    private void callForgetPassApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(FORGET_PASSWORD_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getRegParam());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    private void callResetPassword() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(RESET_PASSWORD_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        requestItem.setParams(getResetParam());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    private Map<String, Object> getRegParam() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_EMAIL, AppUtils.getEditTextString(findEditTextById(R.id.et_email)));
        return map;
    }
    private Map<String, Object> getResetParam() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_ID, ID);
        map.put(KEY_PASSWORD, AppUtils.getEditTextString(findEditTextById(R.id.new_password)));
        map.put(KEY_CONFIRM_PASS, AppUtils.getEditTextString(findEditTextById(R.id.confirm_password)));
        return map;
    }

    JSONObject apiResponse;

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {

            apiResponse = new JSONObject(response.getResponse());
            if (response.getHttpRequestEndPoint().equals(FORGET_PASSWORD_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar(apiResponse.getString("message"));

                } else {
                    JSONObject user = apiResponse.optJSONObject("user");
                    sendOTP = user.getString("otp");
                    ID = user.getString("id");

                    simpleLayout.setVisibility(View.GONE);
                    verifyLayout.setVisibility(View.VISIBLE);

                   showSnackBar("Password reset email sent Enter");
                }
            }else if (response.getHttpRequestEndPoint().equals(RESET_PASSWORD_URL)){
                if (apiResponse.has(KEY_ERROR))
                    showSnackBar(apiResponse.getString("message"));
                else{
                    Toast.makeText(this, "changed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPasswordActivity.this,SigninActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            hideProgressDialog();
        }
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        hideProgressDialog();
    }



}
