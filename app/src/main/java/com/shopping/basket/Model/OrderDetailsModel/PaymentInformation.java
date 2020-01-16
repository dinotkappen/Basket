
package com.shopping.basket.Model.OrderDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentInformation {

    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("paymentMethod")
    @Expose
    private String paymentMethod;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

}
