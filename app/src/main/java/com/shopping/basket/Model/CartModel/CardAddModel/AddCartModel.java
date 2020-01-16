
package com.shopping.basket.Model.CartModel.CardAddModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCartModel {

    @SerializedName("success")
    @Expose
    private CartAddSuccess success;

    public CartAddSuccess getSuccess() {
        return success;
    }

    public void setSuccess(CartAddSuccess success) {
        this.success = success;
    }

}
