package com.sayur.tobos.product;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Product {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<Data> data;

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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("id")
        private String id;
        @SerializedName("category_id")
        private String categoryId;
        @SerializedName("name")
        private String name;
        @SerializedName("slug")
        private String slug;
        @SerializedName("description")
        private String description;
        @SerializedName("price")
        private String price;
        @SerializedName("discon")
        private String discon;
        @SerializedName("max_buy_discon")
        private String maxBuyDiscon;
        @SerializedName("stock")
        private String stock;
        @SerializedName("information")
        private Information information;
        @SerializedName("image")
        private String image;
        @SerializedName("per")
        private String per;
        @SerializedName("isCart")
        private String isCart;
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

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDiscon() {
            return discon;
        }

        public void setDiscon(String discon) {
            this.discon = discon;
        }

        public String getMaxBuyDiscon() {
            return maxBuyDiscon;
        }

        public void setMaxBuyDiscon(String maxBuyDiscon) {
            this.maxBuyDiscon = maxBuyDiscon;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public Information getInformation() {
            return information;
        }

        public void setInformation(Information information) {
            this.information = information;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPer() {
            return per;
        }

        public void setPer(String per) {
            this.per = per;
        }

        public String getIsCart() {
            return isCart;
        }

        public void setIsCart(String isCart) {
            this.isCart = isCart;
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

        public class Information {
            @SerializedName("product_information")
            private String productInformation;
            @SerializedName("nutrition_and_benefits")
            private String nutritionAndBenefits;
            @SerializedName("how_to_save")
            private String howToSave;
            @SerializedName("farmers_and_suppliers")
            private List<FarmersAndSuppliers> farmersAndSuppliers;

            public String getProductInformation() {
                return productInformation;
            }

            public void setProductInformation(String productInformation) {
                this.productInformation = productInformation;
            }

            public String getNutritionAndBenefits() {
                return nutritionAndBenefits;
            }

            public void setNutritionAndBenefits(String nutritionAndBenefits) {
                this.nutritionAndBenefits = nutritionAndBenefits;
            }

            public String getHowToSave() {
                return howToSave;
            }

            public void setHowToSave(String howToSave) {
                this.howToSave = howToSave;
            }

            public List<FarmersAndSuppliers> getFarmersAndSuppliers() {
                return farmersAndSuppliers;
            }

            public void setFarmersAndSuppliers(List<FarmersAndSuppliers> farmersAndSuppliers) {
                this.farmersAndSuppliers = farmersAndSuppliers;
            }
        }
    }
}
