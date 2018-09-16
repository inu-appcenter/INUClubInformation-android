package com.ourincheon.app_center.list_view.Banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ourincheon.app_center.R;

import java.util.ArrayList;

public class BannerAdaptor_introduce extends PagerAdapter {

    private ArrayList<BannerItem_introduce> itemlist;
    private int listSize;
    private LayoutInflater inflater = null;

    public BannerAdaptor_introduce(ArrayList<BannerItem_introduce> itemlist){
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
        View view = inflater.inflate(R.layout.banner_item_introduce, container, false);

        ImageView banner = (ImageView) view.findViewById(R.id.bannerImage);

        Log.d("testBanner", String.valueOf(itemlist.size()) + " " + itemlist.get(0).toString());

        if(itemlist.size() <= 1 && itemlist.get(0).bannerImage.toString() == "noImage"){
            banner.setImageResource(R.drawable.group_6);
        }
        else{
            String imageUrl = "http://appcenter.us.to:3303/" + itemlist.get(position).bannerImage;
            Glide.with(context).load(imageUrl).into(banner);
        }

        container.addView(view);

        return view;
    }
}
