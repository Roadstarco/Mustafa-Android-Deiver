package com.roadstar.customerr.app.module.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.profile.ProfileModel;
import com.roadstar.customerr.app.module.ui.auth.ProfileImageHandlingActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.FileUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.GET_PROFILE_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ERROR;

public class MyProfileActivity extends ProfileImageHandlingActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();
    }

    void init() {
        setActionBar(getString(R.string.my_profile));
        setScreenData();
        bindClicklisteners();
    }

    void setScreenData() {
        findTextViewById(R.id.tv_title).setTextColor(getResources().getColor(R.color.colorWhite));
        tintImageView(R.id.iv_back, R.color.colorWhite);
        findEditTextById(R.id.et_f_name).setText(UserManager.getFirstname());
        findEditTextById(R.id.et_l_name).setText(UserManager.getLastName());
        findEditTextById(R.id.et_email).setText(UserManager.getEmail());
        findEditTextById(R.id.et_country).setText(UserManager.getCountry());
        findEditTextById(R.id.et_address).setText(UserManager.getAddress());
        findVizImageView(R.id.iv_profile).setImage(UserManager.getImage());
    }

    private void bindClicklisteners() {

        findViewById(R.id.btn_save_edit).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        findViewById(R.id.iv_profile).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_save_edit:
                if (isParamValid())
                    updateProfile();

                break;

            case R.id.tv_setting:
                startActivity(this, SettingActivity.class);
                break;
            case R.id.iv_profile:
                checkStoragePermission(R.id.iv_profile);
                break;

        }
    }

    private boolean isParamValid() {
        boolean valid = true;
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_f_name))) //Check the First Name
        {
            findTextInputLayout(R.id.input_lay_f_name).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.input_lay_f_name).setError(null);
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_l_name))) //Check the Last Name
        {
            findTextInputLayout(R.id.input_lay_l_name).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.input_lay_l_name).setError(null);
        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_email))) //Check the Email
        {
            findTextInputLayout(R.id.lay_email).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.lay_email).setError(null);

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_country))) //Check the country
        {
            findTextInputLayout(R.id.input_lay_country).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.input_lay_country).setError(null);

        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_address))) //Check the address
        {
            findTextInputLayout(R.id.input_lay_address).setError(getString(R.string.error_field_empty));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.input_lay_address).setError(null);

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }


    void updateProfile() {
        showProgressDialog();


        /*Create handle for the RetrofitInstance interface*/
        try {


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("first_name", AppUtils.getEditTextString(findEditTextById(R.id.et_f_name)));
            builder.addFormDataPart("last_name", AppUtils.getEditTextString(findEditTextById(R.id.et_l_name)));
            builder.addFormDataPart("mobile", UserManager.getPhone());
            builder.addFormDataPart("email", AppUtils.getEditTextString(findEditTextById(R.id.et_email)));
            builder.addFormDataPart("country_name", AppUtils.getEditTextString(findEditTextById(R.id.et_country)));
            builder.addFormDataPart("address", AppUtils.getEditTextString(findEditTextById(R.id.et_address)));


            String pathToStoredImage = FileUtils.getPath(MyProfileActivity.this, Uri.parse(findVizImageView(R.id.iv_profile).getImageURL()));

            File file = new File("");
            if (pathToStoredImage != null)
                file = new File(pathToStoredImage);

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("picture", file.getName(), requestBody);

            MultipartBody requestBodyMultipart = builder.build();

            ApiInterface apiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
            Call<ResponseBody> call = apiInterface.updateProfile(HEADER_BEARER + UserManager.getToken(), HEADER_KEY_XMLHTTP, requestBodyMultipart);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.v("response", "" + response.body());

                    if (response.code() == 200) {
                        callGetProfileApi();
                    } else {
                        showSnackBar(getString(R.string.something_wrong));
                        hideProgressDialog();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(MyProfileActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            hideProgressDialog();
            e.printStackTrace();
        }
    }


    private ProfileModel getUpdateProfileParam() {
        ProfileModel profileModel = new ProfileModel();
        profileModel.setFirst_name(AppUtils.getEditTextString(findEditTextById(R.id.et_f_name)));
        profileModel.setLast_name(AppUtils.getEditTextString(findEditTextById(R.id.et_l_name)));
        profileModel.setMobile(UserManager.getPhone());
        profileModel.setEmail(AppUtils.getEditTextString(findEditTextById(R.id.et_email)));
        profileModel.setCountry_name(AppUtils.getEditTextString(findEditTextById(R.id.et_country)));
        profileModel.setAddress(AppUtils.getEditTextString(findEditTextById(R.id.et_address)));
        profileModel.setPicture("");
        return profileModel;
    }


    //Get Profile
    private void callGetProfileApi() {
        showProgressDialog();
        String url_param1 = "?device_type=" + UserManager.getDeviceType() +
                "&device_id=" + UserManager.getDeviceId(this) +
                "&device_token=" + UserManager.getDeviceToken();
        HttpRequestItem requestItem = new HttpRequestItem(GET_PROFILE_URL, url_param1);
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    JSONObject apiResponse;

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {

            apiResponse = new JSONObject(response.getResponse());
            if (response.getHttpRequestEndPoint().equals(GET_PROFILE_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar(apiResponse.getString("message"));

                } else {
                    UserManager.saveUserData(apiResponse);
                    showSnackBar(getResources().getString(R.string.profile_update_succ));
                    finish();
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