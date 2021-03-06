package com.example.ssak.Network;

import com.example.ssak.Get.GetAlreadySignedupUserResponse;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetMainResponse;
import com.example.ssak.Get.GetStoreAddressResponse;
import com.example.ssak.Get.GetStoreInformationResponse;
import com.example.ssak.Patch.PatchKakaoProfileRequest;
import com.example.ssak.Patch.PatchStoreInformationRequest;
import com.example.ssak.Post.PostLoginResponse;
import com.example.ssak.Post.PostRegisterStoreRequest;
import com.example.ssak.Post.PostUploadProductRequest;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    // 카카오톡 프로필 수정
    @Multipart
    @PATCH("mypage")
    Call<PatchKakaoProfileRequest> patchKakaoProfileRequest (
            @Header("token") String token,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part profile
    );

    // 스토어 등록
    @POST("store")
    Call<PostRegisterStoreRequest> postRegisterStoreRequest (
            @Header("Content-type") String content_type,
            @Header("token") String token,
            @Body() JsonObject body
    );

    // 상품 등록
    @Multipart
    @POST("product")
    Call<PostUploadProductRequest> postUploadProductRequest (
            @Header("token") String token,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part image,
            @Part("quantity") int quantity,
            @Part("originPrice") int originPrice,
            @Part("comment") RequestBody comment,
            @Part("salePrice") int salePrice,
            @Part("expDate") RequestBody expDate
    );

    // 메인 페이지
    @GET("main/seller")
    Call<GetMainResponse> getMainResponse (
            @Header("Content-type") String content_type,
            @Header("token") String token
    );

    // 스토어 정보 조회
    @GET("mypage/seller")
    Call<GetStoreInformationResponse> getStoreInformationResponse (
            @Header("Content-type") String content_type,
            @Header("token") String token
    );

    // 스토어 전화번호 수정
    @PATCH("mypage/seller/tel")
    Call<PatchStoreInformationRequest> patchStoreNumberRequest (
            @Header("Content-type") String content_type,
            @Header("token") String token,
            @Body() JsonObject body
    );

    // 스토어 주소 수정
    @PATCH("mypage/seller/address")
    Call<PatchStoreInformationRequest> patchStoreAddressRequest (
            @Header("Content-type") String content_type,
            @Header("token") String token,
            @Body() JsonObject body
    );

    // 주소로 위도와 경도 추출
    @GET("address.json")
    Call<GetStoreAddressResponse> getStoreAddressResponse (
            @Header("Authorization") String authorization,
            @Query("query") String query
    );

}
