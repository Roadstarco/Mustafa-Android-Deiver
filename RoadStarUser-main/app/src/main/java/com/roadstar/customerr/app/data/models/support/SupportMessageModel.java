package com.roadstar.customerr.app.data.models.support;

import com.google.gson.annotations.SerializedName;

public class SupportMessageModel {

    @SerializedName("subject")
    private String subject;

    @SerializedName("message")
    private String message;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
