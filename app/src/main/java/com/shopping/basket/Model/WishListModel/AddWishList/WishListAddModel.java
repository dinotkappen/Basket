
package com.shopping.basket.Model.WishListModel.AddWishList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WishListAddModel {

    @SerializedName("success")
    @Expose
    private WishAddSuccess success;

    public WishAddSuccess getSuccess() {
        return success;
    }

    public void setSuccess(WishAddSuccess success) {
        this.success = success;
    }

}
