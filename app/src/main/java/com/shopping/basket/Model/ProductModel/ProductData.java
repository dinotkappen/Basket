
package com.shopping.basket.Model.ProductModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("subcategory_id")
    @Expose
    private Object subcategoryId;
    @SerializedName("childcategory_id")
    @Expose
    private Object childcategoryId;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("previous_price")
    @Expose
    private Integer previousPrice;
    @SerializedName("stock")
    @Expose
    private Object stock;
    @SerializedName("featured")
    @Expose
    private Integer featured;
    @SerializedName("best")
    @Expose
    private Integer best;
    @SerializedName("top")
    @Expose
    private Integer top;
    @SerializedName("hot")
    @Expose
    private Integer hot;
    @SerializedName("latest")
    @Expose
    private Integer latest;
    @SerializedName("colors")
    @Expose
    private String colors;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("discount")
    @Expose
    private Object discount;
    @SerializedName("discount_date")
    @Expose
    private Object discountDate;
    @SerializedName("is_discount")
    @Expose
    private Integer isDiscount;
    @SerializedName("store_id")
    @Expose
    private Integer storeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Object getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Object subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Object getChildcategoryId() {
        return childcategoryId;
    }

    public void setChildcategoryId(Object childcategoryId) {
        this.childcategoryId = childcategoryId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(Integer previousPrice) {
        this.previousPrice = previousPrice;
    }

    public Object getStock() {
        return stock;
    }

    public void setStock(Object stock) {
        this.stock = stock;
    }

    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }

    public Integer getBest() {
        return best;
    }

    public void setBest(Integer best) {
        this.best = best;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getHot() {
        return hot;
    }

    public void setHot(Integer hot) {
        this.hot = hot;
    }

    public Integer getLatest() {
        return latest;
    }

    public void setLatest(Integer latest) {
        this.latest = latest;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getDiscount() {
        return discount;
    }

    public void setDiscount(Object discount) {
        this.discount = discount;
    }

    public Object getDiscountDate() {
        return discountDate;
    }

    public void setDiscountDate(Object discountDate) {
        this.discountDate = discountDate;
    }

    public Integer getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Integer isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

}
