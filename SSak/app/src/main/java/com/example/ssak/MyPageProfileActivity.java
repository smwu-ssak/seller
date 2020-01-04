package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetStoreAddressResponse;
import com.example.ssak.Get.GetStoreInformationResponse;
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

public class MyPageProfileActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");
    NetworkService kakaoService = applicationController.buildNetworkService("https://dapi.kakao.com/v2/local/search/");

    static final int SEARCH_ADDRESS_ACTIVITY = 200;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_profile);

        requestKakaoProfileDataToServer();
        requestStoreInformationToServer();

        moveToAddressEdit();
        moveToTelEdit();
        moveToTimeEdit();
        goToPreviousPage();
    }

    // Customized by SY
    public void moveToAddressEdit() {
        address = findViewById(R.id.mypage_profile_act_address);

        Button button = findViewById(R.id.mypage_profile_act_address_search_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageProfileActivity.this, RegisterLocationWebViewActivity.class);
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String data = intent.getExtras().getString("data");
                if (data != null) {
                    responseStoreAddressToKakaoServer(data);
                }
            }
        }
    }

    public void responseStoreAddressToKakaoServer(final String address) {

        // 위도, 경도 추출
        Call<GetStoreAddressResponse> kakaoCall = kakaoService.getStoreAddressResponse("KakaoAK 90b989bdd867ac69136ad3d63551bf3e", address.substring(7));
        kakaoCall.enqueue(new Callback<GetStoreAddressResponse>() {
            @Override
            public void onResponse(Call<GetStoreAddressResponse> call, Response<GetStoreAddressResponse> response) {
                if (response.isSuccessful()) {
                    String x = response.body().documents.get(0).getX();
                    String y = response.body().documents.get(0).getY();
                    Log.d("위치", x);
                    Log.d("위치", y);
                    float lat = Float.parseFloat(y);
                    float log = Float.parseFloat(x);
                    responseStoreAddressToServer(address.substring(7), lat, log);
                }
            }

            @Override
            public void onFailure(Call<GetStoreAddressResponse> call, Throwable t) {

            }
        });

    }

    public void responseStoreAddressToServer(final String location, float lat, float log) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("address", location);
            jsonObject.put("lat", lat);
            jsonObject.put("log", log);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());

        Call<PatchStoreInformationRequest> call = networkService.patchStoreAddressRequest("application/json", SharedPreferenceController.getMyId(getApplicationContext()), gsonObject);
        call.enqueue(new Callback<PatchStoreInformationRequest>() {
            @Override
            public void onResponse(Call<PatchStoreInformationRequest> call, Response<PatchStoreInformationRequest> response) {
                if (response.isSuccessful()) {
                    int status = response.body().status;
                    if (status == 200) {
                        TextView tv = findViewById(R.id.mypage_profile_act_address);
                        tv.setText(location);
                    }
                }
            }

            @Override
            public void onFailure(Call<PatchStoreInformationRequest> call, Throwable t) {

            }
        });
    }

    // Customized by 민승
    public void moveToTelEdit() {
        Button button = findViewById(R.id.mypage_profile_act_tel_modify_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageNumActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void moveToTimeEdit(){
        TextView button = findViewById(R.id.mypage_profile_act_time);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    // Customized by 민승

    public void requestKakaoProfileDataToServer() {
        Call<GetKakaoProfileResponse> call = networkService.getKakaoProfileResponse("application/json", SharedPreferenceController.getMyId(getApplicationContext()));
        call.enqueue(new Callback<GetKakaoProfileResponse>() {
            @Override
            public void onResponse(Call<GetKakaoProfileResponse> call, Response<GetKakaoProfileResponse> response) {
                if (response.isSuccessful()) {
                    String imgUrl = response.body().data.userProfile;
                    String name = response.body().data.userName;

                    de.hdodenhof.circleimageview.CircleImageView imageView = findViewById(R.id.mypage_profile_act_user_img);
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(imageView);
                    TextView textView = findViewById(R.id.mypage_profile_act_store_name);
                    textView.setText(name);
                }
            }

            @Override
            public void onFailure(Call<GetKakaoProfileResponse> call, Throwable t) {

            }
        });

    }

    public void requestStoreInformationToServer() {
        Call<GetStoreInformationResponse> call = networkService.getStoreInformationResponse("application/json", SharedPreferenceController.getMyId(getApplicationContext()));
        call.enqueue(new Callback<GetStoreInformationResponse>() {
            @Override
            public void onResponse(Call<GetStoreInformationResponse> call, Response<GetStoreInformationResponse> response) {
                if (response.isSuccessful()) {
                    int status = response.body().status;
                    if (status == 200) {
                        String address = response.body().data.address;
                        TextView addressTv = findViewById(R.id.mypage_profile_act_address);
                        addressTv.setText(address);

                        String tel = response.body().data.tel;
                        TextView telTv = findViewById(R.id.mypage_profile_act_tel);
                        telTv.setText(tel);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetStoreInformationResponse> call, Throwable t) {

            }
        });
    }


    public void goToPreviousPage() {
        RelativeLayout btn = findViewById(R.id.mypage_profile_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
