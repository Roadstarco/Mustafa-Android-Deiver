package com.roadstar.customerr.common.Services;

import static com.roadstar.customerr.app.module.ui.main.MainActivity.askDialog;
import static com.roadstar.customerr.app.module.ui.main.MainActivity.askDialogN;
import static com.roadstar.customerr.common.utils.ApiConstants.BOOKING_STATUS_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.CANCEL_REQUEST_API;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CANCEL_REASON;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_REQUEST_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.RATING_URL;

import android.app.Service;
import android.content.*;
import android.os.*;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.business.OnNetworkTaskListener;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.booking_status.BookingStatus;
import com.roadstar.customerr.app.module.ui.booking_activity.RequestHandlerActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    public static int timeCount = 0;
    public static boolean isAsk = false;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                timeCount = timeCount + 1;
                if(timeCount==30){
                    callCheckStatusApi();
                }
//            Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
            handler.postDelayed(runnable, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
//        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }


    public void callCheckStatusApi() {
        /*Create handle for the RetrofitInstance interface*/
        try {

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.getRequest(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);
                    if (response.code() == 200) {
                        String res = "";
                        JSONObject jsonObject = null;
                        try {
                            res = response.body().string();
                            jsonObject = new JSONObject(res);
                            JSONArray apiResponse = new JSONArray(jsonObject.getString("data"));
                            if (apiResponse.length() != 0) {
                                // requestCancelled();
                                Gson gson = new Gson();
                                BookingStatus bookingStatus = gson.fromJson(apiResponse.get(apiResponse.length()-1).toString(), BookingStatus.class);

                                if (bookingStatus.getStatus().equalsIgnoreCase("SEARCHING")) {
//                                    Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_SHORT).show();

                                    callCancelStatusApi(bookingStatus.getId()+"");
                                }else
                                    timeCount = 0;
                            }else
                                timeCount = 0;
                        } catch (JSONException | IOException e) {
                            timeCount = 0;
                            e.printStackTrace();
                        }


//                        finish();

                    }
                }

                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callCancelStatusApi(String id) {
        /*Create handle for the RetrofitInstance interface*/
        try {
            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.cancelRequest(HEADER_BEARER + UserManager.getToken(),id,"cancel");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);
                    if (response.code() == 200) {
                        if(!isAsk) {
//                            handler.removeCallbacks(runnable);
                            isAsk = true;
                            askDialog(id);
                        }else
                            askDialogN(id);

                    }
                }
                @Override
                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
//                    Toast.makeText(getApplicationContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}