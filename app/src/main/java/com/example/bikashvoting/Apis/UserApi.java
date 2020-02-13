package com.example.bikashvoting.Apis;

import com.example.bikashvoting.response.LoginResponse;
import com.example.bikashvoting.response.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UserApi {
    //login
    @Headers("Content-Type: application/json")
    @POST("users/login")
    Call<LoginResponse> checkUser(@Body User user);
}
