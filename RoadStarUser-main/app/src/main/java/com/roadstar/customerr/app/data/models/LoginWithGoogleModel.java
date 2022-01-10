package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;

public class LoginWithGoogleModel {

    @SerializedName("device_type")
    private String device_type ;

    @SerializedName("device_token")
    private String device_token ;

    @SerializedName("accessToken")
    private String accessToken ;

    @SerializedName("device_id")
    private String device_id ;

    @SerializedName("login_by")
    private String login_by ;

    @SerializedName("mobile")
    private String mobile ;

    @SerializedName("status")
    private String status ;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @SerializedName("refresh_token")
    private String refresh_token ;

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLogin_by() {
        return login_by;
    }

    public void setLogin_by(String login_by) {
        this.login_by = login_by;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
