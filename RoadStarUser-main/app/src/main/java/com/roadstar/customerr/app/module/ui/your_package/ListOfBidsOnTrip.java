package com.roadstar.customerr.app.module.ui.your_package;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.TripIdModel;
import com.roadstar.customerr.app.module.ui.your_package.adapter.BidsOnTripAdapter;
import com.roadstar.customerr.app.module.ui.your_package.model.AllBidsOnTripModel;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

public class ListOfBidsOnTrip extends AppCompatActivity {

    ArrayList<AllBidsOnTripModel> available_trips = new ArrayList<>();
    public ProgressBar progressDoalog;

    private TextView tv_no_bid;
    AppCompatImageView backBtn;
    AppCompatTextView title;

    public int selectedTripPos=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_bids_on_trip);


        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);
        tv_no_bid = findViewById(R.id.tv_no_bid);

        if (getIntent().hasExtra("trip_pos")){
            selectedTripPos = getIntent().getIntExtra("trip_pos",-1);
        }

        title.setText(R.string.bids);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDoalog = findViewById(R.id.progressBar);
        getAllAvailable_trips();

    }

    private void getAllAvailable_trips() {

        progressDoalog.setVisibility(View.VISIBLE);

        TripIdModel model = new TripIdModel();
        if (getIntent().hasExtra("trip_id"))
            model.setTrip_id(getIntent().getIntExtra("trip_id",-1));
        else
            model.setTrip_id(-1);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ArrayList<AllBidsOnTripModel>> call = service.getAllBidsOnTrip(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",model/*, BASE_URL, "application/json"*/);
        call.enqueue(new Callback<ArrayList<AllBidsOnTripModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AllBidsOnTripModel>> call, Response<ArrayList<AllBidsOnTripModel>> response) {
                progressDoalog.setVisibility(View.GONE);
                available_trips =  response.body();
                if (available_trips != null && available_trips.size()>0) {
                    tv_no_bid.setVisibility(View.GONE);
                    setupRecyclerview();
                }else{
                    tv_no_bid.setVisibility(View.VISIBLE);
                }

                Log.d("available_trips", new Gson().toJson(available_trips));

            }

            @Override
            public void onFailure(Call<ArrayList<AllBidsOnTripModel>> call, Throwable t) {
                progressDoalog.setVisibility(View.GONE);
                Toast.makeText(ListOfBidsOnTrip.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void setupRecyclerview(){
        RecyclerView rvContacts = findViewById(R.id.available_trips_RV);

        BidsOnTripAdapter adapter = new BidsOnTripAdapter(ListOfBidsOnTrip.this,available_trips);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(ListOfBidsOnTrip.this));
        // That's all!
    }

}