
package com.shopping.basket.Model.DeliveryTimeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryTimeModel {

    @SerializedName("success")
    @Expose
    private DeliverySuccess success;

    public DeliverySuccess getSuccess() {
        return success;
    }

    public void setSuccess(DeliverySuccess success) {
        this.success = success;
    }

}
