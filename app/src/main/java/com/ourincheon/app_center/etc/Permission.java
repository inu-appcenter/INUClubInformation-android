package com.ourincheon.app_center.etc;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.ourincheon.app_center.R;
import com.ourincheon.app_center.mainActivity.Viewpager_main;
import com.tbruyelle.rxpermissions2.RxPermissions;

public class Permission extends AppCompatActivity {

    RxPermissions rxPermissions;
    private int didPermission = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        rxPermissions = new RxPermissions(this);

        FrameLayout bt_permission = (FrameLayout) findViewById(R.id.grant_permissions);

        if(rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION) && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE) && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            didPermission = 1;
        }

        settingPermission();

        bt_permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });

    }

    public void requestPermission(){
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if(granted){
                        Intent intent = new Intent(this, Viewpager_main.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    public void settingPermission(){
        switch (didPermission){
            case 0:
                break;
            case 1:
                startActivity(new Intent(Permission.this, Viewpager_main.class));
                finish();
                break;
        }
    }
}
