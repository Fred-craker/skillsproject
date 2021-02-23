package com.example.xpro.Retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitInterface {
    @GET("v6/19251cbe1533d02f564495cc/latest/{currency}")
    Call<JsonObject> getExchangeCurrency(@Path("currency") String currency);
}
