package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RegisterTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_time);

        goToPreviousPage();
        goToNextPage();
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
                Intent intent = new Intent(getApplicationContext(), RegisterNumberActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
