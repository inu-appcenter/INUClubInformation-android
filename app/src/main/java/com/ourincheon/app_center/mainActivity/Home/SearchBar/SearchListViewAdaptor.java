package com.ourincheon.app_center.mainActivity.Home.SearchBar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.mainActivity.Home.SearchBar.SeachListItem;

import java.util.ArrayList;

public class SearchListViewAdaptor extends BaseAdapter {

    private LayoutInflater layoutInflater = null;
    private ArrayList<SeachListItem> searchItem = null;
    private int layout;

    public SearchListViewAdaptor(ArrayList<SeachListItem> searchItem){
        this.searchItem = searchItem;
        this.layout = searchItem.size();
    }

    @Override
    public int getCount() {
        return searchItem.size();
    }

    @Override
    public Object getItem(int position) {
        return this.searchItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            if(layoutInflater == null){
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.search_list_item, parent, false);
        }

        TextView s_name = (TextView) convertView.findViewById(R.id.seach_club);
        ImageView s_image = (ImageView) convertView.findViewById(R.id.search_img);

        s_image.setImageResource(R.drawable.group_6);
        if(searchItem.get(position).s_img.length() > 5)
        {
            String url = "http://appcenter.us.to:3303/" + searchItem.get(position).s_img;
            Glide.with(context).load(url).into(s_image);
        }

        s_name.setText(searchItem.get(position).s_clubname);

        return convertView;
    }
}
