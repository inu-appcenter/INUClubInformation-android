package com.ourincheon.app_center.etc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.mainActivity.Login;
import com.ourincheon.app_center.mainActivity.Viewpager_main;
import com.ourincheon.app_center.model.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {

    String existed_id; //저장된 아이디
    String existed_pw; //저장된 패스워드
    String club_num; //클럽 넘버
    LoginData loginData; //로그인 데이터(ID,PW)

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences savedToken = getSharedPreferences("loginToken", MODE_PRIVATE);
        existed_id = savedToken.getString("savedID", "noID");
        existed_pw = savedToken.getString("savedPW", "noPW");

        //로그아웃 시 불러오는 값
        if(existed_id == "noID" && existed_pw =="noPW"){
            WaitingTime(0);
        }

        //자동 로그인 시 loginData를 이용해 자동로그인
        else{
            loginData = new LoginData(existed_id, existed_pw);
            LoginMethod();
        }
    }

    //스플래시 애니메이션 처리
    private void WaitingTime(int whereWeGoing) {
        Thread SplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int wait = 0;
                    while (wait < 1000) {
                        sleep(100);
                        wait += 100;
                    }
                    WhichPage(whereWeGoing);
                    finish();
                } catch (InterruptedException e) {

                }
            }

        };
        SplashThread.start();
    }


    //액티비티 전환
    public void WhichPage(int checked){
        Intent intent;
        switch (checked){

            //비로그인 상태
            case 0:
                intent = new Intent(SplashActivity.this, Permission.class);
                startActivity(intent);
                break;

            //로그인 상태
            case 1:
                intent = new Intent(SplashActivity.this, Viewpager_main.class);
                intent.putExtra("clubIdNumber", club_num);
                startActivity(intent);
                break;
        }

    }

    //저장된 ID, PW를 이용한 자동 로그인 처리 메소드
    public void LoginMethod(){
        Call<String> call = NetworkController.getInstance().getNetworkInterface().getLoginInfo(loginData);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //저장된 정보가 정상적으로 로그인 됬을 시
                if(response.isSuccessful()){
                    club_num = response.body().toString();
                    WaitingTime(1);
                }

                //세션만료, 정보 변경으로 인한 로그인 불가 시
                else{
                    WaitingTime(0);

                }

                //201성공
                //406실패
                String code = String.valueOf(response.code());
                Log.d("로그인 코드", code);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("로그인 코드", "로그인 실패");

            }
        });
    }

}