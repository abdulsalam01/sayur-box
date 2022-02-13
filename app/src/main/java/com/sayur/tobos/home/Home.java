package com.sayur.tobos.home;


import com.google.gson.annotations.SerializedName;

public class Home {
    @SerializedName("slider")
    private Slider slider;
    @SerializedName("category")
    private Category category;
    @SerializedName("catalog")
    private Catalog catalog;

    public Slider getSlider() {
        return slider;
    }

    public void setSlider(Slider slider) {
        this.slider = slider;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}
