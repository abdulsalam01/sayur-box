package com.sayur.tobos.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.R;
import com.sayur.tobos.utils.Constraint;
import com.sayur.tobos.utils.Server;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransactionActivity extends AppCompatActivity {
    ApiTransaction apiTransaction;
    TextView no, tagihan, status, totalbelanja, nama, phone, address;
    LinearLayout lnBelannja;
    String[] stsPesanan = {"Menunggu pembayaran", "Proses pengemasan", "Proses pengiriman", "Barang diterima", "Pesanan dibatalkan"};
    RecyclerView rvStep;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ttransaction);
        apiTransaction = new Server().init().create(ApiTransaction.class);

        no = findViewById(R.id.noTransaction);
        tagihan = findViewById(R.id.tagihanTransaction);
        status = findViewById(R.id.statusTransaction);
        totalbelanja = findViewById(R.id.totalBelanja);
        nama = findViewById(R.id.namaTransaction);
        phone = findViewById(R.id.phoneTransaction);
        address = findViewById(R.id.addressTransaction);

        rvStep = findViewById(R.id.rvStepTransaction);
        rvStep.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvStep.setHasFixedSize(true);


        lnBelannja = findViewById(R.id.lnBelanja);
        get();
    }

    private void get(){
        apiTransaction.getDetailTransaction(getIntent().getStringExtra("id")).enqueue(new Callback<SingleTransaction>() {
            @Override
            public void onResponse(Call<SingleTransaction> call, Response<SingleTransaction> response) {
                if(response.body().getStatus().equals("200")){
                    Transaction.Data transaction = response.body().getData();
                    no.setText(transaction.getNoTransaction());
                    tagihan.setText(Constraint.rupiah(transaction.getTotalTagihan()));
                    status.setText(transaction.getStatus().equals("0") || transaction.getStatus().equals("1") ? stsPesanan[Integer.parseInt(transaction.getProcess())] : stsPesanan[4]);
                    totalbelanja.setText(Constraint.rupiah(transaction.getTotalTagihan()));
                    nama.setText(transaction.getDataDelivery().getFullname());
                    phone.setText(transaction.getDataDelivery().getPhone());
                    address.setText(transaction.getDataDelivery().getAddress() + "\n\n" +
                            "Waktu :" + transaction.getDataDelivery().getWaktu() + "\n" +
                            "catatan : " + transaction.getDataDelivery().getCatatan());
                    int stepStatus = transaction.getStatus().equals("0") || transaction.getStatus().equals("1") ? StepTransactionAdapter.STATUS_SUCCESS : StepTransactionAdapter.STATUS_FAILED;
                    rvStep.setAdapter(new StepTransactionAdapter(getApplicationContext(), Integer.parseInt(transaction.getProcess()), stepStatus));

                    for(Transaction.Data.DetailTransaction detailTransaction: transaction.getDetailTransaction()){
                        View vBelanja = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_ringkasan_belanja, null);
                        TextView belanjanama = vBelanja.findViewById(R.id.belanjaNama);
                        belanjanama.setText(detailTransaction.getQty() + "x " + detailTransaction.getProductData().getProductName());
                        TextView belanjatotal = vBelanja.findViewById(R.id.belanjaTotal);
                        belanjatotal.setText(Constraint.rupiah(detailTransaction.getProductData().getProductPrice()));
                        lnBelannja.addView(vBelanja);
                    }

                }
            }

            @Override
            public void onFailure(Call<SingleTransaction> call, Throwable t) {

            }
        });
    }
}
