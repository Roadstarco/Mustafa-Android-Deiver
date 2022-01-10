package com.roadstar.customerr.app.data.models.booking_status;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("request_id")
    @Expose
    private String requestId;
    @SerializedName("promocode_id")
    @Expose
    private Object promocodeId;
    @SerializedName("payment_id")
    @Expose
    private Object paymentId;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("fixed")
    @Expose
    private String fixed;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("commision")
    @Expose
    private double commision;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("surge")
    @Expose
    private String surge;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("payable")
    @Expose
    private String payable;
    @SerializedName("provider_commission")
    @Expose
    private double providerCommission;
    @SerializedName("provider_pay")
    @Expose
    private double providerPay;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String  getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public double getCommision() {
        return commision;
    }

    public void setCommision(double commision) {
        this.commision = commision;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getSurge() {
        return surge;
    }

    public void setSurge(String surge) {
        this.surge = surge;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPayable() {
        return payable;
    }

    public void setPayable(String payable) {
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