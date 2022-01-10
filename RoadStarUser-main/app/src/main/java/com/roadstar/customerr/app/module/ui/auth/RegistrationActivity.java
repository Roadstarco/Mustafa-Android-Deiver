package com.roadstar.customerr.app.module.ui.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.roadstar.customerr.BuildConfig;
import com.roadstar.customerr.MyApplication;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.LoginWithGoogleModel;
import com.roadstar.customerr.app.data.models.card_model.CardDetails;
import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.main.MainActivity;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.FileUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.roadstar.customerr.common.utils.Utilities;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.ANDROID;
import static com.roadstar.customerr.common.utils.ApiConstants.BASE_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.CARD_PAYMENT_LIST;
import static com.roadstar.customerr.common.utils.ApiConstants.CLIENT_SECRET;
import static com.roadstar.customerr.common.utils.ApiConstants.GET_PROFILE_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.GRANT_PASSWORD;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ADDRESS;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CLIENT_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CLIENT_SECRET;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_COUNTRY_NAME;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_DEVICE_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_DEVICE_TOKEN;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_DEVICE_TYPE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_EMAIL;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_ERROR;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_FIRST_NAME;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_GRANT_TYPE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_LAST_NAME;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_LOGIN_BY;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_MOBILE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_PASSWORD;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_PICTURE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_SCOPE;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_SOCIAL_UNIQUE_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_USER_NAME;
import static com.roadstar.customerr.common.utils.ApiConstants.LOGIN_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.MANUAL;
import static com.roadstar.customerr.common.utils.ApiConstants.PHONE;
import static com.roadstar.customerr.common.utils.ApiConstants.SIGNUP_URL;

import androidx.annotation.NonNull;

public class RegistrationActivity extends ProfileImageHandlingActivity implements View.OnClickListener {
    String phoneNumber = "";
    private GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN = 5050;
    private FirebaseAuth mAuth;
    private ProgressBar progressDoalog;
    private String loginBy="manual",socialUniqueId="";
    private String fName,lName,email,address,country="",password;

    private static final String TAG = "LoginActivity";
//    private static final int RC_SIGN_IN = 1001;
    private GoogleApiClient mGoogleApiClient;
//    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    LoginButton facebookLoginButton;
    boolean isCheck = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
        setContentView(R.layout.activity_registration);
        init();
    }

    void init() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        mAuth = FirebaseAuth.getInstance();  mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        progressDoalog = findViewById(R.id.progressBar);
        setActionBar(getString(R.string.register));
        phoneNumber = getIntent().getStringExtra(PHONE);
        findViewById(R.id.img_google).setOnClickListener(this);
        findViewById(R.id.img_fb).setOnClickListener(this);
        bindClicklisteners();

        facebookLoginButton = findViewById(R.id.login_facebook_button);
        facebookLoginButton.setReadPermissions("email",  "public_profile");



        facebookLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResult.getAccessToken();
