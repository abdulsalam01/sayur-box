package com.sayur.tobos.users;

import com.sayur.tobos.utils.ApiRespond;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiUsers {
    @Headers("Content-Type: application/json")
    @POST("login")
    Call<Users> login(@Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("daftar")
    Call<Users> daftar(@Body HashMap<String, Object> data);

    @Headers("Content-Type: application/json")
    @POST("profile/{user_id}")
    Call<ApiRespond> updateProfile(@Path ("user_id") String user_id, @Body Users.Data data);
}
