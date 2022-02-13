package com.sayur.tobos.checkout;

import com.sayur.tobos.utils.ApiRespond;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCheckout {
    @GET("waktu")
    Call<Waktu> getWaktu();

    @Headers("Content-Type: application/json")
    @POST("transaction/{user_id}")
    Call<ApiRespond> transaction(@Path("user_id") String user_id, @Body HashMap<String, Object> data);
}
