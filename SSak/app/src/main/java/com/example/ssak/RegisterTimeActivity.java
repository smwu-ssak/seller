package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ssak.data.RegisterTimeData;
import com.example.ssak.data.StoreOperatingTimeData;
import static com.example.ssak.RegisterProfileActivity.storeData;

import java.util.ArrayList;

// Customized by SY

public class RegisterTimeActivity extends AppCompatActivity {

    StoreOperatingTimeData time[] = new StoreOperatingTimeData[7];
    ArrayList<RegisterTimeData> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_time);

        connectAdapter();
        goToPreviousPage();
        goToNextPage();
    }

    public void connectAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_register_time);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RegisterTimeAdapter adapter = new RegisterTimeAdapter(data);

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

    public void goToPreviousPage() {
        LinearLayout btn = findViewById(R.id.register_time_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterLocationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goToNextPage() {
        LinearLayout btn = findViewById(R.id.register_time_act_next_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i<data.size(); i++) {
                    if (data.get(i).getDay() == 0)
                        time[0] = new StoreOperatingTimeData(data.get(i).getDay(), data.get(i).getStartTime(), data.get(i).getEndTime());
                    else
                        time[i+1] = new StoreOperatingTimeData(data.get(i).getDay(), data.get(i).getStartTime(), data.get(i).getEndTime());
                }
                storeData.setTime(time);
//                for (int i=0; i<storeData.getStoreOperatingTimeData().size(); i++)
//                    Log.d("time", String.valueOf(storeData.getStoreOperatingTimeData().get(i).day));
                Intent intent = new Intent(getApplicationContext(), RegisterNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
