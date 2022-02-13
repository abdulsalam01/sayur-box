package com.sayur.tobos.users;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sayur.tobos.R;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ApiUsers api;
    TextInputLayout fullname, phone, password;
    Button btnDaftar;
    TextView toLogin;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        api = new Server().init().create(ApiUsers.class);
        fullname = findViewById(R.id.fullnameRegister);
        phone = findViewById(R.id.phoneRegister);
        password = findViewById(R.id.passwordRegister);
        btnDaftar = findViewById(R.id.btnDaftar);
        toLogin = findViewById(R.id.toLogin);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phone.getEditText().getText().toString().equals("") || password.getEditText().getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Silahkan Lengkapi Data", Toast.LENGTH_SHORT).show();
                }else{
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("fullname", fullname.getEditText().getText().toString());
                    data.put("phone", phone.getEditText().getText().toString());
                    data.put("password", password.getEditText().getText().toString());
                    api.daftar(data).enqueue(new Callback<Users>() {
                        @Override
                        public void onResponse(Call<Users> call, Response<Users> response) {
                            if(response.body().getStatus().equals("200")){
                                new Sesi(getApplicationContext()).set(response.body().getData());
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Users> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}
