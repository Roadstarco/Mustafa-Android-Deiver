package com.roadstar.customerr.app.network;

import com.roadstar.customerr.app.data.models.AcceptBidModel;
import com.roadstar.customerr.app.data.models.LoginWithGoogleModel;
import com.roadstar.customerr.app.data.models.TripIdModel;
import com.roadstar.customerr.app.data.models.TripTrackModel;
import com.roadstar.customerr.app.data.models.booking_status.User;
import com.roadstar.customerr.app.data.models.support.SupportMessageModel;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.module.ui.your_package.SubmitRatingReq;
import com.roadstar.customerr.app.module.ui.your_package.model.AllBidsOnTripModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.roadstar.customerr.common.utils.ApiConstants.ACCEPT_BID;
import static com.roadstar.customerr.common.utils.ApiConstants.ACCEPT_COUNTER_OFFER;
import static com.roadstar.customerr.common.utils.ApiConstants.ALL_AVAILABLE_TRIPS;
import static com.roadstar.customerr.common.utils.ApiConstants.ALL_BIDS_ON_TRIP;
import static com.roadstar.customerr.common.utils.ApiConstants.ALL_USER_TRIPS;
import static com.roadstar.customerr.common.utils.ApiConstants.BID_ON_TRIPS;
import static com.roadstar.customerr.common.utils.ApiConstants.BOOKING_STATUS_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.CANCEL_REQUEST_API;
import static com.roadstar.customerr.common.utils.ApiConstants.EXTEND_TRIP;
import static com.roadstar.customerr.common.utils.ApiConstants.GET_USER_DETAIL;
import static com.roadstar.customerr.common.utils.ApiConstants.LOGIN_WITH_GOOGLE;
import static com.roadstar.customerr.common.utils.ApiConstants.REJECT_COUNTER_OFFER;
import static com.roadstar.customerr.common.utils.ApiConstants.REQUEST_TRIPS;
import static com.roadstar.customerr.common.utils.ApiConstants.SAVE_LOCATION;
import static com.roadstar.customerr.common.utils.ApiConstants.SEND_CONTER_OFFER;
import static com.roadstar.customerr.common.utils.ApiConstants.SEND_SUPPORT_MESSAGE;
import static com.roadstar.customerr.common.utils.ApiConstants.SUBMIT_RATING;
import static com.roadstar.customerr.common.utils.ApiConstants.SUBMIT_REQUEST_URL;
import static com.roadstar.customerr.common.utils.ApiConstants.SUBMIT_REQUEST_URL_1;
import static com.roadstar.customerr.common.utils.ApiConstants.SUBMIT_REQUEST_URL_2;
import static com.roadstar.customerr.common.utils.ApiConstants.UPDATE_FCM;
import static com.roadstar.customerr.common.utils.ApiConstants.UPDATE_PROFILE_URL;

public interface ApiInterface {
    //synchronous.
    @GET("json?")
    Call<ResponseBody> getResponse(@Query("latlng") String param1, @Query("key") String param2);

    @POST(SUBMIT_REQUEST_URL_1)
    Call<ResponseBody> sendRequest(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body RequestBody requestBody);

    @GET(BOOKING_STATUS_URL)
    Call<ResponseBody> getRequest(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest);

    @POST(SUBMIT_REQUEST_URL_2)
    Call<ResponseBody> reSendRequest(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body RequestBody requestBody);


    @POST(EXTEND_TRIP)
    @FormUrlEncoded
    Call<ResponseBody> extendTrip(@Header("X-Requested-With") String xmlRequest, @Header("Authorization") String strToken,
                                  @Field("request_id") String request_id, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("address") String address);

    @GET(SAVE_LOCATION)
    Call<ResponseBody> getFavoriteLocations(@Header("X-Requested-With") String xmlRequest,
                                            @Header("Authorization") String strToken);

    @POST(SAVE_LOCATION)
    @FormUrlEncoded
    Call<ResponseBody> updateFavoriteLocations(@Header("X-Requested-With") String xmlRequest, @Header("Authorization") String strToken,
                                               @Field("type") String type, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("address") String address);

