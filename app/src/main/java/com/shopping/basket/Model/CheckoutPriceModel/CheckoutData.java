
package com.shopping.basket.Model.CheckoutPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutData {

    @SerializedName("price_details")
    @Expose
    private CheckoutPriceDetails priceDetails;

    public CheckoutPriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(CheckoutPriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

}
