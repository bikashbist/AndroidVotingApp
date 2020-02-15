package com.example.bikashvoting.Apis;

import com.example.bikashvoting.response.ImageResponse;
import com.example.bikashvoting.response.LoginResponse;
import com.example.bikashvoting.response.User;
import com.example.bikashvoting.response.VoteResponse;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserApi {
    //login
    @Headers("Content-Type: application/json")
    @POST("users/login")
    Call<LoginResponse> checkUser(@Body User user);

    //userProfile
    @GET("users/loggedUserDetails")
    Call<User> getUser(@Header("Authorization") String token);

    //imageupload
    @Multipart
    @POST("proImageUpload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    //vote
    @PUT("users/vote/{id}")
    Call<VoteResponse> vote(@Path("id") String id, @Header("Authorization") String token);

    //candidateUserList
    @GET("users/candidateUser/")
    Call<List<User>> getUserList(@Header("Authorization") String token);


    //deletecandidateuser
    @DELETE("users/candidateUser")
    Call<User> deleteUser(@Header("Authorization") String token);

    @PUT("users/candidateUser")
    Call<User> updateUser(@Header("Authorization") String token,@Body User user);

    //signup
    @POST("users/signup")
    Call<Void> registerUser(@Body User user);
}
