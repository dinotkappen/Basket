
package com.shopping.basket.Model.ReviewListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cartlist {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("review_date")
    @Expose
    private String reviewDate;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

}
