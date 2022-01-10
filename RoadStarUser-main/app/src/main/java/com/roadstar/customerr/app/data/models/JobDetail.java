package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class JobDetail implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("zones")
    @Expose
    public String zones;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("provider_name")
    @Expose
    public String providerName;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("capacity")
    @Expose
    public String capacity;
    @SerializedName("fixed")
    @Expose
    public String fixed;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("minute")
    @Expose
    public String minute;
    @SerializedName("distance")
    @Expose
    public String distance;
    @SerializedName("calculator")
    @Expose
    public String calculator;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("estimated_fare")
    @Expose
    public double estimatedFare;
    @SerializedName("time")
    @Expose
    public String time;
    @SerializedName("surge")
    @Expose
    public String surge;
    @SerializedName("surge_value")
    @Expose
    public String surgeValue;
    @SerializedName("tax_price")
    @Expose
    public String taxPrice;
    @SerializedName("base_price")
    @Expose
    public String basePrice;
    @SerializedName("wallet_balance")
    @Expose
    public String walletBalance;

    public String category;
    public String product_type;
    public String weightUnit;
    public String product_weight;
    public String attachment1="";
    public String attachment2="";
    public String attachment3="";
    public String instruction;
    public String receiver_name;
    public String receiver_phone;
    public String payment_mode;
    public String s_address;
    public String d_address;
    public String s_latitude;
    public String s_longitude;
    public String d_latitude;
    public String d_longitude;
    public String service_type;
    public String schedule_time;
    public String schedule_date;

    public String use_wallet;

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getSchedule_time() {
        return schedule_time;
    }

    public void setSchedule_time(String schedule_time) {
        this.schedule_time = schedule_time;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZones() {
        return zones;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getFixed() {
        return fixed;
    }

    public void setFixed(String fixed) {
        this.fixed = fixed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getCalculator() {
        return calculator;
    }

    public void setCalculator(String calculator) {
        this.calculator = calculator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSurge() {
        return surge;
    }

    public void setSurge(String surge) {
        this.surge = surge;
    }

    public String getSurgeValue() {
        return surgeValue;
    }

    public void setSurgeValue(String surgeValue) {
        this.surgeValue = surgeValue;
    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(String walletBalance) {
        this.walletBalance = walletBalance;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getProduct_weight() {
        return product_weight;
    }

    public void setProduct_weight(String product_weight) {
        this.product_weight = product_weight;
    }

    public String getAttachment1() {
        return attachment1;
    }

    public void setAttachment1(String attachment1) {
        this.attachment1 = attachment1;
    }

    public String getAttachment2() {
        return attachment2;
    }

    public void setAttachment2(String attachment2) {
        this.attachment2 = attachment2;
    }

    public String getAttachment3() {
        return attachment3;
    }

    public void setAttachment3(String attachment3) {
        this.attachment3 = attachment3;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
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

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getS_address() {
        return s_address;
    }

    public void setS_address(String s_address) {
        this.s_address = s_address;
    }

    public String getD_address() {
        return d_address;
    }

    public void setD_address(String d_address) {
        this.d_address = d_address;
    }

    public String getS_latitude() {
        return s_latitude;
    }

    public void setS_latitude(String s_latitude) {
        this.s_latitude = s_latitude;
    }

    public String getS_longitude() {
        return s_longitude;
    }

    public void setS_longitude(String s_longitude) {
        this.s_longitude = s_longitude;
    }

    public String getD_latitude() {
        return d_latitude;
    }

    public void setD_latitude(String d_latitude) {
        this.d_latitude = d_latitude;
    }

    public String getD_longitude() {
        return d_longitude;
    }

    public void setD_longitude(String d_longitude) {
        this.d_longitude = d_longitude;
    }

}