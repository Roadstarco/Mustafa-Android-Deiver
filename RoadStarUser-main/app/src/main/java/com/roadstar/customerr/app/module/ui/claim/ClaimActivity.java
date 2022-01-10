package com.roadstar.customerr.app.module.ui.claim;

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

import static com.roadstar.customerr.common.utils.ApiConstants.CLAIM_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ERROR;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_MESSAGE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_SUBJECT;

public class ClaimActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);
        init();
    }

    void init() {
        setActionBar(getString(R.string.claim));
        bindClicklisteners();
    }

    private void bindClicklisteners() {

        findViewById(R.id.btn_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                if(isParamValid()){
                   callClaimApi();
                }

                break;


        }
    }
    private boolean isParamValid() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_req_numb) )) // Req numb
        {
            findTextViewById(R.id.tv_error_req_numb).setText(R.string.field_empty);
            valid = false;
            return valid;
        }
        findTextViewById(R.id.tv_error_req_numb).setText("");

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_info_details))) // Info Details
        {
            findTextViewById(R.id.tv_error_info_detail).setText(R.string.field_empty);
            valid = false;
            return valid;
        }
        findTextViewById(R.id.tv_error_info_detail).setText("");

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    //Claim Api
    private void callClaimApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(CLAIM_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getClaimParams());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    private Map<String, Object> getClaimParams() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_SUBJECT, AppUtils.getEditTextString(findEditTextById(R.id.et_req_numb)));
        map.put(KEY_MESSAGE, AppUtils.getEditTextString(findEditTextById(R.id.et_info_details)));

        return map;
    }

    JSONObject apiResponse;

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {

            apiResponse = new JSONObject(response.getResponse());
            if (response.getHttpRequestEndPoint().equals(CLAIM_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar("something went wrong");

                } else {
                    resetScreen(findViewById(R.id.layout_main));
                    showSnackBar("Complain submitted. Thankyou");
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