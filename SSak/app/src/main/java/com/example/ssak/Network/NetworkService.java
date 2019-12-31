package com.example.ssak.Network;

import com.example.ssak.Get.GetAlreadySignedupUserResponse;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Post.PostLoginResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NetworkService {

    // Customized by SY

    // 카카오톡 로그인
    @POST("user")
    Call<PostLoginResponse> postLoginResponse (
            @Header("Content-type") String content_type,
            @Body() JsonObject body
    );

    // 스토어 등록한 회원 조회
    @GET("user")
    Call<GetAlreadySignedupUserResponse> getAlreadySignedupUserResponse (
            @Header("Content-type") String content_type,
            @Header("token") String token
    );

    // 카카오톡 프로필 조회
    @GET("mypage")
    Call<GetKakaoProfileResponse> getKakaoProfileResponse (
            @Header("Content-type") String content_type,
            @Header("token") String token
    );

}
