
package com.shopping.basket.Model.OrderDetailsModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsData {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("shippingMethod")
    @Expose
    private String shippingMethod;
    @SerializedName("shippingAddress")
    @Expose
    private ShippingAddress shippingAddress;
    @SerializedName("billingAddress")
    @Expose
    private BillingAddress billingAddress;
    @SerializedName("paymentInformation")
    @Expose
    private PaymentInformation paymentInformation;
    @SerializedName("products_data")
    @Expose
    private List<ProductsDatum> productsData = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    public void setPaymentInformation(PaymentInformation paymentInformation) {
        this.paymentInformation = paymentInformation;
    }

    public List<ProductsDatum> getProductsData() {
        return productsData;
    }

    public void setProductsData(List<ProductsDatum> productsData) {
        this.productsData = productsData;
    }

}
