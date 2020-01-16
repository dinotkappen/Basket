
package com.shopping.basket.Model.LoginModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("success")
    @Expose
    private LoginSuccess success;

    public LoginSuccess getSuccess() {
        return success;
    }

    public void setSuccess(LoginSuccess success) {
        this.success = success;
    }

}
