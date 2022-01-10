package com.roadstar.customerr.app.data.models;

import com.google.gson.annotations.SerializedName;
import com.roadstar.customerr.app.business.BaseItem;

import java.util.ArrayList;

public class CombineHistoryModel implements BaseItem {

    @SerializedName("localJobs")
    private ArrayList<Booking> localJobs;

    @SerializedName("internationalJobs")
    private ArrayList<InternationalHistoryModel> internationalJobs;

    public ArrayList<Booking> getLocalJobs() {
        return localJobs;
    }

    public void setLocalJobs(ArrayList<Booking> localJobs) {
        this.localJobs = localJobs;
    }

    public ArrayList<InternationalHistoryModel> getInternationalJobs() {
        return internationalJobs;
    }

    public void setInternationalJobs(ArrayList<InternationalHistoryModel> internationalJobs) {
        this.internationalJobs = internationalJobs;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
