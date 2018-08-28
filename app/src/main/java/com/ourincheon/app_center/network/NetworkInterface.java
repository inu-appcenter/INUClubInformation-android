package com.ourincheon.app_center.network;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.model.ErrorMsgResult;
import com.ourincheon.app_center.model.LoginData;
import com.ourincheon.app_center.model.ModifyClubInfo;
import com.ourincheon.app_center.model.UpdateEvent;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkInterface {

    @GET("club")
    Call<ArrayList<JsonObject>> getAllClub();

    @GET("main")
    Call<ArrayList<JsonObject>> getMainImage();

    @GET("club/search?")
    Call<ArrayList<JsonObject>> getInformation(@Query("keyword") String clubname);

    @GET("/club/info/{clubnum}")
    Call<ArrayList<JsonObject>> getDetailInformation(@Path("clubnum") int num);

    @POST("/club/info/{club}")
    Call<ErrorMsgResult> giveModifiedContents(@Path("club") String num2, @Body ModifyClubInfo modifyClubInfo);

    @POST("user/login")
    Call<String> getLoginInfo(@Body LoginData loginData);

    @Multipart
    @POST("club/image/{clubnum}/{imagenum}")
    Call<ErrorMsgResult> imageUpload(@Path("clubnum") int clubnum, @Path("imagenum") int imagenum,
                                     @Part("userfile") RequestBody userfile, @Part MultipartBody.Part file);

    @GET("event/list/{date}")
    Call<ArrayList<JsonObject>> getTotalEvent(@Path("date") String date);

    @GET("club/event/{clubnum}")
    Call<ArrayList<JsonObject>> getClubEvent(@Path("clubnum") String clubnum);

    @POST("event/new")
    Call<ArrayList<JsonObject>> updateEvent(@Body UpdateEvent updateEvent);

    @POST("event/{eventnum}/edit")
    Call<ArrayList<JsonObject>> editEvent(@Path("eventnum") int eventnum, @Body UpdateEvent updateEvent);

    @POST("event/{eventnum}/delete")
    Call<ErrorMsgResult> deleteEvent(@Path("eventnum") int eventnum);

    @DELETE("club/image/{clubnum}/{imagenum}")
    Call<ErrorMsgResult> deleteImage(@Path("clubnum") int clubnum, @Path("imagenum") int imagenum);




}
