 package com.roadstar.customerr.app.module.ui.payment_method;
/**
 * @Developer android
 * @Company android
 **/

 import android.accounts.NetworkErrorException;
 import android.annotation.SuppressLint;
 import android.app.Activity;
 import android.content.Context;
 import android.content.Intent;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageView;
 import android.widget.Toast;

 import androidx.appcompat.widget.AppCompatButton;

 import com.android.volley.AuthFailureError;
 import com.android.volley.NetworkError;
 import com.android.volley.NetworkResponse;
 import com.android.volley.NoConnectionError;
 import com.android.volley.Request;
 import com.android.volley.TimeoutError;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.braintreepayments.cardform.OnCardFormSubmitListener;
 import com.braintreepayments.cardform.OnCardFormValidListener;
 import com.braintreepayments.cardform.view.CardForm;
 import com.google.android.material.snackbar.Snackbar;
 import com.google.gson.JsonObject;
 import com.koushikdutta.async.future.FutureCallback;
 import com.koushikdutta.ion.Ion;
 import com.koushikdutta.ion.Response;
 import com.roadstar.customerr.MyApplication;
 import com.roadstar.customerr.R;
 import com.roadstar.customerr.app.data.UserManager;
 import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
 import com.roadstar.customerr.app.module.ui.auth.WelcomeActivity;
 import com.roadstar.customerr.common.base.BaseActivity;
 import com.roadstar.customerr.common.utils.CustomDialog;
 import com.roadstar.customerr.common.utils.Message;
 import com.roadstar.customerr.common.utils.Utilities;
 import com.stripe.android.Stripe;
 import com.stripe.android.TokenCallback;
 import com.stripe.android.model.Card;
 import com.stripe.android.model.Token;

 import org.greenrobot.eventbus.EventBus;
 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.HashMap;
 import java.util.Map;
 import java.util.concurrent.TimeoutException;
 import java.util.regex.Pattern;

 import static com.roadstar.customerr.common.utils.ApiConstants.ADD_CARD_TO_ACCOUNT_API;
 import static com.roadstar.customerr.common.utils.ApiConstants.BASE_URL;
 import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
 import static com.roadstar.customerr.common.utils.ApiConstants.LOGIN_URL;
 import static com.roadstar.customerr.common.utils.ApiConstants.CLIENT_ID;
 import static com.roadstar.customerr.common.utils.ApiConstants.CLIENT_SECRET_VALUE;
 import static com.roadstar.customerr.common.utils.ApiConstants.STRIPE_TOKEN;

 public class AddCard extends BaseActivity {

    Activity activity;
    Context context;
    ImageView backArrow, help_month_and_year, help_cvv;
    Button addCard;
    //EditText cardNumber, cvv, month_and_year;
    CardForm cardForm;
    String Card_Token = "";
     public String cardId = "";
     public String brand = "";
     public String last_4 = "";
    CustomDialog customDialog;
    Utilities utils =new Utilities();

    static final Pattern CODE_PATTERN = Pattern
            .compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Mytheme);
            setContentView(R.layout.activity_add_card);
        findViewByIdAndInitialize();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog = new CustomDialog(AddCard.this);
                customDialog.setCancelable(false);
                if (customDialog != null)
                customDialog.show();
                if( cardForm.getCardNumber() == null || cardForm.getExpirationMonth() == null || cardForm.getExpirationYear() == null || cardForm.getCvv() == null ){
                    if ((customDialog != null)&& (customDialog.isShowing()))
                    customDialog.dismiss();
                    displayMessage(context.getResources().getString(R.string.enter_card_details));
                }else{
                    if(cardForm.getCardNumber().equals("") || cardForm.getExpirationMonth().equals("") || cardForm.getExpirationYear().equals("")|| cardForm.getCvv().equals("")){
                        if ((customDialog != null)&& (customDialog.isShowing()))
                        customDialog.dismiss();
                        displayMessage(context.getResources().getString(R.string.enter_card_details));
                    }else {
                    String cardNumber = cardForm.getCardNumber();
                    int month = Integer.parseInt(cardForm.getExpirationMonth());
                    int year = Integer.parseInt(cardForm.getExpirationYear());
                    String cvv = cardForm.getCvv();
                    utils.print("MyTest","CardDetails Number: "+cardNumber+"Month: "+month+" Year: "+year);

                    Card card = new Card(cardNumber, month, year, cvv);
                    try {
                        Stripe stripe = new Stripe(AddCard.this,  STRIPE_TOKEN);
                        stripe.createToken(
                                card,
                                new TokenCallback() {
                                    @SuppressLint("WrongConstant")
                                    public void onSuccess(Token token) {
                                        // Send token to your server
                                        utils.print("CardToken:"," "+token.getId());
                                        utils.print("CardToken:"," "+token.getCard().getLast4());
                                        utils.print("CardToken:"," "+token.getCard().getBrand());
                                        Card_Token = token.getId();

                                        cardId = token.getCard().getId();
                                        brand = token.getCard().getBrand();
                                        last_4 = token.getCard().getLast4();

                                        addCardToAccount(Card_Token);

                                            EventBus.getDefault().post(new Message<>().setMsg_id(Message.MsgId.CARD_ADDED));

                                    }
                                    public void onError(Exception error) {
                                        // Show localized error message
                                        displayMessage(context.getResources().getString(R.string.enter_card_details));
                                        if ((customDialog != null)&& (customDialog.isShowing()))
                                        customDialog.dismiss();
                                    }
                                }
                        );
                    }catch (Exception e){
                        showSnackBar(e.getMessage());
                        e.printStackTrace();
                        if ((customDialog != null)&& (customDialog.isShowing()))
                        customDialog.dismiss();
                    }
                    }

                }
            }
        });

    }


    public void findViewByIdAndInitialize(){
        backArrow = (ImageView)findViewById(R.id.backArrow);
//        help_month_and_year = (ImageView)findViewById(R.id.help_month_and_year);
//        help_cvv = (ImageView)findViewById(R.id.help_cvv);
        addCard = (AppCompatButton) findViewById(R.id.addCard);
//        cardNumber = (EditText) findViewById(R.id.cardNumber);
//        cvv = (EditText) findViewById(R.id.cvv);
//        month_and_year = (EditText) findViewById(R.id.monthAndyear);
        context = AddCard.this;
        activity = AddCard.this;
        cardForm = (CardForm) findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel("Add CardDetails")
                .setup(activity);

//        cardForm.setOnCardFormValidListener(new OnCardFormValidListener() {
//            @Override
//            public void onCardFormValid(boolean valid) {
//                Toast.makeText(getApplicationContext(), ""+valid, Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    public void addCardToAccount(final String cardToken) {

        JsonObject json = new JsonObject();
        json.addProperty("stripe_token", cardToken);
        json.addProperty("last_four",last_4);
        json.addProperty("brand",brand);

        Ion.with(this)
                .load(BASE_URL+ADD_CARD_TO_ACCOUNT_API)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Authorization", HEADER_BEARER + UserManager.getToken())
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> response) {
                        // response contains both the headers and the string result
                        if ((customDialog != null) && (customDialog.isShowing()))
                            customDialog.dismiss();

                        if (e != null) {
                            if (e instanceof NetworkErrorException) {
                                displayMessage(context.getResources().getString(R.string.please_try_again));
                            }
                            if (e instanceof TimeoutException) {
                                addCardToAccount(cardToken);
                            }
                            return;
                        }

                        if (response != null){
                            if (response.getHeaders().code() == 200) {
                                try {
                                    utils.print("SendRequestResponse", response.toString());

                                    JSONObject jsonObject = new JSONObject(response.getResult());
                                    Toast.makeText(AddCard.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                                    // onBackPressed();

                                  //  SharedHelper.putKey(AddCard.this, "cardAdded", "true");

                                    UserManager.setIsCardAdded(true);
                                    UserManager.setCardID(cardId);
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("isAdded", true);
                                    setResult(Activity.RESULT_OK, resultIntent);
                                    finish();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                customDialog.dismiss();
                            }else if (response.getHeaders().code() == 401){
                                customDialog.dismiss();
                                refreshAccessToken();
                            }
                        }
                        /*
                        if (getIntent().hasExtra("isFromPostTrip"))
                            finish(AddCard.this);*/
                    }
                });


    }


    public void displayMessage(String toastString) {
        Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
        // Toast.makeText(context, ""+toastString, Toast.LENGTH_SHORT).show();
    }

    private void refreshAccessToken() {


        JSONObject object = new JSONObject();
        try {

            object.put("grant_type", "refresh_token");
            object.put("client_id", CLIENT_ID);
            object.put("client_secret", CLIENT_SECRET_VALUE);
          //  object.put("refresh_token", SharedHelper.getKey(context, "refresh_token"));
            object.put("refresh_token", UserManager.getRefreshToken());
            object.put("scope", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,LOGIN_URL, object, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.v("SignUpResponse", response.toString());
                UserManager.saveAccessToken(response);
                UserManager.saveRefreshToken(response);
                UserManager.saveTokenType(response);
                addCardToAccount(Card_Token);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                  //  SharedHelper.putKey(context,"loggedIn",context.getResources().getString(R.string.False));
                    SharedPreferenceManager.getInstance().clearPreferences();
                    GoToBeginActivity();
                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(context.getResources().getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(context.getResources().getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        refreshAccessToken();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    public void GoToBeginActivity(){
        Intent mainIntent = new Intent(activity, WelcomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        activity.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
