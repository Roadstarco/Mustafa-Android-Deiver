package com.roadstar.customerr.app.module.ui.booking_activity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.roadstar.customerr.R;
import com.roadstar.customerr.app.data.models.PlacePredictions;
import com.roadstar.customerr.app.data.preferences.PreferenceUtils;
import com.roadstar.customerr.app.data.preferences.SharedPreferenceManager;
import com.roadstar.customerr.app.module.ui.your_package.MapHandlerActivity;
import com.roadstar.customerr.common.utils.DataParser;
import com.roadstar.customerr.common.utils.MapAnimator;
import com.roadstar.customerr.common.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class WhereToGoActivity extends MapHandlerActivity {

    Marker marker;
    //Markers
    String strTimeTaken = "";
    Utilities utils = new Utilities();
    int flowValue = 0;
    public String CurrentStatus = "";

    private LatLng destLatLng;
    private Marker sourceMarker;
    private Marker destinationMarker;
    String current_lat = "", current_lng = "", current_address = "", source_lat = "", source_lng = "", source_address = "",
            dest_lat = "", dest_lng = "", dest_address = "", extend_dest_lat = "", extend_dest_lng = "", extend_dest_address = "";

    String is_track = "";
    public String getPlaceAddress() {

        String currentAddress = "";
        try {


            double latitude = mMap.getCameraPosition().target.latitude;
            double longitude = mMap.getCameraPosition().target.longitude;

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            currentAddress = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentAddress;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode==2000) {
            try {

                PlacePredictions placePredictions = (PlacePredictions) data.getSerializableExtra("Location Address");

                assert placePredictions != null;
                dest_lat = placePredictions.strDestLatitude;
                dest_lng = placePredictions.strDestLongitude;


                source_lat = mMap.getCameraPosition().target.latitude+"";
                source_lng = mMap.getCameraPosition().target.longitude+"";


                SharedPreferenceManager.getInstance().save(PreferenceUtils.SOURCE_LAT, source_lat);
                SharedPreferenceManager.getInstance().save(PreferenceUtils.SOURCE_LNG, source_lng);
                SharedPreferenceManager.getInstance().save(PreferenceUtils.DEST_LAT, dest_lat);
                SharedPreferenceManager.getInstance().save(PreferenceUtils.DEST_LNG, dest_lng);

                String url = Utilities.getUrl(this,Double.parseDouble(source_lat), Double.parseDouble(source_lng)
                        , Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
                FetchUrl fetchUrl = new FetchUrl();
                fetchUrl.execute(url);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = Utilities.downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObj = new JSONObject(result);
                if (!jsonObj.optString("status").equalsIgnoreCase("ZERO_RESULTS")) {
                    ParserTask parserTask = new ParserTask();
                    // Invokes the thread for parsing the JSON data
                    parserTask.execute(result);
                } else {
                    mMap.clear();
                    stopAnim();
                    flowValue = 0;
                    //layoutChanges();
                    gotoCurrentPosition();
                    showToast("No Service");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        DataParser parser;

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            if (result != null) {
                // Traversing through all the routes
                if (result.size() > 0) {
                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        if (!source_lat.equalsIgnoreCase("") && !source_lng.equalsIgnoreCase("")) {
                            LatLng location = new LatLng(Double.parseDouble(source_lat), Double.parseDouble(source_lng));
                            //mMap.clear();
                            if (sourceMarker != null)
                                sourceMarker.remove();
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(location).snippet("My Location")
                                    .title("Pickup").draggable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
                            marker = mMap.addMarker(markerOptions);
                            sourceMarker = mMap.addMarker(markerOptions);
                            //CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(18).build();
                            //mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                        if (!dest_lat.equalsIgnoreCase("") && !dest_lng.equalsIgnoreCase("")) {
                            destLatLng = new LatLng(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng));
                            if (destinationMarker != null)
                                destinationMarker.remove();
                            MarkerOptions destMarker = new MarkerOptions()
                                    .position(destLatLng).title("Drop off").snippet(dest_address).draggable(true)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
                            destinationMarker = mMap.addMarker(destMarker);
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(sourceMarker.getPosition());
                            builder.include(destinationMarker.getPosition());
                            LatLngBounds bounds = builder.build();
                            int padding = 100; // offset from edges of the map in pixels
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                            mMap.moveCamera(cu);
                        }

                        if (flowValue == 1) {
                            if (sourceMarker != null && destinationMarker != null) {
                                sourceMarker.setDraggable(true);
                                destinationMarker.setDraggable(true);
                            }
                        } else {
                            if (is_track.equalsIgnoreCase("YES") &&
                                    (CurrentStatus.equalsIgnoreCase("STARTED") || CurrentStatus.equalsIgnoreCase("PICKEDUP")
                                            || CurrentStatus.equalsIgnoreCase("ARRIVED"))) {
                                if (sourceMarker != null && destinationMarker != null) {
                                    sourceMarker.setDraggable(false);
                                    destinationMarker.setDraggable(true);
                                }
                            } else {
                                if (sourceMarker != null && destinationMarker != null) {
                                    sourceMarker.setDraggable(false);
                                    destinationMarker.setDraggable(false);
                                }
                            }
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        lineOptions.width(2);
                        lineOptions.color(Color.GRAY);

                        Log.d("onPostExecute", "onPostExecute lineoptions decoded");

                    }


                    strTimeTaken = parser.getEstimatedTime();

                } else {
                    mMap.clear();

                    showToast("No Service");
                }

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null && points != null) {
                mMap.addPolyline(lineOptions);
                startAnim(points);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private void startAnim(ArrayList<LatLng> routeList) {
        if (mMap != null && routeList.size() > 1) {
            MapAnimator.getInstance().animateRoute(WhereToGoActivity.this, mMap, routeList);
        }
    }

    private void stopAnim() {
        if (mMap != null) {
            MapAnimator.getInstance().stopAnim();
        }
    }

    public void gotoCurrentPosition() {
        if (!current_lat.equalsIgnoreCase("") && !current_lng.equalsIgnoreCase("")) {
            LatLng myLocation = new LatLng(Double.parseDouble(current_lat), Double.parseDouble(current_lng));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public String getSAddress(){
        if(!source_lat.isEmpty() && !source_lng.isEmpty()) {
            Geocoder geocoder;
            String address = "";
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(source_lat), Double.parseDouble(source_lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0);
            } catch (IndexOutOfBoundsException | IOException e) {
                e.printStackTrace();
            }

            return address;
        }else
            return "";
    }
    public String getDAddress(){
        if(!dest_lat.isEmpty() && !dest_lng.isEmpty()) {
            Geocoder geocoder;
            String address = "";
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(Double.parseDouble(dest_lat), Double.parseDouble(dest_lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0);
            } catch (IndexOutOfBoundsException | IOException e) {
                e.printStackTrace();
            }

            return address;
        }else
            return "";
    }
}
