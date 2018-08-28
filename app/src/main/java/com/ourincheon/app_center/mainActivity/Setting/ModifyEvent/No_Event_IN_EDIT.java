package com.ourincheon.app_center.mainActivity.Setting.ModifyEvent;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ourincheon.app_center.R;

public class No_Event_IN_EDIT extends LinearLayout {
    public No_Event_IN_EDIT(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.recyclerview_no_contents,this,true);
    }
}
