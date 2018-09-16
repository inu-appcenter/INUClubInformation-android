package com.ourincheon.app_center.mainActivity.Home.Banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ourincheon.app_center.R;

import java.util.ArrayList;

public class BannerAdaptor extends PagerAdapter {

    private ArrayList<BannerItem> itemlist;
    private int listSize;
    private LayoutInflater inflater = null;

    public BannerAdaptor(ArrayList<BannerItem> itemlist){
        this.itemlist =itemlist;
        this.listSize = itemlist.size();
    }


    @Override
    public int getCount() {
        return listSize;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Context context = container.getContext();

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.banner_item, container, false);

        ImageView banner = (ImageView) view.findViewById(R.id.bannerImage);

        String imageUrl = "http://appcenter.us.to:3303/" + itemlist.get(position).bannerImage;
        Glide.with(context).load(imageUrl).into(banner);

        container.addView(view);

        return view;
    }
}
