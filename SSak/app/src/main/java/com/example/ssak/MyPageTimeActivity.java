package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ssak.data.RegisterTimeData;

import java.util.ArrayList;

public class MyPageTimeActivity extends AppCompatActivity {

    ArrayList<RegisterTimeData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_time);

        connectAdapter();
    }

    public void connectAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_mypage_time);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        MyPageTimeAdapter adapter = new MyPageTimeAdapter(data);

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(15));
        adapter.notifyDataSetChanged();

        data.add(new RegisterTimeData(R.drawable.sellerregisteration_monday, true, 1, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_tuesday, true, 2, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_wednesday, true, 3, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_thursday, true, 4, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_friday, true, 5, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_saturday, true, 6, "시작 시간", "마감 시간"));
        data.add(new RegisterTimeData(R.drawable.sellerregisteration_sunday, true, 0, "시작 시간", "마감 시간"));
    }

}
