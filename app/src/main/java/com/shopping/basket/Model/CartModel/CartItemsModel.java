package com.shopping.basket.Model.CartModel;

public class CartItemsModel {

    public int id;
    public String title;
    public String short_title;
    public String price;
    public int qty;
    public String url;
    public CartItemsModel(int id,String title,String short_title,String price,int qty,String url){

        this.id=id;
        this.title = title;
        this.short_title = short_title;
        this.price = price;
        this.qty = qty;
        this.url = url;

    }
}
