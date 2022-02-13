package com.sayur.tobos.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayur.tobos.R;
import com.sayur.tobos.product.Product;
import com.sayur.tobos.product.ProductAdapter;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogHolder> implements ProductAdapter.onProductClick {
    Context c;
    List<Catalog.Data> data;
    public CatalogAdapter(Context c) {
        this.c = c;
    }

    onCatalogClick onCatalogClick;
    public void setCatalogListener(onCatalogClick onCatalogClick){
        this.onCatalogClick = onCatalogClick;
    }

    public void Update(List<Catalog.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CatalogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CatalogHolder(LayoutInflater.from(c).inflate(R.layout.item_catalog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogHolder holder, int position) {
        Catalog.Data catalog = data.get(position);
        holder.title.setText(catalog.getTitle());
        holder.subTitle.setText(catalog.getSubTitle());

        if(catalog.getProduct().size() > 0){
            holder.showAll.setVisibility(View.VISIBLE);
        }else{
            holder.showAll.setVisibility(View.GONE);
        }

        if(catalog.getImage() != null){
            holder.banner.setVisibility(View.VISIBLE);
            Glide.with(c).load(Constraint.BASE_URL  + catalog.getImage()).into(holder.banner);
        }else{
            holder.banner.setVisibility(View.GONE);
        }

        ProductAdapter productAdapter = new ProductAdapter(c, ProductAdapter.HORIZONTAL);
        holder.rvProduct.setAdapter(productAdapter);
        productAdapter.Update(catalog.getProduct());
        productAdapter.setProductListener(this);

        holder.showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCatalogClick.openPage(catalog);
            }
        });

        holder.banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCatalogClick.openPage(catalog);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class CatalogHolder extends RecyclerView.ViewHolder {
        TextView title, subTitle, showAll;
        ImageView banner;
        RecyclerView rvProduct;
        public CatalogHolder(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.titleCatalog);
            subTitle = v.findViewById(R.id.subTitleCatalog);
            showAll = v.findViewById(R.id.showCatalog);
            banner = v.findViewById(R.id.bannerCatalog);
            rvProduct = v.findViewById(R.id.productCatalog);
            rvProduct.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false));
            rvProduct.setHasFixedSize(true);
        }
    }

    public interface onCatalogClick{
        void openPage(Catalog.Data mCatalogData);
        void onDetailProduct(Product.Data mProductData);
        void onUpdateCartProduct(Product.Data mProductData);
        void onMinQtyProduct(Product.Data mProductData);
        void onPlusQtyProduct(Product.Data mProductData);
        void onRemoveQtyProduct(Product.Data mProductData);
    }

    @Override
    public void onDetail(Product.Data mProductData) {
        onCatalogClick.onDetailProduct(mProductData);
    }

    @Override
    public void onUpdateCart(Product.Data mProductData) {
        onCatalogClick.onUpdateCartProduct(mProductData);
    }

    @Override
    public void onMinQty(Product.Data mProductData) {
        onCatalogClick.onMinQtyProduct(mProductData);
    }

    @Override
    public void onPlusQty(Product.Data mProductData) {
        onCatalogClick.onPlusQtyProduct(mProductData);
    }

    @Override
    public void onRemoveQty(Product.Data mProductData) {
        onCatalogClick.onRemoveQtyProduct(mProductData);
    }
}
