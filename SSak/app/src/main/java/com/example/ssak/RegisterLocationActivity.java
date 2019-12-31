package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class RegisterLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        goToPreviousPage();
        goToNextPage();
    }

    public void goToPreviousPage() {
        LinearLayout btn = findViewById(R.id.register_location_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void goToNextPage() {
        LinearLayout btn = findViewById(R.id.register_location_act_next_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
