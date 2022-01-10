package com.roadstar.customerr.app.internationalDelivery.Model;

import com.google.gson.annotations.SerializedName;

public class BidDetailsModel {
    @SerializedName("id")
    private Integer id;
    @SerializedName("provider_id")
    private Integer provider_id;

    @SerializedName("user_id")
    private Integer user_id;

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
    private Integer amount;
    @SerializedName("traveller_response")
    private String traveller_response;
    @SerializedName("service_type")
    private String service_type;

    @SerializedName("created_by")
    private String created_by;
    @SerializedName("status")
    private String status;
    @SerializedName("receiver_name")
    private String receiver_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(Integer provider_id) {
        this.provider_id = provider_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
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

    public Integer getCounter_amount() {
        return counter_amount;
    }

    public void setCounter_amount(Integer counter_amount) {
        this.counter_amount = counter_amount;
    }

    public int getIs_counter() {
        return is_counter;
    }

    public void setIs_counter(int is_counter) {
        this.is_counter = is_counter;
    }

    @SerializedName("receiver_phone")
    private String receiver_phone;
    @SerializedName("updated_at")
    private String updated_at;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("counter_amount")
    private Integer counter_amount;
    @SerializedName("is_counter")
    private int is_counter;
}
