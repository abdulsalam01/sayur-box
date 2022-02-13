package com.sayur.tobos.transaction;

import com.google.gson.annotations.SerializedName;

public class SingleTransaction {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private Transaction.Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Transaction.Data getData() {
        return data;
    }

    public void setData(Transaction.Data data) {
        this.data = data;
    }
}
