
package com.shopping.basket.Model.CartModel.CartListModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("cart_list")
    @Expose
    private List<CartList> cartList = null;
    @SerializedName("price_details")
    @Expose
    private PriceDetails priceDetails;

    public List<CartList> getCartList() {
        return cartList;
    }

    public void setCartList(List<CartList> cartList) {
        this.cartList = cartList;
    }

    public PriceDetails getPriceDetails() {
        return priceDetails;
    }

    public void setPriceDetails(PriceDetails priceDetails) {
        this.priceDetails = priceDetails;
    }

}
