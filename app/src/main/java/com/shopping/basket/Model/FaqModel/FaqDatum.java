
package com.shopping.basket.Model.FaqModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqDatum {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
