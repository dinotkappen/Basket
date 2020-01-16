
package com.shopping.basket.Model.CategoryModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeCategoryModel {

    @SerializedName("success")
    @Expose
    private CategorySuccess success;

    public CategorySuccess getSuccess() {
        return success;
    }

    public void setSuccess(CategorySuccess success) {
        this.success = success;
    }

}
