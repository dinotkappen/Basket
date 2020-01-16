
package com.shopping.basket.Model.ProductDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("previous_price")
    @Expose
    private Integer previousPrice;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("wishlists")
    @Expose
    private Integer wishlists;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(Integer previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getstock() {
        return stock;
    }

    public void setstock(Integer stock) {
        this.stock = stock;
    }
    public Integer getWishlists() {
        return wishlists;
    }

    public void setWishlists(Integer wishlists) {
        this.wishlists = wishlists;
    }

}
