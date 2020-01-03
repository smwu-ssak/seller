package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ssak.data.MainProductData;

import java.util.ArrayList;

// Customized by SY

public class MainActivity extends AppCompatActivity {

    static ArrayList<MainProductData> data = new ArrayList();
    static MainProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadProducts();
        connectAdapter();
    }

    public void uploadProducts() {
        ImageView button = findViewById(R.id.product_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadProductActivity.class);
                startActivity(intent);
            }
        });
    }

    public void connectAdapter() {
        RecyclerView recyclerView = findViewById(R.id.rv_main_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        MainProductAdapter adapter = new MainProductAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(15));

        data.add(new MainProductData(R.drawable.rv_main_apple, "사과", 20, 1000, 500));
        data.add(new MainProductData(R.drawable.rv_main_broccoli, "브로콜리", 10, 2000, 1200));
    }

    private long time = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (System.currentTimeMillis() >= 2000 + time) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 가기 버튼을 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() < 2000 + time) {
            finish();
        }
    }
}

