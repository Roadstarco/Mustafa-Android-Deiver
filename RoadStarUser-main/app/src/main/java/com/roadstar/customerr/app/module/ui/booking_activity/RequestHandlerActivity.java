package com.roadstar.customerr.app.module.ui.booking_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.BaseItem;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.JobPost;
import com.roadstar.customerr.app.data.models.Progress;
import com.roadstar.customerr.app.data.models.Service;
import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.adapter.ServicesAdapter;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.Services.BackgroundService;
import com.roadstar.customerr.common.base.recycler_view.BaseRecyclerViewHolder;
import com.roadstar.customerr.common.base.recycler_view.OnRecyclerViewItemClickListener;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.DateUtils;
import com.roadstar.customerr.common.utils.FileUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.roadstar.customerr.common.views.VizImageView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.Services.BackgroundService.isAsk;
import static com.roadstar.customerr.common.utils.ApiConstants.GET_FARE_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.GET_SERVICES_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;
import static com.roadstar.customerr.common.utils.ApiConstants.SUBMIT_REQUEST_URL;
import static com.roadstar.customerr.common.utils.DateUtils.HOUR_FORMAT_1;

public class RequestHandlerActivity extends AttachmentHandlingActivity implements OnRecyclerViewItemClickListener {

    JobPost jobPost = new JobPost();
    private ServicesAdapter adapter;
    RecyclerView recyclerView;
    Handler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("RequestHandlerActivity:", "Activity RequestHandlerActivity called");

    }

