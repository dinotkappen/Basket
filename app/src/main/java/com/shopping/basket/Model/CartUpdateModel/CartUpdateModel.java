
package com.shopping.basket.Model.CartUpdateModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartUpdateModel {

    @SerializedName("success")
    @Expose
    private CartUpdateSuccess success;

    public CartUpdateSuccess getSuccess() {
        return success;
    }

    public void setSuccess(CartUpdateSuccess success) {
        this.success = success;
    }

}
