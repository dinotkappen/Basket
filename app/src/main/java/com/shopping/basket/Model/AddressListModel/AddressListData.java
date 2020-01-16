
package com.shopping.basket.Model.AddressListModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressListData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("shipping_name")
    @Expose
    private String shippingName;
    @SerializedName("shipping_phone")
    @Expose
    private String shippingPhone;
    @SerializedName("shipping_email")
    @Expose
    private String shippingEmail;
    @SerializedName("shipping_address")
    @Expose
    private String shippingAddress;
    @SerializedName("shipping_city")
    @Expose
    private Integer shippingCity;
    @SerializedName("shipping_building_no")
    @Expose
    private String shippingBuildingNo;
    @SerializedName("shipping_zone")
    @Expose
    private String shippingZone;
    @SerializedName("shipping_street")
    @Expose
    private String shippingStreet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public void setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Integer getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(Integer shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingBuildingNo() {
        return shippingBuildingNo;
    }

    public void setShippingBuildingNo(String shippingBuildingNo) {
        this.shippingBuildingNo = shippingBuildingNo;
    }

    public String getShippingZone() {
        return shippingZone;
    }

    public void setShippingZone(String shippingZone) {
        this.shippingZone = shippingZone;
    }

    public String getShippingStreet() {
        return shippingStreet;
    }

    public void setShippingStreet(String shippingStreet) {
        this.shippingStreet = shippingStreet;
    }

}
