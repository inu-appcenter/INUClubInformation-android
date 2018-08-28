package com.ourincheon.app_center.list_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourincheon.app_center.R;

import java.util.ArrayList;

public class ListViewAdaptor extends BaseAdapter {

    private LayoutInflater layoutInflater = null;
    private ArrayList<ListViewItem> itemCategory = null;
    private int layout;

    public ListViewAdaptor(ArrayList<ListViewItem> itemCategory){
        this.itemCategory = itemCategory;
        this.layout = itemCategory.size();
    }

    @Override
    public int getCount() {
        return itemCategory.size();
    }

    @Override
    public Object getItem(int position) {
        return this.itemCategory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        if(convertView == null){
            if(layoutInflater == null){
                layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = layoutInflater.inflate(R.layout.club_list_item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.clubName);
        TextView location = (TextView) convertView.findViewById(R.id.clubPlace);
        ImageView image = (ImageView) convertView.findViewById(R.id.clubImageView);

        image.setImageResource(R.drawable.group_6);
        if(itemCategory.get(position).clubImage.length() > 7)
        {
            String url = "http://appcenter.us.to:3303/" + itemCategory.get(position).clubImage;
            Glide.with(context).load(url).into(image);
        }

        name.setText(itemCategory.get(position).clubName);
        location.setText(itemCategory.get(position).clubLocation);

        return convertView;
    }

}
