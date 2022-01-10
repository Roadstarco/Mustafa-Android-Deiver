package com.roadstar.customerr.app.internationalDelivery.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.internationalDelivery.adapter.InternationalTripsAvailableAdapter;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

public class AvailableTrips extends Fragment {

    View view;
    ArrayList<AvailAbleTripsModel> available_trips = new ArrayList<>();
    private ProgressBar progressDoalog;
    private TextView tv_no_bid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_available_trips, container, false);
        progressDoalog = view.findViewById(R.id.progressBar);
        tv_no_bid = view.findViewById(R.id.tv_no_bid);
        getAllAvailable_trips();


        return view;


    }

    private void getAllAvailable_trips() {

        progressDoalog.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ArrayList<AvailAbleTripsModel>> call = service.getAllAvailableTrips(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest"/*, BASE_URL, "application/json"*/);
        call.enqueue(new Callback<ArrayList<AvailAbleTripsModel>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<AvailAbleTripsModel>> call, @NotNull Response<ArrayList<AvailAbleTripsModel>> response) {
                progressDoalog.setVisibility(View.GONE);

                available_trips =  response.body();
                if (available_trips != null && available_trips.size()>0) {
                    setupRecyclerview();
                    tv_no_bid.setVisibility(View.GONE);
                }else
                    tv_no_bid.setVisibility(View.VISIBLE);

                Log.d("available_trips", new Gson().toJson(available_trips));

            }

            @Override
            public void onFailure(@NotNull Call<ArrayList<AvailAbleTripsModel>> call, @NotNull Throwable t) {
                progressDoalog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void setupRecyclerview(){
        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = view. findViewById(R.id.available_trips_RV);

        // Initialize contacts
        //available_trips = available_trips_model.createContactsList(20);

        // Create adapter passing in the sample user data
        InternationalTripsAvailableAdapter adapter = new InternationalTripsAvailableAdapter(getActivity(),available_trips);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
        // That's all!

    }
}