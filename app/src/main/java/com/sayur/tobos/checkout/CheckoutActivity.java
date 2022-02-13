package com.sayur.tobos.checkout;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sayur.tobos.R;
import com.sayur.tobos.cart.Carts;
import com.sayur.tobos.product.AllProductActivity;
import com.sayur.tobos.product.DetailProductActivity;
import com.sayur.tobos.users.Users;
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

public class CheckoutActivity extends AppCompatActivity {
    ApiCheckout api;
    Users.Data profile;
    TextView nama, alamat, nomor;
    RecyclerView rvWaktu;
    WaktuAdapter waktuAdapter;
    LinearLayout lnPesanan;
    List<Carts.Data> cartData;
    HashMap<String, Object> dataTransaction = new HashMap<>();
    Button btnBayar;
    TextView totalCheckout;
    int total = 0;
    TextInputLayout catatan;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        api = new Server().init().create(ApiCheckout.class);

        profile = new Sesi(this).get();
        nama = findViewById(R.id.namaCheckout);
        nama.setText(profile.getFullname());
        alamat = findViewById(R.id.alamatCheckout);
        alamat.setText(profile.getAddress());
        nomor = findViewById(R.id.nomorCheckout);
        nomor.setText(profile.getPhone());

        rvWaktu = findViewById(R.id.rvWaktu);
        rvWaktu.setLayoutManager(new LinearLayoutManager(this));
        rvWaktu.setHasFixedSize(true);
        waktuAdapter = new WaktuAdapter(this);
        rvWaktu.setAdapter(waktuAdapter);

        totalCheckout = findViewById(R.id.totalCheckout);
        totalCheckout.setText("" + total);
        catatan = findViewById(R.id.catatanCheckout);

        cartData = new Gson().fromJson(getIntent().getStringExtra("data"), new TypeToken<List<Carts.Data>>(){}.getType());

