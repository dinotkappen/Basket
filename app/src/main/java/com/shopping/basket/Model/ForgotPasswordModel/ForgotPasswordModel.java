
package com.shopping.basket.Model.ForgotPasswordModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("success")
    @Expose
    private ForgotSuccess success;

    public ForgotSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ForgotSuccess success) {
        this.success = success;
    }

}
