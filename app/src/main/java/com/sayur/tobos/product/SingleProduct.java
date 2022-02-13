package com.sayur.tobos.product;

import com.google.gson.annotations.SerializedName;

public class SingleProduct {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Product.Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Product.Data getData() {
        return data;
    }

    public void setData(Product.Data data) {
        this.data = data;
    }
}
