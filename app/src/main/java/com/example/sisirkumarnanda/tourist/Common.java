package com.example.sisirkumarnanda.tourist;

import com.example.sisirkumarnanda.tourist.Model.MyPlaces;
import com.example.sisirkumarnanda.tourist.Model.Results;
import com.example.sisirkumarnanda.tourist.RemoteServices.MyGoogleAPIService;
import com.example.sisirkumarnanda.tourist.RemoteServices.RetroClient;
import com.example.sisirkumarnanda.tourist.RemoteServices.RetroScalarClient;

/**
 * Created by SISIR KUMAR NANDA on 02-07-2018.
 */

public class Common {
    private static String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static Results currentResult;
    public static Results[] currentTopPlaceResult;

    public static MyGoogleAPIService myGoogleAPIService(){
        return RetroClient.getClient(GOOGLE_API_URL).create(MyGoogleAPIService.class);
    }

    public static MyGoogleAPIService myGoogleAPIServiceScalars(){
        return RetroScalarClient.getScalarClient(GOOGLE_API_URL).create(MyGoogleAPIService.class);
    }
}
