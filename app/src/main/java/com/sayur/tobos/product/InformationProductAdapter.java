package com.sayur.tobos.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayur.tobos.R;
import com.sayur.tobos.cart.Carts;

import java.util.List;

public class InformationProductAdapter extends RecyclerView.Adapter<InformationProductAdapter.InformationProductHolder> {
    Context c;
    List<InformationProduct> data;

    public InformationProductAdapter(Context c) {
        this.c = c;
    }

    public void Update(List<InformationProduct> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InformationProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InformationProductHolder(LayoutInflater.from(c).inflate(R.layout.item_information_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformationProductHolder holder, int position) {
        InformationProduct informationProduct = data.get(position);
        holder.title.setText(informationProduct.getTitle());
        if(position == 0){
            holder.desc.setVisibility(View.VISIBLE);
        }

        if(position < data.size() - 1){
            holder.desc.setText(informationProduct.getDesc());
        }else{
            SupplierAdapter supplierAdapter = new SupplierAdapter(c, new Gson().fromJson(informationProduct.getDesc(), new TypeToken<List<FarmersAndSuppliers>>(){}.getType()));
            holder.rvSupplier.setAdapter(supplierAdapter);
        }


        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position < data.size() - 1){
                    if(holder.desc.getVisibility() != View.VISIBLE){
                        holder.desc.setVisibility(View.VISIBLE);
//                    holder.desc.setAnimation(AnimationUtils.loadAnimation(c, R.anim.hide_information));
                    }else{
//                    holder.desc.setAnimation(AnimationUtils.loadAnimation(c, R.anim.hide_information));
                        holder.desc.setVisibility(View.GONE);
                    }
                }else{
                    if(holder.rvSupplier.getVisibility() != View.VISIBLE){
                        holder.rvSupplier.setVisibility(View.VISIBLE);
//                    holder.desc.setAnimation(AnimationUtils.loadAnimation(c, R.anim.hide_information));
                    }else{
//                    holder.desc.setAnimation(AnimationUtils.loadAnimation(c, R.anim.hide_information));
                        holder.rvSupplier.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }

    public class InformationProductHolder extends RecyclerView.ViewHolder {
        TextView title, desc;
        ImageView icon;
        RecyclerView rvSupplier;
        public InformationProductHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.titleInfo);
            desc = v.findViewById(R.id.descInfo);
            icon = v.findViewById(R.id.iconInfo);
            rvSupplier = v.findViewById(R.id.rvSupplier);
            FlexboxLayoutManager flex = new FlexboxLayoutManager(c);
            flex.setJustifyContent(JustifyContent.CENTER);
            flex.setAlignItems(AlignItems.CENTER);
            flex.setFlexDirection(FlexDirection.ROW);
            flex.setFlexWrap(FlexWrap.WRAP);
            rvSupplier.setLayoutManager(flex);
            rvSupplier.setHasFixedSize(true);
        }
    }
}
