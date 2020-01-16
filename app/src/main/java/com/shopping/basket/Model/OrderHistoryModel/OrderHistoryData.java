
package com.shopping.basket.Model.OrderHistoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryData {

    @SerializedName("order_number")
    @Expose
    private String orderNumber;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("totalAmount")
    @Expose
    private String totalAmount;
    @SerializedName("status")
    @Expose
    private String status;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
