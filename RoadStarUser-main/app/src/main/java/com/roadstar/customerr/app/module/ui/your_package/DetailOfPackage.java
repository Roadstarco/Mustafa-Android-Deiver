package com.roadstar.customerr.app.module.ui.your_package;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.R.id;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.TripTrackModel;
import com.roadstar.customerr.app.data.models.booking_status.Provider;
import com.roadstar.customerr.app.data.models.booking_status.User;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.internationalDelivery.classes.LockableBottomSheetBehavior;
import com.roadstar.customerr.app.module.ui.chat.ChatActivity;
import com.roadstar.customerr.app.module.ui.your_package.model.AllBidsOnTripModel;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;
import com.roadstar.customerr.common.Services.Config;
import com.transferwise.sequencelayout.SequenceStep;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.roadstar.customerr.app.module.ui.your_package.adapter.BidsOnTripAdapter.getSingle_item;
import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

public class DetailOfPackage extends AppCompatActivity implements View.OnClickListener {


    AppCompatImageView backBtn;
    AppCompatTextView title;
    public LinearLayout inProgress;
    AppCompatButton submitRating;
    private int trip_id = 0;

    private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
    public TextView name,type,size,provider_ID,src,des,riderName,riderNameTitle;
    private SequenceStep approved,arrived,pickedUp,started,Deliverd,rating,completed;
    private RatingBar rat05UserRating;
    private EditText edt05Comment;
    private ProgressBar progressBar;
    private ArrayList<AvailAbleTripsModel> available_trips = new ArrayList<>();
    private AvailAbleTripsModel availAbleTripsModel = new AvailAbleTripsModel();
    private Handler handler;
    private AllBidsOnTripModel acceptedItem;
    private AppCompatButton appCompatButtonChat,appCompatButtonTrack;
    BroadcastReceiver broadcastReceiver;
    String currentStatus="";
    private ImageView ivPicked,ivDropped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_package);

        initialze();
        getAllAvailable_trips();
        startHandler();
        //setValues();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    //   displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    String title = intent.getStringExtra("title");
                    String body = intent.getStringExtra("body");
                    Toast.makeText(getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void getAllAvailable_trips() {

        TripTrackModel tripTrackModel = new TripTrackModel();
        tripTrackModel.setTrip_id(trip_id);
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<ArrayList<AvailAbleTripsModel>> call = service.getUserTrips(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",tripTrackModel);
        call.enqueue(new Callback<ArrayList<AvailAbleTripsModel>>() {
            @Override
            public void onResponse(@NotNull Call<ArrayList<AvailAbleTripsModel>> call, @NotNull Response<ArrayList<AvailAbleTripsModel>> response) {
                available_trips =  response.body();

                if (available_trips != null && available_trips.size()>0) {
//                    if(currentStatus.isEmpty() || currentStatus.equalsIgnoreCase(available_trips.get(0).getTrip_status())) {
                        currentStatus = available_trips.get(0).getTrip_status();
                        checkAndSetPackageStatus(available_trips.get(0).getTrip_status(), available_trips.get(0).getUser_rated());
                        setValues(available_trips.get(0));
//                        startHandler();
//                    }
//                    for (int i=0;i<available_trips.size();i++){
//                        if (available_trips.get(i).getId() == trip_id){
//                            checkAndSetPackageStatus(available_trips.get(i).getTrip_status(),available_trips.get(i).getUser_rated());
//                            setValues(available_trips.get(i));
//                            startHandler();
//                            return;
//                        }
//                    }
                }

                Log.d("available_trips", new Gson().toJson(available_trips));

            }


            @Override
            public void onFailure(@NotNull Call<ArrayList<AvailAbleTripsModel>> call, @NotNull Throwable t) {
                Toast.makeText(DetailOfPackage.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void startHandler() {

        if (handler == null){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initialze();
                    getAllAvailable_trips();
                    handler.postDelayed(this, 3000);
                }
            }, 5000);
        }
    }

    private void setValues(AvailAbleTripsModel availAbleTripsModel) {

        if (availAbleTripsModel.getCreated_by().equals("user")){
            acceptedItem = getSingle_item(0);
            String riderNameString = acceptedItem.getFirst_name() + getString(R.string.space) + acceptedItem.getLast_name();

            riderName.setText(riderNameString);
        }else {
            riderNameTitle.setText(R.string.reciever_name);
            riderName.setText(availAbleTripsModel.getReceiver_name());
        }

        this.availAbleTripsModel = availAbleTripsModel;
        name.setText(availAbleTripsModel.getItem());

        if (availAbleTripsModel.getItem_type() == null){
            type.setText(availAbleTripsModel.getService_type());
        }else {
            type.setText(availAbleTripsModel.getItem_type());
        }

        if(availAbleTripsModel.getPickedup_image()!=null && !availAbleTripsModel.getPickedup_image().equalsIgnoreCase("Null"))
            Glide.with(getApplicationContext()).load(availAbleTripsModel.getPickedup_image()).into(ivPicked);

        if(availAbleTripsModel.getDroppedof_image()!=null && !availAbleTripsModel.getDroppedof_image().equalsIgnoreCase("Null"))
            Glide.with(getApplicationContext()).load(availAbleTripsModel.getDroppedof_image()).into(ivDropped);

        type.setText(availAbleTripsModel.getService_type());
        size.setText(availAbleTripsModel.getItem_size());
        provider_ID.setText(String.valueOf(availAbleTripsModel.getProvider_id()));
        src.setText(availAbleTripsModel.getTripfrom());
        des.setText(availAbleTripsModel.getTripto());

    }

    private void initialze() {

        if (getIntent().hasExtra("pos")){
            int pos = getIntent().getIntExtra("pos", -1);
        }
        if (getIntent().hasExtra("trip_id")){
            trip_id = getIntent().getIntExtra("trip_id",-1);
        }
        setRatingLayout();

        progressBar = findViewById(R.id.progressBarRating);
        backBtn = findViewById(R.id.iv_back);
        title = findViewById(R.id.tv_title);
        appCompatButtonChat = findViewById(id.btnChat);
        appCompatButtonTrack = findViewById(id.btnTrack);
        appCompatButtonTrack.setVisibility(View.GONE);
        submitRating = findViewById(R.id.btn_rate);
        name = findViewById(R.id.tv_packag_name);
        riderNameTitle = findViewById(id.textView3);
        riderName = findViewById(R.id.tv_rider_name);
        type = findViewById(R.id.tv_package_type);
        size = findViewById(R.id.tv_package_size);
        provider_ID = findViewById(R.id.tv_provider_ID);
        src = findViewById(R.id.tv_trip_start_dest);
        des = findViewById(R.id.tv_trip_end_dest);

        inProgress = findViewById(R.id.inProgressLayout);
        pickedUp =(SequenceStep)findViewById(R.id.pickedUp);
        approved =(SequenceStep)findViewById(R.id.approved);
        arrived =(SequenceStep)findViewById(R.id.arrived);
        started=(SequenceStep)findViewById(R.id.started);
        Deliverd =(SequenceStep)findViewById(R.id.deliverd);
        rating =(SequenceStep)findViewById(R.id.rating);
        completed = (SequenceStep)findViewById(R.id.completed);

        ivDropped = findViewById(id.ivDropped);
        ivPicked = findViewById(id.ivPicked);

        title.setText(R.string.status_activity_title);

        ConstraintLayout bottomSheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        if (bottomSheetBehavior instanceof LockableBottomSheetBehavior) {
                            //noinspection rawtypes
                            ((LockableBottomSheetBehavior) bottomSheetBehavior).setLocked(true);
                        }
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        inProgress.setVisibility(View.GONE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if(!rating.isActive())
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


        backBtn.setOnClickListener(this);
        submitRating.setOnClickListener(this);

        appCompatButtonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),TrackOnMapsActivity.class);
                intent.putExtra("id",availAbleTripsModel.getId());
                startActivity(intent);

            }
        });

        appCompatButtonChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                Provider user = new Provider();
                if(acceptedItem!=null) {
                    user.setId(acceptedItem.getProvider_id());
                    user.setFirstName(acceptedItem.getFirst_name());
                    user.setEmail("");
                    user.setAvatar(acceptedItem.getAvatar());
                }else {
                    user.setId(availAbleTripsModel.getProvider_id());
                    user.setFirstName(availAbleTripsModel.getFirst_name());
                    user.setEmail(availAbleTripsModel.getEmail());
                    user.setAvatar(availAbleTripsModel.getAvatar());
                }
                intent.putExtra("user",user);
                startActivity(intent);

            }
        });
    }

    public void checkAndSetPackageStatus(String currentStatus, Integer user_rated) {

        try {
            switch (currentStatus) {

                case "STARTED":

                    removePrevious(approved);
                    appCompatButtonTrack.setVisibility(View.VISIBLE);
                    started.setActive(true);
//                    started.setSubtitle(R.string.current_status);
                    //programatically seting style to Title
                    started.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                    break;

                case "ARRIVED":
                    appCompatButtonTrack.setVisibility(View.VISIBLE);
                    removePrevious(started);
                    arrived.setActive(true);
//                    arrived.setSubtitle(R.string.current_status);
                    //programatically seting style to Title
                    arrived.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                    break;


                case "PICKEDUP":
                    removePrevious(arrived);
                    pickedUp.setActive(true);
//                    pickedUp.setSubtitle(R.string.current_status);
                    appCompatButtonTrack.setVisibility(View.VISIBLE);
                    //programatically seting style to Title
                    pickedUp.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                    break;

                case "DROPPED":

                    removePrevious(pickedUp);
                    appCompatButtonTrack.setVisibility(View.VISIBLE);
                    Deliverd.setActive(true);
//                    Deliverd.setSubtitle(R.string.current_status);
                    //programatically seting style to Title
                    Deliverd.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                    break;

                case "COMPLETED":


                    if (user_rated == 1){
                        appCompatButtonTrack.setVisibility(View.GONE);
                        removePrevious(rating);
                        completed.setActive(true);
//                        completed.setSubtitle(R.string.current_status);
                        //programatically seting style to Title
                        completed.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                    }else {
                        if (!rating.isActive()){
                        removePrevious(Deliverd);
                        rating.setActive(true);
//                        rating.setSubtitle(R.string.current_status);
                        //programatically seting style to Title
                        rating.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);

                        /*   rating.setOnClickListener(DetailOfPackage.this::onClick);
                         */

                        inProgress.setVisibility(View.VISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                    }


                    if (handler != null)
                        handler.removeCallbacks(null);
                    break;

                default:

                    approved.setActive(true);
//                    approved.setSubtitle(R.string.current_status);
                    //programatically seting style to Title
                    approved.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Title);
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void removePrevious(SequenceStep previous) {
        try {

            previous.clearAnimation();
            previous.clearFocus();
            previous.setActive(false);
            previous.setTitleTextAppearance(R.style.TextAppearance_AppCompat_Small);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            inProgress.setVisibility(View.GONE);
        }else {

            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case id.iv_back:
                onBackPressed();
                break;
            case R.id.rating:
                inProgress.setVisibility(View.VISIBLE);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btn_rate:
                submitRatingRequest();
                break;
        }
    }

    private void setRatingLayout() {
        rat05UserRating = findViewById(R.id.rat05UserRating);
        edt05Comment = findViewById(R.id.edt05Comment);
    }

    private void submitRatingRequest() {
        progressBar.setVisibility(View.VISIBLE);
        SubmitRatingReq submitRatingReq = new SubmitRatingReq();
        submitRatingReq.setComment(edt05Comment.getText().toString());
        submitRatingReq.setRating((int) rat05UserRating.getRating());
        submitRatingReq.setTrip_id(this.availAbleTripsModel.getId().toString());

        ApiInterface mApiInterface = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);

        Call<ResponseBody> call = mApiInterface.submitRatingInternational(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",submitRatingReq);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetailOfPackage.this, "Trip completed", Toast.LENGTH_SHORT).show();
                    finish();
                }else
                    Toast.makeText(DetailOfPackage.this, "Trip not completed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.d("onFailure", Objects.requireNonNull(t.getMessage()));

            }

        });
    }
    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }


}