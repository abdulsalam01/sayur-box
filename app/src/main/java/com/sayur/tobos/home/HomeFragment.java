package com.sayur.tobos.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.sayur.tobos.MainActivity;
import com.sayur.tobos.cart.CartAdapter;
import com.sayur.tobos.R;
import com.sayur.tobos.cart.ApiCart;
import com.sayur.tobos.cart.Carts;
import com.sayur.tobos.cart.SingleCart;
import com.sayur.tobos.checkout.CheckoutActivity;
import com.sayur.tobos.product.AllProductActivity;
import com.sayur.tobos.product.DetailProductActivity;
import com.sayur.tobos.product.Product;
import com.sayur.tobos.users.LoginActivity;
import com.sayur.tobos.utils.ApiRespond;
import com.sayur.tobos.utils.Constraint;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CatalogAdapter.onCatalogClick, CartAdapter.onCartClick, CategoryAdapter.onCategoryClick {
    ApiHome apiHome;
    LinearLayout lnSearch;
    ViewPager vpSlider;
    DotsIndicator dotsIndicator;
    SliderAdapter sliderAdapter;
    RecyclerView rvCategory;
    CategoryAdapter categoryAdapter;
    RecyclerView rvCatalog;
    CatalogAdapter catalogAdapter;
    LinearLayout lnHome;

    BottomSheetBehavior bottomSheetBehavior;
    View rootCart;
    TextView totalQty, totalPrice;
    Button checkout;
    RecyclerView rvCart;
    CartAdapter cartAdapter;
    List<Carts.Data> cartData = new ArrayList<>();
    ApiCart apiCart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        apiHome = new Server().init().create(ApiHome.class);
        apiCart = new Server().init().create(ApiCart.class);

        lnSearch = v.findViewById(R.id.lnSearch);
        lnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.KONTEKS, SearchActivity.class);
                i.putExtra("type", "p");
                i.putExtra("slug", "hantu-malam");
                startActivityForResult(i, MainActivity.ADDRESS_CEK);
            }
        });
        lnHome = v.findViewById(R.id.lnHome);
        vpSlider = v.findViewById(R.id.vpSlider);
        dotsIndicator = v.findViewById(R.id.dots_indicator);
        rvCategory = v.findViewById(R.id.rvCategory);
        rvCategory.setLayoutManager(new GridLayoutManager(MainActivity.KONTEKS, 5));
        rvCategory.setHasFixedSize(true);
        categoryAdapter = new CategoryAdapter(MainActivity.KONTEKS, this);
        rvCategory.setAdapter(categoryAdapter);
        rvCatalog = v.findViewById(R.id.rvCatalog);
        rvCatalog.setLayoutManager(new LinearLayoutManager(MainActivity.KONTEKS));
        rvCatalog.setHasFixedSize(true);
        catalogAdapter = new CatalogAdapter(MainActivity.KONTEKS);
        catalogAdapter.setCatalogListener(this);
        rvCatalog.setAdapter(catalogAdapter);

        getSlider();
        getCategory();
        getCatalog();

        /*CART*/
        rootCart = v.findViewById(R.id.lnCart);
        rootCart.setVisibility(View.GONE);
        totalQty = rootCart.findViewById(R.id.totalQtyCart);
        totalPrice = rootCart.findViewById(R.id.totalCart);
        checkout = rootCart.findViewById(R.id.checkCart);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(new Sesi(MainActivity.KONTEKS).get().getAddress() == null || new Sesi(MainActivity.KONTEKS).get().getAddress().equals("")){
                    Toast.makeText(MainActivity.KONTEKS, "Lengkapi alamat anda", Toast.LENGTH_SHORT).show();
                    ((MainActivity)MainActivity.KONTEKS).bottom_navigation.setSelectedItemId(R.id.profile);
                }else{
                    Intent iCheckout = new Intent(MainActivity.KONTEKS, CheckoutActivity.class);
                    iCheckout.putExtra("data", new Gson().toJson(cartData).toString());
                    startActivity(iCheckout);
                }
            }
        });
        rvCart = rootCart.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(MainActivity.KONTEKS));
        rvCart.setHasFixedSize(true);
        cartAdapter = new CartAdapter(MainActivity.KONTEKS, this);
        rvCart.setAdapter(cartAdapter);

        bottomSheetBehavior = BottomSheetBehavior.from(rootCart);
    }

    public void getSlider(){
        apiHome.getSlider().enqueue(new Callback<Slider>() {
            @Override
            public void onResponse(Call<Slider> call, Response<Slider> response) {
                if(response.body().getStatus().equals("200")){
                    sliderAdapter = new SliderAdapter(MainActivity.KONTEKS, response.body().getData());
                    vpSlider.setAdapter(sliderAdapter);
                    dotsIndicator.setViewPager(vpSlider);
                }
            }

            @Override
            public void onFailure(Call<Slider> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCategory(){
        apiHome.getCategory().enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if(response.body().getStatus().equals("200")){
                    categoryAdapter.Update(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCatalog(){
        apiHome.getCatalog(new Sesi(MainActivity.KONTEKS).valid() ? new Sesi(MainActivity.KONTEKS).get().getId() : "0").enqueue(new Callback<Catalog>() {
            @Override
            public void onResponse(Call<Catalog> call, Response<Catalog> response) {
                if(catalogAdapter.data != null){
                    catalogAdapter.data.clear();
                }

                if(response.body().getStatus().equals("200")){
                    catalogAdapter.Update(response.body().getData());
                    getCart();
                }else{
                    catalogAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.KONTEKS, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Catalog> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void openPage(Catalog.Data mCatalogData) {
        Intent i = new Intent(MainActivity.KONTEKS, AllProductActivity.class);
        i.putExtra("type", "p");
        i.putExtra("slug", mCatalogData.getSlug());
        startActivityForResult(i, MainActivity.ADDRESS_CEK);
    }

    @Override
    public void onDetailProduct(Product.Data mProductData) {
        Intent i = new Intent(MainActivity.KONTEKS, DetailProductActivity.class);
        i.putExtra("slug", mProductData.getSlug());
        startActivityForResult(i, MainActivity.ADDRESS_CEK);
    }


    private void getCart(){
        apiCart.getCart(new Sesi(MainActivity.KONTEKS).valid() ? new Sesi(MainActivity.KONTEKS).get().getId() : "0").enqueue(new Callback<Carts>() {
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
                        lnHome.setPadding(0, 0, 0, (int) (55* MainActivity.KONTEKS.getResources().getDisplayMetrics().density + 0.5f));
//                        lnHome.getLayoutParams().
                    }
                }else{
                    cartAdapter.notifyDataSetChanged();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    rootCart.setVisibility(View.GONE);
                    lnHome.setPadding(0, 0, 0, 0);
//                    lnHome.setPadding(0, 0, 0, (int) (55* MainActivity.KONTEKS.getResources().getDisplayMetrics().density + 0.5f));
                }
            }

            @Override
            public void onFailure(Call<Carts> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpdateCartProduct(Product.Data mProductData) {
       if(!new Sesi(MainActivity.KONTEKS).valid()){
           startActivity(new Intent(MainActivity.KONTEKS, LoginActivity.class));
       }else{
           HashMap<String, Object> rowCart = new HashMap<>();
           rowCart.put("user_id", new Sesi(MainActivity.KONTEKS).get().getId());
           rowCart.put("product_id", mProductData.getId());
           rowCart.put("qty", "1");

           apiCart.addCart((new Sesi(MainActivity.KONTEKS).valid() ? new Sesi(MainActivity.KONTEKS).get().getId() : "0"), rowCart).enqueue(new Callback<SingleCart>() {
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
                           lnHome.setPadding(0, 0, 0, (int) (55* MainActivity.KONTEKS.getResources().getDisplayMetrics().density + 0.5f));
                       }
                   }
               }

               @Override
               public void onFailure(Call<SingleCart> call, Throwable t) {
                   Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
               }
           });
       }
    }

    private void updateBuytoQty(Carts.Data mCartDataUpdate){
        for(Catalog.Data mCatalog : catalogAdapter.data){
            for(Product.Data mProduct : mCatalog.getProduct()){
                if(mProduct.getId().equals(mCartDataUpdate.getProductId())){
                    mProduct.setIsCart(mCartDataUpdate.getQty() != null ? mCartDataUpdate.getQty() : null);
                    catalogAdapter.notifyDataSetChanged();
                }
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
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
                        lnHome.setPadding(0, 0, 0, 0);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiRespond> call, Throwable t) {
                Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

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
    public void onMinQtyProduct(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData){
            if(mCartsData.getProductId().equals(mProductData.getId())){
                onMin(mCartsData);
//                mCartsData.setQty((Integer.parseInt(mCartsData.getQty()) - 1) + "");
//                cartAdapter.notifyDataSetChanged();
//                updateBuytoQty(mCartsData);
//                Toast.makeText(MainActivity.KONTEKS, new Gson().toJson(mCartsData).toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPlusQtyProduct(Product.Data mProductData) {
        for(Carts.Data mCartsData : cartData){
            if(mCartsData.getProductId().equals(mProductData.getId())){
                onPlus(mCartsData);
//                mCartsData.setQty((Integer.parseInt(mCartsData.getQty()) + 1) + "");
//                cartAdapter.notifyDataSetChanged();
//                updateBuytoQty(mCartsData);
//                Toast.makeText(MainActivity.KONTEKS, new Gson().toJson(mCartsData).toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRemoveQtyProduct(Product.Data mProductData) {
        int indexCart = 0;
        for(Carts.Data mCartsData : cartData){
            if(mCartsData.getProductId().equals(mProductData.getId())){
                onRemove(cartData, indexCart);
//                Toast.makeText(MainActivity.KONTEKS, new Gson().toJson(cartData.get(indexCart)).toString(), Toast.LENGTH_SHORT).show();
            }
            indexCart++;
        }
    }


    @Override
    public void onPageCategory(Category.Data mCategoryData) {
        Intent i = new Intent(MainActivity.KONTEKS, AllProductActivity.class);
        i.putExtra("type", "c");
        i.putExtra("slug", mCategoryData.getSlug());
        startActivityForResult(i, MainActivity.ADDRESS_CEK);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(catalogAdapter.data != null){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            rootCart.setVisibility(View.GONE);
            lnHome.setPadding(0, 0, 0, 0);
            getCatalog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.ADDRESS_CEK && resultCode == MainActivity.ADDRESS_CEK){
            Toast.makeText(MainActivity.KONTEKS, "Lengkapi alamat anda", Toast.LENGTH_SHORT).show();
            ((MainActivity)MainActivity.KONTEKS).bottom_navigation.setSelectedItemId(R.id.profile);
        }
    }
}
