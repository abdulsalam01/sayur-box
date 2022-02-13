package com.sayur.tobos.product;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiProduct {
    @GET("product/d/{user_id}/{slug}")
    Call<SingleProduct> getProductBy(@Path("user_id") String user_id, @Path("slug") String slug);

    @GET("product/{type}/{user_id}/{slug}")
    Call<Product> getProductByType(@Path("type") String type, @Path("user_id") String user_id, @Path("slug") String slug);
}
