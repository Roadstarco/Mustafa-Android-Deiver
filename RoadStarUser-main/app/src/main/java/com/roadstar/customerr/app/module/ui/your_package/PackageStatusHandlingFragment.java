package com.roadstar.customerr.app.module.ui.your_package;

import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.booking_status.BookingStatus;
import com.roadstar.customerr.app.module.ui.your_package.fragments.MapHandlerFragment;
import com.roadstar.customerr.app.network.AppNetworkTask;
import com.roadstar.customerr.app.network.HttpRequestItem;
import com.roadstar.customerr.app.network.HttpResponseItem;
import com.roadstar.customerr.common.utils.AppUtils;
import com.roadstar.customerr.common.utils.NetworkUtils;
import com.skyfishjy.library.RippleBackground;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import static com.roadstar.customerr.common.utils.ApiConstants.BOOKING_STATUS_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.CANCEL_REQUEST_API;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_CANCEL_REASON;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_COMMENT;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_RATING;
import static com.roadstar.customerr.common.utils.ApiConstants.KEY_REQUEST_ID;
import static com.roadstar.customerr.common.utils.ApiConstants.RATING_URL;

public class PackageStatusHandlingFragment extends MapHandlerFragment {
    public BookingStatus bookingStatus = null;

    //Api Calls
    public void callCheckStatusApi() {
        // showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(BOOKING_STATUS_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_GET);
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(null, this);
        appNetworkTask.execute(requestItem);
    }

    public void callCancelRequestsApi() {
        // showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(CANCEL_REQUEST_API, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getCancelRequestParam());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    public void callRatingApi() {
        // showProgressDialog();
        HttpRequestItem requestItem = new HttpRequestItem(RATING_URL, "");
        requestItem.setHttpRequestType(NetworkUtils.HTTP_POST);
        requestItem.setParams(getRatingParam());
        requestItem.setHeaderParams(AppUtils.getHeaderParams());
        AppNetworkTask appNetworkTask = new AppNetworkTask(getProgressDialog(false), this);
        appNetworkTask.execute(requestItem);
    }

