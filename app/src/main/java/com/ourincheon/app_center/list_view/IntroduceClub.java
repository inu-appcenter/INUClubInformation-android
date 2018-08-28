package com.ourincheon.app_center.list_view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.list_view.Images.Image1;
import com.ourincheon.app_center.list_view.Images.Image2;
import com.ourincheon.app_center.list_view.Images.Image3;
import com.ourincheon.app_center.list_view.Images.Image4;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroduceClub extends FragmentActivity{

    private TextView introduce;
    private TextView location;
    private TextView represent;
    private TextView ph_number;
    private TextView club_name;
    private FrameLayout bt_apply;

    String apply_url;

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        introduce = (TextView) findViewById(R.id.introduceClub);
        location = (TextView) findViewById(R.id.place);
        represent = (TextView) findViewById(R.id.representName);
        ph_number = (TextView) findViewById(R.id.representNumber);
        club_name = (TextView) findViewById(R.id.clubName2);
        bt_apply = (FrameLayout) findViewById(R.id.clientApplication);

        viewPager = (ViewPager) findViewById(R.id.clubImages);
        viewPager.setAdapter(new pageAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);


        Intent intent = getIntent();
        int getNB = intent.getIntExtra("club_number", 0);

        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getDetailInformation(getNB);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {

                String CLname = response.body().get(0).getAsJsonObject().get("clubname").toString().replace("\"", "").replace("\\n", "</br>");
                String CLrepresent = response.body().get(0).getAsJsonObject().get("representative").toString().replace("\"", "").replace("\\n", "</br>");
                String CLphone = response.body().get(0).getAsJsonObject().get("phone").toString().replace("\"", "").replace("\\n", "</br>");
                String CLlocation = response.body().get(0).getAsJsonObject().get("location").toString().replace("\"", "").replace("\\n", "</br>");
                String CLintro = response.body().get(0).getAsJsonObject().get("contents").toString().replace("\"", "").replace("\\n", "</br>");

                apply_url = response.body().get(0).getAsJsonObject().get("application").toString().replace("\"", "");

                club_name.setText(CLname.replace("</br>", "\n"));
                represent.setText(CLrepresent.replace("</br>", "\n"));
                ph_number.setText(CLphone.replace("</br>", "\n"));
                location.setText(CLlocation.replace("</br>", "\n"));
                introduce.setText(CLintro.replace("</br>", "\n"));
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(URLUtil.isValidUrl(apply_url)){
                    LinearLayout linearLayout = new LinearLayout(IntroduceClub.this);
                    ImageView PopupView = new ImageView(IntroduceClub.this);
                    TextView PopupTextView = new TextView(IntroduceClub.this);

                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setPadding(0, 80, 0, 0);

                    PopupView.setImageResource(R.drawable.page_1);

                    PopupTextView.setText("지원서 페이지로 이동합니다");
                    PopupTextView.setTextColor(Color.parseColor("#8a000000"));
                    PopupTextView.setGravity(Gravity.CENTER);
                    PopupTextView.setPadding(0, 50, 0, 30);

                    linearLayout.addView(PopupView);
                    linearLayout.addView(PopupTextView);

                    AlertDialog.Builder dialog = new AlertDialog.Builder(IntroduceClub.this);
                    dialog.setView(linearLayout);

                    dialog.setPositiveButton("동의", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent applyLink = new Intent(Intent.ACTION_VIEW, Uri.parse(apply_url));
                            applyLink.setPackage("com.android.chrome");
                            startActivity(applyLink);
                            dialog.dismiss();
                        }
                    });

                    dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }

                else{
                    Toast.makeText(IntroduceClub.this, "이 동아리는 온라인 지원을 받지 않습니다", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class pageAdapter extends FragmentPagerAdapter {
        public pageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            TextView textView = new TextView(IntroduceClub.this);
            switch (position){
                case 0:
                    return new Image1();
                case 1:
                    return new Image2();
                case 2:
                    return new Image3();
                case 3:
                    return new Image4();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
