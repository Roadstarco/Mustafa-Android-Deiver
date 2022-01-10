package com.roadstar.customerr.app.module.ui.profile;

import android.os.Bundle;
import android.view.View;

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

import static com.roadstar.customerr.common.utils.ApiConstants.CHANGE_PASSWORD_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CONFIRM_PASS;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ERROR;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_OLD_PASSWORD;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_PASSWORD;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }

    void init() {
        setActionBar(getString(R.string.change_password));
        bindClicklisteners();
    }

    private void bindClicklisteners() {
        findViewById(R.id.btn_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_save:
                if (isParamValid())
                    callUpdateProfileApi();
                break;

        }
    }

    private boolean isParamValid() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_old_pass))) //Check the Old Pass
        {
            findTextInputLayout(R.id.lay_old_pass).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_old_pass).setError(null);
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_new_pass))) //Check the New Pass
        {
            findTextInputLayout(R.id.lay_new_pass).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_new_pass).setError(null);
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_cnfrm_new_pass))) //Check the Confirm New Pass
        {
            findTextInputLayout(R.id.lay_cnfrm_new_pass).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_cnfrm_new_pass).setError(null);
        if (!AppUtils.getEditTextString(findEditTextById(R.id.et_new_pass)).equals(AppUtils.getEditTextString(findEditTextById(R.id.et_cnfrm_new_pass)))) //Match the  Pass
        {
            findTextInputLayout(R.id.lay_cnfrm_new_pass).setError(getString(R.string.error_pass_mis_match));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_cnfrm_new_pass).setError(null);
        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    //Signup
    private void callUpdateProfileApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(CHANGE_PASSWORD_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getChangePassParam());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }


    private Map<String, Object> getChangePassParam() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_OLD_PASSWORD, AppUtils.getEditTextString(findEditTextById(R.id.et_old_pass)));
        map.put(KEY_PASSWORD, AppUtils.getEditTextString(findEditTextById(R.id.et_new_pass)));
        map.put(KEY_CONFIRM_PASS, AppUtils.getEditTextString(findEditTextById(R.id.et_cnfrm_new_pass)));


        return map;
    }

    JSONObject apiResponse;

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {

            apiResponse = new JSONObject(response.getResponse());
            if (response.getHttpRequestEndPoint().equals(CHANGE_PASSWORD_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar("The password confirmation does not match.");

                } else {

                    showSnackBar(getString(R.string.pass_update_success));
                    // gotoMainActivity();
                }
            }

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        hideProgressDialog();
    }
}