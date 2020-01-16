
package com.shopping.basket.Model.WishListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WishListModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("wishlist")
    @Expose
    private List<Wishlist> wishlist = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }

}
