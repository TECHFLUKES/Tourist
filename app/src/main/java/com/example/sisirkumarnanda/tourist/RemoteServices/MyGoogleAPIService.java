package com.example.sisirkumarnanda.tourist.RemoteServices;

import com.example.sisirkumarnanda.tourist.Model.DetailOfPlace;
import com.example.sisirkumarnanda.tourist.Model.DetailsOfTopPlaces;
import com.example.sisirkumarnanda.tourist.Model.MyPlaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by SISIR KUMAR NANDA on 02-07-2018.
 */

public interface MyGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);

    @GET
    Call<DetailOfPlace> getDetailOfPlace(@Url String url);

    @GET
    Call<DetailsOfTopPlaces> getDetailsOfTopPlaces(@Url String url);

    @GET("maps/api/directions/json")
    Call<String> getDirections(@Query("origin") String origin,@Query("destination") String destination);
}
