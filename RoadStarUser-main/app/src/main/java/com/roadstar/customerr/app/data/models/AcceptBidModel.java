package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;

public class AcceptBidModel {

    @SerializedName("trip_id")
    private Integer trip_id;
    @SerializedName("bid_id")
    private Integer bid_id;

    public String getCounter_amount() {
        return counter_amount;
    }

    public void setCounter_amount(String counter_amount) {
        this.counter_amount = counter_amount;
    }

    @SerializedName("counter_amount")
    private String counter_amount;

    public Integer getBid_id() {
        return bid_id;
    }

    public void setBid_id(Integer bid_id) {
        this.bid_id = bid_id;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }
}
