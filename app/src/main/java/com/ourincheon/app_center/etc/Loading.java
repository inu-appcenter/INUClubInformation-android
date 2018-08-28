package com.ourincheon.app_center.etc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ourincheon.app_center.R;
import com.ourincheon.app_center.mainActivity.Login;
import com.ourincheon.app_center.mainActivity.Setting.Application.MakeApplication;
import com.ourincheon.app_center.mainActivity.Setting.ModifyClubInformation.ModifyPhoto;
import com.ourincheon.app_center.mainActivity.Setting.ModifyClubInformation.ModifyText;
import com.ourincheon.app_center.mainActivity.Setting.ModifyEvent.Event;
import com.ourincheon.app_center.mainActivity.Setting.ModifyEvent.Event_edit;
import com.ourincheon.app_center.mainActivity.Viewpager_main;

public class Loading extends AppCompatActivity {

    String loadingText;
    String clubnum;
    TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        Viewpager_main.fragmentPosition = 2;
        ((Viewpager_main) Viewpager_main.mainContext).onResume();

        Intent intent = getIntent();
        loadingText = intent.getStringExtra("listValue");
        clubnum = intent.getStringExtra("clubIdNumber");

        textView = (TextView) findViewById(R.id.testhaha);

        OpenNext();
    }

    public void ModifyContent(Boolean param){
        Intent openPage;
        if(param == true){
            openPage = new Intent(Loading.this, ModifyPhoto.class);
            openPage.putExtra("clubIdNumber", clubnum);
        }
        else{
            openPage = new Intent(Loading.this, ModifyText.class);
            openPage.putExtra("clubIdNumber", clubnum);
        }
        startActivity(openPage);
        finish();
    }

    public void ModifyText(Boolean param2){
        Intent openPage;
        if(param2 == true){
            openPage = new Intent(Loading.this, Event.class);
        }
        else{
            openPage = new Intent(Loading.this, Event_edit.class);
            openPage.putExtra("clubIdNumber", clubnum);
        }
        startActivity(openPage);
        finish();
    }

    public void LoginGoGo(){
        Intent intent = new Intent(Loading.this, Login.class);
        intent.putExtra("fromLoading", 1);
        startActivity(intent);
        finish();
    }

    public void LogoutGoGo(){
        SharedPreferences savedToken = getSharedPreferences("loginToken", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedToken.edit();
        editor.putString("savedID", "noID");
        editor.putString("savedPW", "noPW");
        editor.commit();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
        Viewpager_main.viewpagerMain.finish();

    }

    public void ApplicationGoGo(){
        Intent intent = new Intent(Loading.this, MakeApplication.class);
        intent.putExtra("clubIdNumber", clubnum);
        startActivity(intent);
        finish();
    }

    public void OpenNext(){
        switch (loadingText){
            case "행사 등록":
                ModifyText(true);
                break;

            case "행사 수정":
                ModifyText(false);
                break;

            case "동아리 내용 수정":
                ModifyContent(false);
                break;

            case "동아리 사진 수정":
                ModifyContent(true);
                break;

            case "온라인 신청서 만들기":
                ApplicationGoGo();
                break;


            case "로그인":
                LoginGoGo();
                textView.setText(loadingText);
                break;

            case "로그아웃":
                LogoutGoGo();
                break;


            default:
                textView.setText("잘못된 접속입니다.");
                break;
        }
    }

}
