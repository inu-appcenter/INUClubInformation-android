package com.ourincheon.app_center.list_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.mainActivity.Home.MainActivity;
import com.ourincheon.app_center.mainActivity.Home.SeachListItem;
import com.ourincheon.app_center.mainActivity.Home.SearchList;
import com.ourincheon.app_center.mainActivity.Home.SearchListViewAdaptor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by doyeon on 2018-01-16.
 */

public class List extends AppCompatActivity{

    private ListView listView = null;
    ArrayList<ListViewItem> clubList;

    ArrayList<SeachListItem> list = null;
    private ListView search_list = null;
    private TextView search_view;
    ArrayList<SeachListItem> searchArrayList;
    SearchListViewAdaptor searchListViewAdaptor;
    LinearLayout searchlayout;
    boolean ClickCount = true;
    SearchList searchList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_list);

        listView = (ListView) findViewById(R.id.listViewLayout);
        TextView categoryTitle = (TextView) findViewById(R.id.categoryList);
        MainActivity mainActivity = new MainActivity();

        Intent intent = getIntent();
        final String clickCategory = intent.getStringExtra("category");

        categoryTitle.setText(clickCategory);

        search_view = (EditText) findViewById(R.id.SearchEdit);
        searchlayout = (LinearLayout) findViewById(R.id.searchListView);
        list = new ArrayList<>();

        addTextList();

        search_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ClickCount == true){
                    searchList = new SearchList(getApplicationContext());
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
                search(text);
                Log.d("테스트", "4차");

            }
        });




        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getInformation(clickCategory);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                clubList = new ArrayList<>();
                    for(int i = 0; i < response.body().size(); i++){

                        ListViewItem listViewItem = new ListViewItem();

                        String nameOfClub = response.body().get(i).getAsJsonObject().get("clubname").toString();
                        String locationOfClub = response.body().get(i).getAsJsonObject().get("location").toString();
                        String imageOfClub = response.body().get(i).getAsJsonObject().get("image1").toString();
                        int numOfClub = response.body().get(i).getAsJsonObject().get("num").getAsInt();

                        listViewItem.clubImage = imageOfClub.replace("\"", "");
                        listViewItem.clubName = nameOfClub.replace("\"", "");
                        listViewItem.clubLocation = locationOfClub.replace("\"", "");
                        listViewItem.clubNum = numOfClub;
                        clubList.add(listViewItem);

                        ListViewAdaptor adaptor = new ListViewAdaptor(clubList);
                        listView.setAdapter(adaptor);
                    }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(List.this, IntroduceClub.class);
                intent.putExtra("club_number", clubList.get(i).clubNum);
                startActivity(intent);
            }
        });

        TextChangeListener();
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
                Intent intent = new Intent(List.this, IntroduceClub.class);
                intent.putExtra("club_number", list.get(position).s_clubnum);
                startActivity(intent);
                searchlayout.removeView(inflatedView);
                ClickCount = true;
            }
        });
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

                InputMethodManager touch_hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                touch_hide.hideSoftInputFromWindow(searchlayout.getWindowToken(),0);

                searchlayout.removeView(inflatedView);
                ClickCount = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(ClickCount == false){
            searchlayout.removeView(searchList);
            ClickCount = true;
        }
        else{
            super.onBackPressed();
        }
    }
}
