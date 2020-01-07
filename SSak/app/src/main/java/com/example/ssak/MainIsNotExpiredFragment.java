package com.example.ssak;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ssak.DB.SharedPreferenceController;
import com.example.ssak.Get.GetMainResponse;
import com.example.ssak.Network.ApplicationController;
import com.example.ssak.Network.NetworkService;
import com.example.ssak.data.MainProductData;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customized by SY

public class MainIsNotExpiredFragment extends Fragment {

    ApplicationController applicationController = new ApplicationController();
    NetworkService networkService = applicationController.buildNetworkService("http://52.79.193.54:3000/");

    static ArrayList<MainProductData> data = new ArrayList();
    static MainProductAdapter adapter;

    public MainIsNotExpiredFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_is_not_expired, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.isNotExpired_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        Call<GetMainResponse> call = networkService.getMainResponse("application/json", SharedPreferenceController.getMyId(view.getContext()));
        call.enqueue(new Callback<GetMainResponse>() {
            @Override
            public void onResponse(Call<GetMainResponse> call, Response<GetMainResponse> response) {
                if (response.isSuccessful()) {
                    int status = response.body().status;
                    Log.d("status", String.valueOf(status));
                    if (status == 200) {
                        data = response.body().data.possibleList;
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

        return view;
    }

}
