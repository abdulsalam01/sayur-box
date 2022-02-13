package com.sayur.tobos.promo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayur.tobos.R;
import com.sayur.tobos.home.Slider;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoHolder> {
    Context c;
    List<Slider.Data> data;

    public PromoAdapter(Context c, List<Slider.Data> data) {
        this.c = c;
        this.data = data;
    }

    @NonNull
    @Override
    public PromoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PromoHolder(LayoutInflater.from(c).inflate(R.layout.item_promo, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PromoHolder holder, int position) {
        Glide.with(c).load(Constraint.BASE_URL + data.get(position).getImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PromoHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public PromoHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgSlider);
        }
    }
}
