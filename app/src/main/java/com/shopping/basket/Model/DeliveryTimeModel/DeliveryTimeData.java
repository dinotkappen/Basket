
package com.shopping.basket.Model.DeliveryTimeModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryTimeData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("time")
    @Expose
    private String time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
