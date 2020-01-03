package com.example.ssak;

// Customized by 민승

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

public class MyPageTimeAdapter extends RecyclerView.Adapter<MyPageTimeAdapter.MyPageTimeViewHolder>{

    ArrayList<RegisterTimeData> items;

    public MyPageTimeAdapter(ArrayList<RegisterTimeData> items) {
        this.items = items;
    }

    @Override
    public MyPageTimeAdapter.MyPageTimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.rv_item_my_page_time, viewGroup, false);
        return new MyPageTimeAdapter.MyPageTimeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyPageTimeAdapter.MyPageTimeViewHolder viewHolder, int position) {
        final RegisterTimeData item = items.get(position);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getImage())
                .into(viewHolder.mypageTimeDay);
        viewHolder.mypageTimeStart.setText(item.getStartTime());
        viewHolder.mypageTimeEnd.setText(item.getEndTime());
        viewHolder.mypageTimeLine.setVisibility(View.VISIBLE);
        viewHolder.mypageTimeIsOpen.setVisibility(View.INVISIBLE);

        viewHolder.mypageTimeDay.setOnClickListener(new View.OnClickListener() {
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
                                .into(viewHolder.mypageTimeDay);
                        viewHolder.mypageTimeStart.setVisibility(View.INVISIBLE);
                        viewHolder.mypageTimeEnd.setVisibility(View.INVISIBLE);
                        viewHolder.mypageTimeLine.setVisibility(View.INVISIBLE);
                        viewHolder.mypageTimeIsOpen.setVisibility(View.VISIBLE);
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
                                .into(viewHolder.mypageTimeDay);
                        viewHolder.mypageTimeStart.setVisibility(View.VISIBLE);
                        viewHolder.mypageTimeEnd.setVisibility(View.VISIBLE);
                        viewHolder.mypageTimeLine.setVisibility(View.VISIBLE);
                        viewHolder.mypageTimeIsOpen.setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }
        });

        final Calendar start = Calendar.getInstance();
        viewHolder.mypageTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOf, int minOf) {
                        start.set(Calendar.HOUR_OF_DAY, hourOf);
                        start.set(Calendar.MINUTE, minOf);

                        viewHolder.mypageTimeStart.setText(String.valueOf(start.getTime()).substring(11, 16));
                        item.setStartTime(String.valueOf(start.getTime()).substring(11, 16)+":00");
                        Log.d("시간", String.valueOf(start.getTime()).substring(11, 16)+":00");
                    }
                };
                new TimePickerDialog(view.getRootView().getContext(), onTimeSetListener, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true).show();
            }
        });

        final Calendar end = Calendar.getInstance();
        viewHolder.mypageTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOf, int minOf) {
                        end.set(Calendar.HOUR_OF_DAY, hourOf);
                        end.set(Calendar.MINUTE, minOf);

                        viewHolder.mypageTimeEnd.setText(String.valueOf(end.getTime()).substring(11, 16));
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

    static class MyPageTimeViewHolder extends RecyclerView.ViewHolder {

        protected ImageView mypageTimeDay;
        protected TextView mypageTimeStart;
        protected TextView mypageTimeEnd;
        protected TextView mypageTimeLine;
        protected TextView mypageTimeIsOpen;

        public MyPageTimeViewHolder(final View itemView) {
            super(itemView);

            mypageTimeDay = itemView.findViewById(R.id.rv_item_mypage_time_day);
            mypageTimeStart = itemView.findViewById(R.id.rv_item_mypage_time_start);
            mypageTimeEnd = itemView.findViewById(R.id.rv_item_mypage_time_end);
            mypageTimeLine = itemView.findViewById(R.id.rv_item_time_fix);
            mypageTimeIsOpen = itemView.findViewById(R.id.rv_item_mypage_time_open);
        }
    }


}
