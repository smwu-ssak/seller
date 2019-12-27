package com.example.ssak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Post.PostLoginResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// Customized by SY

public class KakaoSignupActivity extends Activity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("카카오 request", "접근 성공");
        requestMe();
    }

    protected void requestMe() { //유저의 정보를 받아오는

        List<String> keys = new ArrayList<>();
        keys.add("properties.id");
        keys.add("properties.profile_image");
        keys.add("kakao_account.email");

        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("카카오 로그인", "실패");
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response result) {
                Log.d("카카오 로그인", "성공");
                Log.d("카카오 user id", String.valueOf(result.getId()));
                Log.d("카카오 profile", String.valueOf(result.getKakaoAccount().getProfile()));
                Log.d("카카오 email", String.valueOf(result.getKakaoAccount().getEmail()));
                Log.e("토큰", Session.getCurrentSession().getTokenInfo().getAccessToken());
                String token = Session.getCurrentSession().getTokenInfo().getAccessToken();
                try {
                    requestLoginToServer(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestLoginToServer(String token) throws JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());

        Call<PostLoginResponse> call = networkService.postLoginResponse("application/json", gsonObject);
        call.enqueue(new Callback<PostLoginResponse>() {
            @Override
            public void onResponse(Call<PostLoginResponse> call, Response<PostLoginResponse> response) {
                if (response.isSuccessful()){
                    String id = response.body().data.token;
                    Log.d("카카오 id", id);
                    SharedPreferenceController.setMyId(getApplicationContext(), id);
                    // Log.d("카카오 아이디", SharedPreferenceController.getMyId(getApplicationContext()));
                    redirectMainActivity();
                }
            }
            @Override
            public void onFailure(Call<PostLoginResponse> call, Throwable t) {
                Log.e("로그인 통신 실패", t.toString());
                redirectLoginActivity();
            }
        });
    }

    private void redirectMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

}
