package com.sayur.tobos.transaction;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.R;
import com.sayur.tobos.checkout.XenditBayar;
import com.sayur.tobos.utils.Constraint;

import java.util.List;

public class ListTransactionAdapter extends RecyclerView.Adapter<ListTransactionAdapter.ListTransactonHolder> {
    Context c;
    List<Transaction.Data> data;
    onTransactionClick onTransactionClick;
    String[] stsPesanan = {"Menunggu pembayaran", "Proses pengemasan", "Proses pengiriman", "Barang diterima", "Pesanan dibatalkan"};
    public ListTransactionAdapter(Context c, onTransactionClick onTransactionClick) {
        this.c = c;
        this.onTransactionClick = onTransactionClick;
    }

    public void Update(List<Transaction.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListTransactonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListTransactonHolder(LayoutInflater.from(c).inflate(R.layout.item_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListTransactonHolder holder, int position) {
        Transaction.Data transaction = data.get(position);
        holder.no.setText(transaction.getNoTransaction());
        holder.date.setText(transaction.getCreatedAt());
        holder.status.setText(transaction.getStatus().equals("0") || transaction.getStatus().equals("1") ? stsPesanan[Integer.parseInt(transaction.getProcess())] : stsPesanan[4]);
        holder.tagihan.setText(Constraint.rupiah(transaction.getTotalTagihan()));
        int stepStatus = transaction.getStatus().equals("0") || transaction.getStatus().equals("1") ? StepTransactionAdapter.STATUS_SUCCESS : StepTransactionAdapter.STATUS_FAILED;
        holder.rvStep.setAdapter(new StepTransactionAdapter(c, Integer.parseInt(transaction.getProcess()), stepStatus));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTransactionClick.onDetail(transaction);
            }
        });
        if(transaction.getPayment_id().equals("")){
            holder.btnSelesaiBayar.setVisibility(View.GONE);
        }else{
            holder.btnSelesaiBayar.setVisibility(View.VISIBLE);
            holder.btnSelesaiBayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(c, XenditBayar.class);
                    i.putExtra("xendit_id", transaction.getPayment_id());
                    c.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ListTransactonHolder extends RecyclerView.ViewHolder {
        TextView no, date, status, tagihan;
        RecyclerView rvStep;
        Button btnSelesaiBayar;
        public ListTransactonHolder(@NonNull View v) {
            super(v);
            no = v.findViewById(R.id.noTrans);
            date = v.findViewById(R.id.dateTrans);
            status = v.findViewById(R.id.statusTrans);
            tagihan = v.findViewById(R.id.tagihanTrans);
            rvStep = v.findViewById(R.id.rvStepTrans);
            rvStep.setLayoutManager(new LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false));
            rvStep.setHasFixedSize(true);
            btnSelesaiBayar = v.findViewById(R.id.btnSelesaikanPembayaran);
        }
    }

    public interface onTransactionClick{
        void onDetail(Transaction.Data mTransactionData);
    }
}
