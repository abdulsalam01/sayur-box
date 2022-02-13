package com.sayur.tobos.users;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.sayur.tobos.MainActivity;
import com.sayur.tobos.R;
import com.sayur.tobos.utils.ApiRespond;
import com.sayur.tobos.utils.Constraint;
import com.sayur.tobos.utils.Server;
import com.sayur.tobos.utils.Sesi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    ApiUsers apiUsers;
    Users.Data profile;
    ImageView img;
    TextInputLayout fullname, phone, address, password;
    Button btnUpdate, btnLogout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        apiUsers = new Server().init().create(ApiUsers.class);
        profile = new Sesi(MainActivity.KONTEKS).get();
        img = v.findViewById(R.id.imgSupplier);
        if(profile.getImage() != null){
            Glide.with(MainActivity.KONTEKS).load(Constraint.BASE_URL + profile.getImage()).into(img);
        }
        img.setOnClickListener(this);
        fullname = v.findViewById(R.id.fullnameProfile);
        fullname.getEditText().setText(profile.getFullname());
        phone = v.findViewById(R.id.phoneProfile);
        phone.getEditText().setText(profile.getPhone());
        address = v.findViewById(R.id.addressProfile);
        address.getEditText().setText(profile.getAddress());
        password = v.findViewById(R.id.passwordProfile);
        btnUpdate = v.findViewById(R.id.btnUpdateProfile);
        btnUpdate.setOnClickListener(this);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgPesanan:
                break;
            case R.id.btnUpdateProfile:
                if(fullname.getEditText().getText().toString().equals("") || phone.getEditText().getText().toString().equals("")){
                    Toast.makeText(MainActivity.KONTEKS, "Lengkapi Data", Toast.LENGTH_SHORT).show();
                }else{
                    Users.Data dataUpdate = new Users.Data();
                    dataUpdate.setFullname(fullname.getEditText().getText().toString());
                    dataUpdate.setPhone(phone.getEditText().getText().toString());
                    dataUpdate.setAddress(address.getEditText().getText().toString());
                    dataUpdate.setPassword(password.getEditText().getText().toString());
                    apiUsers.updateProfile(new Sesi(MainActivity.KONTEKS).get().getId(), dataUpdate).enqueue(new Callback<ApiRespond>() {
                        @Override
                        public void onResponse(Call<ApiRespond> call, Response<ApiRespond> response) {
                            if(response.body().getStatus().equals("200")){
                                Toast.makeText(MainActivity.KONTEKS, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                Users.Data usersData =  profile;
                                usersData.setFullname(fullname.getEditText().getText().toString());
                                usersData.setPhone(phone.getEditText().getText().toString());
                                usersData.setAddress(address.getEditText().getText().toString());
                                new Sesi(MainActivity.KONTEKS).set(usersData);
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiRespond> call, Throwable t) {
                            Toast.makeText(MainActivity.KONTEKS, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.btnLogout:
                AlertDialog.Builder alertLogout = new AlertDialog.Builder(MainActivity.KONTEKS);
                alertLogout.setMessage("Anda yakin ingin keluar?");
                alertLogout.setPositiveButton("ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Sesi(MainActivity.KONTEKS).rmv();
                        ((MainActivity)MainActivity.KONTEKS).bottom_navigation.setSelectedItemId(R.id.home);
                        dialogInterface.cancel();
                    }
                });
                alertLogout.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alertLogout.show();
                break;
        }
    }
}
