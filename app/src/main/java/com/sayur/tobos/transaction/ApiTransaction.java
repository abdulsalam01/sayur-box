package com.sayur.tobos.transaction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiTransaction {
    @GET("transaction/type/{user_id}/{status}")
    Call<Transaction> getTransaction(@Path("user_id") String user_id, @Path("status") String status);

    @GET("transaction/d/{id}")
    Call<SingleTransaction> getDetailTransaction(@Path("id") String id);
}
