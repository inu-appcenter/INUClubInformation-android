package com.ourincheon.app_center.mainActivity.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.list_view.IntroduceClub;
import com.ourincheon.app_center.list_view.List;
import com.ourincheon.app_center.mainActivity.Home.Banner.BannerAdaptor;
import com.ourincheon.app_center.mainActivity.Home.Banner.BannerItem;
import com.ourincheon.app_center.mainActivity.Home.SearchBar.SeachListItem;
import com.ourincheon.app_center.mainActivity.Home.SearchBar.SearchList;
import com.ourincheon.app_center.mainActivity.Home.SearchBar.SearchListViewAdaptor;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Fragment {

    ArrayList<SeachListItem> list = null;
    private ListView search_list = null;
    private TextView search_view;
    ArrayList<SeachListItem> searchArrayList;
    ArrayList<BannerItem> bannerArrayList; //(실제 banner 리스트 + 사이클 반복한) 총 리스트 사이즈
    ArrayList<BannerItem> realBannerSize; //실제 banner리스트 사이즈
    SearchListViewAdaptor searchListViewAdaptor;
    LinearLayout layout;

    private LinearLayout indicator;
    private ImageView[] bannerIndicator = null;

    ViewPager viewPager;
    String mainUrl;

    ThreadMethod threadMethod;

    public static SearchList searchList;
    public static LinearLayout searchlayout;

    public static boolean ClickCount = true;

    int movePosition;
    public MainActivity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.activity_main, container, false);

        list = new ArrayList<>();

        SettingComponent();
        callMainImage();

        SettingSearchBar();

        pageChangeListener();

        viepagerThread();
        threadMethod.start();

        return layout;
    }


    //SearchBar 기능 세팅
    public void SettingSearchBar() {
        layout.bringChildToFront(searchlayout);
        addTextList();
        SearchClickListener();
        TextChangeListener();

    }

    //list의 원본이 되는 searchArrayList를 만든다.
    public void addTextList() {
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getAllClub();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                searchArrayList = new ArrayList<>();
                for (int j = 0; j < response.body().size(); j++) {

                    SeachListItem searchListItem = new SeachListItem();

                    String club_name = response.body().get(j).getAsJsonObject().get("clubname").toString().replace("\"", "");
                    String club_img = response.body().get(j).getAsJsonObject().get("image1").toString().replace("\"", "");
                    String club_num = response.body().get(j).getAsJsonObject().get("num").toString().replace("\"", "");

                    searchListItem.s_clubname = club_name;
                    searchListItem.s_img = club_img;
                    searchListItem.s_clubnum = Integer.parseInt(club_num);
                    searchArrayList.add(searchListItem);
                    Log.d("이미지 주소", club_num);
                }
                list.addAll(searchArrayList);

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

                Log.d("상태코드", "실패");
            }
        });
    }

    //SearchBar 클릭리스너
    public void SearchClickListener() {

        search_view = (EditText) layout.findViewById(R.id.SearchEdit);
        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClickCount == true) {
                    searchList = new SearchList(getActivity().getApplicationContext());
                    searchlayout.addView(searchList);

                    search_list = (ListView) searchlayout.findViewById(R.id.SearchList);
                    keybordControl(searchList);
                    intentToClubIntroduce(searchList);


                    Log.d("리스트", list.toString());

                    //adapter는 list에 있는 내용을 담는다.
                    searchListViewAdaptor = new SearchListViewAdaptor(list);
                    search_list.setAdapter(searchListViewAdaptor);
                    ClickCount = false;
                }
            }
        });

    }

    //SearchBar text 변경 리스너 - 변경시 리스트 내용 달라짐
    public void TextChangeListener() {
        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = search_view.getText().toString();
                Log.d("테스트", "3차");
                search(text.toUpperCase());
                Log.d("테스트", "4차");

            }
        });
    }

    //보여지는 SearchBar 리스트뷰
    public void search(String inputText) {

        list.clear();

        Log.d("서치", "1");

        //아무것도 없으면 전체 리스트 보여줌
        if (inputText.length() < 1) {
            list.addAll(searchArrayList);
            Log.d("서치", "2");
        }

        //특정글자 있으면 list 전부 읽고 있는거만 추가하고 새로고침
        else {
            for (int i = 0; i < searchArrayList.size(); i++) {
                if (searchArrayList.get(i).s_clubname.toUpperCase().contains(inputText)) {
                    list.add(searchArrayList.get(i));
                }
            }
            Log.d("서치", "3");
            searchListViewAdaptor.notifyDataSetChanged();
        }
    }

    //SearchBar 아이템 클리식 해당 페이지로 넘어가기
    public void intentToClubIntroduce(View inflatedView) {
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), IntroduceClub.class);
                intent.putExtra("club_number", list.get(position).s_clubnum);
                startActivity(intent);
                searchlayout.removeView(inflatedView);
                ClickCount = true;
            }
        });
    }


    //컴포넌트 세팅
    public void SettingComponent() {

        searchlayout = (LinearLayout) layout.findViewById(R.id.searchListView);

        ImageButton bu_sports = (ImageButton) layout.findViewById(R.id.sports);
        ImageButton bu_religion = (ImageButton) layout.findViewById(R.id.religion);
        ImageButton bu_culture = (ImageButton) layout.findViewById(R.id.culture);
        ImageButton bu_bongsa = (ImageButton) layout.findViewById(R.id.bongsa);
        ImageButton bu_habit = (ImageButton) layout.findViewById(R.id.habit);
        ImageButton bu_study = (ImageButton) layout.findViewById(R.id.study);

        FrameLayout sports = (FrameLayout) layout.findViewById(R.id.sportsLayout);
        FrameLayout religion = (FrameLayout) layout.findViewById(R.id.religionLayout);
        FrameLayout culture = (FrameLayout) layout.findViewById(R.id.cultureLayout);
        FrameLayout bongsa = (FrameLayout) layout.findViewById(R.id.bongsaLayout);
        FrameLayout habit = (FrameLayout) layout.findViewById(R.id.habitLayout);
        FrameLayout study = (FrameLayout) layout.findViewById(R.id.studyLayout);

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("스포츠");
            }
        });

        bu_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("스포츠");
            }
        });

        religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("종교");
            }
        });

        bu_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("종교");
            }
        });

        culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("문화");
            }
        });

        bu_culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("문화");
            }
        });

        bongsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("봉사");
            }
        });

        bu_bongsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("봉사");
            }
        });

        habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("취미전시");
            }
        });

        bu_habit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("취미전시");
            }
        });

        study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("교양학술");
            }
        });

        bu_study.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentToList("교양학술");
            }
        });
    }

    //Banner 부분 viewpager
    public void callMainImage() {
        viewPager = (ViewPager) layout.findViewById(R.id.main_image);
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getMainImage();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {

                //일반적인 뷰페이저 작성
                bannerArrayList = new ArrayList<>();
                realBannerSize = new ArrayList<>();
                for (int h = 0; h < response.body().size(); h++) {
                    BannerItem bannerItem = new BannerItem();
                    mainUrl = response.body().get(h).getAsJsonObject().get("img").toString().replace("\"", "");
                    bannerItem.bannerImage = mainUrl;
                    bannerArrayList.add(bannerItem);
                    realBannerSize.add(bannerItem);

                    BannerAdaptor bannerAdaptor = new BannerAdaptor(bannerArrayList);
                    viewPager.setAdapter(bannerAdaptor);

                    Log.d("banner", mainUrl);
                }

                //무한 뷰페이저 작성시 사용
                int k =0; // 한 사이클 이후 부터 시작
                while(k != 10){
                    for(int j = 0; j < response.body().size(); j++){
                        BannerItem bannerItem = new BannerItem();
                        String str = bannerArrayList.get(j).bannerImage.toString();
                        bannerItem.bannerImage = str;
                        bannerArrayList.add(bannerItem);

                        BannerAdaptor bannerAdaptor = new BannerAdaptor(bannerArrayList);
                        viewPager.setAdapter(bannerAdaptor);
                    }
                    k++;
                    Log.d("bannerSize", String.valueOf(bannerArrayList.size() + " " + String.valueOf(k)));
                }

                //3번째 사이클부터 viewpager 설정
                viewPager.setCurrentItem(realBannerSize.size()*3);
                first();
            }


            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
    }

    //버튼 클릭시 리스트 페이지로 넘어가기
    public void intentToList(String str) {
        Intent intent = new Intent(getActivity(), List.class);
        intent.putExtra("category", str);
        startActivity(intent);
    }

    //빈공간 클릭시 키보드 숨기기
    public void keybordControl(View inflatedView) {
        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager touch_hide = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                touch_hide.hideSoftInputFromWindow(searchlayout.getWindowToken(), 0);

                searchlayout.removeView(inflatedView);
                ClickCount = true;
            }
        });
    }

    //최초 indicator 설정(아무것도 클릭 안된 상태)
    public void first() {
        indicator = (LinearLayout) layout.findViewById(R.id.indicatorLayout);
        bannerIndicator = new ImageView[realBannerSize.size()];

        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        final int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(5, 0 ,5, 0);

        for (int i = 0; i < realBannerSize.size(); i++) {
            bannerIndicator[i] = new ImageView(getActivity());
            bannerIndicator[i].setLayoutParams(params);
            bannerIndicator[i].setImageDrawable(getActivity().getDrawable(R.drawable.banner_indicator));
            indicator.addView(bannerIndicator[i]);
        }

    }

    //viewpager 넘길 때마다 해당 position 뷰를 바꿈
    public void pageChangeListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //실제 realbanner의 사이즈만큼(한 사이클) 인디케이터 생성
                for (int i = 0; i < realBannerSize.size(); i++) {
                    bannerIndicator[i].setImageDrawable(getActivity().getDrawable(R.drawable.banner_indicator));
                }

                //해당 viewpager item의 String 과 한 사이클내의 문자열을 비교하여, 한 사이클 내 글자와 일치하면 해당 index: n 인 indicator 체크
                for(int n =0; n < realBannerSize.size(); n++){
                    if(bannerArrayList.get(position).bannerImage.toString() == realBannerSize.get(n).bannerImage){
                        bannerIndicator[n].setImageDrawable(getActivity().getDrawable(R.drawable.banner_indicator_checked));
                    }
                }

                movePosition = position;

                /*for(int n = realBannerSize.size(); n > 0; n--){
                    if(position > 2*n){
                        int a = position - (realBannerSize.size()*n);
                        bannerIndicator[a-2].setImageDrawable(getActivity().getDrawable(R.drawable.banner_indicator_checked));
                    }
                }*/

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void viepagerThread(){
        boolean viewpagerBoolean = true;
        threadMethod = new ThreadMethod(viewpagerBoolean) {
            @Override
            void CustomMethod() {
                threadContents();
            }
        };
    }

    public void threadContents(){
        viewPager.setCurrentItem(movePosition);
        movePosition = movePosition + 1;

    }

}
    /* 화면 터치 리스너
    imageView.setOnTouchListener(new View.OnTouchListener() {
        float x, y;
        float x2, y2;

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    x = motionEvent.getX();

                case MotionEvent.ACTION_UP:
                    x2 = motionEvent.getX();
            }

            if(x>x2){
                i++;
                if(i>=main_img.size()){
                    i=0;
                }
                Log.d("인덱스", String.valueOf(i));
                Log.d("인덱스사이즈", String.valueOf(main_img.size()));
                Glide.with(getContext()).load("http://appcenter.us.to:3303/" + main_img.get(i).toString()).into(imageView);
            }
            else if(x<x2){
                i--;
                if(i<0){
                    i = main_img.size()-1;
                }
                Log.d("인덱스", String.valueOf(i));
                Log.d("인덱스사이즈", String.valueOf(main_img.size()));
                Glide.with(getContext()).load("http://appcenter.us.to:3303/" + main_img.get(i).toString()).into(imageView);
            }


            return true;
        }

    });*/


