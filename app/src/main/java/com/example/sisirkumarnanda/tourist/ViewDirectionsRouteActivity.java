package com.example.sisirkumarnanda.tourist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.sisirkumarnanda.tourist.Helper.DirectionsJSONParser;
import com.example.sisirkumarnanda.tourist.Model.MyRoutes;
import com.example.sisirkumarnanda.tourist.Model.Step;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewDirectionsRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LocationRequest mLocationRequest;
    Location lastLocation;
    Marker currentMarker;
    Polyline polyline;
    MyGoogleAPIService myGoogleAPIService;
    Button showTextDirections;

    MyRoutes myRoutes = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_directions_route);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myGoogleAPIService = Common.myGoogleAPIServiceScalars();
        showTextDirections = (Button)findViewById(R.id.buttonShowTextDirections);

        showTextDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDirectionsRouteActivity.this);
                LayoutInflater inflater = LayoutInflater.from(ViewDirectionsRouteActivity.this);
                View stepView = inflater.inflate(R.layout.show_direction_layout,null);
                HtmlTextView htmlTextView = (HtmlTextView)stepView.findViewById(R.id.textRoutes);

                if(myRoutes!=null){
                    for (Step step:myRoutes.routes.get(0).legs.get(0).steps){
                        StringBuilder stringBuilder = new StringBuilder(step.html_instructions);
                        htmlTextView.setHtml(stringBuilder.toString(),new HtmlAssetsImageGetter(htmlTextView));
                    }
                }
               builder.setView(stepView);
               builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });
               builder.show();



            }
        });

        myLocationRequest();
        myLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback, Looper.myLooper());
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            finish();
        }else{
            mMap = googleMap;

            mMap.getUiSettings().setZoomControlsEnabled(true);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lastLocation = location;



                    Log.d("bye", "onSuccess: hi");
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                            .title("Your Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


                    currentMarker = mMap.addMarker(markerOptions);


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude())));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                    LatLng destinationLatLung = new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                            Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));


                    mMap.addMarker( new MarkerOptions()
                            .position(destinationLatLung)
                            .title(Common.currentResult.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


                    findPath(lastLocation,Common.currentResult.getGeometry().getLocation());





                }


            });
        }



    }

    private void findPath(Location lastLocation, com.example.sisirkumarnanda.tourist.Model.Location location) {
        if(polyline!=null)
            polyline.remove();

        String origin = new StringBuilder(String.valueOf(lastLocation.getLatitude()))
                .append(",").append(String.valueOf(lastLocation.getLongitude())).toString();

        String destination = new StringBuilder(location.getLat())
                .append(",").append(location.getLng()).toString();


        Log.d("findpath", "findPath: " + origin + " " + destination);

        myGoogleAPIService.getDirections(origin,destination)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        myRoutes = new Gson().fromJson(response.body().toString(),
                                new TypeToken<MyRoutes>(){}.getType());
                        new ParserTask().execute(response.body().toString());


                        if(myRoutes!=null){
                            showTextDirections.setEnabled(true);
                        }
                                            }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

    private void myLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setSmallestDisplacement(10f);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void myLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastLocation = locationResult.getLastLocation();

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude()))
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mMap.clear();
                currentMarker = mMap.addMarker(markerOptions);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                LatLng destinationLatLung = new LatLng(Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLat()),
                        Double.parseDouble(Common.currentResult.getGeometry().getLocation().getLng()));


                mMap.addMarker( new MarkerOptions()
                        .position(destinationLatLung)
                        .title(Common.currentResult.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));


                findPath(lastLocation,Common.currentResult.getGeometry().getLocation());
            }
        };
    }

    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>> {




        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try{
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();
                routes = directionsJSONParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for(List<HashMap<String,String>> path : lists){
                polylineOptions = null;

                points = new ArrayList();
                polylineOptions = new PolylineOptions();



                for(HashMap<String,String> point : path){


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat,lng));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions!=null){
                mMap.addPolyline(polylineOptions);
            }else{
                Toast.makeText(ViewDirectionsRouteActivity.this, "Directions not found", Toast.LENGTH_SHORT).show();
            }



        }
    }


}
