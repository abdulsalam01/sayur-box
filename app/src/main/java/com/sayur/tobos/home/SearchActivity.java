package com.sayur.tobos.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.sayur.tobos.MainActivity;
import com.sayur.tobos.R;
import com.sayur.tobos.cart.ApiCart;
import com.sayur.tobos.cart.CartAdapter;
import com.sayur.tobos.cart.Carts;
import com.sayur.tobos.cart.SingleCart;
import com.sayur.tobos.checkout.CheckoutActivity;
import com.sayur.tobos.home.Catalog;
import com.sayur.tobos.home.Category;
import com.sayur.tobos.home.HomeFragment;
import com.sayur.tobos.product.ApiProduct;
import com.sayur.tobos.product.DetailProductActivity;
import com.sayur.tobos.product.Product;
import com.sayur.tobos.product.ProductAdapter;
import com.sayur.tobos.users.LoginActivity;
import com.sayur.tobos.utils.ApiRespond;
import com.sayur.tobos.utils.Constraint;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;
import com.sayur.tobos.utils.Toolbars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements ProductAdapter.onProductClick, CartAdapter.onCartClick {
    ImageView  tb_back;
    EditText etSearch;
    RecyclerView rvSearch;
    ProductAdapter productAdapter;
    ApiProduct apiProduct;
    List<Product.Data> data = new ArrayList<>();
    LinearLayout lnSearch;

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
        setContentView(R.layout.activity_search);
        apiProduct = new Server().init().create(ApiProduct.class);
        apiCart = new Server().init().create(ApiCart.class);

        tb_back = findViewById(R.id.tb_back);
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etSearch = findViewById(R.id.etSearch);
        rvSearch = findViewById(R.id.rvSearch);
        rvSearch.setLayoutManager(new GridLayoutManager(this, 2));
        rvSearch.setHasFixedSize(true);
        productAdapter = new ProductAdapter(this, ProductAdapter.GRID);
        productAdapter.setProductListener(this);
        rvSearch.setAdapter(productAdapter);
        lnSearch = findViewById(R.id.lnSearch);


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

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                get(charSequence.toString().equals("") ? "'" : charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void get(String q){
        apiProduct.getProductByType("s", (new Sesi(this).valid() ? new Sesi(this).get().getId() : "0"), q).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                data.clear();
                if(response.body().getStatus().equals("200")){
                    data = response.body().getData();
                    productAdapter.Update(data);
                    getCart();
                }else{
//                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetail(Product.Data mProductData) {
        Intent i = new Intent(this, DetailProductActivity.class);
        i.putExtra("slug", mProductData.getSlug());
        startActivityForResult(i, MainActivity.ADDRESS_CEK);
    }

    @Override
    public void onUpdateCart(Product.Data mProductData) {
        if(!new Sesi(this).valid()){
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            HashMap<String, Object> rowCart = new HashMap<>();
            rowCart.put("user_id", new Sesi(this).get().getId());
            rowCart.put("product_id", mProductData.getId());
            rowCart.put("qty", "1");

            apiCart.addCart( (new Sesi(this).valid() ? new Sesi(this).get().getId() : "0"),rowCart).enqueue(new Callback<SingleCart>() {
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
                            lnSearch.setPadding(0, 0, 0, (int) (55 * getResources().getDisplayMetrics().density + 0.5f));
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

    @Override
    public void onMinQty(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onMin(mCartsData);
            }
        }
    }

    @Override
    public void onPlusQty(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onPlus(mCartsData);
            }
        }
    }

    @Override
    public void onRemoveQty(Product.Data mProductData) {
        int indexCart = 0;
        for(Carts.Data mCartsData : cartData) {
            if (mCartsData.getProductId().equals(mProductData.getId())) {
                onRemove(cartData, indexCart);
            }
            indexCart++;
        }
    }

    private void getCart(){
        apiCart.getCart(new Sesi(this).valid() ? new Sesi(this).get().getId() : "0").enqueue(new Callback<Carts>() {
            @Override
            public void onResponse(Call<Carts> call, Response<Carts> response) {
                if(response.body().getStatus().equals("200")){
                    cartData.clear();
                    cartData = response.body().getData();
                    cartAdapter.Update(cartData);
                    totalQty.setText(response.body().getData().size() + "");
                    getTotalPriceCart();

                    if(response.body().getData().size() > 0){
                        rootCart.setVisibility(View.VISIBLE);
                        lnSearch.setPadding(0, 0, 0, (int) (55* getResources().getDisplayMetrics().density + 0.5f));
//                        lnHome.getLayoutParams().
                    }else{
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        rootCart.setVisibility(View.GONE);
//                        lnHome.setPadding(0, 0, 0, 0);
                        lnSearch.setPadding(0, 0, 0, (int) (55* getResources().getDisplayMetrics().density + 0.5f));
                    }
                }
            }

            @Override
            public void onFailure(Call<Carts> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateBuytoQty(Carts.Data mCartDataUpdate){
        for(Product.Data mProduct : data){
            if(mProduct.getId().equals(mCartDataUpdate.getProductId())){
                mProduct.setIsCart(mCartDataUpdate.getQty() != null ? mCartDataUpdate.getQty() : null);
                productAdapter.notifyDataSetChanged();
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
//                        lnHome.setPadding(0, 0, 0, 0);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.ADDRESS_CEK && resultCode == MainActivity.ADDRESS_CEK){
            Intent i = new Intent();
            setResult(MainActivity.ADDRESS_CEK, i);
            finish();
        }
    }
}
