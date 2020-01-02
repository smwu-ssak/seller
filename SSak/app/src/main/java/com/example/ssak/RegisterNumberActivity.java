package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import static com.example.ssak.RegisterProfileActivity.storeData;

// Customized by SY

public class RegisterNumberActivity extends AppCompatActivity {

    EditText telNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);

        goToPreviousPage();
        goToNextPage();
    }

    public void goToPreviousPage() {
        LinearLayout btn = findViewById(R.id.register_number_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goToNextPage() {
        LinearLayout btn = findViewById(R.id.register_number_act_next_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telNum = findViewById(R.id.register_number_act_store_tel_et);
                storeData.setTel(String.valueOf(telNum.getText()));
                Log.d("data", String.valueOf(storeData));

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
