
package com.shopping.basket.Model.CartModel.CartListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CartList {

    @SerializedName("cart_id")
    @Expose
    private Integer cartId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_photo")
    @Expose
    private String productPhoto;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;
    @SerializedName("sub_total")
    @Expose
    private Integer subTotal;

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

}
