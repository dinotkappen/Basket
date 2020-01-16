
package com.shopping.basket.Model.CheckoutPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutPriceModel {

    @SerializedName("success")
    @Expose
    private CheckOutSuccess success;

    public CheckOutSuccess getSuccess() {
        return success;
    }

    public void setSuccess(CheckOutSuccess success) {
        this.success = success;
    }

}
