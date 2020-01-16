
package com.shopping.basket.Model.ProductDetailsModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDataArray {

    @SerializedName("product_data")
    @Expose
    private ProductData productData;
    @SerializedName("photo_gallery")
    @Expose
    private List<String> photoGallery = null;
    @SerializedName("product_stars")
    @Expose
    private String productStars;
    @SerializedName("product_ratings_list")
    @Expose
    private List<Object> productRatingsList = null;

    public ProductData getProductData() {
        return productData;
    }

    public void setProductData(ProductData productData) {
        this.productData = productData;
    }

    public List<String> getPhotoGallery() {
        return photoGallery;
    }

    public void setPhotoGallery(List<String> photoGallery) {
        this.photoGallery = photoGallery;
    }

    public String getProductStars() {
        return productStars;
    }

    public void setProductStars(String productStars) {
        this.productStars = productStars;
    }

    public List<Object> getProductRatingsList() {
        return productRatingsList;
    }

    public void setProductRatingsList(List<Object> productRatingsList) {
        this.productRatingsList = productRatingsList;
    }

}
