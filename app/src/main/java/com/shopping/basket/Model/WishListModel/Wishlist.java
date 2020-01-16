
package com.shopping.basket.Model.WishListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wishlist {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("customer_id")
    @Expose
    private Integer customerId;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("short_description")
    @Expose
    private String shortDescription;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("sale_price")
    @Expose
    private String salePrice;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("length")
    @Expose
    private Object length;
    @SerializedName("width")
    @Expose
    private Object width;
    @SerializedName("height")
    @Expose
    private Object height;
    @SerializedName("distance_unit")
    @Expose
    private Object distanceUnit;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("mass_unit")
    @Expose
    private String massUnit;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("ram")
    @Expose
    private String ram;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("processor")
    @Expose
    private String processor;
    @SerializedName("camera")
    @Expose
    private String camera;
    @SerializedName("dimensions")
    @Expose
    private String dimensions;
    @SerializedName("storage")
    @Expose
    private String storage;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("video")
    @Expose
    private String video;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("new_product_flag")
    @Expose
    private String newProductFlag;
    @SerializedName("featured_flag")
    @Expose
    private String featuredFlag;
    @SerializedName("special_flag")
    @Expose
    private String specialFlag;
    @SerializedName("type")
    @Expose
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getLength() {
        return length;
    }

    public void setLength(Object length) {
        this.length = length;
    }

    public Object getWidth() {
        return width;
    }

    public void setWidth(Object width) {
        this.width = width;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Object getDistanceUnit() {
        return distanceUnit;
    }

    public void setDistanceUnit(Object distanceUnit) {
        this.distanceUnit = distanceUnit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMassUnit() {
        return massUnit;
    }

    public void setMassUnit(String massUnit) {
        this.massUnit = massUnit;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getNewProductFlag() {
        return newProductFlag;
    }

    public void setNewProductFlag(String newProductFlag) {
        this.newProductFlag = newProductFlag;
    }

    public String getFeaturedFlag() {
        return featuredFlag;
    }

    public void setFeaturedFlag(String featuredFlag) {
        this.featuredFlag = featuredFlag;
    }

    public String getSpecialFlag() {
        return specialFlag;
    }

    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
