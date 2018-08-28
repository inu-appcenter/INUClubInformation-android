package com.ourincheon.app_center.mainActivity.Setting.ModifyEvent;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.mainActivity.Viewpager_main;
import com.ourincheon.app_center.model.ErrorMsgResult;
import com.ourincheon.app_center.model.UpdateEvent;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SanJuku on 2018-02-22.
 */

public class Event extends AppCompatActivity {

    TextView calendar;
    TextView clock;
    EditText eventTitle;
    EditText eventLocation;

    Button bt_delete;
    Button bt_update;
    int mYear, mMonth, mDay;
    int mHour, mMinutes;
    String M, H;
    String mt_Month, mt_Day;

    String clubnum;

    int num_event;
    String mEventname, mDate, mTime, mLocation;
    String s_min, s_hour;
    String e_title, e_location, e_date, e_time;

    LinearLayout layout;

    static final int DATEDIALOG = 1;
    static final int TIMEDIALOG = 2;

    Calendar sCalendar;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private boolean existDate = false;
    private boolean existTime = false;

    static int defaultValue = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        layout = (LinearLayout) findViewById(R.id.EventPage);

        keybordControl();

        calendar = (TextView) findViewById(R.id.dateContents);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existDate = true;
                sCalendar = Calendar.getInstance();
                mYear = sCalendar.get(Calendar.YEAR);
                mMonth = sCalendar.get(Calendar.MONTH);
                mDay = sCalendar.get(Calendar.DAY_OF_MONTH);
                showDialog(DATEDIALOG);
            }
        });


        clock = (TextView) findViewById(R.id.timeContents);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                existTime = true;
                sCalendar = Calendar.getInstance();
                mHour = sCalendar.get(Calendar.HOUR_OF_DAY);
                mMinutes = sCalendar.get(Calendar.MINUTE);
                showDialog(TIMEDIALOG);

            }
        });

        Intent intent = getIntent();
        num_event = intent.getIntExtra("eventnumber", defaultValue);
        clubnum = intent.getStringExtra("clubIdNumber");

        eventTitle = (EditText) findViewById(R.id.eventContents);
        eventLocation = (EditText) findViewById(R.id.locationContents);

        e_title = intent.getStringExtra("eventname");
        e_location = intent.getStringExtra("eventlocation");
        e_date = intent.getStringExtra("eventdate");
        e_time = intent.getStringExtra("eventtime");

        eventTitle.setText(e_title);
        eventLocation.setText(e_location);
        calendar.setText(e_date);
        clock.setText(e_time);

        if(num_event != defaultValue){
            LinearLayout mlayout = (LinearLayout) findViewById(R.id.deleteLayout);
            bt_delete = new Button(this);
            bt_delete.setText("삭제");
            bt_delete.setTextSize(20);
            bt_delete.setTextColor(getColor(R.color.whiteStatus));
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            int height2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
            bt_delete.setLayoutParams(param);
            bt_delete.setBackground(getDrawable(R.drawable.btn_setting));

            LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height2);
            TextView textView = new TextView(this);
            textView.setLayoutParams(param2);

            mlayout.addView(bt_delete);
            mlayout.addView(textView);

            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<ErrorMsgResult> call = NetworkController.getInstance().getNetworkInterface().deleteEvent(num_event);
                    call.enqueue(new Callback<ErrorMsgResult>() {
                        @Override
                        public void onResponse(Call<ErrorMsgResult> call, Response<ErrorMsgResult> response) {

                        }

                        @Override
                        public void onFailure(Call<ErrorMsgResult> call, Throwable t) {

                        }
                    });

                    Intent intent = new Intent(Event.this, Event_edit.class);
                    intent.putExtra("clubIdNumber", clubnum);
                    startActivity(intent);
                    Toast.makeText(Event.this, "삭제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }


        bt_update = (Button) findViewById(R.id.UpdateButton);
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(calendar.getText().toString().length() == 0 || clock.getText().toString().length() == 0
                        || eventTitle.getText().toString().length() == 0 || eventLocation.getText().toString().length() == 0){

                    Toast.makeText(Event.this, "모든 내용을 작성해주세요", Toast.LENGTH_SHORT).show();
                    return;

                }
                else{
                    postEvent();
                }



                Log.d("등록 내용", mEventname);
                Log.d("등록 장소", mLocation);
                Log.d("등록 날짜", mDate);
                Log.d("등록 시간", mTime);
                Log.d("등록 숫자", String.valueOf(num_event));

                finish();
                Viewpager_main.fragmentPosition = 2;
                ((Viewpager_main) Viewpager_main.mainContext).onResume();
            }
        });

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            mYear = year;
            mMonth = month;
            mDay = day;

            calendar.setText(mYear + "년" + " " + (mMonth + 1) + "월" + " " + mDay + "일");
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {

            case DATEDIALOG:

                datePickerDialog = new DatePickerDialog(Event.this, R.style.DatePiclerTheme, mDateSetListener, mYear, mMonth, mDay);

                return datePickerDialog;

            case TIMEDIALOG:

                timePickerDialog = new TimePickerDialog(Event.this, R.style.CunstomTime, mTimeSetListener, mHour, mMinutes, false);

                return timePickerDialog;

        }
        return null;
    }
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            mHour = hour;
            mMinutes = minutes;

            if(mHour>=13){
                clock.setText("오후" + " " + " " + hours() + " " + " :" + " " + minutes());
            }
            else if(mHour == 12){
                clock.setText("오후" + " " + " " + hours() + " " + " :" + " " + minutes());
            }
            else
            {
                clock.setText("오전" + " " + " " + hours() + " " + " :" + " " + minutes());
            }


        }

        public String minutes(){
            if(mMinutes <= 9){
                M = "0" + String.valueOf(mMinutes);
            }
            else{
                M = String.valueOf(mMinutes);
            }
            return M;
        }

        public String hours(){
            if(mHour <= 9){
                H = "0" + String.valueOf(mHour);
            }
            else if(mHour >= 13){
                int mmHour = mHour - 12;
                if(mmHour <= 9){
                    H = "0" + String.valueOf(mmHour);
                }
                else{
                    H = String.valueOf(mmHour);
                }
            }
            else{
                H = String.valueOf(mHour);
            }
            return H;
        }

    };

    public void keybordControl(){
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager touch_hide = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                touch_hide.hideSoftInputFromWindow(layout.getWindowToken(),0);
            }
        });
    }

    public void NewEvent(){
        UpdateEvent updateEvent = new UpdateEvent(mEventname, mDate, mTime, mLocation);
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().updateEvent(updateEvent);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
        Toast.makeText(Event.this, "행사가 등록되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void EditEvent(){
        UpdateEvent updateEvent = new UpdateEvent(mEventname, mDate, mTime, mLocation);
        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().editEvent(num_event, updateEvent);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {

            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });
        Toast.makeText(Event.this, "행사가 수정되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void setTextContents(int setContents){

        switch (setContents){
            case 0: //아무것도 안눌렀을때
                mDate = e_date;
                mTime = e_time;
                break;

            case 1: //날짜만 변경했을때
                mDate = mYear + "-" + mt_Month + "-" + mt_Day;
                mTime = e_time;
                break;

            case 2: //시간만 변경했을때
                mDate = e_date;
                mTime = s_hour + ":" + s_min;
                break;

            case 3: //둘 다 변경했을때
                mDate = mYear + "-" + mt_Month + "-" + mt_Day;
                mTime = s_hour + ":" + s_min;
                break;

                default:
                    break;
        }
    }

    public void postEvent(){
        if(mMonth+1 < 10){
            mt_Month = "0" + String.valueOf(mMonth+1);
        }
        else{
            mt_Month = String.valueOf(mMonth+1);
        }

        if(mDay < 10){
            mt_Day = "0" + String.valueOf(mDay);
        }
        else{
            mt_Day = String.valueOf(mDay);
        }

        if(mHour < 10){
            s_hour = "0" + String.valueOf(mHour);
        }
        else{
            s_hour = String.valueOf(mHour);
        }

        if(mMinutes < 10){
            s_min = "0" + String.valueOf(mMinutes);
        }
        else{
            s_min = String.valueOf(mMinutes);
        }

        if(num_event == defaultValue){
            mDate = mYear + "-" + mt_Month + "-" + mt_Day;
            mEventname = eventTitle.getText().toString();
            mLocation = eventLocation.getText().toString();
            mTime = s_hour + ":" + s_min;
            NewEvent();
        }
        else{
            bt_update.setText("수정");
            mEventname = eventTitle.getText().toString();
            mLocation = eventLocation.getText().toString();

            if(existDate == false && existTime == false){
                setTextContents(0);
            }

            else if(existDate == true && existTime == false){
                setTextContents(1);
            }

            else if(existDate == false && existTime == true){
                setTextContents(2);
            }

            else{
                setTextContents(3);
            }
            EditEvent();
        }
    }

}
