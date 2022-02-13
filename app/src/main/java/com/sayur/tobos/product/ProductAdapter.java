package com.sayur.tobos.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sayur.tobos.R;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    Context c;
    int type;
    List<Product.Data> data;

    public static final int HORIZONTAL = 0;
    public static final int GRID = 1;
    public ProductAdapter(Context c, int type) {
        this.c = c;
        this.type = type;
    }

    onProductClick onProductClick;
    public void setProductListener(onProductClick onProductClick){
        this.onProductClick = onProductClick;
    }

    public void Update(List<Product.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(c).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product.Data product = data.get(position);
        if(type == HORIZONTAL){
            holder.root.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            LinearLayout.LayoutParams pCard = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 290, c.getResources().getDisplayMetrics())
            );
            pCard.setMargins(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics())
            );
            holder.card.setLayoutParams(pCard);
            holder.img.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, c.getResources().getDisplayMetrics())
            ));
        }else{
            holder.root.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout.LayoutParams pCard = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 310, c.getResources().getDisplayMetrics())
            );
            pCard.setMargins(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, c.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, c.getResources().getDisplayMetrics())
            );
            holder.card.setLayoutParams(pCard);
            holder.img.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, c.getResources().getDisplayMetrics())
            ));
        }


        Glide.with(c).load(Constraint.BASE_URL + product.getImage()).into(holder.img);
        holder.name.setText(product.getName());
        if(!product.getDescription().equals("")){
            holder.info.setText(product.getDescription());
            holder.info.setVisibility(View.VISIBLE);
        }else{
            holder.info.setVisibility(View.GONE);
        }

        if(!product.getDiscon().replace("%", "").equals("") && !product.getDiscon().replace("%", "").equals("0")){
            holder.disconPrice.setText(Constraint.rupiah(product.getPrice()));
            holder.discon.setText("Save " + product.getDiscon());
            holder.disconPrice.setVisibility(View.VISIBLE);
            holder.discon.setVisibility(View.VISIBLE);
            holder.price.setText( Constraint.rupiah( (product.getDiscon().toString().substring(product.getDiscon().length() -1 ).equals("%")) ? "" + ( Integer.parseInt(product.getPrice()) - ( (Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(product.getPrice()) - Integer.parseInt(product.getDiscon())) ) );
        }else{
            holder.disconPrice.setVisibility(View.GONE);
            holder.discon.setVisibility(View.GONE);
            holder.price.setText(Constraint.rupiah(product.getPrice()));
        }
        holder.per.setText("/ " + product.getPer());

        if(!product.getMaxBuyDiscon().equals("0")){
            holder.promo.setText("Promo!! maximal " + product.getMaxBuyDiscon().toString());
            holder.promo.setVisibility(View.VISIBLE);
            if(product.getIsCart() != null && Integer.parseInt(product.getIsCart()) >= Integer.parseInt(product.getMaxBuyDiscon())){
                holder.plus.setEnabled(false);
                holder.plus.setColorFilter(Color.parseColor("#BCBCBC"));
            }else{
                holder.plus.setEnabled(true);
                holder.plus.setColorFilter(c.getResources().getColor(R.color.color_primary));
            }
        }else{
            holder.promo.setVisibility(View.GONE);
            holder.plus.setEnabled(true);
            holder.plus.setColorFilter(c.getResources().getColor(R.color.color_primary));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClick.onDetail(product);
            }
        });

        if(product.getIsCart() != null){
            holder.buy.setVisibility(View.GONE);
            holder.lnQty.setVisibility(View.VISIBLE);
        }else{
            holder.buy.setVisibility(View.VISIBLE);
            holder.lnQty.setVisibility(View.GONE);
        }

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClick.onUpdateCart(product);
            }
        });

        holder.qty.setText(product.getIsCart() != null ? product.getIsCart() : "0");

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(product.getIsCart()) < 2){
                    onProductClick.onRemoveQty(product);
                }else{
                    onProductClick.onMinQty(product);
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProductClick.onPlusQty(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        LinearLayout root;
        CardView card;
        ImageView img;
        TextView name, info, disconPrice, discon, price, per, promo;
        Button buy;

        LinearLayout lnQty;
        ImageView min, plus;
        TextView qty;
        public ProductHolder(@NonNull View v) {
            super(v);
            root = v.findViewById(R.id.rootProduct);
            card = v.findViewById(R.id.cardProduct);
            img = v.findViewById(R.id.imgProduct);
            name = v.findViewById(R.id.nameDetailProduct);
            info = v.findViewById(R.id.informationDetailProduct);
            disconPrice = v.findViewById(R.id.disconPriceDetailProduct);
            disconPrice.setPaintFlags(disconPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            discon = v.findViewById(R.id.disconDetailProduct);
            price = v.findViewById(R.id.priceDetailProductl);
            per = v.findViewById(R.id.perDetailProduct);
            promo = v.findViewById(R.id.promoDetailProduct);
            buy = v.findViewById(R.id.buyProduct);

            lnQty = v.findViewById(R.id.lnQty);
            min = v.findViewById(R.id.btnMinProduct);
            plus = v.findViewById(R.id.btnPlusProduct);
            qty = v.findViewById(R.id.qtyProduct);
        }
    }

    public interface onProductClick{
        void onDetail(Product.Data mProductData);
        void onUpdateCart(Product.Data mProductData);
        void onMinQty(Product.Data mProductData);
        void onPlusQty(Product.Data mProductData);
        void onRemoveQty(Product.Data mProductData);
    }

}
