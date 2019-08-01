package com.ourincheon.app_center.application;

import android.app.Application;

import com.ourincheon.app_center.network.NetworkInterface;

import okhttp3.OkHttpClient;    //okhttp3
import retrofit2.Retrofit;      //retrofit2
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkController extends Application{
    private static NetworkController ourInstance;
    private static String URL = "http://appcenter.us.to:3303/";
    //inuclub.us.to:3303/
    //13.124.254.99:3303/
    //appcenter.us//
    private NetworkInterface networkInterface = null;

    public static String getURL(){
        return URL;
    }

    public static NetworkController getInstance(){
        return ourInstance;
    }


    public NetworkInterface getNetworkInterface() {
        return this.networkInterface;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        ourInstance = this;
        buildService();

    }

    public void buildService(){

        OkHttpClient.Builder okClient = new OkHttpClient.Builder();     //client 객체 만들기
        okClient.interceptors().add(new AddCookiesInterceptor());       //okClient쿠키
        okClient.interceptors().add(new ReceieveCookiesInterceptor());  //okClient쿠키

        this.networkInterface = (NetworkInterface) new Retrofit.Builder()       //client 객체 만들기_설정
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okClient.build())
                .build()
                .create(NetworkInterface.class);
    }
}

