package com.sayur.tobos.checkout;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.R;

import java.util.List;


public class WaktuAdapter extends RecyclerView.Adapter<WaktuAdapter.WaktuHolder> {
    Context c;
    List<Waktu.Data> data;
    public Waktu.Data waktu_selected = null;

    public WaktuAdapter(Context c) {
        this.c = c;
    }

    public void Update(List<Waktu.Data> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WaktuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WaktuHolder(LayoutInflater.from(c).inflate(R.layout.item_waktu_pengiriman, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WaktuHolder holder, int position) {
        Waktu.Data waktu = data.get(position);
        holder.slot.setText(waktu.getName());
        holder.waktu.setText(waktu.getStart() + " - " + waktu.getEnd() + " " + waktu.getTimezone().toUpperCase());
        if(waktu.isPilih()){
            holder.consWaktu.setBackgroundColor(Color.parseColor("#5747B04B"));
            holder.btnWaktu.setVisibility(View.GONE);
        }else{
            holder.consWaktu.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.btnWaktu.setVisibility(View.VISIBLE);
        }
        holder.btnWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Waktu.Data wdt : data){
                    if(wdt.getId().equals(waktu.getId())){
                        wdt.setPilih(true);
                        waktu_selected = wdt;
                    }else{
                        wdt.setPilih(false);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class WaktuHolder extends RecyclerView.ViewHolder {
        TextView slot, waktu;
        Button btnWaktu;
        ConstraintLayout consWaktu;
        public WaktuHolder(@NonNull View v) {
            super(v);
            slot = v.findViewById(R.id.slot);
            waktu = v.findViewById(R.id.waktu);
            btnWaktu = v.findViewById(R.id.btnPilihWaktu);
            consWaktu = v.findViewById(R.id.consWaktu);
        }
    }

}
