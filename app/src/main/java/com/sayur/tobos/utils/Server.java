package com.sayur.tobos.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {
    private static Retrofit retrofit = null;

    public Retrofit init(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constraint.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
