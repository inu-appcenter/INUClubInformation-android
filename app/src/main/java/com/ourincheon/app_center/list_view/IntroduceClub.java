package com.ourincheon.app_center.list_view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
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
import com.ourincheon.app_center.list_view.Banner.BannerAdaptor_introduce;
import com.ourincheon.app_center.list_view.Banner.BannerItem_introduce;

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
    private LinearLayout indicator;
    private ImageView[] bannerIndicator = null;

    ArrayList<String> pauseList;

    String imageLocation;
    ArrayList<BannerItem_introduce> bannerList_introduce;

    String apply_url;

    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        bannerList_introduce = new ArrayList<>();

        introduce = (TextView) findViewById(R.id.introduceClub);
        location = (TextView) findViewById(R.id.place);
        represent = (TextView) findViewById(R.id.representName);
        ph_number = (TextView) findViewById(R.id.representNumber);
        club_name = (TextView) findViewById(R.id.clubName2);
        bt_apply = (FrameLayout) findViewById(R.id.clientApplication);

        viewPager = (ViewPager) findViewById(R.id.clubImages);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(4);
        callImage();

        pageChangeListener();

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

    public void callImage(){
        Intent intent = getIntent();

        int getNB = intent.getIntExtra("club_number", 0);

        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getDetailInformation(getNB);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                pauseList = new ArrayList<>();
                for(int i=0; i<response.body().size(); i++)
                {
                    imageLocation = response.body().get(0).getAsJsonObject().get("image1").toString().replace("\"", "");
                    String imageLocation2 = response.body().get(0).getAsJsonObject().get("image2").toString().replace("\"", "");
                    String imageLocation3 = response.body().get(0).getAsJsonObject().get("image3").toString().replace("\"", "");
                    String imageLocation4 = response.body().get(0).getAsJsonObject().get("image4").toString().replace("\"", "");

                    pauseList.add(imageLocation);
                    pauseList.add(imageLocation2);
                    pauseList.add(imageLocation3);
                    pauseList.add(imageLocation4);

                    for(int j=0; j<pauseList.size(); j++){
                        if(pauseList.get(j).toString().length()>5){
                            BannerItem_introduce bannerItem_introduce = new BannerItem_introduce();
                            bannerItem_introduce.bannerImage = pauseList.get(j).toString();
                            bannerList_introduce.add(bannerItem_introduce);
                        }
                    }

                    if(bannerList_introduce.size() < 1){
                        BannerItem_introduce noBannerItem = new BannerItem_introduce();
                        noBannerItem.bannerImage = "noImage";
                        bannerList_introduce.add(noBannerItem);
                    }

                    Log.d("ImageLocation", imageLocation2+"\n"+imageLocation3 + "\n" +imageLocation4);

                    BannerAdaptor_introduce bannerAdaptor_introduce = new BannerAdaptor_introduce(bannerList_introduce);
                    viewPager.setAdapter(bannerAdaptor_introduce);
                }
                first();
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
    }

    //최초 indicator 설정(아무것도 클릭 안된 상태)
    public void first() {
        indicator = (LinearLayout) findViewById(R.id.indicatorLayout);
        bannerIndicator = new ImageView[bannerList_introduce.size()];

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(5, 0 ,5, 0);

        for (int i = 0; i < bannerList_introduce.size(); i++) {
            bannerIndicator[i] = new ImageView(this);
            bannerIndicator[i].setLayoutParams(params);
            bannerIndicator[i].setImageDrawable(getDrawable(R.drawable.banner_indicator));
            indicator.addView(bannerIndicator[i]);
        }

    }

    //viewpager 넘길 때마다 해당 position 뷰를 바꿈
    public void pageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                for (int i = 0; i < bannerList_introduce.size(); i++) {
                    bannerIndicator[i].setImageDrawable(getDrawable(R.drawable.banner_indicator));
                }
                bannerIndicator[position].setImageDrawable(getDrawable(R.drawable.banner_indicator_checked));
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
