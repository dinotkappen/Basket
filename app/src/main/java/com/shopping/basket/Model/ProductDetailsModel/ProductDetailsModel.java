
package com.shopping.basket.Model.ProductDetailsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailsModel {

    @SerializedName("success")
    @Expose
    private ProductDetailsSuccess success;

    public ProductDetailsSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ProductDetailsSuccess success) {
        this.success = success;
    }

}
