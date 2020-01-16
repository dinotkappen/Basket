
package com.shopping.basket.Model.ProfileUpdateModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUpdateModel {

    @SerializedName("success")
    @Expose
    private ProfileUpdateSuccess success;

    public ProfileUpdateSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ProfileUpdateSuccess success) {
        this.success = success;
    }

}
