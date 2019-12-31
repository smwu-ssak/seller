package com.example.ssak;

import android.app.TimePickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ssak.data.RegisterTimeData;

import java.util.ArrayList;
import java.util.Calendar;

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
                .load(item.getImage())
                .into(viewHolder.registerTimeDay);
        viewHolder.registerTimeStart.setText(item.getStartTime());
        viewHolder.registerTimeEnd.setText(item.getEndTime());
        viewHolder.registerTimeLine.setVisibility(View.VISIBLE);
        viewHolder.registerTimeIsOpen.setVisibility(View.INVISIBLE);

        viewHolder.registerTimeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while(true) {
                    if (item.getIsOpen()) {
                        item.setIsOpen(false);
                        switch (item.getDay()) {
                            case 0: item.setImage(R.drawable.sellerregisteration_sundayoff); break;
                            case 1: item.setImage(R.drawable.sellerregisteration_mondayoff); break;
                            case 2: item.setImage(R.drawable.sellerregisteration_tuesdayoff); break;
                            case 3: item.setImage(R.drawable.sellerregisteration_wednesdayoff); break;
                            case 4: item.setImage(R.drawable.sellerregisteration_thursdayoff); break;
                            case 5: item.setImage(R.drawable.sellerregisteration_fridayoff); break;
                            case 6: item.setImage(R.drawable.sellerregisteration_saturdayoff); break;
                        }
                        Glide.with(viewHolder.itemView.getContext())
                                .load(item.getImage())
                                .into(viewHolder.registerTimeDay);
                        viewHolder.registerTimeStart.setVisibility(View.INVISIBLE);
                        viewHolder.registerTimeEnd.setVisibility(View.INVISIBLE);
                        viewHolder.registerTimeLine.setVisibility(View.INVISIBLE);
                        viewHolder.registerTimeIsOpen.setVisibility(View.VISIBLE);
                        item.setStartTime(null);
                        item.setEndTime(null);
                        break;
                    }
                    else {
                        item.setIsOpen(true);
                        switch (item.getDay()) {
                            case 0: item.setImage(R.drawable.sellerregisteration_sunday); break;
                            case 1: item.setImage(R.drawable.sellerregisteration_monday); break;
                            case 2: item.setImage(R.drawable.sellerregisteration_tuesday); break;
                            case 3: item.setImage(R.drawable.sellerregisteration_wednesday); break;
                            case 4: item.setImage(R.drawable.sellerregisteration_thursday); break;
                            case 5: item.setImage(R.drawable.sellerregisteration_friday); break;
                            case 6: item.setImage(R.drawable.sellerregisteration_saturday); break;
                        }
                        Glide.with(viewHolder.itemView.getContext())
                                .load(item.getImage())
                                .into(viewHolder.registerTimeDay);
                        viewHolder.registerTimeStart.setVisibility(View.VISIBLE);
                        viewHolder.registerTimeEnd.setVisibility(View.VISIBLE);
                        viewHolder.registerTimeLine.setVisibility(View.VISIBLE);
                        viewHolder.registerTimeIsOpen.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }
        });

        final Calendar start = Calendar.getInstance();
        viewHolder.registerTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOf, int minOf) {
                        start.set(Calendar.HOUR_OF_DAY, hourOf);
                        start.set(Calendar.MINUTE, minOf);

                        viewHolder.registerTimeStart.setText(String.valueOf(start.getTime()).substring(11, 16));
                        item.setStartTime(String.valueOf(start.getTime()).substring(11, 16)+":00");
                        Log.d("시간", String.valueOf(start.getTime()).substring(11, 16)+":00");
                    }
                };
                new TimePickerDialog(view.getRootView().getContext(), onTimeSetListener, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true).show();
            }
        });

        final Calendar end = Calendar.getInstance();
        viewHolder.registerTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOf, int minOf) {
                        end.set(Calendar.HOUR_OF_DAY, hourOf);
                        end.set(Calendar.MINUTE, minOf);

                        viewHolder.registerTimeEnd.setText(String.valueOf(end.getTime()).substring(11, 16));
                        item.setEndTime(String.valueOf(end.getTime()).substring(11, 16)+":00");
                        Log.d("시간", String.valueOf(end.getTime()).substring(11, 16)+":00");
                    }
                };
                new TimePickerDialog(view.getRootView().getContext(), onTimeSetListener, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), true).show();
            }
        });

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