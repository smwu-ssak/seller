package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetStoreInformationResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPageProfileActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_profile);

        requestKakaoProfileDataToServer();
        requestStoreInformationToServer();
        moveToTelEdit();
        moveToTimeEdit();
        goToPreviousPage();
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
