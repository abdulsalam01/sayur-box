package com.sayur.tobos.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.sayur.tobos.R;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class SliderAdapter extends PagerAdapter {
    Context c;
    List<Slider.Data> data;

    public SliderAdapter(Context c, List<Slider.Data> data) {
        this.c = c;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        View v = LayoutInflater.from(c).inflate(R.layout.item_slider, container, false);
        ImageView img = v.findViewById(R.id.imgSlider);
        Glide.with(c).load(Constraint.BASE_URL + data.get(position).getImage()).into(img);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View)object);
    }
}
