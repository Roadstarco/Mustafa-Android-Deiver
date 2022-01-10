package com.roadstar.customerr.app.module.ui.booking_activity;

import static android.os.Environment.getExternalStoragePublicDirectory;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.FileProvider;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.dialog.CancelReqDialog;
import com.roadstar.customerr.app.module.ui.dialog.ReqAcceptDialog;
import com.roadstar.customerr.app.module.ui.payment_method.PaymentMethodActivity;

import java.io.File;

public class BookingActivity extends RequestHandlerActivity implements View.OnClickListener, CancelReqDialog.OnInputListener, ReqAcceptDialog.OnInputListener {

    public TextView textViewDestination;
    Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i("BookingActivity:", "Activity BookingActivity called");
        setContentView(R.layout.activity_booking);
        init();

    }

    void init() {


        SharedPreferenceManager.getInstance().save(PreferenceUtils.DEST_LNG, "");
        SharedPreferenceManager.getInstance().save(PreferenceUtils.DEST_LAT, "");
        SharedPreferenceManager.getInstance().save(PreferenceUtils.SOURCE_LNG, "");
        SharedPreferenceManager.getInstance().save(PreferenceUtils.SOURCE_LAT, "");

        textViewDestination = findViewById(R.id.destination);
        if(!getDAddress().isEmpty())
            textViewDestination.setText(getDAddress());

        initMap();
        bindClicklisteners();
        setupServiceRecycleView();
        callGetServiceApi(); //Get the services list
    }

    private void bindClicklisteners() {
        findViewById(R.id.search_lay_click).setOnClickListener(this);

//Vehicle Type
        findViewById(R.id.iv_vechicle_back).setOnClickListener(this);
        findViewById(R.id.lay_vechicle).setOnClickListener(this);

//        Pricing lay
        findViewById(R.id.iv_pricing_back).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);

//        package detail lay
        findViewById(R.id.iv_pkg_detail_back).setOnClickListener(this);
        findViewById(R.id.btn_pkg_dtl_next).setOnClickListener(this);

        //        Receiver detail lay
        findViewById(R.id.iv_recev_detail_back).setOnClickListener(this);
        findViewById(R.id.btn_pay).setOnClickListener(this);

        //        Payment detail lay
        findViewById(R.id.iv_pay_back).setOnClickListener(this);
        findViewById(R.id.btn_submit_req).setOnClickListener(this);
        findViewById(R.id.btn_cancel_req).setOnClickListener(this);

        //Rating Screen
        findViewById(R.id.iv_rating_back).setOnClickListener(this);

        setSpinners();

        //Lay attachment listner
        findViewById(R.id.iv_attach_1).setOnClickListener(this);
        findViewById(R.id.iv_attach_2).setOnClickListener(this);
        findViewById(R.id.iv_attach_3).setOnClickListener(this);
        findViewById(R.id.img_remove1).setOnClickListener(this);
        findViewById(R.id.img_remove2).setOnClickListener(this);
        findViewById(R.id.img_remove3).setOnClickListener(this);

    }

    boolean isCategorySelected = true, isProductSelected = true, isWeightUnitSelected = true;

    void setSpinners() {
        //Spinner Category
        AppCompatSpinner spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_category);
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
        Spinner spinnerProduct = (Spinner) findViewById(R.id.spinner_product_type);

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
                return;
            }
        });

        //Spinner Weight Unit
        Spinner spinnerWeight = (Spinner) findViewById(R.id.spinner_weight);

        spinnerWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here
                if (isWeightUnitSelected) {
                    isWeightUnitSelected = !isWeightUnitSelected;
                } else {
                    findTextViewById(R.id.tv_weight_unit).setText(spinnerWeight.getSelectedItem().toString());
                    spinnerWeight.setSelection(0);
                    isWeightUnitSelected = true;
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.search_lay_click:

                Intent intentDest = new Intent(this, CustomGooglePlacesSearch.class);
                intentDest.putExtra("cursor", "destination");
                if(getSAddress().isEmpty())
                    intentDest.putExtra("s_address", getPlaceAddress());
                else
                    intentDest.putExtra("s_address", getSAddress());
                intentDest.putExtra("d_address", getDAddress());
                startActivityForResult(intentDest, 2000);
                findViewById(R.id.lay_choose_vehicle).setVisibility(View.VISIBLE);
                findViewById(R.id.lay_pricing).setVisibility(View.GONE);
                findViewById(R.id.lay_pkg_detail).setVisibility(View.GONE);
                findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);
                findViewById(R.id.lay_pay_detail).setVisibility(View.GONE);
                //startActivityForResult(intentDest, PLACE_AUTOCOMPLETE_REQUEST_CODE_DEST);
                break;

            case R.id.iv_vechicle_back:
                onBackPressed();
                break;
            case R.id.lay_vechicle:
                findViewById(R.id.lay_choose_vehicle).setVisibility(View.GONE);
                findViewById(R.id.lay_pricing).setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pricing_back:
                findViewById(R.id.lay_pricing).setVisibility(View.GONE);
                findViewById(R.id.lay_choose_vehicle).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_next: //lay pricing Next
                findViewById(R.id.lay_pricing).setVisibility(View.GONE);
                findViewById(R.id.lay_pkg_detail).setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pkg_detail_back:
                findViewById(R.id.lay_pricing).setVisibility(View.VISIBLE);
                findViewById(R.id.lay_pkg_detail).setVisibility(View.GONE);
                break;
            case R.id.btn_pkg_dtl_next:
                if (isPackageParamValid())
                    showLayPackageDetail();
                break;
            //Reciver detail lay
            case R.id.iv_recev_detail_back:

                findViewById(R.id.lay_pkg_detail).setVisibility(View.VISIBLE);
                findViewById(R.id.lay_recv_detail).setVisibility(View.GONE);
                break;
            case R.id.btn_pay:

                if (isReceiverDetailParamValid())
                    showLayReceiverDetail();
                break;
            //Patment detail lay
            case R.id.iv_pay_back:
                findViewById(R.id.lay_pay_detail).setVisibility(View.GONE);
                findViewById(R.id.lay_recv_detail).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit_req:
                if (findRadioButtonById(R.id.radio_btn_card).isChecked() && !UserManager.isCardAdded()){ /*payment method is card but card not added*/
                    showConfirmationDialog(PaymentMethodActivity.class);
                } else {
                    callSendRequestApi();
                }
                break;
            case R.id.btn_cancel_req:
                cancelReqDialog();
                break;
//                Rating detail
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
                startActivity(BookingActivity.this, activityToOpen); // add card first
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
    }


    private void showReqAcceptDialog() {
        ReqAcceptDialog reqAcceptDialog = new ReqAcceptDialog();
        reqAcceptDialog.show(getSupportFragmentManager(), "ReqAcceptDialog");
        reqAcceptDialog.setCancelable(false);
    }

    @Override
    public void onReqResponse(Boolean val) {
        if (val) {

            // callSubmitRequestApi();

            // callVolleySendReuqestApi();
        }

    }


    private void cancelReqDialog() {
        CancelReqDialog cancelReqDialog = new CancelReqDialog();
        cancelReqDialog.show(getSupportFragmentManager(), "CancelReqDialog");
        cancelReqDialog.setCancelable(false);

    }

    @Override
    public void onCancelReqResponse(Boolean val) {

        if (val) {
            resetLay(getString(R.string.req_cancel));
        }

    }

    @Override
    public void onResume(){
        if(!getDAddress().isEmpty())
            textViewDestination.setText(getDAddress());
        super.onResume();
    }

}