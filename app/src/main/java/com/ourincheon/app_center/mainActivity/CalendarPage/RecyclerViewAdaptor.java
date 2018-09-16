package com.ourincheon.app_center.mainActivity.CalendarPage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourincheon.app_center.R;

import java.util.ArrayList;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<ViewHolder>{

    private ArrayList<RecyclerViewItem> recyclerViewItems;

    public RecyclerViewAdaptor(ArrayList<RecyclerViewItem> recyclerViewItems){
        this.recyclerViewItems = recyclerViewItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View convertView = inflater.inflate(R.layout.recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(convertView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RecyclerViewItem recyclerViewItem = recyclerViewItems.get(position);

        TextView tv_date = holder.date;

        TextView tv_contents = holder.contents;
        TextView tv_times = holder.times;
        TextView tv_location = holder.location;
        TextView tv_clubname = holder.clubname;

        tv_date.setText(recyclerViewItem.rDate);

        tv_clubname.setText(recyclerViewItem.rClubname);
        tv_contents.setText(recyclerViewItem.rContents);
        tv_times.setText(recyclerViewItem.rTime);
        tv_location.setText(recyclerViewItem.rLocation);

    }

    @Override
    public int getItemCount() {
        return recyclerViewItems.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{

    public TextView date;
    public TextView contents;
    public TextView times;
    public TextView location;
    public TextView clubname;

    public ViewHolder(View itemView) {
        super(itemView);

        clubname = (TextView) itemView.findViewById(R.id.rc_clubname);
        date = (TextView) itemView.findViewById(R.id.rc_date);
        location = (TextView) itemView.findViewById(R.id.rc_location);
        contents = (TextView) itemView.findViewById(R.id.rc_contents);
        times = (TextView) itemView.findViewById(R.id.rc_time);
    }
}
