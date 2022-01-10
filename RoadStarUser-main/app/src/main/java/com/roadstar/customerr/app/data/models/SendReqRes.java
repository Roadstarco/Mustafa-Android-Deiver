package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;



public class SendReqRes  {

    @SerializedName("message")
    public String messsage;

    @SerializedName("request_id")
    public String request_id;

    @SerializedName("current_provider")
    public String current_provider;

    @SerializedName("error")
    public String error;



    public String getMesssage() {
        return messsage;
    }

    public void setMesssage(String messsage) {
        this.messsage = messsage;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getCurrent_provider() {
        return current_provider;
    }

    public void setCurrent_provider(String current_provider) {
        this.current_provider = current_provider;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
