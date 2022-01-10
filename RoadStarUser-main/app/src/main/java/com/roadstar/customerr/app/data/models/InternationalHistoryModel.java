package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;
import com.roadstar.customerr.app.business.BaseItem;

public class InternationalHistoryModel implements BaseItem {


    @SerializedName("id")
    private int id;

    @SerializedName("provider_id")
    private int provider_id;


    @SerializedName("user_id")
    private int user_id;

    @SerializedName("tripfrom")
    private String tripfrom;

    @SerializedName("tripto")
    private String tripto;

    @SerializedName("arrival_date")
    private String arrival_date;

    @SerializedName("recurrence")
    private String recurrence;

    @SerializedName("item_size")
    private String item_size;

    @SerializedName("item")
    private String item;

    @SerializedName("item_type")
    private String item_type;

    @SerializedName("other_information")
    private String other_information;

    @SerializedName("service_type")
    private String service_type;

    @SerializedName("tripfrom_lat")
    private String tripfrom_lat;

    @SerializedName("tripfrom_lng")
    private String tripfrom_lng;

    @SerializedName("tripto_lat")
    private String tripto_lat;

    @SerializedName("tripto_lng")
    private String tripto_lng;

    @SerializedName("trip_amount")
    private String trip_amount;

    @SerializedName("receiver_name")
    private String receiver_name;

    @SerializedName("receiver_phone")
    private String receiver_phone;

    @SerializedName("picture1")
    private String picture1;

    @SerializedName("picture2")
    private String picture2;

    @SerializedName("picture3")
    private String picture3;

    @SerializedName("created_by")
    private String created_by;

    @SerializedName("trip_status")
    private String trip_status;

    @SerializedName("status")
    private String status;

    @SerializedName("user_rated")
    private String user_rated;

    @SerializedName("provider_rated")
    private String provider_rated;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("payment")
    private PaymentInHistory payment;


    @SerializedName("trip_request")
    private HistoryBidModel trip_request;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTripfrom() {
        return tripfrom;
    }

    public void setTripfrom(String tripfrom) {
        this.tripfrom = tripfrom;
    }

    public String getTripto() {
        return tripto;
    }

    public void setTripto(String tripto) {
        this.tripto = tripto;
    }

    public String getArrival_date() {
        return arrival_date;
    }

    public void setArrival_date(String arrival_date) {
        this.arrival_date = arrival_date;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public String getItem_size() {
        return item_size;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getOther_information() {
        return other_information;
    }

    public void setOther_information(String other_information) {
        this.other_information = other_information;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getTripfrom_lat() {
        return tripfrom_lat;
    }

    public void setTripfrom_lat(String tripfrom_lat) {
        this.tripfrom_lat = tripfrom_lat;
    }

    public String getTripfrom_lng() {
        return tripfrom_lng;
    }

    public void setTripfrom_lng(String tripfrom_lng) {
        this.tripfrom_lng = tripfrom_lng;
    }

    public String getTripto_lat() {
        return tripto_lat;
    }

    public void setTripto_lat(String tripto_lat) {
        this.tripto_lat = tripto_lat;
    }

    public String getTripto_lng() {
        return tripto_lng;
    }

    public void setTripto_lng(String tripto_lng) {
        this.tripto_lng = tripto_lng;
    }

    public String getTrip_amount() {
        return trip_amount;
    }

    public void setTrip_amount(String trip_amount) {
        this.trip_amount = trip_amount;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_phone() {
        return receiver_phone;
    }

    public void setReceiver_phone(String receiver_phone) {
        this.receiver_phone = receiver_phone;
    }

    public String getPicture1() {
        return picture1;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getPicture3() {
        return picture3;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_rated() {
        return user_rated;
    }

    public void setUser_rated(String user_rated) {
        this.user_rated = user_rated;
    }

    public String getProvider_rated() {
        return provider_rated;
    }

    public void setProvider_rated(String provider_rated) {
        this.provider_rated = provider_rated;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public PaymentInHistory getPayment() {
        return payment;
    }

    public void setPayment(PaymentInHistory payment) {
        this.payment = payment;
    }

    public HistoryBidModel getTrip_request() {
        return trip_request;
    }

    public void setTrip_request(HistoryBidModel trip_request) {
        this.trip_request = trip_request;
    }


    @Override
    public int getItemType() {
        return 1;
    }
}
