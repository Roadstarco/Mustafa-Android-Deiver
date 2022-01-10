package com.roadstar.customerr.app.internationalDelivery.Model;

import com.google.gson.annotations.SerializedName;

public class AvailAbleTripsModel {

    @SerializedName("id")
    private Integer id;

    public BidDetailsModel getBid_details() {
        return bid_details;
    }

    public void setBid_details(BidDetailsModel bid_details) {
        this.bid_details = bid_details;
    }

    @SerializedName("bid_details")
    private BidDetailsModel bid_details;


    @SerializedName("item")
    private String item;

    @SerializedName("item_type")
    private String item_type;

    @SerializedName("item_size")
    private String item_size;

    @SerializedName("trip_id")
    private Integer trip_id;

    @SerializedName("provider_id")
    private Integer provider_id;

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    @SerializedName("receiver_name")
    private String receiver_name;

    public Integer getBid_id() {
        return bid_id;
    }

    public void setBid_id(Integer bid_id) {
        this.bid_id = bid_id;
    }

    @SerializedName("bid_id")
    private Integer bid_id;

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

    @SerializedName("status")
    private String status;

    @SerializedName("trip_status")
    private String trip_status;

    @SerializedName("updated_at")
    private String updated_at;

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @SerializedName("created_by")
    private String created_by;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("email")
    private String email;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("user_rated")
    private Integer user_rated;

    public Integer getUser_rated() {
        return user_rated;
    }

    public void setUser_rated(Integer user_rated) {
        this.user_rated = user_rated;
    }

    @SerializedName("amount")
    private String amount;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("pickedup_image")
    private String pickedup_image;

    @SerializedName("droppedof_image")
    private String droppedof_image;



    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItem() {
        return item;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public void setItem(String item) {
        this.item = item;
    }


    public Integer getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(Integer trip_id) {
        this.trip_id = trip_id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
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

    public String getPickedup_image() {
        return pickedup_image;
    }

    public void setPickedup_image(String pickedup_image) {
        this.pickedup_image = pickedup_image;
    }

    public String getDroppedof_image() {
        return droppedof_image;
    }

    public void setDroppedof_image(String droppedof_image) {
        this.droppedof_image = droppedof_image;
    }


    //private static int lastContactId = 0;

/*    public static ArrayList<available_trips_model> createContactsList(int numContacts) {
        ArrayList<available_trips_model> contacts = new ArrayList<available_trips_model>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new available_trips_model("Person " + ++lastContactId, i <= numContacts / 2));
        }

        return contacts;
    }*/

}
