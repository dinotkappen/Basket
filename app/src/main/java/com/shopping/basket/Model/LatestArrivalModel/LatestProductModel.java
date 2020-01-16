
package com.shopping.basket.Model.LatestArrivalModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LatestProductModel {

    @SerializedName("success")
    @Expose
    private LatestSuccess success;

    public LatestSuccess getSuccess() {
        return success;
    }

    public void setSuccess(LatestSuccess success) {
        this.success = success;
    }

}
