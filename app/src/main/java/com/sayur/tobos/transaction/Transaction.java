package com.sayur.tobos.transaction;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Transaction {
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<Data> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        @SerializedName("id")
        private String id;
        @SerializedName("no_transaction")
        private String noTransaction;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("data_delivery")
        private DataDelivery dataDelivery;
        @SerializedName("process")
        private String process;
        @SerializedName("status")
        private String status;
        @SerializedName("kurir_id")
        private String kurir_id;
        @SerializedName("kurir_status")
        private String kurir_status;
        @SerializedName("payment_id")
        private String payment_id;
        @SerializedName("totalTagihan")
        private String totalTagihan;
        @SerializedName("detail_transaction")
        private List<DetailTransaction> detailTransaction;
        @SerializedName("created_at")
        private String createdAt;
        @SerializedName("updated_at")
        private String updatedAt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNoTransaction() {
            return noTransaction;
        }

        public void setNoTransaction(String noTransaction) {
            this.noTransaction = noTransaction;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public DataDelivery getDataDelivery() {
            return dataDelivery;
        }

        public void setDataDelivery(DataDelivery dataDelivery) {
            this.dataDelivery = dataDelivery;
        }

        public String getProcess() {
            return process;
        }

        public void setProcess(String process) {
            this.process = process;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getKurir_id() {
            return kurir_id;
        }

        public void setKurir_id(String kurir_id) {
            this.kurir_id = kurir_id;
        }

        public String getKurir_status() {
            return kurir_status;
        }

        public void setKurir_status(String kurir_status) {
            this.kurir_status = kurir_status;
        }

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getTotalTagihan() {
            return totalTagihan;
        }

        public void setTotalTagihan(String totalTagihan) {
            this.totalTagihan = totalTagihan;
        }

        public List<DetailTransaction> getDetailTransaction() {
            return detailTransaction;
        }

        public void setDetailTransaction(List<DetailTransaction> detailTransaction) {
            this.detailTransaction = detailTransaction;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public class DataDelivery{
            @SerializedName("fullname")
            private String fullname;
            @SerializedName("phone")
            private String phone;
            @SerializedName("address")
            private String address;
            @SerializedName("catatan")
            private String catatan;
            @SerializedName("waktu")
            private String waktu;

            public String getFullname() {
                return fullname;
            }

            public void setFullname(String fullname) {
                this.fullname = fullname;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCatatan() {
                return catatan;
            }

            public void setCatatan(String catatan) {
                this.catatan = catatan;
            }

            public String getWaktu() {
                return waktu;
            }

            public void setWaktu(String waktu) {
                this.waktu = waktu;
            }
        }

        public class DetailTransaction{
            @SerializedName("id")
            private String id;
            @SerializedName("transaction_id")
            private String transactionId;
            @SerializedName("product_id")
            private String productId;
            @SerializedName("product_data")
            private ProductData productData;
            @SerializedName("qty")
            private String qty;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public ProductData getProductData() {
                return productData;
            }

            public void setProductData(ProductData productData) {
                this.productData = productData;
            }

            public String getQty() {
                return qty;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public class ProductData{
                @SerializedName("product_price")
                private String productPrice;
                @SerializedName("product_name")
                private String productName;

                public String getProductPrice() {
                    return productPrice;
                }

                public void setProductPrice(String productPrice) {
                    this.productPrice = productPrice;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }
            }
        }
    }

}
