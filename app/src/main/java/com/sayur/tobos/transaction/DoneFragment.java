package com.sayur.tobos.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sayur.tobos.R;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoneFragment extends Fragment implements ListTransactionAdapter.onTransactionClick {
    RecyclerView rvTransaction;
    ListTransactionAdapter listTransactionAdapter;
    ApiTransaction apiTransaction;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        apiTransaction = new Server().init().create(ApiTransaction.class);
        rvTransaction = v.findViewById(R.id.rvTransaction);
        rvTransaction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTransaction.setHasFixedSize(true);
        listTransactionAdapter = new ListTransactionAdapter(getActivity(), this);
        rvTransaction.setAdapter(listTransactionAdapter);
        get();
    }

    private void get(){
        apiTransaction.getTransaction( (new Sesi(getActivity()).valid() ? new Sesi(getActivity()).get().getId() : "0"), "1").enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                if(response.body().getStatus().equals("200")){
                    listTransactionAdapter.Update(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDetail(Transaction.Data mTransactionData) {
        Intent i = new Intent(getActivity(), DetailTransactionActivity.class);
        i.putExtra("id", mTransactionData.getId());
        startActivity(i);
    }
}
