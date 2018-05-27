package com.example.ranggarizky.bukakayakgini.util;


import com.example.ranggarizky.bukakayakgini.model.Join;
import com.example.ranggarizky.bukakayakgini.model.Kategori;
import com.example.ranggarizky.bukakayakgini.model.RequestObject;
import com.example.ranggarizky.bukakayakgini.model.ResponseApi;
import com.example.ranggarizky.bukakayakgini.model.ResponseCategory;
import com.example.ranggarizky.bukakayakgini.model.ResponseCity;
import com.example.ranggarizky.bukakayakgini.model.ResponseJoin;
import com.example.ranggarizky.bukakayakgini.model.ResponseKategori;
import com.example.ranggarizky.bukakayakgini.model.ResponseNotif;
import com.example.ranggarizky.bukakayakgini.model.ResponseObject;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaan;
import com.example.ranggarizky.bukakayakgini.model.ResponsePermintaanSingle;
import com.example.ranggarizky.bukakayakgini.model.ResponseProfile;
import com.example.ranggarizky.bukakayakgini.model.ResponseProvinsi;
import com.example.ranggarizky.bukakayakgini.model.ResponseSingleTawaran;
import com.example.ranggarizky.bukakayakgini.model.ResponseTawaran;
import com.example.ranggarizky.bukakayakgini.model.Tawaran;
import com.example.ranggarizky.bukakayakgini.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by ranggarizky on 6/4/2016.
 */
public interface WEB_API {
    //variable base URL
    public static String baseURL = "https://bl.rendrahutama.my.id/";

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


    @Multipart
    @POST("demand/store")
    public Call<ResponseApi> storePermintaan( @Part("id_user") RequestBody id_user,
                                               @Part("nama") RequestBody nama,
                                               @Part("deskripsi") RequestBody deskripsi,
                                               @Part("harga") RequestBody harga,
                                               @Part("kategori") RequestBody kategori,
                                               @Part("secret") RequestBody secret,
                                               @Part("kondisi") RequestBody kondisi,
                                               @Part("jumlah") RequestBody jumlah,
                                               @Part MultipartBody.Part file);

    @Multipart
    @POST("demand/update")
    public Call<ResponseApi> updatePermintaan( @Part("id") RequestBody id,
                                              @Part("deskripsi") RequestBody deskripsi,
                                              @Part("harga") RequestBody harga,
                                              @Part("kategori") RequestBody kategori,
                                              @Part("secret") RequestBody secret,
                                              @Part("kondisi") RequestBody kondisi,
                                              @Part("jumlah") RequestBody jumlah,
                                              @Part MultipartBody.Part file);

    @Multipart
    @POST("demand/storeCopy")
    public Call<ResponseApi> copyPermintaan(  @Part("id_buyer") RequestBody  id_buyer ,
                                               @Part("id_asli") RequestBody id_asli,
                                              @Part("nama") RequestBody nama ,
                                               @Part("deskripsi") RequestBody deskripsi,
                                               @Part("harga") RequestBody harga,
                                               @Part("kategori") RequestBody kategori,
                                               @Part("secret") RequestBody secret,
                                               @Part("kondisi") RequestBody kondisi,
                                               @Part("jumlah") RequestBody jumlah,
                                               @Part MultipartBody.Part file);




    @POST("supply/bid")
    public Call<ResponseApi> tawarkan( @Body Tawaran tawaran);

    @POST("demand/join")
    public Call<ResponseApi> join( @Body Join join);


    @GET("demand/fetch")
    public Call<ResponsePermintaanSingle> getPermintaanbyID(@Query("id") String id,
                                                          @Query("secret") String secret,
                                                            @Query("showSupplies") String showSupplies);

    @GET("demand/fetch")
    public Call<ResponsePermintaanSingle> getPermintaanbyID(@Query("id") String id,
                                                            @Query("id_user") String id_user,
                                                            @Query("secret") String secret,
                                                            @Query("showSupplies") String showSupplies);

    @GET("demand/fetchByUser")
    public Call<ResponsePermintaan> getPermintaanku(@Query("id_user") String user_id,
                                                    @Query("secret") String secret,
                                                    @Query("showSupplies") String showSupplies,
                                                    @Query("limit") String limit);

    @GET("demand/fetchByUser")
    public Call<ResponsePermintaan> getPermintaanku(@Query("status") String status,
                                                    @Query("id_user") String user_id,
                                                    @Query("secret") String secret,
                                                    @Query("showSupplies") String showSupplies,
                                                    @Query("limit") String limit);



    @GET("demand/fetchJoinByDemand")
    public Call<ResponseJoin> getAllUserJoin( @Query("id_demand") String id_demand,
                                              @Query("status") String status,
                                              @Query("secret") String secret
                                            );

    @GET("user/fetchStats")
    public Call<ResponseProfile> getuserStat(@Query("id_user") String id_user,
                                             @Query("secret") String secret
    );

    @GET("demand/fetchAll")
    public Call<ResponsePermintaan> getAllPermintaan(@Query("secret") String secret,
                                                     @Query("status ") String status ,
                                                     @Query("showSupplies") String showSupplies,
                                                     @Query("limit") String limit);


    @GET("demand/search")
    public Call<ResponsePermintaan> searchDemand(@Query("keyword") String keyword,
                                                 @Query("secret") String secret,
                                                 @Query("showSupplies") String showSupplies,
                                                 @Query("limit") String limit);

    @GET("demand/fetchBySeller")
    public Call<ResponsePermintaan> fetchInterestRequest(@Query("id_seller") String id_seller,
                                                 @Query("secret") String secret,
                                                 @Query("showSupplies") String showSupplies,
                                                 @Query("limit") String limit);

