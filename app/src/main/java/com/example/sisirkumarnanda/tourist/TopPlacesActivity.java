package com.example.sisirkumarnanda.tourist;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_places);

        recyclerView = (RecyclerView)findViewById(R.id.myRecyclerView);
        items = new ArrayList<String>();
        photoReference = new ArrayList<String>();
        myGoogleAPIService = Common.myGoogleAPIService();

        myGoogleAPIService.getDetailsOfTopPlaces(getStateUrl()).enqueue(new Callback<DetailsOfTopPlaces>() {
            @Override
            public void onResponse(Call<DetailsOfTopPlaces> call, Response<DetailsOfTopPlaces> response) {

                for(int i = 0;i<response.body().getResults().length;i++){
                    items.add(response.body().getResults()[i].getName());

                    for(int j = 0;j<response.body().getResults()[i].getPhotos().length;j++){
                        photoReference.add(response.body().getResults()[i].getPhotos()[j].getPhoto_reference());

                    }

                }




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


        //Log.d(TAG, "onCreate photo: " + photoReference);








    }



    private String getStateUrl(){
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?query=rajasthan+point of interest&language=en");
        url.append("&key=" + getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
