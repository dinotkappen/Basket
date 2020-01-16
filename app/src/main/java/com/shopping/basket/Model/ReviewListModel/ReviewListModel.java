
package com.shopping.basket.Model.ReviewListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewListModel {

    @SerializedName("success")
    @Expose
    private Success success;

    public Success getSuccess() {
        return success;
    }

    public void setSuccess(Success success) {
        this.success = success;
    }

}
