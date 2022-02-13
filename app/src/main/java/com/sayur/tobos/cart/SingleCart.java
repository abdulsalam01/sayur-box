package com.sayur.tobos.cart;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleCart {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Carts.Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Carts.Data getData() {
        return data;
    }

    public void setData(Carts.Data data) {
        this.data = data;
    }


}
