
package com.shopping.basket.Model.CartModel.CartListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PriceDetails {

    @SerializedName("total_mrp")
    @Expose
    private Integer totalMrp;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("shipping_amount")
    @Expose
    private String shipping_amount;
    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(Integer totalMrp) {
        this.totalMrp = totalMrp;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getShipping() {
        return shipping_amount;
    }

    public void setShipping(String shipping_amount) {
        this.shipping_amount = shipping_amount;
    }
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
