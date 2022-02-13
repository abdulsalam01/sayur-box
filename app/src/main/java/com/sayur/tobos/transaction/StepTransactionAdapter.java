package com.sayur.tobos.transaction;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.R;

public class StepTransactionAdapter extends RecyclerView.Adapter<StepTransactionAdapter.StepTransactionHolder> {
    Context c;
    int steped;
    int[] icons = {R.drawable.ic_payment, R.drawable.ic_kemas, R.drawable.ic_delivery, R.drawable.ic_recived};

    public static int STATUS_FAILED = -1;
    public static int STATUS_SUCCESS = 1;
    int status;
    public StepTransactionAdapter(Context c, int steped, int status) {
        this.c = c;
        this.steped = steped;
        this.status = status;
    }

    @NonNull
    @Override
    public StepTransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StepTransactionHolder(LayoutInflater.from(c).inflate(R.layout.item_step_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepTransactionHolder holder, int position) {
        if(position == 0){
            holder.line.setVisibility(View.GONE);
        }

        if(position <= steped){
            holder.line.setBackgroundColor( status == STATUS_SUCCESS ? c.getResources().getColor(R.color.color_primary) : Color.parseColor("#FF0000") );
            holder.icon.setBackgroundResource( status == STATUS_SUCCESS ? R.drawable.badge_step_view_transaction_success : R.drawable.badge_step_view_transaction_failed);
            holder.icon.setColorFilter(status == STATUS_SUCCESS ? c.getResources().getColor(R.color.color_primary) : Color.parseColor("#FF0000"), PorterDuff.Mode.SRC_IN);
        }else{
            holder.line.setBackgroundColor(Color.parseColor("#BCBCBC"));
            holder.icon.setBackgroundResource(R.drawable.badge_step_view_transaction);
            holder.icon.setColorFilter(Color.parseColor("#BCBCBC"), PorterDuff.Mode.SRC_IN);
        }

        holder.icon.setImageResource(icons[position]);
    }

    @Override
    public int getItemCount() {
        return icons.length;
    }

    public class StepTransactionHolder extends RecyclerView.ViewHolder {
        View line;
        ImageView icon;
        public StepTransactionHolder(@NonNull View v) {
            super(v);
            line = v.findViewById(R.id.lineStepTransaction);
            icon = v.findViewById(R.id.iconStepTransaction);
        }
    }
}
