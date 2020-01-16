
package com.shopping.basket.Model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryModel {

    @SerializedName("success")
    @Expose
    private OrderHistorySuccess success;

    public OrderHistorySuccess getSuccess() {
        return success;
    }

    public void setSuccess(OrderHistorySuccess success) {
        this.success = success;
    }

}
