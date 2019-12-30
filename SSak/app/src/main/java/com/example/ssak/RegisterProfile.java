package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterProfile extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        Log.d("내 아이디", SharedPreferenceController.getMyId(this));
        String token = SharedPreferenceController.getMyId(this);
        requestKakaoProfileDataToServer(token);

    }

    public void requestKakaoProfileDataToServer(String token) {
        Call<GetKakaoProfileResponse> call = networkService.getKakaoProfileResponse("application/json", token);
        call.enqueue(new Callback<GetKakaoProfileResponse>() {
            @Override
            public void onResponse(Call<GetKakaoProfileResponse> call, Response<GetKakaoProfileResponse> response) {
                if (response.isSuccessful()) {
                    String name = response.body().data.userName;
                    String imgUrl = response.body().data.userProfile;

                    de.hdodenhof.circleimageview.CircleImageView targetIv = (de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.register_profile_act_store_img);
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(targetIv);
                    EditText targetTv = (EditText)findViewById(R.id.register_profile_act_store_name);
                    targetTv.setText(name);
                }
            }

            @Override
            public void onFailure(Call<GetKakaoProfileResponse> call, Throwable t) {
                Log.v("통신 실패", t.toString());
            }
        });
    }
}
