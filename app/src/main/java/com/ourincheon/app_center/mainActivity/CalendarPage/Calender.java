package com.ourincheon.app_center.mainActivity.CalendarPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by doyeon on 2018-02-07.
 */


public class Calender extends Fragment{

    private RecyclerView recyclerView;
    ArrayList<RecyclerViewItem> recyclerViewItemList = null;
    String date;
    String data;
    String rMonth;
    String rDayofMonth;
    RecyclerViewItem recyclerViewItem;
    RecyclerViewAdaptor adaptor;
    CalendarView calendarview;


    public Calender(){

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.activity_calender, container, false);


        recyclerView = (RecyclerView) layout.findViewById(R.id.recylerviewInCarlendar);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity().getApplicationContext(), new LinearLayoutManager(getActivity()).getOrientation());

        //recyclerView.addItemDecoration(dividerItemDecoration);//리사이클러뷰 divider 추가

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM.dd");
        Date today = new Date();
        data = simpleDateFormat.format(today);
        date = simpleDateFormat2.format(today);


        UpdateRecyclerView();


        calendarview = (CalendarView) layout.findViewById(R.id.calendar);
        calendarview.invalidate();
        recyclerView.invalidate();
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                month = month +1;

                date = (month) + "." + dayOfMonth;

                if(month < 10){
                    rMonth = "0" + String.valueOf(month);
                }
                else{
                    rMonth = String.valueOf(month);
                }

                if(dayOfMonth < 10){
                    rDayofMonth = "0" + String.valueOf(dayOfMonth);
                }
                else{
                    rDayofMonth = String.valueOf(dayOfMonth);
                }

                data = year + "-" + rMonth + "-" + rDayofMonth;

                Log.d("호잇", String.valueOf(year));
                Log.d("호잇2", String.valueOf(month));
                Log.d("호잇3", String.valueOf(dayOfMonth));

                UpdateRecyclerView();

        }
        });

        return layout;
    }

    public void UpdateRecyclerView(){
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getTotalEvent(data);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {


                recyclerViewItemList = new ArrayList<>();

                recyclerViewItem = new RecyclerViewItem();


                if(response.body().toString().length() <= 3){
                    Log.d("일정", "등록된 행사 없음");
                    recyclerViewItem.rClubname = null;
                    recyclerViewItem.rDate = date;
                    recyclerViewItem.rContents = null;
                    recyclerViewItem.rTime = "오늘은 일정이 없습니다";
                    recyclerViewItem.rLocation = null;
                    recyclerViewItemList.add(recyclerViewItem);
                }
                else{
                    for(int i = 0; i < response.body().size(); i++){
                        recyclerViewItem = new RecyclerViewItem();
                        recyclerViewItem.rDate = date;
                        recyclerViewItem.rClubname = response.body().get(i).getAsJsonObject().get("clubname").toString().replace("\"","");
                        recyclerViewItem.rContents ="내용: " + response.body().get(i).getAsJsonObject().get("eventname").toString().replace("\"","");
                        recyclerViewItem.rTime = "시간: " +response.body().get(i).getAsJsonObject().get("time").toString().replace("\"", "");
                        recyclerViewItem.rLocation = "장소: " +response.body().get(i).getAsJsonObject().get("location").toString().replace("\"", "");
                        recyclerViewItemList.add(recyclerViewItem);
                    }
                    Log.d("로그", "등록된 행사 있음");
                }

                adaptor = new RecyclerViewAdaptor(recyclerViewItemList);
                recyclerView.setAdapter(adaptor);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));



                Log.d("리사이클러뷰", "아무것도 없는거");

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {
                Log.d("리사이클러뷰", "실패");

            }
        });
    }

}