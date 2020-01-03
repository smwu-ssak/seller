package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Post.PostRegisterStoreRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ssak.RegisterProfileActivity.storeData;

// Customized by SY

public class RegisterNumberActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

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

                JSONObject jsonObject = new JSONObject();
                JSONArray timeObject = new JSONArray();
                JSONObject object[] = new JSONObject[7];
                try {
                    jsonObject.put("name", storeData.getName());
                    jsonObject.put("address", storeData.getAddress());
                    jsonObject.put("lat", storeData.getLat());
                    jsonObject.put("log", storeData.getLog());
                    jsonObject.put("tel", storeData.getTel());
                    for (int i=0; i<storeData.getStoreOperatingTimeData().length; i++) {
                        object[i] = new JSONObject();
                        object[i].put("day", storeData.getStoreOperatingTimeData()[i].day);
                        object[i].put("startTime", storeData.getStoreOperatingTimeData()[i].startTime);
                        object[i].put("endTime", storeData.getStoreOperatingTimeData()[i].endTime);
                        timeObject.put(object[i]);
                    }
                    jsonObject.put("time", timeObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());

                Call<PostRegisterStoreRequest> call = networkService.postRegisterStoreRequest("application/json", SharedPreferenceController.getMyId(getApplicationContext()), gsonObject);
                call.enqueue(new Callback<PostRegisterStoreRequest>() {
                    @Override
                    public void onResponse(Call<PostRegisterStoreRequest> call, Response<PostRegisterStoreRequest> response) {
                        int status = response.body().status;
                        Log.d("status", String.valueOf(status));
                        if (status == 200) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<PostRegisterStoreRequest> call, Throwable t) {
                        Log.e("스토어 등록 실패", t.toString());
                    }
                });
            }
        });
    }
}
