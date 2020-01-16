
package com.shopping.basket.Model.CartModel.CardAddModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartAddSuccess {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;

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

}
