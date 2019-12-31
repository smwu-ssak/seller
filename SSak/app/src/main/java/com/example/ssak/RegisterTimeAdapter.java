package com.example.ssak;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ssak.data.RegisterTimeData;

import java.util.ArrayList;

// Customized by SY

public class RegisterTimeAdapter extends RecyclerView.Adapter<RegisterTimeAdapter.RegisterTimeViewHolder> {

    ArrayList<RegisterTimeData> items;

    public RegisterTimeAdapter(ArrayList<RegisterTimeData> items) {
        this.items = items;
    }

    @Override
    public RegisterTimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.rv_item_register_time, viewGroup, false);
        return new RegisterTimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RegisterTimeViewHolder viewHolder, int position) {
        final RegisterTimeData item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.image)
                .into(viewHolder.registerTimeDay);
        viewHolder.registerTimeStart.setText(item.startTime);
        viewHolder.registerTimeEnd.setText(item.endTime);
        viewHolder.registerTimeLine.setVisibility(View.VISIBLE);
        viewHolder.registerTimeIsOpen.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    static class RegisterTimeViewHolder extends RecyclerView.ViewHolder {

        protected ImageView registerTimeDay;
        protected TextView registerTimeStart;
        protected TextView registerTimeEnd;
        protected TextView registerTimeLine;
        protected TextView registerTimeIsOpen;

        public RegisterTimeViewHolder(final View itemView) {
            super(itemView);

            registerTimeDay = itemView.findViewById(R.id.rv_item_register_time_day);
            registerTimeStart = itemView.findViewById(R.id.rv_item_register_time_start);
            registerTimeEnd = itemView.findViewById(R.id.rv_item_register_time_end);
            registerTimeLine = itemView.findViewById(R.id.rv_item_time_fix);
            registerTimeIsOpen = itemView.findViewById(R.id.rv_item_register_time_open);
        }
    }

}