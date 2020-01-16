
package com.shopping.basket.Model.CheckoutPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckOutSuccess {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private CheckoutData data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CheckoutData getData() {
        return data;
    }

    public void setData(CheckoutData data) {
        this.data = data;
    }

}
