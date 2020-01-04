package com.example.ssak;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.Patch.PatchKakaoProfileRequest;
import com.example.ssak.data.StoreData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.ssak.DB.SharedPreferenceController.clearMyId;

// Customized by SY

public class RegisterProfileActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");

    public static StoreData storeData = new StoreData();

    MultipartBody.Part image;
    de.hdodenhof.circleimageview.CircleImageView targetIv;
    EditText targetTv;

    private static final int REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);

        Log.d("내 아이디", SharedPreferenceController.getMyId(this));
        String token = SharedPreferenceController.getMyId(this);
        requestKakaoProfileDataToServer(token);
        uploadImage();

        goToLoginActivity();
        goToNextPage();
    }

    public void requestKakaoProfileDataToServer(String token) {
        Call<GetKakaoProfileResponse> call = networkService.getKakaoProfileResponse("application/json", token);
        call.enqueue(new Callback<GetKakaoProfileResponse>() {
            @Override
            public void onResponse(Call<GetKakaoProfileResponse> call, Response<GetKakaoProfileResponse> response) {
                if (response.isSuccessful()) {
                    String name = response.body().data.userName;
                    String imgUrl = response.body().data.userProfile;

                    targetIv = findViewById(R.id.register_profile_act_store_img);
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(targetIv);

                    targetTv = findViewById(R.id.register_profile_act_store_name);
                    targetTv.setText(name);
                }
            }

            @Override
            public void onFailure(Call<GetKakaoProfileResponse> call, Throwable t) {
                Log.v("통신 실패", t.toString());
            }
        });
    }

    public void uploadImage() {
        ImageView imageView = findViewById(R.id.register_profile_act_stor_img_edit_btn);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

                        targetIv = findViewById(R.id.register_profile_act_store_img);
                        targetIv.setImageBitmap(bitmap);
                    }
                } catch (Exception e) {

                }
            }
            else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void goToNextPage() {
        RelativeLayout btn = findViewById(R.id.register_profile_act_bottom_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(targetTv.getText());
                storeData.setName(name);
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
                                Intent intent = new Intent(getApplicationContext(), RegisterLocationActivity.class);
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

    public void goToLoginActivity() {
        RelativeLayout btn = findViewById(R.id.register_profile_act_back_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                clearMyId(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
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

}
