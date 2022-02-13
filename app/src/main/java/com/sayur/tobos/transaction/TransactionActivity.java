package com.sayur.tobos.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sayur.tobos.R;

public class TransactionActivity extends AppCompatActivity {
    /*Toolbar*/
    ImageView tb_back;
    TextView tb_title;
    View tb_shadow;

    TabLayout tabTransaction;
    ViewPager vpTransaction;
    TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        tb_back = findViewById(R.id.tb_back);
        tb_back.setVisibility(View.VISIBLE);
        tb_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tb_title = findViewById(R.id.tb_title);
        tb_title.setText("Transaction");
        tb_shadow = findViewById(R.id.tb_shadow);
        tb_shadow.setVisibility(View.GONE);

        tabTransaction = findViewById(R.id.tabTransaction);
        tabTransaction.addTab(tabTransaction.newTab().setText("Dalam Proses"));
        tabTransaction.addTab(tabTransaction.newTab().setText("Selesai"));
        vpTransaction = findViewById(R.id.vpTransaction);
        transactionAdapter = new TransactionAdapter(getSupportFragmentManager(), tabTransaction.getTabCount());
        vpTransaction.setAdapter(transactionAdapter);
        vpTransaction.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabTransaction));
        tabTransaction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpTransaction.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
