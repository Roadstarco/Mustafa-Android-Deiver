package com.roadstar.customerr.app.internationalDelivery;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.Service;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.internationalDelivery.classes.LockableBottomSheetBehavior;
import com.roadstar.customerr.app.internationalDelivery.fragments.AvailableTrips;
import com.roadstar.customerr.app.module.ui.booking_activity.RequestHandlerActivity;
import com.roadstar.customerr.app.module.ui.dialog.CancelReqDialog;
import com.roadstar.customerr.app.module.ui.payment_method.PaymentMethodActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.FileUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.app.internationalDelivery.InternationalMainActivity.internationalMainActivity;
import static com.roadstar.customerr.app.internationalDelivery.adapter.InternationalTripsAvailableAdapter.getSingle_item;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;

public class InternationalTripDetailsActivity extends RequestHandlerActivity implements View.OnClickListener , CancelReqDialog.OnInputListener {

    private AppCompatImageView backBtn;
    private AppCompatTextView title;
    public LinearLayout inProgress;
    private EditText et_weight,et_width,et_height,et_intruct,amount;
    private ImageView iv_pkg_detail_back;
    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    private Service inputValues;
    private ProgressBar progressDoalog;
    private Handler handler ;
    private AppCompatButton viewCounterOfferButton,addBidButton;
    public int trip_id;
    private final boolean cardIsAvailable = false;
    private AvailAbleTripsModel item;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail_activity);

        setActionBar("Trip details");

        viewCounterOfferButton = findButtonById(R.id.view_counterOffer);
        addBidButton = findButtonById(R.id.postTrip);

        if (getIntent().hasExtra("trip_id"))
           trip_id = getIntent().getIntExtra("trip_id",-1);
        else
            trip_id = -1;

        if (getIntent().hasExtra("pos")){
            int pos = getIntent().getIntExtra("pos", 0);

             item = getSingle_item(pos);

            TextView srcDes = findViewById(R.id.srcDes);
            TextView service_type = findViewById(R.id.service_type);
            TextView size_value = findViewById(R.id.size_value);
            TextView practical_value = findViewById(R.id.practical_value);
            TextView arival_value = findViewById(R.id.arival_value);

            srcDes.setText(item.getTripfrom()+"-"+item.getTripto());
            service_type.setText(item.getService_type());
            size_value.setText(item.getItem_size());
            practical_value.setText(item.getOther_information());
            arival_value.setText(item.getArrival_date());

            setSpinners();

            if (item.getBid_details().getIs_counter() == 1){
                viewCounterOfferButton.setVisibility(View.VISIBLE);
            }else
                viewCounterOfferButton.setVisibility(View.GONE);

            if (item.getBid_details().getId() != null){
                addBidButton.setText(R.string.request_sent_text);
                addBidButton.setEnabled(false);
            }else
                addBidButton.setText(R.string.add_bid);

        }else{
            Toast.makeText(this, "no data found ", Toast.LENGTH_SHORT).show();
        }

        progressDoalog = findViewById(R.id.progressBar);

        inProgress = findViewById(R.id.inProgressLayout);
        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);

        backBtn.setOnClickListener(this);

        initiBottomSheetViews();
    }

    private void initiBottomSheetViews() {


        //et_weight = findViewById(R.id.et_weight);
        //et_width = findViewById(R.id.et_width);
        et_intruct = findViewById(R.id.et_intruct);
        //et_height = findViewById(R.id.et_height);
        amount = findViewById(R.id.amount);

        handler = new Handler();

        ConstraintLayout bottomSheet = findViewById(R.id.bottomSheet);
        iv_pkg_detail_back = findViewById(R.id.iv_pkg_detail_back);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (bottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            ((LockableBottomSheetBehavior) bottomSheetBehavior).setLocked(true);
                        }
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        inProgress.setVisibility(View.GONE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        findViewById(R.id.iv_attach_1).setOnClickListener(this);
        findViewById(R.id.iv_attach_2).setOnClickListener(this);
        findViewById(R.id.iv_attach_3).setOnClickListener(this);
        findViewById(R.id.img_remove1).setOnClickListener(this);
        findViewById(R.id.img_remove2).setOnClickListener(this);
        findViewById(R.id.img_remove3).setOnClickListener(this);

        findViewById(R.id.btn_pkg_dtl_next).setOnClickListener(this);

        AppCompatButton makeRequest = findViewById(R.id.btn_pay);
        makeRequest.setText(R.string.submit);
        makeRequest.setOnClickListener(this);

       // findViewById(R.id.iv_rating_back).setOnClickListener(this);
        findViewById(R.id.btn_submit_req).setOnClickListener(this);
        findViewById(R.id.btn_cancel_req).setOnClickListener(this);


        findViewById(R.id.iv_pay_back).setOnClickListener(this);
        findViewById(R.id.iv_recev_detail_back).setOnClickListener(this);

        iv_pkg_detail_back.setOnClickListener(this);

    }


    boolean isCategorySelected = true, isProductSelected = true, isWeightUnitSelected = true;

    public void setSpinners() {
        //Spinner Category
        AppCompatSpinner spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_product_type);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
                if (isCategorySelected) {
                    isCategorySelected = !isCategorySelected;
                } else {
                    findTextViewById(R.id.tv_category).setText(spinnerCategory.getSelectedItem().toString());
                    spinnerCategory.setSelection(0);
                    isCategorySelected = true;
                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        //Spinner Product Type
        Spinner spinnerProduct = (Spinner) findViewById(R.id.spinner_product_size);

        spinnerProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here

                if (isProductSelected) {
                    isProductSelected = !isProductSelected;
                } else {
                    findTextViewById(R.id.tv_product).setText(spinnerProduct.getSelectedItem().toString());
                    spinnerProduct.setSelection(0);
                    isProductSelected = true;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void cancelReqDialog() {
        CancelReqDialog cancelReqDialog = new CancelReqDialog();
        cancelReqDialog.show(getSupportFragmentManager(), "CancelReqDialog");
        cancelReqDialog.setCancelable(false);

    }


    @Override
    public void onCancelReqResponse(Boolean val) {

        if (val) {
            reset(getString(R.string.req_cancel));
        }

    }

    public void reset(String msg){
        Toast.makeText(InternationalTripDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
        finish(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_pkg_detail_back:

                inProgress.setVisibility(View.GONE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                break;
            case R.id.btn_submit_req:
                if (findRadioButtonById(R.id.radio_btn_card).isChecked() && !UserManager.isCardAdded()) /*//payment method is card but card not added*/ {
                    startActivity(this, PaymentMethodActivity.class); // add card first
                } else {
                    callSendRequestApi();
                }

                break;
            case R.id.btn_cancel_req:
                cancelReqDialog();
                break;

            case R.id.iv_rating_back:
                findViewById(R.id.lay_rating).setVisibility(View.GONE);
                break;
            case R.id.iv_attach_1:
                checkStoragePermission(R.id.iv_attach_1);

                break;
            case R.id.iv_attach_2:
                checkStoragePermission(R.id.iv_attach_2);

                break;
            case R.id.iv_attach_3:
                checkStoragePermission(R.id.iv_attach_3);

                break;
            case R.id.img_remove1:
                removeImage(R.id.img_remove1);

                break;
            case R.id.img_remove2:
                removeImage(R.id.img_remove2);

                break;
            case R.id.img_remove3:
                removeImage(R.id.img_remove3);
                break;
            case R.id.postTrip:
                if (addBidButton.getText().toString().toLowerCase().equals("request sent") && item != null){
                    showBidDetail(item.getId());
                }else {
                    inProgress.setVisibility(View.VISIBLE);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                break;
            case R.id.view_counterOffer:
                if (item != null && item.getBid_details().getIs_counter() == 1 && item.getBid_details().getCounter_amount() != null)
                    showCounterOffer(item.getBid_details().getCounter_amount(),item.getBid_details().getId(),item.getBid_details().getTrip_id());

                break;
            case R.id.btn_pkg_dtl_next:

                showDialogProgressbar();
                if (isPackageParamValidNew())
                    showLayPackageDetail();
                break;
            case R.id.btn_pay:

                if (isReceiverDetailParamValid()){
                    if (!UserManager.isCardAdded()){
                        showConfirmationDialog(PaymentMethodActivity.class);
                    }else {
                        callSendRequestApi();
                    }

                    /*
                    showDialogProgressbar();
                    showLayReceiverDetail();*/
                }
                break;

            case R.id.iv_pay_back:


                showDialogProgressbar();
                animate();

                findViewById(R.id.lay_recv_detail).setVisibility(View.VISIBLE);
                findViewById(R.id.lay_pay_detail).setVisibility(View.GONE);

                break;
            case R.id.iv_recev_detail_back:


                showDialogProgressbar();
                animate();

                findViewById(R.id.lay_pkg_detail).setVisibility(View.VISIBLE);
                findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);

                break;
            case R.id.inProgressLayout:
                break;
            default:
                super.onBackPressed();
                break;
        }
    }

    private void showConfirmationDialog(Class<PaymentMethodActivity> activityToOpen) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.entrcard_info_layout, null, false);


        TextView link = dialogView.findViewById(R.id.link);

        AppCompatButton proceed = dialogView.findViewById(R.id.proceed);
        AppCompatButton cancel = dialogView.findViewById(R.id.cancel);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.termConditionLink)));
                startActivity(browserIntent);
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent cardDetail = new Intent(InternationalTripDetailsActivity.this, activityToOpen);
                startActivity(cardDetail);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

    }


    private void showCounterOffer(Integer counterAmount, Integer bid_id, int trip_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.counter_bid_layout, null, false);


        TextView closeDialog = dialogView.findViewById(R.id.closeDialog);
        closeDialog.setVisibility(View.VISIBLE);

        TextView title = dialogView.findViewById(R.id.titleOffers);
        ProgressBar progressBarInDialog = dialogView.findViewById(R.id.progressBarInDialog);
        title.setText(R.string.counter_offer_details);
        EditText amount = dialogView.findViewById(R.id.et_bid_amount);
        amount.setText(String.valueOf(counterAmount));
        amount.setFocusable(false);
        amount.setClickable(false);
        amount.setEnabled(false);
        AppCompatButton sendButton = dialogView.findViewById(R.id.btn_accept);
        sendButton.setText(R.string.Accept);
        AppCompatButton cancel = dialogView.findViewById(R.id.cancel);
        cancel.setText(R.string.Reject);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(amount.getText())){
                    amount.setError(getString(R.string.add_bid_amount));
                }else {
                    AcceptCounterOffer(progressBarInDialog,bid_id,trip_id,true,alertDialog);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptCounterOffer(progressBarInDialog,bid_id,trip_id,false,alertDialog);            }
        });
    }

    private void AcceptCounterOffer(ProgressBar progressBarInDialog, int bid_id, int trip_id, boolean isAccept, AlertDialog alertDialog) {

        progressBarInDialog.setVisibility(View.VISIBLE);
        AvailAbleTripsModel model = new AvailAbleTripsModel();
        //model.setTrip_id(trip_id);
        model.setBid_id(bid_id);
        model.setTrip_id(trip_id);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call;
        if (isAccept){
            call = service.AcceptCounterOffer(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",model);
        }else {
            call = service.rejectCounterOffer(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",model);
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                progressBarInDialog.setVisibility(View.GONE);
                //((ListOfBidsOnTrip)context).progressDoalog.setVisibility(View.GONE);
                if (response.code() == 200){
                    alertDialog.dismiss();

                    if (isAccept)
                        Toast.makeText(InternationalTripDetailsActivity.this, "counter offer send !", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(InternationalTripDetailsActivity.this, "counter offer Rejected", Toast.LENGTH_SHORT).show();
                    if(internationalMainActivity!=null)
                        internationalMainActivity.finish();
                    finish();
                }
                if(internationalMainActivity!=null)
                    internationalMainActivity.finish();
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                progressBarInDialog.setVisibility(View.GONE);
                alertDialog.dismiss();
                Toast.makeText(InternationalTripDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showBidDetail(Integer id) {

    }

    MultipartBody getJobReqParam() {

        MultipartBody.Builder builder = new MultipartBody.Builder();

        try {

            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("trip_id", String.valueOf(trip_id)/*AppUtils.getEditTextString(findEditTextById(R.id.et_receiver_phone_numb))*/);
            builder.addFormDataPart("item", AppUtils.getEditTextString(findEditTextById(R.id.et_name)));
            builder.addFormDataPart("receiver_name",AppUtils.getEditTextString(findEditTextById(R.id.et_receiver_name)));
            builder.addFormDataPart("receiver_phone", AppUtils.getEditTextString(findEditTextById(R.id.et_receiver_phone_numb)));
            builder.addFormDataPart("item_type", findTextViewById(R.id.tv_category).getText().toString());
            builder.addFormDataPart("item_size", findTextViewById(R.id.tv_product).getText().toString());
            builder.addFormDataPart("description", AppUtils.getEditTextString(findEditTextById(R.id.et_intruct)));

            builder.addFormDataPart("amount", AppUtils.getEditTextString(findEditTextById(R.id.amount)));


            String pathToStoredImage1 = FileUtils.getPath(InternationalTripDetailsActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_1).getImageURL()));

            if (pathToStoredImage1 != null) {
                File file1 = new File(pathToStoredImage1);
                RequestBody requestBody1 = RequestBody.create(MediaType.parse("image/*"), file1);
                builder.addFormDataPart("picture1", file1.getName(), requestBody1);
            }


            String pathToStoredImage2 = FileUtils.getPath(InternationalTripDetailsActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_2).getImageURL()));
            if (pathToStoredImage2 != null) {
                File file2 = new File(pathToStoredImage2);
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                builder.addFormDataPart("picture2", file2.getName(), requestBody2);
            }
            String pathToStoredImage3 = FileUtils.getPath(InternationalTripDetailsActivity.this, Uri.parse(findVizImageView(R.id.iv_attach_3).getImageURL()));
            if (pathToStoredImage3 != null) {
                File file3 = new File(pathToStoredImage3);
                RequestBody requestBody3 = RequestBody.create(MediaType.parse("image/*"), file3);
                builder.addFormDataPart("picture3", file3.getName(), requestBody3);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
        return builder.build();


    }

    public void callSendRequestApi() {
        showProgressDialog();
        try {

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<AvailAbleTripsModel> call = apiInterface.bidOnTrip(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, getJobReqParam());
            call.enqueue(new Callback<AvailAbleTripsModel>() {
                @Override
                public void onResponse(@NotNull Call<AvailAbleTripsModel> call, @NotNull Response<AvailAbleTripsModel> response) {

                    hideProgressDialog();

                    //SendReqRes sendReqRes = (SendReqRes) new Gson().fromJson(String.valueOf(response.body()), SendReqRes.class);

                    if (response.code() == 200) {
                        UserManager.setRideInprogress(true);

                        addBidButton.setText(R.string.request_sent_text);
                        addBidButton.setEnabled(false);

                        Toast.makeText(InternationalTripDetailsActivity.this, "Your request is sent successfully", Toast.LENGTH_LONG).show();
                        if(internationalMainActivity!=null)
                            internationalMainActivity.finish();
                        finish();

                    } else
                        showSnackBar(getString(R.string.job_already_in_progress));

                }

                @Override
                public void onFailure(@NotNull Call<AvailAbleTripsModel> call, @NotNull Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(InternationalTripDetailsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }

    private void showDialogProgressbar() {
        progressDoalog.setVisibility(View.VISIBLE);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDoalog.setVisibility(View.GONE);
            }
        },500);

    }

    public void showLayReceiverDetail() {
        animate();
        if (inputValues != null){
            inputValues.setReceiver_name(findEditTextById(R.id.et_receiver_name).getText().toString());
            inputValues.setReceiver_phone(findEditTextById(R.id.et_receiver_phone_numb).getText().toString());
        }
        findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_pay_detail).setVisibility(View.VISIBLE);
    }

    public void showLayPackageDetail() {
        animate();
        findViewById(R.id.lay_pkg_detail).setVisibility(View.GONE);
        findViewById(R.id.lay_recv_detail).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            inProgress.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        }else
            super.onBackPressed();

    }


}