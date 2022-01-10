package com.roadstar.customerr.app.module.ui.payment_method;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.roadstar.customerr.MyApplication;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.card_model.CardDetails;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.adapter.PaymentListAdapter;
import com.roadstar.customerr.app.module.ui.auth.WelcomeActivity;
import com.roadstar.customerr.app.module.ui.main.MainActivity;
import com.roadstar.customerr.app.network.ConnectionHelper;
import com.roadstar.customerr.common.base.BaseActivity;
import com.roadstar.customerr.common.utils.CustomDialog;
import com.roadstar.customerr.common.utils.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.roadstar.customerr.MyApplication.trimMessage;
import static com.roadstar.customerr.common.utils.ApiConstants.BASE_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.CARD_PAYMENT_LIST;
import static com.roadstar.customerr.common.utils.ApiConstants.CLIENT_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.CLIENT_SECRET_VALUE;
import static com.roadstar.customerr.common.utils.ApiConstants.DELETE_CARD_FROM_ACCOUNT_API;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_KEY_XMLHTTP;
import static com.roadstar.customerr.common.utils.ApiConstants.LOGIN_URL;

public class PaymentMethodActivity extends BaseActivity implements View.OnClickListener {
    private final int ADD_CARD_CODE = 435;
    Activity activity;
    Context context;
    View rootView;
    CustomDialog customDialog;

    FloatingActionButton addCard;
    ListView payment_list_view;
    ArrayList<JSONObject> listItems;
    ListAdapter paymentAdapter;
    TextView empty_text;
    Utilities utils = new Utilities();
    JSONObject deleteCard = new JSONObject();
    //Internet
    ConnectionHelper helper;
    Boolean isInternet;

    RelativeLayout cashLayout;

    //ImageView tickImg;

    private ArrayList<CardDetails> cardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        init();
    }

    void init() {
        activity = PaymentMethodActivity.this;
        context = PaymentMethodActivity.this;
        setActionBar(getString(R.string.payment_method));

        addCard =  findViewById(R.id.addCard);
        payment_list_view = (ListView) findViewById(R.id.payment_list_view);
        empty_text = (TextView) findViewById(R.id.empty_text);
        helper = new ConnectionHelper(PaymentMethodActivity.this);
        //tickImg = (ImageView) rootView.findViewById(R.id.tick_img);
        isInternet = helper.isConnectingToInternet();
        cashLayout = (RelativeLayout) findViewById(R.id.cash_layout);

        bindClicklisteners();
        getCardList();
    }

    private void bindClicklisteners() {


        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToAddCard();
            }
        });

        payment_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    String json = new Gson().toJson(paymentAdapter.getItem(i));
                    JSONObject object = new JSONObject(json);
                    utils.print("MyTest", "" + paymentAdapter.getItem(i));
                    DeleteCardDailog(object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                startActivityWithNoHistory(this, MainActivity.class);
                break;
        }
    }

    private void DeleteCardDailog(final JSONObject object) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(getString(R.string.are_you_sure))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteCard = object;
                        deleteCard();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void deleteCard() {
        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("card_id", deleteCard.optString("card_id"));
            object.put("_method", "DELETE");

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL + DELETE_CARD_FROM_ACCOUNT_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                utils.print("SendRequestResponse", response.toString());
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                getCardList();
                deleteCard = new JSONObject();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.getString("message"));
                            } catch (Exception e) {
                                displayMessage(errorObj.optString("error"));
                                //displayMessage(getString(R.string.something_went_wrong));
                            }
                            utils.print("MyTest", "" + errorObj.toString());
                        } else if (response.statusCode == 401) {
                            refreshAccessToken("DELETE_CARD");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_wrong));
                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        deleteCard();
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", HEADER_KEY_XMLHTTP);
                headers.put("Authorization", HEADER_BEARER + UserManager.getToken());
                return headers;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void getCardList() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(BASE_URL + CARD_PAYMENT_LIST, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                utils.print("GetPaymentList", response.toString());
                if (response != null && response.length() > 0) {
                    listItems = getArrayListFromJSONArray(response);
                    if (listItems.isEmpty()) {
                        //empty_text.setVisibility(View.VISIBLE);
                        payment_list_view.setVisibility(View.GONE);
                        UserManager.setIsCardAdded(false);
                    } else {
                        //empty_text.setVisibility(View.GONE);
                        payment_list_view.setVisibility(View.VISIBLE);
                    }
                    cardArrayList = new ArrayList<>();
                    for (JSONObject jsonObject : listItems) {
                        Gson gson = new Gson();
                        CardDetails card = gson.fromJson(jsonObject.toString(), CardDetails.class);
//                        if (payment.equalsIgnoreCase("CARD") && SharedHelper.getKey(getContext(), "card_id").equalsIgnoreCase(card.card_id))
//                            card.setSelected("true");
//                        else
//                            card.setSelected("false");

                        cardArrayList.add(card);
                    }

                    paymentAdapter = new PaymentListAdapter(context, R.layout.payment_list_item, cardArrayList);
                    payment_list_view.setAdapter(paymentAdapter);
                } else {
                    //empty_text.setVisibility(View.VISIBLE);
                    payment_list_view.setVisibility(View.GONE);
                    UserManager.setIsCardAdded(false);
                }
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if ((customDialog != null) && (customDialog.isShowing()))
                    customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }

                        } else if (response.statusCode == 401) {
                            refreshAccessToken("PAYMENT_LIST");
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));

                        } else {
                            displayMessage(getString(R.string.please_try_again));

                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));

                    }

                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        getCardList();
                    }
                }
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


    private void refreshAccessToken(final String tag) {


        JSONObject object = new JSONObject();
        try {

            object.put("grant_type", "refresh_token");
            object.put("client_id", CLIENT_ID);
            object.put("client_secret", CLIENT_SECRET_VALUE);
            object.put("refresh_token", UserManager.getToken()); // TODO: 10/1/2020 get refresh token
            object.put("scope", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, LOGIN_URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                utils.print("SignUpResponse", response.toString());
                UserManager.saveAccessToken(response);
                UserManager.saveRefreshToken(response);
                UserManager.saveTokenType(response);

                if (tag.equalsIgnoreCase("PAYMENT_LIST")) {
                    getCardList();
                } else {
                    deleteCard();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (response != null && response.data != null) {
                    SharedPreferenceManager.getInstance().clearPreferences();
                    GoToBeginActivity();
                } else {
                    if (error instanceof NoConnectionError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof NetworkError) {
                        displayMessage(getString(R.string.oops_connect_your_internet));
                    } else if (error instanceof TimeoutError) {
                        refreshAccessToken(tag);
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


    public void displayMessage(String toastString) {
        showSnackBar(toastString);

    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, WelcomeActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        activity.finish();
    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(activity, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void GoToAddCard() {

        Intent mainIntent = new Intent(this, AddCard.class);
        startActivityForResult(mainIntent, ADD_CARD_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CARD_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                boolean result = data.getBooleanExtra("isAdded", false);
                if (result) {
                    getCardList();
                }
            }
        }
    }
}
