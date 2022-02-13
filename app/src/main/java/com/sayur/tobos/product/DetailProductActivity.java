package com.sayur.tobos.product;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.sayur.tobos.MainActivity;
import com.sayur.tobos.R;
import com.sayur.tobos.cart.ApiCart;
import com.sayur.tobos.cart.CartAdapter;
import com.sayur.tobos.cart.Carts;
import com.sayur.tobos.cart.SingleCart;
import com.sayur.tobos.checkout.CheckoutActivity;
import com.sayur.tobos.users.LoginActivity;
import com.sayur.tobos.utils.ApiRespond;
import com.sayur.tobos.utils.Constraint;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity implements CartAdapter.onCartClick {
    public static DetailProductActivity KONTEKS;
    ApiProduct apiProduct;

    ImageView back, img;
    TextView name, desc, priceDiscon, discon, price, per, promo;
    RecyclerView rvInformation;
    InformationProductAdapter informationProductAdapter;
    NestedScrollView nested;
    LinearLayout lnQty;
    ImageView min, plus;
    TextView qty;
    Button buy;
    Product.Data product;

    BottomSheetBehavior bottomSheetBehavior;
    View rootCart;
    TextView totalQty, totalPrice;
    Button checkout;
    RecyclerView rvCart;
    CartAdapter cartAdapter;
    List<Carts.Data> cartData = new ArrayList<>();
    ApiCart apiCart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        KONTEKS = DetailProductActivity.this;
        apiProduct = new Server().init().create(ApiProduct.class);
        apiCart = new Server().init().create(ApiCart.class);

        back = findViewById(R.id.backDetailProduct);
        img = findViewById(R.id.imgDetailProduct);
        name = findViewById(R.id.nameDetailProduct);
        desc = findViewById(R.id.informationDetailProduct);
        priceDiscon = findViewById(R.id.disconPriceDetailProduct);
        priceDiscon.setPaintFlags(priceDiscon.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        discon = findViewById(R.id.disconDetailProduct);
        price = findViewById(R.id.priceDetailProductl);
        per = findViewById(R.id.perDetailProduct);
        promo = findViewById(R.id.promoDetailProduct);
        rvInformation = findViewById(R.id.rvInfo);
        rvInformation.setLayoutManager(new LinearLayoutManager(this));
        rvInformation.setHasFixedSize(true);
        informationProductAdapter = new InformationProductAdapter(this);
        rvInformation.setAdapter(informationProductAdapter);
        nested = findViewById(R.id.nested);
        lnQty = findViewById(R.id.lnQty);
        min = findViewById(R.id.btnMinProduct);
        plus = findViewById(R.id.btnPlusProduct);
        qty = findViewById(R.id.qtyProduct);
        buy = findViewById(R.id.btnBuy);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        get();

        /*CART*/
        rootCart = findViewById(R.id.lnCart);
        rootCart.setVisibility(View.GONE);
        totalQty = rootCart.findViewById(R.id.totalQtyCart);
        totalPrice = rootCart.findViewById(R.id.totalCart);
        checkout = rootCart.findViewById(R.id.checkCart);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new Sesi(getApplicationContext()).get().getAddress() == null || new Sesi(getApplicationContext()).get().getAddress().equals("")){
                    Intent i = new Intent();
                    setResult(MainActivity.ADDRESS_CEK, i);
                    finish();
                }else{
                    Intent iCheckout = new Intent(getApplicationContext(), CheckoutActivity.class);
                    iCheckout.putExtra("data", new Gson().toJson(cartData).toString());
                    startActivity(iCheckout);
                }
            }
        });
        rvCart = rootCart.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setHasFixedSize(true);
        cartAdapter = new CartAdapter(this, this);
        rvCart.setAdapter(cartAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(rootCart);
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                switch (newState){
//                    case BottomSheetBehavior.STATE_HIDDEN:
//                        Toast.makeText(getApplicationContext(), "HIDDEN", Toast.LENGTH_SHORT).show();
//                        break;
//                    case BottomSheetBehavior.STATE_EXPANDED:
//                        Toast.makeText(getApplicationContext(), "EXPANDED", Toast.LENGTH_SHORT).show();
//                        break;
//                    case BottomSheetBehavior.STATE_COLLAPSED:
//                        Toast.makeText(getApplicationContext(), "COLLAPSED", Toast.LENGTH_SHORT).show();
//                        break;
//                    case BottomSheetBehavior.STATE_DRAGGING:
//                        Toast.makeText(getApplicationContext(), "DRAGGING", Toast.LENGTH_SHORT).show();
//                        break;
//                    case BottomSheetBehavior.STATE_SETTLING:
//                        Toast.makeText(getApplicationContext(), "SETTLING", Toast.LENGTH_SHORT).show();
//                        break;
//                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCart(product);
            }
        });

        min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(product.getIsCart()) < 2){
                    onRemoveQty(product);
                }else{
                    onMinQty(product);
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlusQty(product);
            }
        });
    }

    public void get(){
        apiProduct.getProductBy((new Sesi(this).valid() ? new Sesi(this).get().getId() : "0"), getIntent().getStringExtra("slug")).enqueue(new Callback<SingleProduct>() {
            @Override
            public void onResponse(Call<SingleProduct> call, Response<SingleProduct> response) {
                if(response.body().getStatus().equals("200")){
                    product = response.body().getData();
                    Glide.with(getApplicationContext()).load(Constraint.BASE_URL + product.getImage()).into(img);
                    name.setText(product.getName());
                    desc.setText(product.getDescription());
                    if(!product.getDiscon().replace("%", "").equals("") && !product.getDiscon().replace("%", "").equals("0")){
                        priceDiscon.setText(Constraint.rupiah(product.getPrice()));
                        discon.setText("Save " + product.getDiscon());
                        priceDiscon.setVisibility(View.VISIBLE);
                        discon.setVisibility(View.VISIBLE);
                        price.setText( Constraint.rupiah( (product.getDiscon().toString().substring(product.getDiscon().length() -1 ).equals("%")) ? "" + ( Integer.parseInt(product.getPrice()) - ( (Integer.parseInt(product.getPrice()) * Integer.parseInt(product.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(product.getPrice()) - Integer.parseInt(product.getDiscon())) ) );
                    }else{
                        priceDiscon.setVisibility(View.GONE);
                        discon.setVisibility(View.GONE);
                        price.setText(Constraint.rupiah(product.getPrice()));
                    }
                    per.setText("/ " + product.getPer());

                    if(!product.getMaxBuyDiscon().equals("0")){
                        promo.setText("Promo!! maximal " + product.getMaxBuyDiscon().toString());
                        promo.setVisibility(View.VISIBLE);
                        if(product.getIsCart() != null && Integer.parseInt(product.getIsCart()) >= Integer.parseInt(product.getMaxBuyDiscon())){
                            plus.setEnabled(false);
                            plus.setColorFilter(Color.parseColor("#BCBCBC"));
                        }else{
                            plus.setEnabled(true);
                            plus.setColorFilter(getResources().getColor(R.color.color_primary));
                        }
                    }else{
                        promo.setVisibility(View.GONE);
                        plus.setEnabled(true);
                        plus.setColorFilter(getResources().getColor(R.color.color_primary));
                    }

                    List<InformationProduct> dataInfo = new ArrayList<>();
                    dataInfo.add(new InformationProduct("Product Information", product.getInformation().getProductInformation()));
                    dataInfo.add(new InformationProduct("Nutrition and Benefits", product.getInformation().getNutritionAndBenefits()));
                    dataInfo.add(new InformationProduct("How to Save", product.getInformation().getHowToSave()));
                    dataInfo.add(new InformationProduct("Farmers and Suppliers", new Gson().toJson(product.getInformation().getFarmersAndSuppliers()).toString()));
                    informationProductAdapter.Update(dataInfo);

                    if(product.getIsCart() != null){
                        buy.setVisibility(View.GONE);
                        lnQty.setVisibility(View.VISIBLE);
                    }else{
                        buy.setVisibility(View.VISIBLE);
                        lnQty.setVisibility(View.GONE);
                    }

                    qty.setText(product.getIsCart() != null ? product.getIsCart() : "0");
                    getCart();
                }
            }

            @Override
            public void onFailure(Call<SingleProduct> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCart(){
        apiCart.getCart(new Sesi(this).valid() ? new Sesi(this).get().getId() : "0").enqueue(new Callback<Carts>() {
            @Override
            public void onResponse(Call<Carts> call, Response<Carts> response) {
                cartData.clear();
                if(response.body().getStatus().equals("200")){
                    cartData = response.body().getData();
                    cartAdapter.Update(cartData);
                    totalQty.setText(response.body().getData().size() + "");
                    getTotalPriceCart();

                    if(response.body().getData().size() > 0){
                        rootCart.setVisibility(View.VISIBLE);
                        nested.setPadding(0, 0, 0, (int) (55* getResources().getDisplayMetrics().density + 0.5f));
//                        lnHome.getLayoutParams().
                    }
                }else{
                    cartAdapter.notifyDataSetChanged();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    rootCart.setVisibility(View.GONE);
                    nested.setPadding(0, 0, 0, 0);
//                    lnHome.setPadding(0, 0, 0, (int) (55* getActivity().getResources().getDisplayMetrics().density + 0.5f));
                }
            }

            @Override
            public void onFailure(Call<Carts> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateBuytoQty(Carts.Data mCartDataUpdate){
        if(mCartDataUpdate.getProductId().equals(product.getId())){
            qty.setText(mCartDataUpdate.getQty() != null ? mCartDataUpdate.getQty() : "0");
            product.setIsCart(mCartDataUpdate.getQty() != null ? mCartDataUpdate.getQty() : null);
            if(mCartDataUpdate.getQty() == null){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            if(mCartDataUpdate.getQty() != null){
                buy.setVisibility(View.GONE);
                lnQty.setVisibility(View.VISIBLE);
            }else{
                buy.setVisibility(View.VISIBLE);
                lnQty.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onMin(Carts.Data mCartData) {
        String qty = (Integer.parseInt(mCartData.getQty()) - 1) + "";

        HashMap<String, Object> dataUpQty = new HashMap<>();
        dataUpQty.put("id", mCartData.getCartID());
        dataUpQty.put("qty", qty);

        apiCart.upQty(dataUpQty).enqueue(new Callback<ApiRespond>() {
            @Override
            public void onResponse(Call<ApiRespond> call, Response<ApiRespond> response) {
                if(response.body().getStatus().equals("200")){
                    mCartData.setQty(qty);
                    cartAdapter.notifyDataSetChanged();
                    updateBuytoQty(mCartData);
                    getTotalPriceCart();
                }
            }

            @Override
            public void onFailure(Call<ApiRespond> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPlus(Carts.Data mCartData) {
        String qty = (Integer.parseInt(mCartData.getQty()) + 1) + "";

        HashMap<String, Object> dataUpQty = new HashMap<>();
        dataUpQty.put("id", mCartData.getCartID());
        dataUpQty.put("qty", qty);

        apiCart.upQty(dataUpQty).enqueue(new Callback<ApiRespond>() {
            @Override
            public void onResponse(Call<ApiRespond> call, Response<ApiRespond> response) {
                if(response.body().getStatus().equals("200")){
                    mCartData.setQty(qty);
                    cartAdapter.notifyDataSetChanged();
                    updateBuytoQty(mCartData);
                    getTotalPriceCart();
                }
            }

            @Override
            public void onFailure(Call<ApiRespond> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRemove(List<Carts.Data> mCartList, int position) {
        HashMap<String, Object> dataDelQty = new HashMap<>();
        dataDelQty.put("id", mCartList.get(position).getCartID());;

        apiCart.delQty(dataDelQty).enqueue(new Callback<ApiRespond>() {
            @Override
            public void onResponse(Call<ApiRespond> call, Response<ApiRespond> response) {
                if(response.body().getStatus().equals("200")){
                    mCartList.get(position).setQty(null);
                    updateBuytoQty(mCartList.get(position));
                    mCartList.remove(position);
                    cartAdapter.notifyDataSetChanged();
                    totalQty.setText((Integer.parseInt(totalQty.getText().toString()) - 1) + "");
                    getTotalPriceCart();

                    if(cartData.size() == 0){
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        rootCart.setVisibility(View.GONE);
                        nested.setPadding(0, 0, 0, 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiRespond> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void getTotalPriceCart(){
//        int priceTotal = 0;
//        for(Carts.Data mCartData : cartData){
//            priceTotal += (Integer.parseInt(mCartData.getQty()) * Integer.parseInt(mCartData.getPrice()));
//        }
//        totalPrice.setText("Rp. " + priceTotal);
//    }

    private void getTotalPriceCart(){
        int priceTotal = 0;
        for(Carts.Data mCartData : cartData){
            if(!mCartData.getDiscon().replace("%", "").equals("") && !mCartData.getDiscon().replace("%", "").equals("0")){
                priceTotal += (Integer.parseInt(mCartData.getQty()) * Integer.parseInt((mCartData.getDiscon().toString().substring(mCartData.getDiscon().length() -1 ).equals("%") ? "" + ( Integer.parseInt(mCartData.getPrice()) - ( (Integer.parseInt(mCartData.getPrice()) * Integer.parseInt(mCartData.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(mCartData.getPrice()) - Integer.parseInt(mCartData.getDiscon())) )));
            }else{
                priceTotal += (Integer.parseInt(mCartData.getQty()) * Integer.parseInt(mCartData.getPrice()));
            }

        }
        totalPrice.setText(Constraint.rupiah(String.valueOf(priceTotal)));
    }

//    PRODUK TO CART
    private void addCart(Product.Data mProductData){
        if(!new Sesi(this).valid()){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            HashMap<String, Object> rowCart = new HashMap<>();
            rowCart.put("user_id", new Sesi(this).get().getId());
            rowCart.put("product_id", mProductData.getId());
            rowCart.put("qty", "1");

            apiCart.addCart((new Sesi(this).valid() ? new Sesi(this).get().getId() : "0"), rowCart).enqueue(new Callback<SingleCart>() {
                @Override
                public void onResponse(Call<SingleCart> call, Response<SingleCart> response) {
                    if(response.body().getStatus().equals("200")){
                        cartData.add(response.body().getData());
                        cartAdapter.Update(cartData);
                        totalQty.setText((Integer.parseInt(totalQty.getText().toString()) + 1) + "");
                        updateBuytoQty(response.body().getData());
                        getTotalPriceCart();

                        if(cartData.size() > 0){
                            rootCart.setVisibility(View.VISIBLE);
                            nested.setPadding(0, 0, 0, (int) (55 * getResources().getDisplayMetrics().density + 0.5f));
                        }
                    }
                }

                @Override
                public void onFailure(Call<SingleCart> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onMinQty(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onMin(mCartsData);
            }
        }
    }

    public void onPlusQty(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onPlus(mCartsData);
            }
        }
    }

    public void onRemoveQty(Product.Data mProductData) {
        int indexCart = 0;
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onRemove(cartData, indexCart);
            }
            indexCart++;
        }
    }
}
