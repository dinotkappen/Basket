
package com.shopping.basket.Model.FaqModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqModel {

    @SerializedName("success")
    @Expose
    private FaqSuccess success;

    public FaqSuccess getSuccess() {
        return success;
    }

    public void setSuccess(FaqSuccess success) {
        this.success = success;
    }

}
