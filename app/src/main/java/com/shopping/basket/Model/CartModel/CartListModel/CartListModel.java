
package com.shopping.basket.Model.CartModel.CartListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartListModel {

    @SerializedName("success")
    @Expose
    private CartListSuccess success;

    public CartListSuccess getSuccess() {
        return success;
    }

    public void setSuccess(CartListSuccess success) {
        this.success = success;
    }

}
