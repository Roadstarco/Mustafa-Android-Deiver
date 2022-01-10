package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SendRequestApiResponse implements Serializable {


    @Expose
    @SerializedName("message")
    public String message;

    @Expose
    @SerializedName("request_id")
    public String request_id;

    @Expose
    @SerializedName("for_test_different")
    public String for_test_different;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFor_test_different() {
        return for_test_different;
    }

    public void setFor_test_different(String for_test_different) {
        this.for_test_different = for_test_different;
    }

    public String getMesssage() {
        return message;
    }

    public void setMesssage(String messsage) {
        this.message = messsage;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

//    public String getCurrent_provider() {
//        return current_provider;
//    }
//
//    public void setCurrent_provider(String current_provider) {
//        this.current_provider = current_provider;
//    }
}
