
package com.shopping.basket.Model.CheckoutPriceModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckoutPriceDetails {

    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("shipping_amount")
    @Expose
    private String shippingAmount;
    @SerializedName("total_mrp")
    @Expose
    private Integer totalMrp;

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(String shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public Integer getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(Integer totalMrp) {
        this.totalMrp = totalMrp;
    }

}
