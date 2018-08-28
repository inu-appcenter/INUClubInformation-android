package com.ourincheon.app_center.list_view.Images;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.ourincheon.app_center.R;
import com.ourincheon.app_center.application.NetworkController;
import com.ourincheon.app_center.list_view.IntroduceClub;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Image3 extends Fragment {

    String imageLocation;

    public Image3(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, final ViewGroup ImageContainer, Bundle savedInstanceState){
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.apply_image3, ImageContainer, false);

        final ImageView image3 = (ImageView) layout.findViewById(R.id.image3);

        Intent intent = getActivity().getIntent();

        int getNB = intent.getIntExtra("club_number", 0);

        Call<ArrayList<JsonObject>> call = NetworkController.getInstance().getNetworkInterface().getDetailInformation(getNB);
        call.enqueue(new Callback<ArrayList<JsonObject>>() {
            @Override
            public void onResponse(Call<ArrayList<JsonObject>> call, Response<ArrayList<JsonObject>> response) {
                imageLocation = response.body().get(0).getAsJsonObject().get("image3").toString().replace("\"", "");
                if(imageLocation.length() < 5)
                {
                    image3.setImageResource(R.drawable.group_6);
                }
                else{
                    String url = NetworkController.getURL() + imageLocation;
                    Glide.with(Image3.this).load(url).into(image3);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<JsonObject>> call, Throwable t) {

            }
        });

        return layout;
    }
}
