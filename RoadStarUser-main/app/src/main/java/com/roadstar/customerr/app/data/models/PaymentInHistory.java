
package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInHistory {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("trip_id")
    @Expose
    private int trip_id;

    @SerializedName("bid_id")
    @Expose
    private int bid_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("provider_id")
    @Expose
    private int provider_id;

    @SerializedName("fixed")
    @Expose
    private String fixed;

    @SerializedName("commision")
    @Expose
    private String commision;

    @SerializedName("tax")
    @Expose
    private String tax;

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("provider_pay")
    @Expose
    private String provider_pay;
    @SerializedName("payment_id")
    @Expose
    private String payment_id;
    @SerializedName("payment_mode")
    @Expose
    private String payment_mode;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public int getBid_id() {
        return bid_id;
    }

    public void setBid_id(int bid_id) {
        this.bid_id = bid_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProvider_id() {
        return provider_id;
    }

    public void setProvider_id(int provider_id) {
        this.provider_id = provider_id;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getProvider_pay() {
        return provider_pay;
    }

    public void setProvider_pay(String provider_pay) {
        this.provider_pay = provider_pay;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
