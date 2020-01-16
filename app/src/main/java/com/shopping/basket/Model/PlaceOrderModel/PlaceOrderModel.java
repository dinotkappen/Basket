
package com.shopping.basket.Model.PlaceOrderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrderModel {

    @SerializedName("success")
    @Expose
    private PlaceOrderSuccess success;

    public PlaceOrderSuccess getSuccess() {
        return success;
    }

    public void setSuccess(PlaceOrderSuccess success) {
        this.success = success;
    }

}
