
package com.shopping.basket.Model.BannerModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BannerList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title_text")
    @Expose
    private String titleText;
    @SerializedName("subtitle_text")
    @Expose
    private String subtitleText;
    @SerializedName("photo")
    @Expose
    private String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getSubtitleText() {
        return subtitleText;
    }

    public void setSubtitleText(String subtitleText) {
        this.subtitleText = subtitleText;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
