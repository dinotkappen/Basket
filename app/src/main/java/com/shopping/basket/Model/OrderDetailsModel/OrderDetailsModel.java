
package com.shopping.basket.Model.OrderDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsModel {

    @SerializedName("success")
    @Expose
    private OrderDetailSuccess success;

    public OrderDetailSuccess getSuccess() {
        return success;
    }

    public void setSuccess(OrderDetailSuccess success) {
        this.success = success;
    }

}
