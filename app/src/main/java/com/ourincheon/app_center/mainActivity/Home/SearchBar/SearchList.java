package com.ourincheon.app_center.mainActivity.Home.SearchBar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ourincheon.app_center.R;

public class SearchList extends LinearLayout {
    public SearchList(Context context, AttributeSet attrs){
        super(context, attrs);

        init(context);

    }

    public SearchList(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_list,this,true);
    }
}
