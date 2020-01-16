
package com.shopping.basket.Model.AddressListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressListModel {

    @SerializedName("success")
    @Expose
    private AddressListSuccess success;

    public AddressListSuccess getSuccess() {
        return success;
    }

    public void setSuccess(AddressListSuccess success) {
        this.success = success;
    }

}