    @GET("demand/fetchTrending")
    public Call<ResponsePermintaan> fetchTrending( @Query("secret") String secret,
                                                         @Query("limit") String limit);

    @GET("demand/fetchChild")
    public Call<ResponsePermintaan> fetchChild( @Query("id_demand") String id_demand,
                                                @Query("secret") String secret,
                                                @Query("showSupplies") String showSupplies,
                                                   @Query("limit") String limit);

    @GET("demand/search")
    public Call<ResponsePermintaan> searchDemand(@Query("keyword") String keyword,
                                                 @Query("secret") String secret,
                                                 @Query("showSupplies") String showSupplies,
                                                 @Query("limit") String limit,
                                                 @Query("id_user") String id_user);

    @GET("supply/fetchByDemand")
    public Call<ResponseTawaran> fetchSupplyByDemand(@Query("id_demand") String id_request,
                                                     @Query("secret") String secret,
                                                     @Query("limit") String limit);

    @GET("supply/fetchByDemand")
    public Call<ResponseTawaran> fetchSupplySelected(@Query("id_demand") String id_request,
                                                     @Query("secret") String secret,
                                                     @Query("status") String status);

    @GET("supply/fetchByDemand")
    public Call<ResponseTawaran> fetchSupplyByDemand(@Query("id_demand") String id_request,
                                                     @Query("secret") String secret,
                                                     @Query("limit") String limit,
                                                     @Query("sortBy") String sortBy);

    @GET("supply/fetchByDemand")
    public Call<ResponseTawaran> fetchSupplyByFilter(@Query("id_demand") String id_request,
                                                     @Query("secret") String secret,
                                                     @Query("limit") String limit,
                                                     @Query("filterByKota") String filterByKota ,
                                                     @Query("filterByKondisi") String filterByKondisi ,
                                                     @Query("filterByTandai") String filterByTandai);

    @GET("supply/fetchBySellerGrouped")
    public Call<ResponsePermintaan> fetchmySupplies(@Query("id_seller") String id_seller,
                                                     @Query("secret") String secret);

    @GET("location/province")
    public Call<ResponseProvinsi> getProvince(@Query("secret") String secret);

    @GET("notif/fetchByUser")
    public Call<ResponseNotif> getNotif(@Query("id_user") String id_user,
                                        @Query("secret") String secret);

    @GET("notif/countByUser")
    public Call<ResponseNotif> countNotReadedNotif(@Query("id_user") String id_user,
                                                   @Query("status") String status,
                                            @Query("secret") String secret);

    @FormUrlEncoded
    @POST("notif/updateToRead")
    public Call<ResponseApi> updateNotif(@Field("id_user") String id_user,
                                                   @Field("status") String status,
                                                   @Field("secret") String secret);

    @FormUrlEncoded
    @POST("user/store")
    public Call<ResponseApi> storeUser(@Field("id_bl") String id_bl,
                                         @Field("nama") String nama,
                                       @Field("id_device") String id_device ,
                                       @Field("email") String email,
                                       @Field("username") String username ,
                                         @Field("secret") String secret);

    @GET("location/city")
    public Call<ResponseCity> getCity(@Query("secret") String secret,
                                      @Query("id_provinsi") String id_provinsi);

    @GET("supply/fetchBySeller")
    public Call<ResponseTawaran> fetchmySuppliesbyDemand(@Query("id_seller") String id_seller,
                                                             @Query("secret") String secret,
                                                            @Query("id_demand") String id_demand,
                                                            @Query("limit") String limit);

    @FormUrlEncoded
    @POST("demand/updateStatus")
    public Call<ResponseApi> closeTawaran(@Field("id") String id,
                                          @Field("status") String status,
                                           @Field("secret") String secret);
    @FormUrlEncoded
    @POST("demand/updateJoin")
    public Call<ResponseApi> updateJoin(@Field("id_join") String id_join,
                                          @Field("status") String status,
                                          @Field("secret") String secret);

    @FormUrlEncoded
    @POST("supply/updateStatus")
    public Call<ResponseApi> updateStatusTawaran(@Field("id_supply") String id_supply ,
                                        @Field("status") String status,
                                        @Field("secret") String secret);

    @FormUrlEncoded
    @POST("supply/deal")
    public Call<ResponseApi> deal(@Field("id_supply") String id_supply ,
                                                 @Field("id_buyer") String id_buyer ,
                                                 @Field("secret") String secret);

    @FormUrlEncoded
    @POST("category/subscribe")
    public Call<ResponseApi> subscribe(@Field("id_seller") String id_seller  ,
                                  @Field("kategori") String kategori,
                                  @Field("secret") String secret);

    @FormUrlEncoded
    @POST("category/unsubscribe")
    public Call<ResponseApi> unsubscribe(@Field("id_seller") String id_seller  ,
                                       @Field("kategori") String kategori,
                                       @Field("secret") String secret);

   @GET("category/fetchBySeller")
    public Call<ResponseKategori> getSubscribebySeller(@Query("id_seller") String id_seller  ,
                                                       @Query("secret") String secret);

    @GET("supply/fetch")
    public Call<ResponseSingleTawaran> getSupplybyID(@Query("id_supply") String id_supply  ,
                                                     @Query("secret") String secret);

    @GET("supply/checkTransInBL")
    public Call<ResponseApi> checkTransInBL(@Query("id_buyer") String id_buyer  ,
                                                     @Query("token") String token,
                                                 @Query("secret") String secret);


}