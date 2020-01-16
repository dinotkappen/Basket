
package com.shopping.basket.Model.CityListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityListModel {

    @SerializedName("success")
    @Expose
    private CitySuccess success;

    public CitySuccess getSuccess() {
        return success;
    }

    public void setSuccess(CitySuccess success) {
        this.success = success;
    }

}
