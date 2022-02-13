package com.sayur.tobos.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayur.tobos.R;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    Context c;
    List<Category.Data> data;
    onCategoryClick onCategoryClick;
    public CategoryAdapter(Context c, onCategoryClick onCategoryClick) {
        this.c = c;
        this.onCategoryClick = onCategoryClick;
    }

    public void Update(List<Category.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryHolder(LayoutInflater.from(c).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Category.Data category = data.get(position);
        Glide.with(c).load(Constraint.BASE_URL + category.getImage()).into(holder.img);
        holder.name.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCategoryClick.onPageCategory(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CategoryHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public CategoryHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgCategory);
            name = v.findViewById(R.id.nameCategory);
        }
    }

    public interface onCategoryClick{
        void onPageCategory(Category.Data mCategoryData);
    }
}
