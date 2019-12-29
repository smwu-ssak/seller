package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

// Customized by SY

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uploadProducts();
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
