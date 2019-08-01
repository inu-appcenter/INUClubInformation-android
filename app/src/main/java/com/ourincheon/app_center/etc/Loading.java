package com.ourincheon.app_center.etc;

import android.content.Intent;      //Intent를 이용한 Activity 전환
import android.content.SharedPreferences;       //초기 설정값이나 자동로그인 여부 등 간단한 값을 저장하기 위해 사용
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {      //Activity의 상태가 변경될때마다 호출되는, Activity 생명주기(LifeCycle) 메서드,
                                                                        //액티비티가 실행될때 onCreate가 호출되면서 Bundle객체를 전달
                                                                        //Bundle: 동적상태의 데이터를 저장하고 복원하는 클래스, 참조: https://itpangpang.xyz/101
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        Viewpager_main.fragmentPosition = 2;
        ((Viewpager_main) Viewpager_main.mainContext).onResume();

        Intent intent = getIntent();        //메시지 객체, (액티비티, 서비스)등등 사이 데이터 주고받기용
                                            // 페이지 전환과 페이지간 데이터 전달
        loadingText = intent.getStringExtra("listValue");       //데이터 사용 함수
        clubnum = intent.getStringExtra("clubIdNumber");

        textView = (TextView) findViewById(R.id.testhaha);      //main.xml 레이웃에 설정된 뷰들을 가져오는 메소드
                                                        //https://yongku.tistory.com/entry/안드로이드-스튜디오Android-Studio-findViewById

        OpenNext();
    }

    public void ModifyContent(Boolean param){
        Intent openPage;
        if(param == true){
            openPage = new Intent(Loading.this, ModifyPhoto.class);     //Intent: 액티비티 화면 전환
            openPage.putExtra("clubIdNumber", clubnum); //putExtra: 액티비티 값 넘기기
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
        startActivity(intent);      //액티비티 띄우기 위해 사용
        finish();
    }

    public void LogoutGoGo(){
        SharedPreferences savedToken = getSharedPreferences("loginToken", MODE_PRIVATE);
        SharedPreferences.Editor editor = savedToken.edit();
        editor.putString("savedID", "noID");        //"전자"키값으로 "후자"데이터 저장
        editor.putString("savedPW", "noPW");
        editor.commit();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        finish();
        Viewpager_main.viewpagerMain.finish();

    }

    public void ApplicationGoGo(){      //동아리 관리자 설정
        Intent intent = new Intent(Loading.this, MakeApplication.class);
        intent.putExtra("clubIdNumber", clubnum);       //액티비티 이동 + 값넘기기
        startActivity(intent);
        finish();
    }

    public void OpenNext(){         //관리자 설정 페이지뷰
        switch (loadingText){
            case "행사 등록":       //loadingText 변수가 "행사등록"일 경우
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
                textView.setText(loadingText);      //textview _ java에서도 뷰를 만들수도 있다
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
