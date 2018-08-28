package com.ourincheon.app_center.mainActivity.Setting.ModifyEvent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ourincheon.app_center.R;

import java.util.ArrayList;

public class RecyclerViewAdaptor_edit extends RecyclerView.Adapter<ViewHolder> {

    private ArrayList<Edit_item> edit_item;

    public RecyclerViewAdaptor_edit(ArrayList<Edit_item> edit_item){
        this.edit_item = edit_item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        View convertView = inflater.inflate(R.layout.recyclerview_item_edit, parent, false);

        ViewHolder viewHolder = new ViewHolder(convertView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Edit_item edit_event_item = edit_item.get(position);

        TextView ee_num = holder.event_num;
        TextView ee_title = holder.event_title;


        ee_num.setText(String.valueOf(edit_event_item.event_number));
        ee_title.setText(edit_event_item.event_title);

    }

    @Override
    public int getItemCount() {
        return edit_item.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder{

    public TextView event_num;
    public TextView event_title;

    public ViewHolder(View itemView) {
        super(itemView);

        event_num = (TextView) itemView.findViewById(R.id.editEvent_num);
        event_title = (TextView) itemView.findViewById(R.id.editEvent_contents);
    }
}

