package com.roadstar.customerr.app.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.roadstar.customerr.app.business.BaseItem;

public class Jobs implements BaseItem, Parcelable {
    public Jobs() {
    }

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("person")
    @Expose
    private int person;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("bid_id")
    @Expose
    private Object bidId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("job_desc")
    @Expose
    private String jobDesc;
    @SerializedName("status")
    @Expose
    private String status;

    protected Jobs(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        lat = in.readString();
        lng = in.readString();
        address = in.readString();
        person = in.readInt();
        createdAt = in.readString();
        userId = in.readInt();
        jobDesc = in.readString();
        status = in.readString();
    }

    public static final Creator<Jobs> CREATOR = new Creator<Jobs>() {
        @Override
        public Jobs createFromParcel(Parcel in) {
            return new Jobs(in);
        }

        @Override
        public Jobs[] newArray(int size) {
            return new Jobs[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getBidId() {
        return bidId;
    }

    public void setBidId(Object bidId) {
        this.bidId = bidId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getItemType() {
        return BaseItem.ITEM_HISTORY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeString(address);
        parcel.writeInt(person);
        parcel.writeString(createdAt);
        parcel.writeInt(userId);
        parcel.writeString(jobDesc);
        parcel.writeString(status);
    }
}
