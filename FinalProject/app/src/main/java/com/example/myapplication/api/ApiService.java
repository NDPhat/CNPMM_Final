package com.example.myapplication.api;
import com.example.myapplication.model.UserRequest;
import com.example.myapplication.model.UserRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
            Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd-HH:mm:ss")
            .create();
    ApiService apiService=new Retrofit.Builder()
            .baseUrl("http://group-31-ccnpmm.me/start/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);


    @POST("signup_function/")
    Call<UserRequest> requestRegisterUser(@Body UserRequest user);


    @POST("login_function/")
    Call<UserRequest> requestLoginUser  (@Body UserRequest user);

}
