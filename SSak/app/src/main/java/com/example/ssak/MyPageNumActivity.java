package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Patch.PatchStoreInformationRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class MyPageNumActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_num);

        goToPreviousPage();
        finishTelNum();
    }

    public void goToPreviousPage() {
        RelativeLayout btn = findViewById(R.id.mypage_profile_num_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void finishTelNum() {
        LinearLayout btn = findViewById(R.id.mypage_tel_act_next_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.mypage_tel_act_store_tel_et);
                String telNum = String.valueOf(editText.getText());

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("tel", telNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());

                Call<PatchStoreInformationRequest> call = networkService.patchStoreNumberRequest("application/json", SharedPreferenceController.getMyId(getApplicationContext()), gsonObject);
                call.enqueue(new Callback<PatchStoreInformationRequest>() {
                    @Override
                    public void onResponse(Call<PatchStoreInformationRequest> call, Response<PatchStoreInformationRequest> response) {
                        if (response.isSuccessful()) {
                            int status = response.body().status;
                            if (status == 200) {
                                Intent intent = new Intent(getApplicationContext(), MyPageProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PatchStoreInformationRequest> call, Throwable t) {

                    }
                });

            }
        });

    }
}
