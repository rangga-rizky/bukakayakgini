package com.example.ranggarizky.bukakayakgini.util;


import com.example.ranggarizky.bukakayakgini.model.BarangBukaLapak;
import com.example.ranggarizky.bukakayakgini.model.ResponseCategory;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ranggarizky on 6/4/2016.
 */
public interface API {
    //variable base URL
    public static String baseURL = "https://api.bukalapak.com/v2/";

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    Retrofit client = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @POST("authenticate.json")
    public Call<User> login(@Header("Authorization")String token);

    @GET("products.json")
    public Call<ResponseObject> getProduk(@Query("keywords") String keywords,
                                          @Query("page")String page,
                                          @Query("per_page")String per_page);

    @GET("categories.json")
    public Call<ResponseCategory> getCategories();

    @GET("users/info.json")
    public Call<ResponseObject> getUser(@Header("Authorization")String token);

    @GET("users/{user_id}/profile.json")
    public Call<ResponseObject> getUserbyID( @Path("user_id") String user_id);

    @GET("products/{product_id}")
    public Call<ResponseObject> getProdukbyID( @Path("product_id") String product_id);

    @POST("carts/add_product/{product_id}")
    public Call<ResponseObject> addToCart(@Header("Authorization")String token, @Path("product_id") String product_id, @Body BarangBukaLapak produk);

    @GET("products/mylapak.json")
    public Call<ResponseObject> getMyProduct(@Header("Authorization")String token);

    @GET("products/mylapak.json")
    public Call<ResponseObject> getMyProduct(@Header("Authorization")String token,@Query("keywords") String keywords );




}