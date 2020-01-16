
package com.shopping.basket.Model.ProfileListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileLisModel {

    @SerializedName("success")
    @Expose
    private ProfileListSuccess success;

    public ProfileListSuccess getSuccess() {
        return success;
    }

    public void setSuccess(ProfileListSuccess success) {
        this.success = success;
    }

}
