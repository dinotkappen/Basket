
package com.shopping.basket.Model.ReviewListModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("cartlist")
    @Expose
    private List<Cartlist> cartlist = null;
    @SerializedName("ratingCount")
    @Expose
    private RatingCount ratingCount;

    public List<Cartlist> getCartlist() {
        return cartlist;
    }

    public void setCartlist(List<Cartlist> cartlist) {
        this.cartlist = cartlist;
    }

    public RatingCount getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(RatingCount ratingCount) {
        this.ratingCount = ratingCount;
    }

}
