package com.sayur.tobos.cart;

import com.sayur.tobos.utils.ApiRespond;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCart {
    @GET("cart/{user_id}")
    Call<Carts> getCart(@Path("user_id") String user_id);

    @POST("cart/{user_id}")
    Call<SingleCart> addCart(@Path("user_id") String user_id, @Body HashMap<String, Object> data);

    @POST("cart/up")
    Call<ApiRespond> upQty(@Body HashMap<String, Object> data);

    @POST("cart/del")
    Call<ApiRespond> delQty(@Body HashMap<String, Object> data);
}
