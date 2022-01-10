package com.roadstar.customerr.app.architecture;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.base.BaseFragment;
import com.roadstar.customerr.common.utils.AppConstants;
import com.roadstar.customerr.common.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bilal on 26/12/2017.
 */

/**
 *  Common practice calling web apis
 *  (Note: this applies also for calling web apis from activities required that the activity
 *  is extending
 */

public class WebApiCall extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public String getTitle() {
        return null;
    }

    private void callServerApi(){
        /**
         *  First create a {@link HttpRequestItem}
         */
        HttpRequestItem requestItem = new HttpRequestItem(
                AppConstants.getServerUrl(AppConstants.HELPER_LOGIN_VERIFICATION)); //Parameter is the rest api full url

        //  Set request type {@link NetworkUtils.HTTP_POST} by default its GET
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);

        // Set header (Cookies) params if required by server
        Map<String, String> headers = new HashMap<>();
        headers.put("Cookie", "value");
        requestItem.setHeaderParams(headers);
//        requestItem.setHeaderParams(AppUtils.getHeaderParams());    // Add header params (Cookies)

        /**
         *  Add body params/ query params required by the api call
         */
        Map<String, Object> params = new HashMap<>();
        params.put("mobileNumber", "number");
        params.put("verificationCode","code");
        requestItem.setParams(params);

        /**
         *  Now for calling rest api AppNetwork task class is used.\
         *  Parameters
         *  1) Progress Dialog to show during call (If not required send null). Try to use
         *  {@link BaseFragment#getProgressDialog(boolean)}
         *  {@link com.roadstar.customerr.common.base.BaseActivity#getProgressDialog(boolean)}
         *  2) {@link com.roadstar.customerr.app.business.OnNetworkTaskListener}
         *      Interface already implementing in BaseFragment & BaseActivity
         */
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false),
                this);
        appNetworkTask.execute(requestItem);
    }

    /**
     * (Overide that method always to handle network response)
     * HTTP network operation is successfully completed
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkSuccess(HttpResponseItem response ) {
        super.onNetworkSuccess(response);
        try {
            JSONObject responseJson = new JSONObject(response.getResponse());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * For some reasons there is/are some error(s) in network operation
     *
     * @param response {@link HttpResponseItem}
     */
    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
    }
}
