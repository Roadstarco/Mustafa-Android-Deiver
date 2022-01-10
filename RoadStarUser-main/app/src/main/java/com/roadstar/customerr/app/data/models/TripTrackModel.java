package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;

public class TripTrackModel {
    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    @SerializedName("trip_id")
    private Integer trip_id;

    @SerializedName("current_longitude")
    private String current_longitude;

    @SerializedName("current_latitude")
    private String current_latitude;

    @SerializedName("service_type")
    private String service_type;

    @SerializedName("timeout")
    private String timeout;

    @SerializedName("arrivalTime")
    private String arrivalTime;

    @SerializedName("departureTime")
    private String departureTime;

    public String getCurrent_longitude() {
        return current_longitude;
    }

    public void setCurrent_longitude(String current_longitude) {
        this.current_longitude = current_longitude;
    }

    public String getCurrent_latitude() {
        return current_latitude;
    }

    public void setCurrent_latitude(String current_latitude) {
        this.current_latitude = current_latitude;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