        lnPesanan = findViewById(R.id.lnPesanan);
        List<HashMap<String, Object>> itemsTransaction = new ArrayList<>();
        for(Carts.Data cData : cartData){
            if(!cData.getDiscon().replace("%", "").equals("") && !cData.getDiscon().replace("%", "").equals("0")){
                View v = LayoutInflater.from(this).inflate(R.layout.item_pesanan, null);
                ImageView imgPesanan = v.findViewById(R.id.imgPesanan);
                Glide.with(this).load(Constraint.BASE_URL + cData.getImage()).into(imgPesanan);
                TextView titlePesaan = v.findViewById(R.id.titlePesanan);
                titlePesaan.setText(cData.getName());
                TextView pricePesanan = v.findViewById(R.id.pricePesanan);
                pricePesanan.setText(Constraint.rupiah( "" + (Integer.parseInt(cData.getQty()) * Integer.parseInt((cData.getDiscon().toString().substring(cData.getDiscon().length() -1 ).equals("%") ? "" + ( Integer.parseInt(cData.getPrice()) - ( (Integer.parseInt(cData.getPrice()) * Integer.parseInt(cData.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(cData.getPrice()) - Integer.parseInt(cData.getDiscon())) ))) ));
                TextView perPesanan = v.findViewById(R.id.perPesanan);
                perPesanan.setText("/ Kg");
                TextView qtyPesanan = v.findViewById(R.id.qtyPesanan);
                qtyPesanan.setText("x " + cData.getQty());
                lnPesanan.addView(v);

                HashMap<String, Object> detailItemsTransaction = new HashMap<>();
                detailItemsTransaction.put("product_id", cData.getProductId());
                HashMap<String, Object> productData = new HashMap<>();
                productData.put("product_name", cData.getName());
                productData.put("product_price", (Integer.parseInt(cData.getQty()) * Integer.parseInt((cData.getDiscon().toString().substring(cData.getDiscon().length() -1 ).equals("%") ? "" + ( Integer.parseInt(cData.getPrice()) - ( (Integer.parseInt(cData.getPrice()) * Integer.parseInt(cData.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(cData.getPrice()) - Integer.parseInt(cData.getDiscon())) ))) );
                detailItemsTransaction.put("product_data", productData);
                detailItemsTransaction.put("qty", cData.getQty());
                itemsTransaction.add(detailItemsTransaction);

                total += ( Integer.parseInt(cData.getQty()) * (Integer.parseInt(cData.getQty()) * Integer.parseInt((cData.getDiscon().toString().substring(cData.getDiscon().length() -1 ).equals("%") ? "" + ( Integer.parseInt(cData.getPrice()) - ( (Integer.parseInt(cData.getPrice()) * Integer.parseInt(cData.getDiscon().replace("%", ""))) / 100 ) ) : "" + (Integer.parseInt(cData.getPrice()) - Integer.parseInt(cData.getDiscon())) ))) );
            }else{
                View v = LayoutInflater.from(this).inflate(R.layout.item_pesanan, null);
                ImageView imgPesanan = v.findViewById(R.id.imgPesanan);
                Glide.with(this).load(Constraint.BASE_URL + cData.getImage()).into(imgPesanan);
                TextView titlePesaan = v.findViewById(R.id.titlePesanan);
                titlePesaan.setText(cData.getName());
                TextView pricePesanan = v.findViewById(R.id.pricePesanan);
                pricePesanan.setText(Constraint.rupiah(cData.getPrice()));
                TextView perPesanan = v.findViewById(R.id.perPesanan);
                perPesanan.setText("/ Kg");
                TextView qtyPesanan = v.findViewById(R.id.qtyPesanan);
                qtyPesanan.setText("x " + cData.getQty());
                lnPesanan.addView(v);

                HashMap<String, Object> detailItemsTransaction = new HashMap<>();
                detailItemsTransaction.put("product_id", cData.getProductId());
                HashMap<String, Object> productData = new HashMap<>();
                productData.put("product_name", cData.getName());
                productData.put("product_price", cData.getPrice());
                detailItemsTransaction.put("product_data", productData);
                detailItemsTransaction.put("qty", cData.getQty());
                itemsTransaction.add(detailItemsTransaction);

                total += ( Integer.parseInt(cData.getQty()) * Integer.parseInt(cData.getPrice()) );
            }
        }
        dataTransaction.put("items", itemsTransaction);
        totalCheckout.setText(Constraint.rupiah("" + total));

        getWaktu();

        btnBayar = findViewById(R.id.btnBayar);
        btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(waktuAdapter.waktu_selected != null){
                    bayar();
//                    Toast.makeText(getApplicationContext(),  new Gson().toJson(dataTransaction).toString(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Lengkapi Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Log.d("JSON CHECKOUT", new Gson().toJson(dataTransaction).toString());

    }

    private void getWaktu(){
        api.getWaktu().enqueue(new Callback<Waktu>() {
            @Override
            public void onResponse(Call<Waktu> call, Response<Waktu> response) {
                if(response.body().getStatus().equals("200")){
                    waktuAdapter.Update(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<Waktu> call, Throwable t) {

            }
        });
    }

//    private List<Waktu.Data> get(){
//        List<Waktu.Data> data = new ArrayList<>();
//
//        for(int i = 0; i < 5;i++){
//            Waktu.Data wd = new Waktu.Data();
//            wd.setId("" + i);
//            wd.setSlot("Slot " + i + " Hari");
//            wd.setMulai("12:00");
//            wd.setSelesai("19:00");
//            wd.setTimezone("WIB");
//            data.add(wd);
//        }
//        return data;
//    }

    private void bayar(){
        HashMap<String, Object> dataDelivery = new HashMap<>();
        dataDelivery.put("fullname", profile.getFullname());
        dataDelivery.put("phone", profile.getPhone());
        dataDelivery.put("address", profile.getAddress());
        dataDelivery.put("waktu", waktuAdapter.waktu_selected != null ? waktuAdapter.waktu_selected.getName() + " | " + waktuAdapter.waktu_selected.getStart() + " - " + waktuAdapter.waktu_selected.getEnd() + " " + waktuAdapter.waktu_selected.getTimezone().toUpperCase() : "Waktu Tidak Ada");
        dataDelivery.put("catatan", catatan.getEditText().getText().toString());
        dataTransaction.put("data_delivery", dataDelivery);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("H", new Gson().toJson(dataTransaction).toString()));

        api.transaction(new Sesi(this).get().getId(), dataTransaction).enqueue(new Callback<ApiRespond>() {
            @Override
            public void onResponse(Call<ApiRespond> call, Response<ApiRespond> response) {
                if(response.body().getStatus().equals("200")){
                    String id = response.body().getMessage().split("-")[1];
                    Intent i = new Intent(getApplicationContext(), XenditBayar.class);
                    i.putExtra("xendit_id", id);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiRespond> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
