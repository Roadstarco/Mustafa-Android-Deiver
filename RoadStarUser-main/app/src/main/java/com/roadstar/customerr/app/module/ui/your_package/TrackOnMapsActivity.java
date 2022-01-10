package com.roadstar.customerr.app.module.ui.your_package;

import static com.roadstar.customerr.common.utils.ApiConstants.HEADER_BEARER;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.UserManager;
import com.roadstar.customerr.app.data.models.TripTrackModel;
import com.roadstar.customerr.app.internationalDelivery.Model.AvailAbleTripsModel;
import com.roadstar.customerr.app.network.ApiInterface;
import com.roadstar.customerr.app.network.RetrofitClientInstance;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackOnMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public GoogleMap mMap;
    public LocationManager locationManager;
    public static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    Handler handler;
    Runnable runnable;

    public static final int TAG_CODE_PERMISSION_LOCATION = 001;

    int trip_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_track_on_maps);

        trip_id  = getIntent().getIntExtra("id",0);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMapCurrentLoc( );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case TAG_CODE_PERMISSION_LOCATION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    /*All Permission Allow and Do Your Code here*/
                    setMapCurrentLoc();
                }

                return;
        }

    }
    private void setMapCurrentLoc( ) {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);

            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            getTracking();
            startHandler();
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,  this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        } else {
            ActivityCompat.requestPermissions(TrackOnMapsActivity.this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);

        }
    }

    private void getTracking() {

        TripTrackModel tripTrackModel = new TripTrackModel();
        tripTrackModel.setTrip_id(trip_id);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = RetrofitClientInstance.getRetrofitInstance().create(ApiInterface.class);
        Call<TripTrackModel> call = service.trackTrip(HEADER_BEARER + UserManager.getToken(), "XMLHttpRequest",tripTrackModel);
        call.enqueue(new Callback<TripTrackModel>() {
            @Override
            public void onResponse(@NotNull Call<TripTrackModel> call, @NotNull Response<TripTrackModel> response) {
                if(response.body()!=null) {
                    LatLng latLng = new LatLng(Double.parseDouble(response.body().getCurrent_latitude()), Double.parseDouble(response.body().getCurrent_longitude()));
                    // Creating a marker
                    MarkerOptions markerOptions = new MarkerOptions();

                    MarkerOptions markerOptionss = new MarkerOptions();
                    if(response.body().getService_type().equalsIgnoreCase("By Road")) {
                        if(Double.parseDouble(response.body().getCurrent_latitude())<1 || Double.parseDouble(response.body().getCurrent_longitude())<1){
                            Toast.makeText(getApplicationContext(), "Tracking not available", Toast.LENGTH_SHORT).show();
                            return;
                        }else
                            markerOptionss.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                    }
                    else if(response.body().getService_type().equalsIgnoreCase("By Air")) {
                        if(response.body().getTimeout().equalsIgnoreCase("ok")) {
                            if(Double.parseDouble(response.body().getCurrent_latitude())<1 || Double.parseDouble(response.body().getCurrent_longitude())<1){
                                Toast.makeText(getApplicationContext(), "Tracking not available", Toast.LENGTH_SHORT).show();
                                return;
                            }else
                                markerOptionss.icon(BitmapDescriptorFactory.fromResource(R.drawable.aeroplane));
                        }
                        else {
                            if (Long.parseLong(response.body().getArrivalTime()+"000") > System.currentTimeMillis()) {
                                Toast.makeText(getApplicationContext(), "Flight Arrived", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (Long.parseLong(response.body().getDepartureTime()+"000") > System.currentTimeMillis()) {
                                Toast.makeText(getApplicationContext(), "Flight still not departure", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                    }
                    else if(response.body().getService_type().equalsIgnoreCase("By Sea")){
                        if(Double.parseDouble(response.body().getCurrent_latitude())<1 || Double.parseDouble(response.body().getCurrent_longitude())<1){
                            Toast.makeText(getApplicationContext(), "Tracking not available", Toast.LENGTH_SHORT).show();
                            return;
                        }else
                            markerOptionss.icon(BitmapDescriptorFactory.fromResource(R.drawable.ship));
                    }





                    // Setting the position for the marker
                    markerOptionss.position(latLng);

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                    // Clears the previously touched position
                    mMap.clear();

                    // Animating to the touched position
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                    mMap.animateCamera(cameraUpdate);

                    // Placing a marker on the touched position
                    mMap.addMarker(markerOptionss);
                }
            }
            @Override
            public void onFailure(@NotNull Call<TripTrackModel> call, @NotNull Throwable t) {
                Toast.makeText(TrackOnMapsActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startHandler() {

        if (handler == null){
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getTracking();
                    handler.postDelayed(this, 10000);
                }
            }, 10000);
        }

    }

    @Override
    public void onPause(){
        if(handler!=null)
            handler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}