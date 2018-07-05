package com.example.sisirkumarnanda.tourist.RemoteServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by SISIR KUMAR NANDA on 04-07-2018.
 */

public class RetroScalarClient {
    private static Retrofit retrofit = null;
    public static Retrofit getScalarClient(String baseUrl){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
