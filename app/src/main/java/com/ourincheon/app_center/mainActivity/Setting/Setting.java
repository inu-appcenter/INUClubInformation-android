package com.ourincheon.app_center.mainActivity.Setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.etc.Loading;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Setting extends Fragment {

    int c_num;
    TextView clubNB;
    ImageView clubImage;
    String clubnum;

    public Setting(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_profile, container, false);

        ListView listView = (ListView) layout.findViewById(R.id.setting_item);
        clubNB = (TextView) layout.findViewById(R.id.clubID);
        clubImage = (ImageView) layout.findViewById(R.id.profile);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                touch_hide.hideSoftInputFromWindow(container.getWindowToken(), 0);
            }
        });

        Intent intent = getActivity().getIntent();
        clubnum = intent.getStringExtra("clubIdNumber");
        String noLogin = "동아리 관리자 설정";
        clubNB.setTextColor(Color.BLUE);

        if(clubnum == null){
            clubNB.setText(noLogin);
        }
        else{
            c_num = Integer.parseInt(clubnum);
            getClubName();
        }

        final ArrayList<String> settingList = new ArrayList<String>();
        settingList.add("행사 등록");
        settingList.add("행사 수정");
        settingList.add("동아리 내용 수정");
        settingList.add("동아리 사진 수정");
        settingList.add("온라인 신청서 만들기");

        if(clubNB.getText().toString() == noLogin){
            settingList.add("로그인");
        }
        else{
            settingList.add("로그아웃");
        }


        ArrayAdapter<String> listviewapater = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, settingList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textview = (TextView) view.findViewById(android.R.id.text1);
                textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                textview.setPadding(10, 70, 10, 50);

                return view;
            }
        };

        listView.setAdapter(listviewapater);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(settingList.get(5).toString() == "로그인"){
                    if(settingList.get(i).toString() == "로그인"){
                        Intent intent = new Intent(getActivity(), Loading.class);
                        intent.putExtra("listValue", settingList.get(i).toString());
                        intent.putExtra("clubIdNumber", clubnum);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(getActivity(), "로그인이 필요한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else{
                    if(settingList.get(5).toString() == "로그아웃"){
                        if(settingList.get(i).toString() == "로그아웃"){
                            getActivity().finish();
                        }
                        else{

                        }
                        Intent intent = new Intent(getActivity(), Loading.class);
                        intent.putExtra("listValue", settingList.get(i).toString());
                        intent.putExtra("clubIdNumber", clubnum);
                        startActivity(intent);

                    }
                }
            }
        });


        return layout;
    }

    public void getClubName(){
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getDetailInformation(c_num);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                String settingUser = response.body().get(0).getAsJsonObject().get("clubname").toString().replace("\"", "");
                String imageUrl = response.body().get(0).getAsJsonObject().get("image1").toString().replace("\"", "");
                    clubImage.setImageResource(R.drawable.oval);
                    if(imageUrl.length() > 7){
                        Glide.with(getContext()).load("http://appcenter.us.to:3303/" + imageUrl).into(clubImage);
                    }
                Log.d("이미지URL체크", imageUrl);
                clubNB.setText(settingUser);
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
    }

}
