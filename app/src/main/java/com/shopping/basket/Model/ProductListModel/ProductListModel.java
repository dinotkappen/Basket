
package com.shopping.basket.Model.ProductListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductListModel {

    @SerializedName("success")
    @Expose
    private ProductListSuccess success;

    public ProductListSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ProductListSuccess success) {
        this.success = success;
    }

}
