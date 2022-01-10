package com.roadstar.customerr.common.utils;

/**
 * Contains constants used in application
 */
public class AppConstants {
    public static final String HELPER_LOGIN_VERIFICATION = "helper/login/verification";
    //Argument Values
    public static final String ON_WAY_JOB = "job/%s/mover/status/update?updatedStatus=1";
    public static final String ARRIVED_JOB = "job/%s/mover/status/update?updatedStatus=2";
    public static final String START_JOB = "job/%s/mover/startJob?updatedStatus=3";
    public static final String PAUSE_JOB = "job/%s/mover/pauseJob?updatedStatus=4";
    public static final String FINISH_JOB = "job/%s/mover/finishedJob";
    public static final String APP_VERSION = "appVersion";

    //API RESPONSE
    public static int SUCCESS = 1;

    //Verify Phone KEY
    public enum AuthType {
        AUTH_SIGN_IN,
        AUTH_SIGN_UP
    }

    //Verify User Type
    public enum SignupType {

        FREELANCER_SIGN_UP,
        COMPANY_MOVER_SIGN_UP,
        MERCHANT_SIGN_UP,

    }

    //Set Locations
    public enum LocationTypes {
        SET_LOCATION,
        UPDATE_LOCATION,
        HOME_LOCATION,
        WORK_LOCATION,
        UPDATE_WORK_LOCATION,
        UPDATE_HOME_LOCATION
    }

    //image type
    public static String IMAGE_THUMB = "_thumb";
    public static String IMAGE_MINI_THUMB = "_mini_thumb";

    //RequestCodes
    public static final int GET_LOCATION_REQUEST_CODE = 100;
    public static final int GET_COMPLETE_ADDRESS = 200;

    /**
     * Create a full server rest api url by embedding provide end point with the server url
     *
     * @param api Rest api endpoint
     * @return Full rest api url
     */
    public static String getServerUrl(String api) {
        return ApiConstants.BASE_URL + api;
    }

    public static final String KEY_PROFILE = "Profile";

    public static final String KEY_NEWS_URL = "NewsUrl";

    public static final String KEY_TITLE = "Title";

    //Permissions
    public static final int REQUEST_STORAGE_READ_WRITE_ACCESS_PERMISSION = 101;
}
