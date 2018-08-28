package com.ourincheon.app_center.mainActivity.Setting.ModifyEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event_edit extends AppCompatActivity {

    Edit_item event_edit_item;
    ArrayList<Edit_item> edit_item_list = null;
    String clubnum;
    RecyclerView recyclerView;
    No_Event_IN_EDIT no_event_in_edit;
    LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        Intent intent = getIntent();
        clubnum = intent.getStringExtra("clubIdNumber");
        layout = (LinearLayout) findViewById(R.id.forSetting);

        recyclerView = (RecyclerView)findViewById(R.id.recylerviewInEditEvent);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getApplicationContext(), new LinearLayoutManager(this).getOrientation());

        recyclerView.addItemDecoration(dividerItemDecoration);//리사이클러뷰 divider 추가
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(Event_edit.this, recyclerView, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int event_num_server = edit_item_list.get(position).event_number_from_server;
                Intent intent = new Intent(Event_edit.this, Event.class);
                intent.putExtra("eventnumber", event_num_server);
                intent.putExtra("clubIdNumber", clubnum);
                intent.putExtra("eventname", edit_item_list.get(position).event_title);
                intent.putExtra("eventlocation", edit_item_list.get(position).event_location);
                intent.putExtra("eventdate", edit_item_list.get(position).event_date);
                intent.putExtra("eventtime", edit_item_list.get(position).event_time);
                startActivity(intent);
                finish();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        EventEditList();

    }

    public void EventEditList(){
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getClubEvent(clubnum);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                edit_item_list = new ArrayList<>();

                if(response.body().toString().length() <= 3){
                    Log.d("뭐가 문제야", "응");
                    no_event_in_edit = new No_Event_IN_EDIT(getApplicationContext());
                    layout.addView(no_event_in_edit);
                }

                else{
                    for(int i = 0; i<response.body().size(); i++){

                        event_edit_item = new Edit_item();

                        String item_title = response.body().get(i).getAsJsonObject().get("eventname").toString().replace("\"", "");
                        String item_location = response.body().get(i).getAsJsonObject().get("location").toString().replace("\"", "");
                        String item_date = response.body().get(i).getAsJsonObject().get("date").toString().replace("\"", "");
                        String item_time = response.body().get(i).getAsJsonObject().get("time").toString().replace("\"", "");
                        String event_number = response.body().get(i).getAsJsonObject().get("eventnum").toString().replace("\"", "");
                        Log.d("이벤트넘버", event_number);

                        event_edit_item.event_number_from_server = Integer.parseInt(event_number);
                        event_edit_item.event_number = i+1;
                        event_edit_item.event_title = item_title;
                        event_edit_item.event_location = item_location;
                        event_edit_item.event_date = item_date;
                        event_edit_item.event_time = item_time;
                        edit_item_list.add(event_edit_item);
                    }

                    RecyclerViewAdaptor_edit recyclerViewAdaptor_edit = new RecyclerViewAdaptor_edit(edit_item_list);
                    recyclerView.setAdapter(recyclerViewAdaptor_edit);
                    recyclerView.setLayoutManager(new LinearLayoutManager(Event_edit.this));
                }


            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
    }
}
