package com.roadstar.customerr.common.utils;

public class ApiConstants {
    /**
     * Create a full server rest api url by embedding provide end point with the server url
     *
     * @param api Rest api endpoint
     * @return Full rest api url
     */
    public static String getServerUrl(String api) {
        return BASE_URL + api;
    }

    public static final int CLIENT_ID = 2;  // server side passport credentials
    public static final String CLIENT_SECRET_VALUE = "WX2IZR5Yi6gpZ3ajSJ4meKik3R0K1z2vomJVc2Qw";   // server side passport credentials
        public static final String STRIPE_TOKEN = "pk_test_51HeDaeHV7Xodq4BS6RlcHrqGwXVVQZNgTcZPUn3T3lQdxr7jbGFQ72lx8ZcpAhiLZCmnghkuTTrI9u0dLdhnPgo600rrYtZzSS"; //


    public static final String HEADER_KEY_XMLHTTP = "XMLHttpRequest";
    public static final String HEADER_BEARER = "Bearer ";
    //Api Url

    public static final String BASE_URL = "https://myroadstar.org/";
    public static final String UPLOAD_PROFILE_IMAGE = "users/uploadUserPicture/picture";
    public static final String UPLOAD_PLAYLIST_PIC = "playlists/upload/picture";
    public static final String GET_PROFILE_URL = "api/user/details";
    public static final String LOGIN_URL = "oauth/token";
    public static final String SIGNUP_URL = "api/user/signup";
    public static final String UPDATE_PROFILE_URL = "api/user/update/profile";
    public static final String FORGET_PASSWORD_URL = "api/user/forgot/password";
    public static final String RESET_PASSWORD_URL = "api/user/reset/password";
    public static final String CHANGE_PASSWORD_URL = "api/user/change/password";
    public static final String GET_HISTORY_URL = "api/user/trips";
    public static final String CLAIM_URL = "api/user/support/message";
    public static final String GET_SERVICES_URL = "api/user/services";
    public static final String GET_FARE_URL = "api/user/estimated/fare";
    public static final String SUBMIT_REQUEST_URL = "api/user/send/request";
    public static final String SUBMIT_REQUEST_URL_1 = "api/user/send/request1";
    public static final String SUBMIT_REQUEST_URL_2 = "api/user/resend/request1";
    public static final String CANCEL_REQUEST_API = "api/user/cancel/request";
    public static final String EXTEND_TRIP = "api/user/update/request";
    public static final String SAVE_LOCATION = "api/user/location";
    public static final String BOOKING_HISTORY = "api/user/trips";
    public static final String BOOKING_STATUS_URL = "api/user/request/check";
    public static final String RATING_URL = "api/user/rate/provider";
    //    Payment
    public static final String CARD_PAYMENT_LIST = "api/user/card";
    public static final String ADD_CARD_TO_ACCOUNT_API = "api/user/card";
    public static final String DELETE_CARD_FROM_ACCOUNT_API = "api/user/card/destory";

    //naveed work start from here

    public static final String ALL_AVAILABLE_TRIPS="api/user/travels";
    public static final String LOGIN_WITH_GOOGLE="api/user/auth/google";
    public static final String GET_USER_DETAIL="api/user/details";
    public static final String SEND_SUPPORT_MESSAGE="api/user/support/message";
    public static final String REQUEST_TRIPS = "api/user/create-trip";
    public static final String BID_ON_TRIPS = "api/user/request-trip";
    public static final String ALL_USER_TRIPS = "api/user/user-trips";
    public static final String ALL_MY_TRIPS = "api/user/user-trips";
    public static final String ALL_BIDS_ON_TRIP = "api/user/trip-bids";
    public static final String ACCEPT_BID = "api/user/accept-bid";
    public static final String SUBMIT_RATING = "api/user/rate-trip-provider";
    public static final String SEND_CONTER_OFFER = "api/user/bid/counter-offer";
    public static final String REJECT_COUNTER_OFFER = "api/user/bid/counter-reject";
    public static final String ACCEPT_COUNTER_OFFER = "api/user/bid/counter-accept";
    public static final String UPDATE_FCM = "api/user/update/profile/fcm";


    //Constants
    public static final String PHONE = "phone";
    public static final String ANDROID = "android";
    public static final String KEY_STATUS = "status";
    public static final String KEY_DATA = "data";
    public static final String KEY_ERROR = "error";
    public static final String CLIENT_SECRET = "WX2IZR5Yi6gpZ3ajSJ4meKik3R0K1z2vomJVc2Qw";
    public static final String GRANT_PASSWORD = "password";
    public static final String MANUAL = "manual";

    //Header Param Keys
    public static final int LIMIT = 10;
    public static final String ACCESS_TOKEN = "access_token";
    public static final String KEY_API_KEY = "apiKey";

    //Login Keys
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_DEVICE_TYPE = "device_type";
    public static final String KEY_GRANT_TYPE = "grant_type";
    public static final String KEY_CLIENT_ID = "client_id";
    public static final String KEY_CLIENT_SECRET = "client_secret";
    public static final String KEY_USER_NAME = "username";
    public static final String KEY_SCOPE = "scope";
    public static final String KEY_DEVICE_ID = "device_id";
    public static final String KEY_DEVICE_TOKEN = "device_token";


    //Register Keys
    public static final String KEY_LOGIN_BY = "login_by";
    public static final String KEY_SOCIAL_UNIQUE_ID = "social_unique_id";
    public static final String KEY_PICTURE = "picture";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_COUNTRY_NAME = "country_name";

    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_IMG_URL = "img_url";
    public static final String KEY_ID = "id";

    //CreateJob Keys
    public static final String KEY_JOB_DESC = "job_desc";
    public static final String KEY_LONG = "lng";
    public static final String KEY_LAT = "lat";
    public static final String KEY_PERSON = "person";
    public static final String KEY_START_TIME = "start_time";
    public static final String KEY_END_TIME = "end_time";

    //Compaint Keys
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";
    public static final String KEY_JOB_ID = "job_id";
    //Change Password Keys

    public static final String KEY_CONFIRM_PASS = "password_confirmation";
    public static final String KEY_OLD_PASSWORD = "old_password";
    //Claim Keys
    public static final String KEY_SUBJECT = "subject";
    public static final String KEY_MESSAGE = "message";

    //REQUEST Handling
    public static final String KEY_REQUEST_ID = "request_id";
    public static final String KEY_CANCEL_REASON = "cancel_reason";
    public static final String KEY_RATING = "rating";
    public static final String KEY_COMMENT = "comment";


}
