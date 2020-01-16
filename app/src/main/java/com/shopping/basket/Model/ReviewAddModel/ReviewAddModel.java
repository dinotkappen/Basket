
package com.shopping.basket.Model.ReviewAddModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewAddModel {

    @SerializedName("success")
    @Expose
    private ReviewSuccess success;

    public ReviewSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ReviewSuccess success) {
        this.success = success;
    }

}
