package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import static com.example.ssak.RegisterProfileActivity.storeData;

// Customized by SY

public class RegisterLocationActivity extends AppCompatActivity {

    static final int SEARCH_ADDRESS_ACTIVITY = 200;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        searchAddress();
        goToPreviousPage();
        goToNextPage();
    }

    public void searchAddress() {
        address = findViewById(R.id.register_location_act_address_et);
        Button btn = findViewById(R.id.register_location_act_search_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterLocationActivity.this, RegisterLocationWebViewActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String data = intent.getExtras().getString("data");
                if (data != null)
                    address.setText(data);
            }
        }
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
                storeData.setAddress(String.valueOf(address.getText()));
                // 위도, 경도 수정
                storeData.setLat((float)37.544163);
                storeData.setLog((float)126.965948);
                // 위도, 경도 수정
                Intent intent = new Intent(getApplicationContext(), RegisterTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
