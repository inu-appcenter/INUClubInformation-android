package com.ourincheon.app_center.application;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase();

        // Preference에서 cookies를 가져오는 작업을 수행
        Set<String> preferences =  sharedPreferenceBase.getSharedPreference(CookieSave.SHARED_PREFERENCE_NAME_COOKIE, new HashSet<String>());

        for (String cookie : preferences) {
            builder.addHeader("cookie", cookie);
            Log.d("쿠키", "쿠키 추가 성공");
        }



        return chain.proceed(builder.build());
    }
}
