package com.ourincheon.app_center.mainActivity.Setting.ModifyClubInformation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.model.ErrorMsgResult;
import com.ourincheon.app_center.model.ModifyClubInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SanJuku on 2018-02-22.
 */

public class ModifyText extends AppCompatActivity {

    private EditText mContents;
    private EditText mRepresent;
    private EditText mPhone;
    private EditText mApplication;
    LinearLayout layout;

    ModifyClubInfo modifyClubInfo;
    String clubnum = null;
    String phone, link, intro, represent;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_text);

        mContents = (EditText) findViewById(R.id.writeContentsEdit);
        mRepresent = (EditText) findViewById(R.id.writeRepresentEdit);
        mPhone = (EditText) findViewById(R.id.writePhoneEdit);
        mApplication = (EditText) findViewById(R.id.writeApplicationEdit);
        layout = (LinearLayout) findViewById(R.id.Modify_text_Page);

        Button bt_submit = (Button) findViewById(R.id.B_submit);

        keybordControl();

        Intent intent = getIntent();
        clubnum = intent.getStringExtra("clubIdNumber");

        existedContents();


        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String represent = mRepresent.getText().toString();
                String phone = mPhone.getText().toString();
                String contents = mContents.getText().toString();
                String application = mApplication.getText().toString();

                modifyClubInfo = new ModifyClubInfo(represent, phone, application, contents);

                Call<ErrorMsgResult> call = NetworkController.getInstance().getNetworkInterface().giveModifiedContents(clubnum, modifyClubInfo);
                call.enqueue(new Callback<ErrorMsgResult>() {
                    @Override
                    public void onResponse(Call<ErrorMsgResult> call, Response<ErrorMsgResult> response) {

                    }

                    @Override
                    public void onFailure(Call<ErrorMsgResult> call, Throwable t) {

                    }
                });

                finish();
                Toast.makeText(ModifyText.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void keybordControl(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                touch_hide.hideSoftInputFromWindow(layout.getWindowToken(),0);
            }
        });
    }

    public void existedContents(){
        int clubnumParseInt = Integer.parseInt(clubnum);
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getDetailInformation(clubnumParseInt);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {

                represent = response.body().get(0).getAsJsonObject().get("representative").toString().replace("\"", "").replace("\\n", "</br>");
                phone = response.body().get(0).getAsJsonObject().get("phone").toString().replace("\"", "").replace("\\n", "</br>");
                link = response.body().get(0).getAsJsonObject().get("application").toString().replace("\"", "").replace("\\n", "</br>");
                intro = response.body().get(0).getAsJsonObject().get("contents").toString().replace("\"", "").replace("\\n", "</br>");

                mContents.setText(intro.replace("</br>", "\n"));
                mPhone.setText(phone.replace("</br>", "\n"));
                mRepresent.setText(represent.replace("</br>", "\n"));

                Intent intent2 = getIntent();
                int from_newLink = intent2.getIntExtra("FromNew", 999);
                if(from_newLink == 111){
                    String LinkAdress = intent2.getStringExtra("Link");
                    mApplication.setText(LinkAdress);
                }
                else{
                    mApplication.setText(link);
                }

                Log.d("동아리내용테스트", represent);
                Log.d("동아리내용테스트", phone);
                Log.d("동아리내용테스트", link);
                Log.d("동아리내용테스트", intro);
            }


            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

    }
}