//    @Override
//    public void setContentView(@LayoutRes int layoutResID) {
//        super.setContentView(layoutResID);
//
//    }

    //Claim Api
    public void callGetServiceApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(GET_SERVICES_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    //Get Service Fare Api
    public void callGetServiceFareApi() {
        showProgressDialog();


        String d_longitude = SharedPreferenceManager.getInstance().read(PreferenceUtils.DEST_LNG, "");
        String d_latitude = SharedPreferenceManager.getInstance().read(PreferenceUtils.DEST_LAT, "");
        String s_longitude = SharedPreferenceManager.getInstance().read(PreferenceUtils.SOURCE_LNG, "");
        String s_latitude = SharedPreferenceManager.getInstance().read(PreferenceUtils.SOURCE_LAT, "");


        String url_param = "?s_latitude=" + s_latitude +
                "&s_longitude=" + s_longitude +
                "&d_latitude=" + d_latitude +
                "&d_longitude=" + d_longitude +
                "&service_type=" + service.getId();
        HttpRequestItem requestItem = new HttpRequestItem(GET_FARE_URL, url_param);
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);


    }


    public static Map<String, String> getHeaderParams() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", HEADER_BEARER + UserManager.getToken());
        map.put("X-Requested-With", HEADER_KEY_XMLHTTP);
        //map.put("Content-Type", "application/x-www-form-urlencode");

        return map;
    }

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {


            if (response.getHttpRequestEndPoint().equals(GET_SERVICES_URL)) {
                JSONArray apiResponse = new JSONArray(response.getResponse());
                if (apiResponse.length() == 0) {
                    showSnackBar("No data found");

                } else {

                    List<BaseItem> list = new Gson().fromJson(apiResponse.toString(), new TypeToken<List<Service>>() {
                    }.getType());
                    populateList(list);
                }
            } else if (response.getHttpRequestEndPoint().equals(GET_FARE_URL)) {
                JSONObject apiResponse = new JSONObject(response.getResponse());
                if (apiResponse == null) {
                    showSnackBar("No data found");

                } else {

                    jobPost.setDistance(String.valueOf(apiResponse.getInt("distance")));
                    jobPost.setEstimatedFare(String.valueOf(apiResponse.getDouble("estimated_fare")));
                    jobPost.setTime(apiResponse.getString("time"));
                    showLayPricing(jobPost);
                }
            } else if (response.getHttpRequestEndPoint().equals(SUBMIT_REQUEST_URL)) {
                JSONObject apiResponse = new JSONObject(response.getResponse());
                if (apiResponse == null) {
                    showSnackBar("No data found");

                } else {
                    animate();
                    // resetLay();
                    findViewById(R.id.lay_rating).setVisibility(View.VISIBLE);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        hideProgressDialog();
    }

    public void setupServiceRecycleView() {

        recyclerView = findViewById(R.id.recycler_view_service);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerView.setHasFixedSize(false);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

    }


    private void addProgressItem() {
        populateItem(new Progress());
    }

    private void populateItem(BaseItem item) {

        if (adapter == null) {
            adapter = new ServicesAdapter(this, null, this);
            recyclerView.setAdapter(adapter);
        }
        adapter.add(item);
    }

    private void removeProgressItem() {
        if (adapter != null && adapter.getAdapterCount() > 0 &&
                adapter.getItemAt(adapter.getAdapterCount() - 1) instanceof Progress) {
            adapter.remove(adapter.getAdapterCount() - 1);
        }
    }

    private void populateList(List<BaseItem> itemList) {

        if (itemList.size() > 0) {

            if (adapter == null) {
                adapter = new ServicesAdapter(this, itemList, this);
                recyclerView.setAdapter(adapter);

            } else {
                adapter.addAll(itemList);
                adapter.notifyDataSetChanged();


            }
        } else
            checkLoadedItems();


    }

    private void checkLoadedItems() {

//        if (getParentView()!=null)
//            findViewById(R.id.lay_info).setVisibility(isListEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean isListEmpty() {
        if (adapter == null)
            return true;
        if (adapter.getItemCount() == 0)
            return true;
        return false;
    }

    Service service;


    @Override
    public void onRecyclerViewItemClick(BaseRecyclerViewHolder holder) {
        super.onRecyclerViewItemClick(holder);
        service = (Service) adapter.getItemAt(holder.getAdapterPosition());
        jobPost.setName(service.getName());
        jobPost.setService_type(String.valueOf(service.getId()));
        callGetServiceFareApi();
    }

    @Override
    public void onRecyclerViewChildItemClick(BaseRecyclerViewHolder holder, int resourceId) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        clearAdapter();
    }

    void clearAdapter() {
        adapter = null;
        if (recyclerView != null)
        recyclerView.setAdapter(null);
//        if (!isViewNull())
//            findViewById(R.id.lay_info).setVisibility(View.GONE);
    }

    public void showLayPricing(JobPost jobPost) {
        animate();
        findViewById(R.id.lay_choose_vehicle).setVisibility(View.GONE);
        findViewById(R.id.lay_pricing).setVisibility(View.VISIBLE);
        ImageView imageView =  findViewById(R.id.img_vehicle);
        findTextViewById(R.id.tv_vehicle_type).setText(jobPost.getName());
        findTextViewById(R.id.tv_total_distance).setText(jobPost.getDistance() + getString(R.string.distance_unit));
        findTextViewById(R.id.tv_est_fare).setText(jobPost.getEstimatedFare() + getString(R.string.currency_unit));
        Glide.with(getApplicationContext()).load(service.getImage()).into(imageView);

    }

    public void showLayPackageDetail() {
        animate();
        if (service != null){
            service.setCategory(findTextViewById(R.id.tv_category).getText().toString());
            service.setProduct_type(findTextViewById(R.id.tv_product).getText().toString());
            service.setProduct_weight(findEditTextById(R.id.et_weight).getText().toString());
            service.setWeightUnit(findTextViewById(R.id.tv_weight_unit).getText().toString());
            service.setInstruction(findEditTextById(R.id.et_intruct).getText().toString());
        }
        findViewById(R.id.lay_pkg_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_recv_detail).setVisibility(View.VISIBLE);
    }

    public void showLayReceiverDetail() {
        animate();
        if (service != null){
            service.setReceiver_name(findEditTextById(R.id.et_receiver_name).getText().toString());
            service.setReceiver_phone(findEditTextById(R.id.et_receiver_phone_numb).getText().toString());
        }
        findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_pay_detail).setVisibility(View.VISIBLE);
    }


    public void resetLay(String msg) {
        Toast.makeText(RequestHandlerActivity.this, msg, Toast.LENGTH_SHORT).show();

        findViewById(R.id.lay_choose_vehicle).setVisibility(View.GONE);
        findViewById(R.id.lay_pricing).setVisibility(View.GONE);
        findViewById(R.id.lay_pkg_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_pay_detail).setVisibility(View.GONE);
        onBackPressed();
    }

    public boolean isPackageParamValid() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findTextviewTextById(R.id.tv_category))) //Check Product weight
        {
            Toast.makeText(getApplicationContext(), "Category required", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }
        if (!AppUtils.isNotFieldEmpty(findTextviewTextById(R.id.tv_product))) //Check Product weight
        {
            Toast.makeText(getApplicationContext(), "Product required", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_weight))) //Check Product weight
        {
            findEditTextById(R.id.et_weight).setError("Weight required");//getString(R.string.field_empty));
            findEditTextById(R.id.et_weight).requestFocus();
            valid = false;
            return valid;
        }
        if (!AppUtils.isNotFieldEmpty(findTextviewTextById(R.id.tv_weight_unit))) //Check Product weight
        {
            Toast.makeText(getApplicationContext(), "Weight unit required", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if(service.getName().equalsIgnoreCase("MiniTruck")){
            if(Integer.parseInt(findEditTextById(R.id.et_weight).getText().toString())>800){
                Toast.makeText(getApplicationContext(), "Weight can't be more 800kg", Toast.LENGTH_SHORT).show();
                findEditTextById(R.id.et_weight).requestFocus();
                valid = false;
                return valid;
            }
        }else if(service.getName().equalsIgnoreCase("Medium Truck")){
            if(Integer.parseInt(findEditTextById(R.id.et_weight).getText().toString())>1900){
                Toast.makeText(getApplicationContext(), "Weight can't be more 1900kg", Toast.LENGTH_SHORT).show();
                findEditTextById(R.id.et_weight).requestFocus();
                valid = false;
                return valid;
            }
        }
        else if(service.getName().equalsIgnoreCase("Large Truck")){
            if(Integer.parseInt(findEditTextById(R.id.et_weight).getText().toString())>40000){
                Toast.makeText(getApplicationContext(), "Weight can't be more 40000kg", Toast.LENGTH_SHORT).show();
                findEditTextById(R.id.et_weight).requestFocus();
                valid = false;
                return valid;
            }
        }

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_width))) //Check Product weight
        {
            findEditTextById(R.id.et_width).setError("Product width required");//getString(R.string.field_empty));
            findEditTextById(R.id.et_width).requestFocus();
            valid = false;
            return valid;
        }
        if(Integer.parseInt(findEditTextById(R.id.et_width).getText().toString())>10){
            Toast.makeText(getApplicationContext(), "Product width can't exceed 10ft", Toast.LENGTH_SHORT).show();
            findEditTextById(R.id.et_width).requestFocus();
            valid = false;
            return valid;
        }