    public Map<String, Object> getCancelRequestParam() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_REQUEST_ID, bookingStatus.getId());
        map.put(KEY_CANCEL_REASON, findEditTextById(R.id.et_cancel_reason).getText().toString());
        return map;
    }

    public Map<String, Object> getRatingParam() {
        Map<String, Object> map = new HashMap<>();
        map.put(KEY_REQUEST_ID, bookingStatus.getId());
        map.put(KEY_RATING, findRatingBarByID(R.id.rating_bar_user).getRating());
        map.put(KEY_COMMENT, findEditTextById(R.id.et_review).getText().toString());

        return map;
    }

    @Override
    public void onNetworkSuccess(HttpResponseItem response) {
        super.onNetworkSuccess(response);
        //hideProgressDialog();

//        animate(R.id.booking_id);
        try {
            JSONObject jsonObject = new JSONObject((response.getResponse()));
            switch (response.getHttpRequestEndPoint()) {
                case BOOKING_STATUS_URL:  //Check status url

                    JSONArray apiResponse = new JSONArray(jsonObject.getString("data"));
                    if (apiResponse.length() != 0) {
                        // requestCancelled();
                        Gson gson = new Gson();
                        bookingStatus = gson.fromJson(apiResponse.get(apiResponse.length()-1).toString(), BookingStatus.class);
                        handleStatusLayout(bookingStatus);
                        findViewById(R.id.tvnoData).setVisibility(View.GONE);
                        if(bookingStatus.getProviderId()!=0) {
                            LatLng latLng = new LatLng(bookingStatus.getProvider_latitude(), bookingStatus.getProvider_longitude());
                            // Creating a marker
                            MarkerOptions markerOptions = new MarkerOptions();

                            // Setting the position for the marker
                            markerOptions.position(latLng);

                            // Setting the title for the marker.
                            // This will be displayed on taping the marker
                            markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                            // Clears the previously touched position
                            mMap.clear();

                            // Animating to the touched position
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mMap.animateCamera(cameraUpdate);

                            // Placing a marker on the touched position
                            mMap.addMarker(markerOptions);
                        }

                    }else findViewById(R.id.tvnoData).setVisibility(View.VISIBLE);
                    break;
                case RATING_URL:  //Rating Url
                    if (jsonObject.has("message")) {
                        UserManager.setRideInprogress(false);
                        getActivity().onBackPressed();
                    } else {
                        showSnackBar(getString(R.string.something_wrong));
                    }

                    break;
                case CANCEL_REQUEST_API:  //Req cancel Url
                    if (jsonObject.has("message")) {
                        requestCancelled();
                    } else {
                        showSnackBar(getString(R.string.something_wrong));
                    }

                    break;
            }

        } catch (Exception e) {
            //showSnackBar(getString(R.string.something_wrong));
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkError(HttpResponseItem response) {
        super.onNetworkError(response);
        // hideProgressDialog();
    }

    public void requestCancelled() {
        Toast.makeText(getActivity(), R.string.req_cancel, Toast.LENGTH_SHORT).show();
        UserManager.setRideInprogress(false);
        getActivity().onBackPressed();
    }


    public void openPhoneDialer() {
        if (bookingStatus == null || bookingStatus.getProvider().getMobile() == null) return;
        if (checkPermissions())
            AppUtils.initiateCall(getActivity(), bookingStatus.getProvider().getMobile());
    }


    void handleStatusLayout(BookingStatus bookingStatus) {
        if (bookingStatus.getStatus().equalsIgnoreCase("SEARCHING")) {
            RippleBackground rippleBackground = (RippleBackground) findViewById(R.id.ripple_anim);
            rippleBackground.startRippleAnimation();
            findViewById(R.id.lay_searching).setVisibility(View.VISIBLE);
            findTextViewById(R.id.lblStatus_searching).setText(getString(R.string.finding_ride));
            findButtonById(R.id.btnCancelBookingSearch).setText(getString(R.string.cancel_booking));
            return;
        } else {
            findViewById(R.id.lay_searching).setVisibility(View.GONE);
        }

        //handle Driver data
        // findVizImageView(R.id.imgProvider).setImage(bookingStatus.getProvider().get() );
        findTextViewById(R.id.lblProvider).setText(MessageFormat.format("{0} {1}", bookingStatus.getProvider().getFirstName(), bookingStatus.getProvider().getLastName()));
        findRatingBarByID(R.id.ratingProvider).setRating(Float.parseFloat(bookingStatus.getProvider().getRating()));

        //handle cab detail
        if (!findVizImageView(R.id.imgServiceRequested).getImageURL().isEmpty())
            findVizImageView(R.id.imgServiceRequested).setImage(bookingStatus.getServiceType().getImage());
        findTextViewById(R.id.lblServiceRequested).setText(bookingStatus.getServiceType().getName());
        String modelNumb = bookingStatus.getProviderService().getServiceNumber() + " " + bookingStatus.getProviderService().getServiceModel();
        findTextViewById(R.id.lblModelNumber).setText(modelNumb);


        //handle surge
        if (bookingStatus.getSurge() > 0) {
            findTextViewById(R.id.lblSurgePrice).setVisibility(View.VISIBLE);
            findTextViewById(R.id.lblSurgePrice).setText(String.format(getString(R.string.surcharge_s), bookingStatus.getSurge()));
        }

//handle status layout
        if (bookingStatus.getStatus().equalsIgnoreCase("STARTED")) {

            findViewById(R.id.lay_service_accept).setVisibility(View.VISIBLE);
            findTextViewById(R.id.lblStatus).setText(getString(R.string.arriving));
            findButtonById(R.id.btnCancelBooking).setText(getString(R.string.cancel_trip));

        } else if (bookingStatus.getStatus().equalsIgnoreCase("ARRIVED")) {

            findViewById(R.id.lay_service_accept).setVisibility(View.VISIBLE);
            findTextViewById(R.id.lblStatus).setText(getString(R.string.arrived));
            findButtonById(R.id.btnCancelBooking).setText(getString(R.string.cancel_trip));
        } else if (bookingStatus.getStatus().equalsIgnoreCase("PICKEDUP")) {

            findViewById(R.id.lay_service_accept).setVisibility(View.VISIBLE);
            findTextViewById(R.id.lblStatus).setText(getString(R.string.picked_up));
            findButtonById(R.id.btnCancelBooking).setText(getString(R.string.share));
        } else if (bookingStatus.getStatus().equalsIgnoreCase("DROPPED")) {

            findViewById(R.id.lay_service_accept).setVisibility(View.VISIBLE);
            if (bookingStatus.getPaid() == 0) { //Show Invoice
                showInvoice();
            }
        } else if (bookingStatus.getStatus().equalsIgnoreCase("COMPLETED")) {

            if (bookingStatus.getPaid() == 1 && bookingStatus.getUserRated() == 0) { //Review left

                findViewById(R.id.lay_service_accept).setVisibility(View.GONE); //hide status layout
                findViewById(R.id.layout_invoice).setVisibility(View.GONE); //hide invoice layout
                findViewById(R.id.lay_rating_bsd).setVisibility(View.VISIBLE); //show rating layout
                findViewById(R.id.map).setVisibility(View.VISIBLE);
                findViewById(R.id.tvnoData).setVisibility(View.GONE);
            } else { //hide all layout
                findViewById(R.id.lay_service_accept).setVisibility(View.GONE); //hide status layout
                findViewById(R.id.layout_invoice).setVisibility(View.GONE); //hide invoice layout
                findViewById(R.id.lay_rating_bsd).setVisibility(View.GONE);//show rating layout
                findViewById(R.id.map).setVisibility(View.GONE);
                findViewById(R.id.tvnoData).setVisibility(View.VISIBLE);
//                UserManager.setRideInprogress(false);
//                onBackPressed(); //open new booking screen
                //Toast.makeText(getActivity(), "Ride Completed", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showInvoice() {
        findViewById(R.id.lay_service_accept).setVisibility(View.GONE); //hide status layout
        findViewById(R.id.layout_invoice).setVisibility(View.VISIBLE); //show invoice layout

        findTextViewById(R.id.tv_booking_id).setText(bookingStatus.getBookingId());
        findTextViewById(R.id.tv_distance_travel).setText(MessageFormat.format("{0}{1}", bookingStatus.getDistance(), getString(R.string.distance_unit)));
        findTextViewById(R.id.tv_time_taken).setText(MessageFormat.format("{0}{1}", bookingStatus.getTravelTime(), getString(R.string.time_unit)));
        findTextViewById(R.id.tv_base_price).setText(MessageFormat.format("{0}{1}", bookingStatus.getPayment().getFixed(), getString(R.string.currency_unit)));
        findTextViewById(R.id.tv_trip_fare).setText(MessageFormat.format("{0}{1}", bookingStatus.getPayment().getPayable(), getString(R.string.currency_unit)));
        findTextViewById(R.id.tv_tax).setText(MessageFormat.format("{0}{1}", bookingStatus.getPayment().getTax(), getString(R.string.currency_unit)));
        findTextViewById(R.id.tv_total_amount).setText(MessageFormat.format("{0}{1}", bookingStatus.getPayment().getTotal(), getString(R.string.currency_unit)));
        findTextViewById(R.id.tv_payment_type).setText(MessageFormat.format("{0}{1}", bookingStatus.getPaymentMode(), getString(R.string.currency_unit)));

        if(bookingStatus.getPaymentMode().equalsIgnoreCase("CASH"))
        {
            findViewById(R.id.btnPayNow).setVisibility(View.GONE);
            findViewById(R.id.btnPayNow).setVisibility(View.GONE);
            findImageView(R.id.img_pay_type).setImageResource(R.drawable.ic_cash);
        }

    }


}