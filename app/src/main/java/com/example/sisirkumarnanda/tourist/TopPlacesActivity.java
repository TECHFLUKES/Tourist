package com.example.sisirkumarnanda.tourist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.Adapters.RecyclerViewAdapter;
import com.example.sisirkumarnanda.tourist.Model.DetailOfPlace;
import com.example.sisirkumarnanda.tourist.Model.DetailsOfTopPlaces;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopPlacesActivity extends AppCompatActivity {
    private static final String TAG = "TopPlacesActivity";

    RecyclerView recyclerView;
    ArrayList<String> items;
    ArrayList<String> photoReference;
    MyGoogleAPIService myGoogleAPIService;
    DetailsOfTopPlaces detailsOfTopPlaces;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_places);

        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        items = new ArrayList<String>();
        photoReference = new ArrayList<String>();
        myGoogleAPIService = Common.myGoogleAPIService();

        Intent thisActivityIntent = getIntent();
        String category = thisActivityIntent.getExtras().getString("discover");
        String query = "";

        switch(category){
            case "palaceCard":
                query = "palace";
                break;
            case "wildLifeCard":
                query = "wildlife";
                break;
            case "templeCard":
                query = "temple";
                break;
            case "museumCard":
                query = "museum";
                break;
            case "lakeCard":
                query = "lake";
                break;
            case "fortCard":
                query = "fort";
                break;
            case "placeofintrest":
                    query = "place of intrest";
                    break;
            default:
                break;


        }

        myGoogleAPIService.getDetailsOfTopPlaces(getStateUrl(query)).enqueue(new Callback<DetailsOfTopPlaces>() {
            @Override
            public void onResponse(Call<DetailsOfTopPlaces> call, Response<DetailsOfTopPlaces> response) {
                detailsOfTopPlaces = response.body();


                for(int i = 0;i<response.body().getResults().length;i++){
                    items.add(response.body().getResults()[i].getName());



                        for (int j = 0; j < response.body().getResults()[i].getPhotos().length; j++) {
                            photoReference.add(response.body().getResults()[i].getPhotos()[j].getPhoto_reference());

                        }
                    }



                Common.currentTopPlaceResult = detailsOfTopPlaces.getResults();





                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(TopPlacesActivity.this,items,photoReference);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TopPlacesActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(recyclerViewAdapter);

            }



            @Override
            public void onFailure(Call<DetailsOfTopPlaces> call, Throwable t) {
                Toast.makeText(TopPlacesActivity.this, "Could not fetch data", Toast.LENGTH_SHORT).show();

            }
        });
    }





    private String getStateUrl(String category){

        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=rajasthan");
        url.append("+"+category);
        url.append("&language=en");
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