//        if(Integer.parseInt(findEditTextById(R.id.et_width).getText().toString())>)
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_height))) //Check Product weight
        {
            findEditTextById(R.id.et_height).setError("Product height required");//getString(R.string.field_empty));
            findEditTextById(R.id.et_height).requestFocus();
            valid = false;
            return valid;
        }
        if(Integer.parseInt(findEditTextById(R.id.et_height).getText().toString())>14){
            Toast.makeText(getApplicationContext(), "Product height can't exceed 14ft", Toast.LENGTH_SHORT).show();
            findEditTextById(R.id.et_height).requestFocus();
            valid = false;
            return valid;
        }

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_intruct))) //Check Product instruction
        {
            findEditTextById(R.id.et_intruct).setError("Instruction required");//getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        findEditTextById(R.id.et_intruct).setError(null);
        findEditTextById(R.id.et_weight).setError(null);
        findEditTextById(R.id.et_width).setError(null);
        findEditTextById(R.id.et_height).setError(null);
        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    public boolean isPackageParamValidNew() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_name))) //Check Product weight
        {
            findEditTextById(R.id.et_name).setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        findEditTextById(R.id.et_name).setError(null);

        if (TextUtils.isEmpty(findTextViewById(R.id.tv_product).getText().toString())) //Check Product instruction
        {
            Toast.makeText(this, "product size missing", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }


        if (TextUtils.isEmpty(findTextViewById(R.id.tv_category).getText().toString()))  //Check Product instruction
        {
            Toast.makeText(this, "product type missing", Toast.LENGTH_SHORT).show();
            valid = false;
            return valid;
        }

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.amount))) //Check Product weight
        {
            findEditTextById(R.id.amount).setError(getString(R.string.field_empty));
            valid = false;
            return valid;
        }
        findEditTextById(R.id.amount).setError(null);


        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }


    public boolean isReceiverDetailParamValid() {

        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_receiver_name))) //Check Receiver Details
        {
            findEditTextById(R.id.et_receiver_name).setError("Receiver name required");//getString(R.string.field_empty));
            findEditTextById(R.id.et_receiver_name).requestFocus();
            valid = false;
            return valid;
        }
        findEditTextById(R.id.et_receiver_name).setError(null);
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_receiver_phone_numb))) //Check Product instruction
        {
            findEditTextById(R.id.et_receiver_phone_numb).setError("Receiver Phone no required");//getString(R.string.field_empty));
            findEditTextById(R.id.et_receiver_phone_numb).requestFocus();
            valid = false;
            return valid;
        }
        findEditTextById(R.id.et_receiver_phone_numb).setError(null);
        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }


    public void callSendTripRequestApi(){
        showProgressDialog();
        /*Create handle for the RetrofitInstance interface*/
        try {


            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.requestTrip(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, getJobReqParam());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    hideProgressDialog();

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);

                    if (response.code() == 200) {
                        UserManager.setRideInprogress(true);
                        Toast.makeText(RequestHandlerActivity.this, "Your request is sent successfully", Toast.LENGTH_LONG).show();
                        finish();

                    } else
                        showSnackBar(getString(R.string.job_already_in_progress));

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(RequestHandlerActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }
    public void callSendRequestApi() {
        showProgressDialog();
        /*Create handle for the RetrofitInstance interface*/
        try {

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.sendRequest(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, getJobReqParam());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    hideProgressDialog();
                    String res = "";
                    JSONObject jsonObject = null;
                    try {
                         res = response.body().string();
                        jsonObject = new JSONObject(res);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);
                    if (response.code() == 200) {
                        try {
                            if(jsonObject.has("error")){
                                Toast.makeText(RequestHandlerActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();
                                askDialog();

                            }else {
                                if (jsonObject.getString("message").equalsIgnoreCase("No Drivers Found")) {
                                    askDialog();
                                }else{
                                    UserManager.setRideInprogress(true);
                                    Toast.makeText(RequestHandlerActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                    BackgroundService.timeCount =0;
//                                    startService(new Intent(getApplicationContext(),BackgroundService.class));
                                    finish();
                                }

//                                Toast.makeText(RequestHandlerActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                        finish();

                    } else
                        showSnackBar(getString(R.string.job_already_in_progress));
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(RequestHandlerActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }

    public void askDialog(){
        new AlertDialog.Builder(this).setTitle("No Driver Found!")
                .setMessage("Do you want to send request to other available drivers?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // Perform Action & Dismiss dialog
                                callReSendRequestApi();
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    public void callReSendRequestApi() {


        showProgressDialog();
        /*Create handle for the RetrofitInstance interface*/
        try {

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.reSendRequest(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, getJobReqParam());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    hideProgressDialog();
                    String res = "";
                    JSONObject jsonObject = null;
                    try {
                        res = response.body().string();
                        jsonObject = new JSONObject(res);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);

                    if (response.code() == 200) {
                        try {
                            if(jsonObject.has("error")){
                                Toast.makeText(RequestHandlerActivity.this, jsonObject.getString("error"), Toast.LENGTH_LONG).show();

                            }else {
                                if (!jsonObject.getString("message").equalsIgnoreCase("No Drivers Found")) {
                                    UserManager.setRideInprogress(true);
                                    BackgroundService.timeCount =0;
                                    isAsk = false;
//                                    startService(new Intent(getApplicationContext(),BackgroundService.class));
                                }
                                BackgroundService.timeCount =0;
                                isAsk = false;
                                Toast.makeText(RequestHandlerActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();

                    } else
                        showSnackBar(getString(R.string.job_already_in_progress));
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(RequestHandlerActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }


    MultipartBody getJobReqParam() {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        try {

            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("receiver_phone", AppUtils.getEditTextString(findEditTextById(R.id.et_receiver_phone_numb)));
            builder.addFormDataPart("receiver_name", AppUtils.getEditTextString(findEditTextById(R.id.et_receiver_name)));
            builder.addFormDataPart("instruction", AppUtils.getEditTextString(findEditTextById(R.id.et_intruct)));
            builder.addFormDataPart("product_distribution", AppUtils.getEditTextString(findEditTextById(R.id.et_product_distribution)));
            builder.addFormDataPart("product_weight", AppUtils.getEditTextString(findEditTextById(R.id.et_weight)));
            builder.addFormDataPart("product_width", AppUtils.getEditTextString(findEditTextById(R.id.et_width)));
            builder.addFormDataPart("product_height", AppUtils.getEditTextString(findEditTextById(R.id.et_height)));
            builder.addFormDataPart("product_type", findTextViewById(R.id.tv_product).getText().toString());
            builder.addFormDataPart("category", findTextViewById(R.id.tv_category).getText().toString());
            builder.addFormDataPart("weight_unit", findTextViewById(R.id.tv_weight_unit).getText().toString());
            builder.addFormDataPart("payment_mode", findRadioButtonById(R.id.radio_btn_cash).isChecked() ? "CASH" : "CARD");

            if (findRadioButtonById(R.id.radio_btn_card).isChecked())
                builder.addFormDataPart("card_id", UserManager.getCardID());
            builder.addFormDataPart("walletBalance", "0");
            builder.addFormDataPart("schedule_time", DateUtils.getCurrentTime(HOUR_FORMAT_1));
            builder.addFormDataPart("d_address", SharedPreferenceManager.getInstance().read(PreferenceUtils.DEST_ADDRESS, ""));
            builder.addFormDataPart("s_address", SharedPreferenceManager.getInstance().read(PreferenceUtils.SOURCE_ADDRESS, ""));
            builder.addFormDataPart("d_longitude", SharedPreferenceManager.getInstance().read(PreferenceUtils.DEST_LNG, ""));
            builder.addFormDataPart("d_latitude", SharedPreferenceManager.getInstance().read(PreferenceUtils.DEST_LAT, ""));
            builder.addFormDataPart("s_longitude", SharedPreferenceManager.getInstance().read(PreferenceUtils.SOURCE_LNG, ""));
            builder.addFormDataPart("s_latitude", SharedPreferenceManager.getInstance().read(PreferenceUtils.SOURCE_LAT, ""));
            builder.addFormDataPart("schedule_date", "");
            builder.addFormDataPart("name", service.getName());
            builder.addFormDataPart("service_type", String.valueOf(service.getId()));
            builder.addFormDataPart("distance", String.valueOf(jobPost.getDistance()));


            String pathToStoredImage1 = FileUtils.getPath(RequestHandlerActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_1).getImageURL()));

            if (pathToStoredImage1 != null) {
                File file1 = new File(pathToStoredImage1);
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                builder.addFormDataPart("attachment1", file1.getName(), requestBody1);
            }


            String pathToStoredImage2 = FileUtils.getPath(RequestHandlerActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_2).getImageURL()));
            if (pathToStoredImage2 != null) {
                File file2 = new File(pathToStoredImage2);
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                builder.addFormDataPart("attachment2", file2.getName(), requestBody2);
            }
            String pathToStoredImage3 = FileUtils.getPath(RequestHandlerActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_3).getImageURL()));
            if (pathToStoredImage3 != null) {
                File file3 = new File(pathToStoredImage3);
                RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/*"), file3);
                builder.addFormDataPart("attachment3", file3.getName(), requestBody3);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.build();


    }
}


