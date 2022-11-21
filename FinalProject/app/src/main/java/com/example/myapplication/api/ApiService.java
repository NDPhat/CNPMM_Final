package com.example.myapplication.api;
import com.example.myapplication.model.user;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
            Gson gson=new GsonBuilder()
            .setDateFormat("yyyy-MM-dd-HH:mm:ss")
            .create();
    ApiService apiService=new Retrofit.Builder()
            .baseUrl("http://52.8.255.180:5000/start/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);
    @POST("recognize_function/")
    Call<user> requestImageUser (@Body user user);
}
