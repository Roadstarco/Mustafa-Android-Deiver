package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;

public class TripIdModel {
    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    @SerializedName("trip_id")
    private Integer trip_id;
}
