package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetMainResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.data.MainProductData;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class MainActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService();

    static ArrayList<MainProductData> data = new ArrayList();
    static MainProductAdapter adapter;

    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestKakaoProfileDataToServer();
        moveToMypage();
        uploadProducts();
        connectAdapter();
        UptoLayout();
    }

    public void UptoLayout() {
        circleImageView = findViewById(R.id.product_upload);
        circleImageView.bringToFront();
    }

    public void requestKakaoProfileDataToServer() {
        Call<GetKakaoProfileResponse> call = networkService.getKakaoProfileResponse("application/json", SharedPreferenceController.getMyId(getApplicationContext()));
        call.enqueue(new Callback<GetKakaoProfileResponse>() {
            @Override
            public void onResponse(Call<GetKakaoProfileResponse> call, Response<GetKakaoProfileResponse> response) {
                if (response.isSuccessful()) {
                    String imgUrl = response.body().data.userProfile;
                    String name = response.body().data.userName;

                    de.hdodenhof.circleimageview.CircleImageView imageView = findViewById(R.id.main_act_seller_img);
                    Glide.with(getApplicationContext())
                            .load(imgUrl)
                            .into(imageView);
                    TextView textView = findViewById(R.id.main_act_seller_name);
                    textView.setText(name);
                }
            }

            @Override
            public void onFailure(Call<GetKakaoProfileResponse> call, Throwable t) {

            }
        });
    }

    public void moveToMypage(){
        RelativeLayout button = findViewById(R.id.main_act_topbar_mypage_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });
    }

    public void uploadProducts() {
        ImageView button = findViewById(R.id.product_upload);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UploadProductActivity.class);
                startActivity(intent);
            }
        });
    }

    public void connectAdapter() {
        final RecyclerView recyclerView = findViewById(R.id.rv_main_product);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        Call<GetMainResponse> call = networkService.getMainResponse("application/json", SharedPreferenceController.getMyId(getApplicationContext()));
        call.enqueue(new Callback<GetMainResponse>() {
            @Override
            public void onResponse(Call<GetMainResponse> call, Response<GetMainResponse> response) {
                if (response.isSuccessful()) {
                    int status = response.body().status;
                    if (status == 200) {
                        TextView countTv = findViewById(R.id.main_act_product_quantity);
                        int count = response.body().data.count;
                        countTv.setText(String.valueOf(count));

                        data = response.body().data.mainList;
                        adapter = new MainProductAdapter(data);
                        recyclerView.setAdapter(adapter);
                        recyclerView.addItemDecoration(new RecyclerViewDecoration(15));
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetMainResponse> call, Throwable t) {

            }
        });

//        data.add(new MainProductData(R.drawable.rv_main_apple, "사과", 20, 1000, 500));
//        data.add(new MainProductData(R.drawable.rv_main_broccoli, "브로콜리", 10, 2000, 1200));
    }

    private long time = 0;
    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if (System.currentTimeMillis() >= 2000 + time) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 가기 버튼을 한 번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() < 2000 + time) {
            finish();
        }
    }

}

