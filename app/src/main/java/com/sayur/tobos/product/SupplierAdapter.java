package com.sayur.tobos.product;

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

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.SupplierHolder> {
    Context c;
    List<FarmersAndSuppliers> data;

    public SupplierAdapter(Context c, List<FarmersAndSuppliers> data) {
        this.c = c;
        this.data = data;
    }

    @NonNull
    @Override
    public SupplierHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SupplierHolder(LayoutInflater.from(c).inflate(R.layout.item_supplier, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierHolder holder, int position) {
        FarmersAndSuppliers supplier = data.get(position);
        Glide.with(c).load(Constraint.BASE_URL + supplier.getImage()).into(holder.img);
        holder.name.setText(supplier.getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SupplierHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public SupplierHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgSupplier);
            name = v.findViewById(R.id.nameSupplier);
        }
    }
}
