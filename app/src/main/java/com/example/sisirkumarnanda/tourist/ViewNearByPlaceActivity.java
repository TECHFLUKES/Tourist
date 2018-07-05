package com.example.sisirkumarnanda.tourist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sisirkumarnanda.tourist.Model.DetailOfPlace;
import com.example.sisirkumarnanda.tourist.Model.Photos;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewNearByPlaceActivity extends AppCompatActivity {

    ImageView myLocationPhoto;
    MyGoogleAPIService myGoogleAPIService;
    RatingBar ratingBar;
    TextView oHours,placeAddress,placeName;
    Button buttonToGoToMap;
    Button buttonShowRoute;

    DetailOfPlace myPlaceDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_near_by_place);

        initComponents();

        placeAddress.setText("");
        placeName.setText("");
        oHours.setText("");

        buttonToGoToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(myPlaceDetail.getResult().getUrl()));
                startActivity(intent);
            }
        });

        buttonShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewNearByPlaceActivity.this,ViewDirectionsRouteActivity.class);
                startActivity(intent);
            }
        });

        if(Common.currentResult.getPhotos()!=null && Common.currentResult.getPhotos().length>0){

            Picasso.with(this).load(getPhotoPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(myLocationPhoto);
        }
        if(Common.currentResult.getRating()!=null&&!TextUtils.isEmpty(Common.currentResult.getRating())){
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
            
        }
        else{
            ratingBar.setVisibility(View.GONE);
        }

        //Here we get the opening hours
        if(Common.currentResult.getOpening_hours()!=null){
            oHours.setText("Open Now: "+Common.currentResult.getOpening_hours().getOpen_now());

        }
        else{
            oHours.setVisibility(View.GONE);
        }

        //address and name
        myGoogleAPIService.getDetailOfPlace(getPlaceUrl(Common.currentResult.getPlace_id())).enqueue(new Callback<DetailOfPlace>() {
            @Override
            public void onResponse(Call<DetailOfPlace> call, Response<DetailOfPlace> response) {
                myPlaceDetail = response.body();
                placeAddress.setText(myPlaceDetail.getResult().getFormatted_address());
                placeName.setText(myPlaceDetail.getResult().getName());
            }

            @Override
            public void onFailure(Call<DetailOfPlace> call, Throwable t) {

            }
        });





    }

    private String getPlaceUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid=" + place_id);
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();

    }

    private void initComponents() {
        myGoogleAPIService = Common.myGoogleAPIService();


        myLocationPhoto = (ImageView)findViewById(R.id.myLocationPhoto);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        oHours = (TextView)findViewById(R.id.placeOpenHours);
        placeAddress = (TextView)findViewById(R.id.placeAddress);
        placeName = (TextView)findViewById(R.id.placeName);
        buttonToGoToMap = (Button)findViewById(R.id.buttonShowOnMap);
        buttonShowRoute = (Button)findViewById(R.id.buttonViewRoute);
    }


    private String getPhotoPlace(String photoReference,int maxWidth) {
        StringBuilder photoUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        photoUrl.append("?maxwidth=" + maxWidth);
        photoUrl.append("&photoreference=" + photoReference);
        photoUrl.append("&key=" + getResources().getString(R.string.browser_key));
        return photoUrl.toString();

    }
}
