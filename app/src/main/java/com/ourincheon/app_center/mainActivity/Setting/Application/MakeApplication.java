package com.ourincheon.app_center.mainActivity.Setting.Application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ourincheon.app_center.R;

public class MakeApplication extends AppCompatActivity {
    String clubnum;
    public static Activity makeApplication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        makeApplication = MakeApplication.this;

        Button bt_how = (Button) findViewById(R.id.UseMethodLink);
        Button bt_new = (Button) findViewById(R.id.MakeApplicationLink);

        Intent intent = getIntent();
        clubnum = intent.getStringExtra("clubIdNumber");

        bt_how.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeApplication.this, Method_Application.class);
                startActivity(intent);
            }
        });

        bt_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeApplication.this, New_Application.class);
                intent.putExtra("clubIdNumber", clubnum);
                startActivity(intent);
            }
        });
    }
}
