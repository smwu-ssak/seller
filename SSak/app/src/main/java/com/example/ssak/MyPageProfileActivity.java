package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetStoreAddressResponse;
import com.example.ssak.Get.GetStoreInformationResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Patch.PatchKakaoProfileRequest;
import com.example.ssak.Patch.PatchStoreInformationRequest;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class MyPageProfileActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");
    NetworkService kakaoService = applicationController.buildNetworkService("https://dapi.kakao.com/v2/local/search/");

    private static final int REQUEST_CODE = 100;
    static final int SEARCH_ADDRESS_ACTIVITY = 200;
    TextView address;

    MultipartBody.Part image;
    de.hdodenhof.circleimageview.CircleImageView circleImageView;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_profile);

        requestKakaoProfileDataToServer();
        requestStoreInformationToServer();

        uploadImage();
        moveToAddressEdit();
        moveToTelEdit();
        moveToTimeEdit();
        goToPreviousPage();
    }

    public void uploadImage() {
        ImageView imageView = findViewById(R.id.mypage_profile_act_user_img_edit_btn);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestReadExternalStoragePermission();
            }
        });
    }

    public void showAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    if (data != null) {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // BitmapFactory.Options options = new BitmapFactory.Options();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                        inputStream.close();

                        String imgURI = getRealPathFromURI(data.getData());
                        File photo = new File(imgURI);

                        RequestBody photoBody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);
                        image = MultipartBody.Part.createFormData("profile", photo.getName(), photoBody);

                        circleImageView = findViewById(R.id.mypage_profile_act_user_img);
                        circleImageView.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {

                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == SEARCH_ADDRESS_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                String str = data.getExtras().getString("data");
                if (str != null) {
                    responseStoreAddressToKakaoServer(str);
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public void requestReadExternalStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 사용자에게 권한을 왜 허용해야 되는지에 대한 메시지 추가
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        }
        else
            showAlbum();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAlbum();
            }
            else
                finish();
        }
    }

    public void goToPreviousPage() {
        RelativeLayout btn = findViewById(R.id.mypage_profile_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(editText.getText());
                RequestBody nameBody = RequestBody.create(MediaType.parse("text/plain"), name);

                Call<PatchKakaoProfileRequest> call = networkService.patchKakaoProfileRequest(SharedPreferenceController.getMyId(getApplicationContext()), nameBody, image);
                Log.d("status", "status");
                call.enqueue(new Callback<PatchKakaoProfileRequest>() {
                    @Override
                    public void onResponse(Call<PatchKakaoProfileRequest> call, Response<PatchKakaoProfileRequest> response) {
                        if (response.isSuccessful()) {
                            int status = response.body().status;
                            Log.d("status", String.valueOf(status));
                            if (status == 200) {
                                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Log.d("fail", "fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<PatchKakaoProfileRequest> call, Throwable t) {
                        Log.e("Upload error:", t.getMessage());
                    }
                });

            }
        });
    }

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
                    editText = findViewById(R.id.mypage_profile_act_store_name_et);
                    editText.setText(name);
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

}
