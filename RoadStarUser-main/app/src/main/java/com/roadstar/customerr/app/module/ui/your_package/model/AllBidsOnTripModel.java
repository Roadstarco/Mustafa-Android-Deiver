package com.roadstar.customerr.app.module.ui.your_package.model;

import com.google.gson.annotations.SerializedName;

public class AllBidsOnTripModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("user_id")
    private Integer user_id;
    @SerializedName("provider_id")
    private Integer provider_id;
    @SerializedName("is_counter")
    private Integer is_counter;

    public Integer getIs_counter() {
        return is_counter;
    }

    public void setIs_counter(Integer is_counter) {
        this.is_counter = is_counter;
    }

    public Integer getCounter_amount() {
        return counter_amount;
    }

    public void setCounter_amount(Integer counter_amount) {
        this.counter_amount = counter_amount;
    }

    @SerializedName("counter_amount")
    private Integer counter_amount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
    }

    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getSend_from() {
        return send_from;
    }

    public void setSend_from(String send_from) {
        this.send_from = send_from;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_size() {
        return item_size;
    }

    public void setItem_size(String item_size) {
        this.item_size = item_size;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTraveller_response() {
        return traveller_response;
    }

    public void setTraveller_response(String traveller_response) {
        this.traveller_response = traveller_response;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @SerializedName("trip_id")
    private Integer trip_id;
    @SerializedName("item")
    private String item;
    @SerializedName("send_from")
    private String send_from;
    @SerializedName("send_to")
    private String send_to;
    @SerializedName("item_type")
    private String item_type;
    @SerializedName("item_size")
    private String item_size;
    @SerializedName("picture1")
    private String picture1;
    @SerializedName("picture2")
    private String picture2;
    @SerializedName("picture3")
    private String picture3;
    @SerializedName("description")
    private String description;
    @SerializedName("amount")
    private String amount;
    @SerializedName("traveller_response")
    private String traveller_response;
    @SerializedName("service_type")
    private String service_type;
    @SerializedName("created_by")
    private String created_by;

    @SerializedName("status")
    private String status;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("first_name")
    private String first_name;
    @SerializedName("last_name")
    private String last_name;
    @SerializedName("avatar")
    private String avatar;


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

    public String getOther_information() {
        return other_information;
    }

    public void setOther_information(String other_information) {
        this.other_information = other_information;
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

    @SerializedName("tripfrom")
    private String tripfrom;
    @SerializedName("tripto")
    private String tripto;
    @SerializedName("arrival_date")
    private String arrival_date;
    @SerializedName("recurrence")
    private String recurrence;
    @SerializedName("other_information")
    private String other_information;
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
}
