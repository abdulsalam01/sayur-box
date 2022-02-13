package com.sayur.tobos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sayur.tobos.home.HomeFragment;
import com.sayur.tobos.promo.PromoFragment;
import com.sayur.tobos.transaction.TransactionActivity;
import com.sayur.tobos.users.LoginActivity;
import com.sayur.tobos.users.ProfileFragment;
import com.sayur.tobos.utils.Sesi;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public static MainActivity KONTEKS;
    public BottomNavigationView bottom_navigation;
    public static final int ADDRESS_CEK = 1010101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KONTEKS = MainActivity.this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        bottom_navigation = findViewById(R.id.bottom_navigation);
        bottom_navigation.setOnNavigationItemSelectedListener(this);
        setFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment f = null;
        switch (item.getItemId()){
            case R.id.home:
                f = new HomeFragment();
                break;
            case R.id.transaction:
                startActivity(new Intent(this, TransactionActivity.class));
                break;
            case R.id.discon:
                f = new PromoFragment();
                break;
            case R.id.profile:
                if(!new Sesi(this).valid()){
                    startActivity(new Intent(this, LoginActivity.class));
                }else {
                    f = new ProfileFragment();
                }
                break;
        }
        return setFragment(f);
    }

    public boolean setFragment(Fragment f){
        if(f != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, f)
                    .commit();
            return true;
        }
        return false;
    }

    public void cekAddressToProfile(){
        bottom_navigation.setSelectedItemId(R.id.profile);
//        setFragment(new ProfileFragment());
    }
    public void logoutToHome(){
        bottom_navigation.setSelectedItemId(R.id.home);
        setFragment(new HomeFragment());
    }
}