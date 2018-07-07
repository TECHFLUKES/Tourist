package com.example.sisirkumarnanda.tourist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.Model.DetailOfPlace;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTopPlaceDetailsActivity extends AppCompatActivity {
    ImageView locationImageView;
    TextView openingHoursTextView,placeName;
    TextView addressTextView;
    RatingBar ratingBar;
    Button buttonToGoToMap;
    Button buttonShowRoute;
    MyGoogleAPIService myGoogleAPIService;

    DetailOfPlace detailOfPlace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_top_place_details);

        locationImageView = (ImageView)findViewById(R.id.myLocationPhoto);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        openingHoursTextView= (TextView)findViewById(R.id.placeOpenHours);
        addressTextView = (TextView)findViewById(R.id.placeAddress);
        placeName = (TextView)findViewById(R.id.placeName);
        buttonToGoToMap = (Button)findViewById(R.id.buttonShowOnMap);
        buttonShowRoute = (Button)findViewById(R.id.buttonViewRoute);



        myGoogleAPIService = Common.myGoogleAPIService();

        buttonShowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewTopPlaceDetailsActivity.this,ViewDirectionsRouteActivity.class));
            }
        });

        Intent intentThisActivity = getIntent();

        if(intentThisActivity.hasExtra("rating")){
            String name = intentThisActivity.getExtras().getString("name");
            String rating = intentThisActivity.getExtras().getString("rating");
            String placeId = intentThisActivity.getExtras().getString("place_id");
            String opening = intentThisActivity.getExtras().getString("opening_hours");
            String photoAddress = intentThisActivity.getExtras().getString("photoAddress");

            myGoogleAPIService.getDetailOfPlace(getPlaceUrl(placeId)).enqueue(new Callback<DetailOfPlace>() {
                @Override
                public void onResponse(Call<DetailOfPlace> call, Response<DetailOfPlace> response) {
                    detailOfPlace = response.body();
                    addressTextView.setText(detailOfPlace.getResult().getFormatted_address());
                }

                @Override
                public void onFailure(Call<DetailOfPlace> call, Throwable t) {

                }
            });

            placeName.setText(name);
            openingHoursTextView.setText(opening);

            Picasso.with(this).load(photoAddress).placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_outline_black_24dp)
                    .into(locationImageView);

            ratingBar.setRating(Float.parseFloat(rating));





        }






    }

    private String getPlaceUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid=" + place_id);
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();

    }
}
