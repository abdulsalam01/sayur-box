package com.sayur.tobos.home;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiHome {
    @GET("slider")
    Call<Slider> getSlider();

    @GET("category")
    Call<Category> getCategory();

    @GET("catalog/{user_id}")
    Call<Catalog> getCatalog(@Path("user_id") String user_id);

    @GET("promo")
    Call<Slider> getPromo();
}
