package com.sayur.tobos.promo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.MainActivity;
import com.sayur.tobos.R;
import com.sayur.tobos.home.ApiHome;
import com.sayur.tobos.home.Slider;
import com.sayur.tobos.utils.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoFragment extends Fragment {
    RecyclerView rvPromo;
    ApiHome apiHome;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_promo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        apiHome = new Server().init().create(ApiHome.class);
        rvPromo = v.findViewById(R.id.rvPromo);
        rvPromo.setLayoutManager(new LinearLayoutManager(MainActivity.KONTEKS));
        rvPromo.setHasFixedSize(true);

        apiHome.getPromo().enqueue(new Callback<Slider>() {
            @Override
            public void onResponse(Call<Slider> call, Response<Slider> response) {
                if(response.body().getStatus().equals("200")){
                    rvPromo.setAdapter(new PromoAdapter(MainActivity.KONTEKS, response.body().getData()));
                }
            }

            @Override
            public void onFailure(Call<Slider> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
