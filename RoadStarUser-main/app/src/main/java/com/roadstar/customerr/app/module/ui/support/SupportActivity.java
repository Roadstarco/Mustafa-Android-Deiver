package com.roadstar.customerr.app.module.ui.support;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.support.SupportMessageModel;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.base.BaseActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

public class SupportActivity extends BaseActivity implements View.OnClickListener {

    private ProgressBar progressDoalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        init();
    }

    void init() {
        setActionBar(getString(R.string.support));
        progressDoalog = findViewById(R.id.progressBar);
        bindClicklisteners();
    }

    private void bindClicklisteners() {

        findViewById(R.id.send_message).setOnClickListener(this);
        findViewById(R.id.message_layout).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.send_message:

                if (TextUtils.isEmpty(findEditTextById(R.id.et_subject).getText()) || TextUtils.isEmpty(findEditTextById(R.id.et_message).getText()))
                    Toast.makeText(this, "fill all values", Toast.LENGTH_SHORT).show();
                else{
                    callSendMessageApi();
                }


                break;
            case R.id.message_layout:
                findEditTextById(R.id.et_message).requestFocus();
                break;

        }
    }

    private void callSendMessageApi() {

        progressDoalog.setVisibility(View.VISIBLE);

        SupportMessageModel supportMessageModel  = new SupportMessageModel();
        supportMessageModel.setSubject(Objects.requireNonNull(findEditTextById(R.id.et_subject).getText()).toString());
        supportMessageModel.setMessage(Objects.requireNonNull(findEditTextById(R.id.et_message).getText()).toString());


        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ResponseBody> call = service.sendSupportMessageN(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",supportMessageModel);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                progressDoalog.setVisibility(View.GONE);

                if (response.code() == 200){
                    resetScreen(findViewById(R.id.layout_main));
                    Toast.makeText(SupportActivity.this, "Support Email Send successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if (response.code() == 500) {

                    Toast.makeText(SupportActivity.this, R.string.server_down, Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(SupportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                progressDoalog.setVisibility(View.GONE);
                Toast.makeText(SupportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

}