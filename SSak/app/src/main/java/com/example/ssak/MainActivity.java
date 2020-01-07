package com.example.ssak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetKakaoProfileResponse;
import com.example.ssak.Get.GetMainResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.data.MainProductData;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class MainActivity extends AppCompatActivity {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");

    static ArrayList<MainProductData> data = new ArrayList();
    static MainProductAdapter adapter;

    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMenubar();
        requestKakaoProfileDataToServer();
        moveToMypage();
        uploadProducts();
        UptoLayout();
    }

    public void setMenubar() {
        TabLayout tabs = findViewById(R.id.main_tabs);
        tabs.addTab(tabs.newTab().setText("구조 요청 현황"));
        tabs.addTab(tabs.newTab().setText("구조 완료 현황"));
        tabs.setTabGravity(tabs.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.main_viewpager);
        final MainFragmentPagerAdapter pagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager(), 2);
        viewPager.setAdapter(pagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
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

