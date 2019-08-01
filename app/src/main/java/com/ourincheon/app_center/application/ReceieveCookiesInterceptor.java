package com.ourincheon.app_center.application;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceieveCookiesInterceptor implements Interceptor {        //Intercepter 객체를 생성, Set-Cookie에 접근, 헤더 빼오기

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase();

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();

            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            // Preference에 cookies를 넣어주는 작업을 수행
            sharedPreferenceBase.putSharedPreference(CookieSave.SHARED_PREFERENCE_NAME_COOKIE, cookies);
            Log.d("쿠키", "쿠키 받기 완료");


        }

        return originalResponse;
    }
}