    @DELETE(SAVE_LOCATION + "/" + "{id}")
    Call<ResponseBody> deleteFavoriteLocations(@Path("id") String id, @Header("X-Requested-With") String xmlRequest, @Header("Authorization") String strToken,
                                               @Query("type") String type, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("address") String address);

//    @GET("api/user/card")
//    Call< ArrayList<CardDetails>> getCardsList(@Header("X-Requested-With") String xmlRequest,
//                                               @Header("Authorization") String strToken);

    @POST(UPDATE_PROFILE_URL)
    Call<ResponseBody> updateProfile(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body RequestBody requestBody);


    @POST(ALL_AVAILABLE_TRIPS)
    Call<ArrayList<AvailAbleTripsModel>> getAllAvailableTrips(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

    @POST(REQUEST_TRIPS)
    Call<ResponseBody> requestTrip(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body RequestBody requestBody);

    @POST(BID_ON_TRIPS)
    Call<AvailAbleTripsModel> bidOnTrip(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body RequestBody requestBody);

    @POST("api/user/send-message-notification-to-provider")
    Call<ResponseBody> sendnotification(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body User requestBody);

    @POST("api/user/track-trip-new")
    Call<TripTrackModel> trackTrip(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest,@Body TripTrackModel request_id);


    @POST(ALL_USER_TRIPS)
    Call<ArrayList<AvailAbleTripsModel>> getAllUserTrips(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

    @POST("api/user/user-trips-id")
    Call<ArrayList<AvailAbleTripsModel>> getUserTrips(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest,@Body TripTrackModel request_id/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

//    @POST(ALL_USER_TRIPS)
//    Call<ArrayList<AvailAbleTripsModel>> getAllUserTrips(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

    @POST(ALL_BIDS_ON_TRIP)
    Call<ArrayList<AllBidsOnTripModel>> getAllBidsOnTrip(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body TripIdModel trip_id/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

    @POST(ACCEPT_BID)
    Call<ResponseBody> acceptBid(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body AcceptBidModel trip_id/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);

    @POST(SUBMIT_RATING)
    Call<ResponseBody> submitRatingInternational( @Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest,@Body SubmitRatingReq submitRatingReq);

    @POST(SEND_CONTER_OFFER)
    Call<ResponseBody> sendCounterOffer(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body AcceptBidModel trip_id/*, @Header("Host")String host, @Header("Content-Type")String contentType*/);


    @POST(ACCEPT_COUNTER_OFFER)
    Call<ResponseBody> AcceptCounterOffer(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest,@Body AvailAbleTripsModel model);

    @POST(REJECT_COUNTER_OFFER)
    Call<ResponseBody> rejectCounterOffer(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest,@Body AvailAbleTripsModel model);


    @POST(LOGIN_WITH_GOOGLE)
    Call<LoginWithGoogleModel> loginWithGoogle(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body LoginWithGoogleModel loginWithGoogleModel);


    @GET(GET_USER_DETAIL)
    Call<JSONObject> getUserDetails(@Header("Authorization") String auth, @Body LoginWithGoogleModel loginWithGoogleModel);

    @POST(SEND_SUPPORT_MESSAGE)
    Call<ResponseBody> sendSupportMessage(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body SupportMessageModel supportMessageModel);

    @POST("api/user/send-support-message-user")
    Call<ResponseBody> sendSupportMessageN(@Header("Authorization") String auth, @Header("X-Requested-With") String xmlRequest, @Body SupportMessageModel supportMessageModel);

    @POST(UPDATE_FCM)
    @FormUrlEncoded
    Call<JSONObject> setFcm(@Header("Authorization") String auth, @Field("fcm") String type);

    @POST("api/user/recreatesend/request1")
    @FormUrlEncoded
    Call<ResponseBody> recreateReq(@Header("Authorization") String auth, @Field("id") String type);

    @POST(CANCEL_REQUEST_API)
    @FormUrlEncoded
    Call<ResponseBody> cancelRequest(@Header("Authorization") String auth,@Field("request_id") String request_id,@Field("cancel_reason") String cancel_reason);


}
