package com.ourincheon.app_center.application;

import android.util.Log;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements emf {
    //Interceptor의 경우 "로그인 처리"에 이용, 스프링내의 모든 객체에 접근이 가능.
    //순서를 가로채서 먼저 수행 ->중복코드 감소

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        SharedPreferenceBase sharedPreferenceBase = new SharedPreferenceBase();

        // Preference에서 cookies를 가져오는 작업을 수행
        Set<String> preferences =  sharedPreferenceBase.getSharedPreference(CookieSave.SHARED_PREFERENCE_NAME_COOKIE, new HashSet<String>());

        for (String cookie : preferences) { //preferences에서 꺼낸 객체를 cookie에 넣는다. 꺼낼 객체가 없을 때 까지!
            builder.addHeader("cookie", cookie);
            Log.d("쿠키", "쿠키 추가 성공");
        }



        return chain.proceed(builder.build());
    }
}
