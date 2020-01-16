
package com.shopping.basket.Model.CartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cartItems")
    @Expose
    private List<CartItem> cartItems = null;
    @SerializedName("subtotal")
    @Expose
    private Integer subtotal;
    @SerializedName("total")
    @Expose
    private Integer total;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
