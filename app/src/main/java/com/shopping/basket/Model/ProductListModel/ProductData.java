
package com.shopping.basket.Model.ProductListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("stars")
    @Expose
    private String stars;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Integer getWishlists() {
        return wishlists;
    }

    public void setWishlists(Integer wishlists) {
        this.wishlists = wishlists;
    }

}
