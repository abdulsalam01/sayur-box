package com.sayur.tobos.cart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    Context c;
    List<Carts.Data> data;
    onCartClick onCartClick;
    public CartAdapter(Context c, onCartClick onCartClick) {
        this.c = c;
        this.onCartClick = onCartClick;
    }

    public void Update(List<Carts.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(c).inflate(R.layout.item_cart_bottom_dialog, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Carts.Data cart = data.get(position);
        Glide.with(c).load(Constraint.BASE_URL + cart.getImage()).into(holder.img);
        holder.title.setText(cart.getName());
        holder.desc.setText(cart.getDescription());
        holder.qty.setText(cart.getQty());

        if(!cart.getDiscon().replace("%", "").equals("") && !cart.getDiscon().replace("%", "").equals("0")){
            holder.discon.setText(Constraint.rupiah(cart.getPrice()));
            holder.discon.setVisibility(View.VISIBLE);
            holder.price.setText( Constraint.rupiah( (cart.getDiscon().toString().substring(cart.getDiscon().length() -1 ).equals("%") ? "" + ( Integer.parseInt(cart.getPrice()) - ( (Integer.parseInt(cart.getPrice()) * Integer.parseInt(cart.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(cart.getPrice()) - Integer.parseInt(cart.getDiscon())) ) ) );
        }else{
            holder.discon.setVisibility(View.GONE);
            holder.price.setText(Constraint.rupiah(cart.getPrice()));
        }

        if(!cart.getMaxBuyDiscon().equals("0")){
            if(Integer.parseInt(cart.getQty()) >= Integer.parseInt(cart.getMaxBuyDiscon())){
                holder.plus.setEnabled(false);
                holder.plus.setColorFilter(Color.parseColor("#BCBCBC"));
            }else{
                holder.plus.setEnabled(true);
                holder.plus.setColorFilter(c.getResources().getColor(R.color.color_primary));
            }
        }else{
            holder.plus.setEnabled(true);
            holder.plus.setColorFilter(c.getResources().getColor(R.color.color_primary));
        }

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartClick.onPlus(cart);
            }
        });

        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(cart.getQty()) < 2){
                    onCartClick.onRemove(data, position);
                }else{
                    onCartClick.onMin(cart);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data!=null ? data.size() : 0;
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, desc, discon, price, per, qty;
        ImageView min, plus;
        public CartHolder(@NonNull View v) {
            super(v);
            img = v.findViewById(R.id.imgPesanan);
            title = v.findViewById(R.id.titlePesanan);
            desc = v.findViewById(R.id.descriptionItemCart);
            discon = v.findViewById(R.id.disconItemCart);
            discon.setPaintFlags(discon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            price = v.findViewById(R.id.pricePesanan);
            per = v.findViewById(R.id.perPesanan);
            qty = v.findViewById(R.id.qtyItemCart);
            min = v.findViewById(R.id.btnMinItemCart);
            plus = v.findViewById(R.id.btnPlusItemCart);
        }
    }

    public interface onCartClick{
        void onMin(Carts.Data mCartData);
        void onPlus(Carts.Data mCartData);
        void onRemove(List<Carts.Data> mCartList, int position);
    }
}
