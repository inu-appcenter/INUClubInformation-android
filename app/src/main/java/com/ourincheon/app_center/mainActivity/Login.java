package com.ourincheon.app_center.mainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.model.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    TextView textView; //로그인 실패 시 실패문구 적용 textview
    EditText id_Info; //아이디 edit
    EditText pw_Info; //패스워드 edit
    Button button; //로그인 버튼

    LinearLayout layout; //R.layout.activity_login

    LoginData loginData; //로그인 데이터

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_Info = (EditText) findViewById(R.id.idInfo);
        pw_Info = (EditText) findViewById(R.id.pwInfo);
        button = (Button) findViewById(R.id.Blogin);
        textView = (TextView) findViewById(R.id.resultF);
        layout = (LinearLayout) findViewById(R.id.LoginPage);

        keybordControl();


        //로그인 버튼 클릭 이벤트
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = id_Info.getText().toString();
                String pw = pw_Info.getText().toString();

                Login.this.loginData = new LoginData(id, pw); // 로그인 데이터(id, pw)

                //자동로그인 체크박스
                CheckBox Token = (CheckBox) findViewById(R.id.AutoLoginToken);
                if(Token.isChecked()){
                    SharedPreferences savedToken = getSharedPreferences("loginToken", MODE_PRIVATE);
                    SharedPreferences.Editor editor = savedToken.edit();
                    editor.putString("savedID", id);
                    editor.putString("savedPW", pw);
                    editor.commit();
                }
                ////////////////////

                LoginMethod();

            }
        });
    }

    //빈공간 터치시 키보드 숨김
    public void keybordControl(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                touch_hide.hideSoftInputFromWindow(layout.getWindowToken(),0);
            }
        });
    }

    //로그인 처리 메소드
    public void LoginMethod(){
        Call<String> call = NetworkController.getInstance().getNetworkInterface().getLoginInfo(Login.this.loginData);

        Intent whereAreUComing = getIntent();
        int j = whereAreUComing.getIntExtra("fromLoading", 0);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                //로그인 성공
                if(response.isSuccessful()){
                    Intent intent = new Intent(Login.this, Viewpager_main.class);
                    intent.putExtra("clubIdNumber", response.body().toString() );
                    startActivity(intent);
                    Toast.makeText(Login.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    finish();
                    Viewpager_main.viewpagerMain.finish(); //스택에 쌓인 Viewpager_main 액티비티 종료

                }

                //로그인 실패
                else{
                    textView.setText("학번 또는 비밀번호를 다시 확인해 주세요");
                    textView.setTextColor(getResources().getColor(R.color.loginfale));
                }
                Log.d("로그인 코드", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