//                authWithFacebook(loginResult.getAccessToken());
//                firebaseAuthWithFacebook(loginResult.getAccessToken());
                //Bundle is use for passing data as K/V pair like a Map
                Bundle bundle=new Bundle();
                //Fields is the key of bundle with values that matched the proper Permissions Reference provided by Facebook
                bundle.putString("fields","id, email, first_name, last_name, gender,birthday,location");

                //Graph API to access the data of user's facebook account
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("Login Success", response.toString());

                                //For safety measure enclose the request with try and catch
                                try {
                                    loginBy = "facebook";
                                    socialUniqueId = object.getString("id");
                                    fName = object.getString("first_name");
                                    lName = object.getString("last_name");
                                    email = object.getString("email");
                                    password = object.getString("id");
                                    address = "";
                                    country = "";
//                                    object.getString("location");
//                                    try {
//                                        address = object.getString("location");
//                                    }catch (NullPointerException | JSONException e){
//                                        address = "";
//                                    }

                                    callSignupApi();

                                }
                                //If no data has been retrieve throw some error
                                catch (JSONException e) {
                                    Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    address = "";
//                                    callSignupApi();
//                                    ErrorDialog(e.getMessage(),"facebookAuth");
                                }

                            }
                        });


                //Set the bundle's data as Graph's object data
                request.setParameters(bundle);

                //Execute this Graph request asynchronously
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(RegistrationActivity.this, "Succes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegistrationActivity.this, "Succes", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void bindClicklisteners() {
        findViewById(R.id.lay_signin).setOnClickListener(this);
        findViewById(R.id.btn_next).setOnClickListener(this);
        findViewById(R.id.iv_profile).setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_next:
                if (isParamValid()) {
                    fName = AppUtils.getEditTextString(findEditTextById(R.id.et_f_name));
                    lName = AppUtils.getEditTextString(findEditTextById(R.id.et_l_name));
                    email = AppUtils.getEditTextString(findEditTextById(R.id.et_email)).replace(" ","").trim();
                    password = AppUtils.getEditTextString(findEditTextById(R.id.et_password));
                    address = AppUtils.getEditTextString(findEditTextById(R.id.et_address));
                    country = AppUtils.getEditTextString(findEditTextById(R.id.et_country));
                    callSignupApi();
                }
                break;
            case R.id.lay_signin:
                startActivityWithNoHistory(this, SigninActivity.class);
                break;
            case R.id.iv_profile:

                checkStoragePermission(R.id.iv_profile);
                break;
            case R.id.img_google:
                isCheck = false;
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("234677169665-a2qalvl55n94ianmjlo6k9o7ar69to3o.apps.googleusercontent.com")/// 480273579736-nshbr92tqicl8jkvv2r3rmvast4nogjh.apps.googleusercontent.com")
                        .requestEmail()
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

                signIn();

                break;
            case R.id.img_fb:
                isCheck = false;
                LoginManager.getInstance().logOut();
                facebookLoginButton.performClick();
                break;


        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // Google Sign In was successful, authenticate with Firebase
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginBy = "google";
            socialUniqueId = account.getId();
            fName = account.getGivenName();
            lName = account.getFamilyName();
            email = account.getEmail();
            password = account.getId();
            address = "";
            country = "";
            callSignupApi();


//            callSignInWithGoogleApi(account.getIdToken());

        } catch (ApiException e) {
            e.getMessage();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            //Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void callSignInWithGoogleApi(String accessToken) {
        progressDoalog.setVisibility(View.VISIBLE);

        String token = "";
        if (FirebaseInstanceId.getInstance().getToken() != null)
            token = FirebaseInstanceId.getInstance().getToken();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        LoginWithGoogleModel loginWithGoogleModel = new LoginWithGoogleModel();
        loginWithGoogleModel.setDevice_type("android");
        loginWithGoogleModel.setDevice_token(token);
        loginWithGoogleModel.setAccessToken(accessToken);
        loginWithGoogleModel.setDevice_id(deviceId);
        loginWithGoogleModel.setLogin_by("google");
        loginWithGoogleModel.setMobile(phoneNumber);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<LoginWithGoogleModel> call = service.loginWithGoogle(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",loginWithGoogleModel);
        call.enqueue(new Callback<LoginWithGoogleModel>() {
            @Override
            public void onResponse(@NotNull Call<LoginWithGoogleModel> call, @NotNull Response<LoginWithGoogleModel> response) {
                progressDoalog.setVisibility(View.GONE);

                if (response.code() == 200){
                    getUserDetails(response.body().getAccessToken());
                }else
                    Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NotNull Call<LoginWithGoogleModel> call, @NotNull Throwable t) {
                progressDoalog.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserDetails(String accessToken) {
        progressDoalog.setVisibility(View.VISIBLE);

        String token = "";
        if (FirebaseInstanceId.getInstance().getToken() != null)
            token = FirebaseInstanceId.getInstance().getToken();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        LoginWithGoogleModel loginWithGoogleModel = new LoginWithGoogleModel();
        loginWithGoogleModel.setDevice_type("android");
        loginWithGoogleModel.setDevice_token(token);
        loginWithGoogleModel.setDevice_id(deviceId);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<JSONObject> call = service.getUserDetails(HEADER_BEARER + accessToken, loginWithGoogleModel);
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(@NotNull Call<JSONObject> call, @NotNull Response<JSONObject> response) {
                progressDoalog.setVisibility(View.GONE);

                if (response.code() == 200){

                    gotoMainActivity(response.body());

                }else
                    Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(@NotNull Call<JSONObject> call, @NotNull Throwable t) {
                progressDoalog.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void GoToBeginActivity(){
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        // Toast.makeText(context, ""+toastString, Toast.LENGTH_SHORT).show();
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


        if (!AppUtils.isNotFieldEmpty(findEditTextById(R.id.et_password))) //Check the Password
        {
            findTextInputLayout(R.id.et_password).setError(getString(R.string.error_enter_pass));
            valid = false;
            return valid;
        }
        findTextInputLayout(R.id.input_lay_pass).setError(null);

        AppUtils.hideSoftKeyboard(this);//Hide keyboard
        return valid;
    }

    //Signup
    private void callSignupApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(SIGNUP_URL, "");
        //  HttpRequestItem requestItem = new HttpRequestItem( "https://emernox.com/api/user/signup");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getRegParam());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }


    private Map<String, Object> getRegParam() {
        Map<String, Object> map = new HashMap<>();

        String token = "";
        if (FirebaseInstanceId.getInstance().getToken() != null)
            token = FirebaseInstanceId.getInstance().getToken();
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        map.put(KEY_LOGIN_BY, loginBy);
        map.put(KEY_FIRST_NAME, fName);//AppUtils.getEditTextString(findEditTextById(R.id.et_f_name)));
        map.put(KEY_LAST_NAME, lName);//AppUtils.getEditTextString(findEditTextById(R.id.et_l_name)));
        map.put(KEY_MOBILE, phoneNumber);
        String path = FileUtils.getPath(RegistrationActivity.this, Uri.parse(findVizImageView(R.id.iv_profile).getImageURL()));
        map.put(KEY_PICTURE, "");
        map.put(KEY_SOCIAL_UNIQUE_ID, socialUniqueId);
        map.put(KEY_EMAIL, email);//AppUtils.getEditTextString(findEditTextById(R.id.et_email)));
        map.put(KEY_GRANT_TYPE, GRANT_PASSWORD);
        map.put(KEY_CLIENT_ID, 2);
        map.put(KEY_CLIENT_SECRET, CLIENT_SECRET);
        map.put(KEY_PASSWORD, password);//AppUtils.getEditTextString(findEditTextById(R.id.et_password)));
        map.put(KEY_DEVICE_TYPE, ANDROID);
        map.put(KEY_DEVICE_ID, deviceId);
        map.put(KEY_DEVICE_TOKEN, token);
        map.put(KEY_ADDRESS, address);//AppUtils.getEditTextString(findEditTextById(R.id.et_address)));
        map.put(KEY_COUNTRY_NAME, country);//AppUtils.getEditTextString(findEditTextById(R.id.et_country)));
//Address and country missing
        Log.i("Fcm Token", token);
        Log.i("Device ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        return map;
    }

    JSONObject apiResponse;

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        hideProgressDialog();
        try {

            apiResponse = new JSONObject(response.getResponse());
            if (response.getHttpRequestEndPoint().equals(SIGNUP_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    if(loginBy.equalsIgnoreCase("facebook") || loginBy.equalsIgnoreCase("google")){
                        socialUniqueId = password;
                        callLoginApi();
                    }else
                        showSnackBar("The email has already been taken.");

                } else {
                    if(loginBy.equalsIgnoreCase("facebook") || loginBy.equalsIgnoreCase("google")){
                        socialUniqueId = password;
                        callLoginApi();
                    }else {
                        callLoginApi();
                    }
//                        gotoMainActivity(null);
                }
            }else if (response.getHttpRequestEndPoint().equals(LOGIN_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar(apiResponse.getString("message"));

                } else {

                    UserManager.saveAccessToken(apiResponse);
                    callGetProfileApi();
                    // gotoMainActivity();
                }
            } else if (response.getHttpRequestEndPoint().equals(GET_PROFILE_URL)) {

                if (apiResponse.has(KEY_ERROR)) {
                    showSnackBar(apiResponse.getString("message"));

                } else {

                    gotoMainActivity();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            showSnackBar(getString(R.string.something_wrong));
        }
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        hideProgressDialog();
        if(!isCheck && (loginBy.equalsIgnoreCase("facebook") || loginBy.equalsIgnoreCase("google"))){
            socialUniqueId = password;
            callLoginApi();
            isCheck = true;
        }else
            showSnackBar("Email already exists");
    }

    void gotoMainActivity(JSONObject res) {
        if (res != null){

            UserManager.saveAccessToken(res);
            UserManager.saveUserData(res);
        }else {

            UserManager.saveAccessToken(apiResponse);
            UserManager.saveUserData(apiResponse);
        }

        SharedPreferenceManager.getInstance().save(PreferenceUtils.IS_LOGGED_IN, true);
        startActivityWithNoHistory(RegistrationActivity.this, MainActivity.class);
    }

    private void callLoginApi() {
        showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(LOGIN_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getRegLoginParam());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }


    private Map<String, Object> getRegLoginParam() {
        Map<String, Object> map = new HashMap<>();

        String token = "";
        if (FirebaseInstanceId.getInstance().getToken() != null)
            token = FirebaseInstanceId.getInstance().getToken();

        map.put(KEY_USER_NAME, email);
        map.put(KEY_PASSWORD, socialUniqueId);
        map.put(KEY_GRANT_TYPE, "password");
        map.put(KEY_CLIENT_ID, "2");
        map.put(KEY_CLIENT_SECRET, CLIENT_SECRET);
        map.put(KEY_SCOPE, "");
        map.put(KEY_DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        map.put(KEY_DEVICE_TOKEN, token);
        map.put(KEY_DEVICE_TYPE, ANDROID);
        Log.i("Fcm Token", token);
        Log.i("Device ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        return map;
    }

    void gotoMainActivity() {
        UserManager.saveUserData(apiResponse);
        UserManager.setIsUserLogin(true);
        //SharedPreferenceManager.getInstance().save(PreferenceUtils.IS_LOGGED_IN, true);
        //startActivityWithNoHistory(SigninActivity.this, MainActivity.class);

        checkCardInfo();
    }

    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray) {

        ArrayList<JSONObject> aList = new ArrayList<JSONObject>();

        try {
            if (jsonArray != null) {

                for (int i = 0; i < jsonArray.length(); i++) {

                    aList.add(jsonArray.getJSONObject(i));

                }

            }
        } catch (JSONException je) {
            je.printStackTrace();
        }

        return aList;

    }

    //Get Profile Api
    private void callGetProfileApi() {
        showProgressDialog();
        // String url_param = "?device_type=android&device_id=cb7e21f01dfd9ea2&device_token=cBS7Ax3UQYyUoqCqgS9AAA:APA91bFDZ8ymt6ZvEZIAZbZdbBYB7HsQJie4q08cvFXUm_2D3gIGMD24Y9SFdW-2tpxEwtwiKoqsZGBCHjB4FMh6jcmRTkIvpY7QAkFNTP5O6mjb1XjcyFjJNr6Ucn8AcacDXD7E3WCH";
        String url_param1 = "?device_type="+UserManager.getDeviceType()+
                "&device_id="+UserManager.getDeviceId(this)+
                "&device_token="+UserManager.getDeviceToken();
        HttpRequestItem requestItem = new HttpRequestItem(  GET_PROFILE_URL, url_param1);
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    public void checkCardInfo() {

        Utilities utils = new Utilities();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BASE_URL + CARD_PAYMENT_LIST, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                utils.print("GetPaymentList", response.toString());
                if (response != null && response.length() > 0) {
                    ArrayList<JSONObject> listItems = getArrayListFromJSONArray(response);
                    if (listItems.isEmpty()) {
                        UserManager.setIsCardAdded(false);
                    }

                    for (JSONObject jsonObject : listItems) {
                        Gson gson = new Gson();
                        CardDetails card = gson.fromJson(jsonObject.toString(), CardDetails.class);

                        UserManager.setIsCardAdded(true);
                        UserManager.setCardID(card.getCard_id());

                        break;
                    }
                } else {

                    UserManager.setIsCardAdded(false);
                }

                startActivityWithNoHistory(RegistrationActivity.this, MainActivity.class);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                UserManager.setIsCardAdded(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", HEADER_BEARER + UserManager.getToken());
                return headers;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonArrayRequest);
    }

}