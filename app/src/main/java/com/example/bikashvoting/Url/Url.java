package com.example.bikashvoting.Url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Url {
    //token generated during login is set here
    public static String token = "Bearer ";

    //backend hosted path
    public static final String backend_URL = "http://10.0.2.2:3020/";

    //profile Image path
    public static String uploads = backend_URL +"proImage/";
    //post Image path


    //to bind the response from backend
    public static Retrofit getInstance() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.backend_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
