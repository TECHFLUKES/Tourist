package com.example.sisirkumarnanda.tourist;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.Model.MyPlaces;
import com.example.sisirkumarnanda.tourist.Model.Results;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Currency;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int PERMISSION_CODE = 1000;
    private GoogleMap mMap;

    private Button findplace;
    private double latitude;
    private double longitude;
    private Location lastLocation;
    private Marker mMarker;
    MyGoogleAPIService myGoogleAPIService;
    MyPlaces currentPlace;

    //Here we get the new location o
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    private LocationRequest mLocationRequest;

    //private Button locationSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myGoogleAPIService = Common.myGoogleAPIService();

        //locationSelector = (Button)findViewById(R.id.locationSelector);
        findplace = findViewById(R.id.findPlace);

        //Now to request runtime permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            checkingLocationPermission();
            Log.e("Hello", "Darling");
        }

        //Below code was for the recycler view

//        Intent intentThisActivity = getIntent();
//        final String nearByLocation = getIntent().getExtras().getString("nearByLocation");
//        locationSelector.setText(" Click to get the  nearest " + nearByLocation);
//        Toast.makeText(this, nearByLocation, Toast.LENGTH_SHORT).show();
//
//        locationSelector.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nearByPlace(nearByLocation);
//            }
//        });


        //Below code is to setup the bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.myBottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_hospital:
                        nearByPlace("hospital");
                        break;
                    case R.id.action_hotel:
                        nearByPlace("lodging");
                        break;
                    case R.id.action_school:
                        nearByPlace("school");
                        break;
                    case R.id.action_market:
                        nearByPlace("shopping_mall");
                        break;
                    case R.id.action_restaurant:
                        nearByPlace("restaurant");
                        break;
                        default:
                            break;
                }
                return true;
            }
        });

        myLocationRequest();
        myLocationCallback();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

        findplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    findPlace(v);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



    }

    @Override
    protected void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();

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

                if(mMarker!=null)
                    mMarker.remove();

                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();

                Log.d("Hello", "onLocationChanged: " + latitude);

                LatLng latLng = new LatLng(latitude,longitude);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title("Your Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                mMarker = mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            }
        };
    }

    private void nearByPlace(final String typeOfPlace) {
        mMap.clear();
        Log.d("Hello", "onLocationChanged: " + latitude);
        String url = getUrl(latitude,longitude,typeOfPlace);
        myGoogleAPIService.getNearByPlaces(url).enqueue(new Callback<MyPlaces>() {
            @Override
            public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {
                currentPlace = response.body();
                if(response.isSuccessful()){
                    for(int i = 0;i<response.body().getResults().length;i++){
                        MarkerOptions markerOptions = new MarkerOptions();
                        Results gogPlace = response.body().getResults()[i];
                        double lati = Double.parseDouble(gogPlace.getGeometry().getLocation().getLat());
                        double longi = Double.parseDouble(gogPlace.getGeometry().getLocation().getLng());
                        String placeName = gogPlace.getName();
                        String vicinity = gogPlace.getVicinity();
                        LatLng latLng = new LatLng(lati,longi);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName);
                        if(typeOfPlace.equals("hospital"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hospital));
                        else if(typeOfPlace.equals("shopping_mall"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_market));
                        else if(typeOfPlace.equals("restaurant"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_resturant));
                        else if(typeOfPlace.equals("school"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_school));
                        else if(typeOfPlace.equals("lodging"))
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hotel));
                        else
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                        markerOptions.snippet(String.valueOf(i)); //Assigning the index for the marker

                        mMap.addMarker(markerOptions);
                        //moving camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));


                    }
                }
            }

            @Override
            public void onFailure(Call<MyPlaces> call, Throwable t) {

            }
        });
    }

    private String getUrl(double latitude, double longitude, String typeOfPlace) {
       StringBuilder gogPlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
       gogPlacesUrl.append("location="+latitude+","+longitude);
       gogPlacesUrl.append("&radius="+10000);
       gogPlacesUrl.append("&type="+typeOfPlace);
       gogPlacesUrl.append("&sensor=true");
       gogPlacesUrl.append("&key="+getResources().getString(R.string.browser_key));
        Log.d("locationUrl",gogPlacesUrl.toString());
        return gogPlacesUrl.toString();
    }

    private boolean checkingLocationPermission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
           }else{
               ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_CODE);
           }
           return false;
        }
        else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:
            {
                if(grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                            mMap.setMyLocationEnabled(true);
                        myLocationRequest();
                        myLocationCallback();

                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
                        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,locationCallback, Looper.myLooper());

                    }
                }

            }
            break;
        }
    }
    public void findPlace (View v) throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
        startActivityForResult(intent,0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode){
            case 0:
                if(resultCode==RESULT_OK)
                {

                    Place place = PlaceAutocomplete.getPlace(getApplicationContext(), data);

                    //Log.e("Places", place.getLatLng().toString());
                    LatLng latLng = place.getLatLng();
                    Double destlatitue = latLng.latitude;
                    Double destlongitude = latLng.longitude;
                    String destLat = Double.toString(destlatitue);
                    String destLong = Double.toString(destlongitude);
                    //Log.e("Long",destLat+destLong);
                    Intent intent = new Intent(this,ShowRoute.class);
                    String srcLat = Double.toString(latitude);
                    String srcLong = Double.toString(longitude);

                    intent.putExtra("DestLat",destLat);
                    intent.putExtra("DestLong",destLong);
                    Log.e("destLat",destLat+','+destLong);

                    intent.putExtra("srcLat",srcLat);
                    intent.putExtra("srcLong",srcLong);
                    Log.e("srcLat",srcLat+','+srcLong);
                    startActivity(intent);



                }else if(resultCode==PlaceAutocomplete.RESULT_ERROR)
                {
                    Log.e("COmming","Here");
                    Status status = PlaceAutocomplete.getStatus(getApplicationContext(),data);
                    Log.e("Error",status.getStatusMessage());
                }

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       //We have to initialize google play services
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                mMap.setMyLocationEnabled(true);
            }
        }
        else{

            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getSnippet() != null) {


                    //what we do is when the user clicks on a marker we get the place information result and store it in the currentResult
                    Common.currentResult = currentPlace.getResults()[Integer.parseInt(marker.getSnippet())];
                    startActivity(new Intent(MapsActivity.this, ViewNearByPlaceActivity.class));
                }
                return true;
            }
        });

    }









}
