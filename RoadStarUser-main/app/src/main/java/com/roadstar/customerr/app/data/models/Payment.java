
package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("request_id")
    @Expose
    private int requestId;
    @SerializedName("promocode_id")
    @Expose
    private Object promocodeId;
    @SerializedName("payment_id")
    @Expose
    private Object paymentId;
    @SerializedName("payment_mode")
    @Expose
    private Object paymentMode;
    @SerializedName("fixed")
    @Expose
    private int fixed;
    @SerializedName("distance")
    @Expose
    private int distance;
    @SerializedName("commision")
    @Expose
    private double commision;
    @SerializedName("discount")
    @Expose
    private int discount;
    @SerializedName("tax")
    @Expose
    private int tax;
    @SerializedName("wallet")
    @Expose
    private int wallet;
    @SerializedName("surge")
    @Expose
    private int surge;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("payable")
    @Expose
    private int payable;
    @SerializedName("provider_commission")
    @Expose
    private double providerCommission;
    @SerializedName("provider_pay")
    @Expose
    private double providerPay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Object getPromocodeId() {
        return promocodeId;
    }

    public void setPromocodeId(Object promocodeId) {
        this.promocodeId = promocodeId;
    }

    public Object getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Object paymentId) {
        this.paymentId = paymentId;
    }

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getFixed() {
        return fixed;
    }

    public void setFixed(int fixed) {
        this.fixed = fixed;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public int getSurge() {
        return surge;
    }

    public void setSurge(int surge) {
        this.surge = surge;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPayable() {
        return payable;
    }

    public void setPayable(int payable) {
        this.payable = payable;
    }

    public double getProviderCommission() {
        return providerCommission;
    }

    public void setProviderCommission(double providerCommission) {
        this.providerCommission = providerCommission;
    }

    public double getProviderPay() {
        return providerPay;
    }

    public void setProviderPay(double providerPay) {
        this.providerPay = providerPay;
    }

}
