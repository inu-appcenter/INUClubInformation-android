package com.ourincheon.app_center.mainActivity.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.list_view.IntroduceClub;
import com.ourincheon.app_center.list_view.List;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Fragment {

    ArrayList<SeachListItem> list = null;
    private ListView search_list = null;
    private TextView search_view;
    ArrayList<SeachListItem> searchArrayList;
    ArrayList<String> main_img; //main 이미지 배열리스트
    SearchListViewAdaptor searchListViewAdaptor;
    LinearLayout layout;

    int i = 0;

    String mainUrl;

    ImageView imageView;

    public static SearchList searchList;
    public static LinearLayout searchlayout;

    public static boolean ClickCount = true;

    public MainActivity(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.activity_main, container, false);

        search_view = (EditText) layout.findViewById(R.id.SearchEdit);
        searchlayout = (LinearLayout) layout.findViewById(R.id.searchListView);
        imageView = (ImageView) layout.findViewById(R.id.main_image);
        list = new ArrayList<>();

        callMainImage();

        layout.bringChildToFront(searchlayout);

        bt_clickEvent();
        addTextList();

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

        });

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClickCount == true){
                    searchList = new SearchList(getActivity().getApplicationContext());
                    searchlayout.addView(searchList);

                    search_list = (ListView) searchlayout.findViewById(R.id.SearchList);
                    keybordControl(searchList);
                    intentToClubIntroduce(searchList);


                    Log.d("리스트", list.toString());

                    searchListViewAdaptor = new SearchListViewAdaptor(list);
                    search_list.setAdapter(searchListViewAdaptor);
                    ClickCount = false;
                }
            }
        });

        TextChangeListener();

        return layout;
    }

    public void TextChangeListener(){
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

    public void intentToClubIntroduce(View inflatedView){
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

    public void intentToList(String str){
        Intent intent = new Intent(getActivity(), List.class);
        intent.putExtra("category", str);
        startActivity(intent);
    }

    public void search(String inputText){

        list.clear();

        Log.d("서치", "1");

        if(inputText.length() < 1){
            list.addAll(searchArrayList);
            Log.d("서치", "2");
        }

        else{
            for(int i =0; i< searchArrayList.size(); i++){
                if(searchArrayList.get(i).s_clubname.toUpperCase().contains(inputText))
                {
                    list.add(searchArrayList.get(i));
                }
            }
            Log.d("서치", "3");
            searchListViewAdaptor.notifyDataSetChanged();
        }
    }

    public void addTextList(){
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getAllClub();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                searchArrayList = new ArrayList<>();
                for(int j = 0; j < response.body().size(); j++){

                    SeachListItem searchListItem = new SeachListItem();

                    String club_name = response.body().get(j).getAsJsonObject().get("clubname").toString().replace("\"", "");
                    String club_img = response.body().get(j).getAsJsonObject().get("image1").toString().replace("\"","");
                    String club_num = response.body().get(j).getAsJsonObject().get("num").toString().replace("\"","");

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

    public void keybordControl(View inflatedView){
        searchlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager touch_hide = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                touch_hide.hideSoftInputFromWindow(searchlayout.getWindowToken(),0);

                searchlayout.removeView(inflatedView);
                ClickCount = true;
            }
        });
    }

    public void bt_clickEvent(){
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

    public void callMainImage(){
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getMainImage();
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                main_img = new ArrayList<String>();
                for(int h=0; h<response.body().size(); h++){
                    mainUrl = response.body().get(h).getAsJsonObject().get("img").toString().replace("\"", "");
                    main_img.add(mainUrl);
                }
                Glide.with(getContext()).load("http://appcenter.us.to:3303/" + main_img.get(0).toString()).into(imageView);
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
    }


}
