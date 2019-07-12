package com.example.changeme.kitchenapp.Model;

/**
 * Created by SHEGZ on 1/1/2018.
 */
public class Order {
    private String userpphone;
    private String productid;

    private String productname;
    private String quantity;
    private String price;
    private String discount;

    public Order(){

    }

    public Order(String productname, String quantity, String price) {
        this.productname = productname;
        this.quantity = quantity;
        this.price = price;
    }

    public Order(String userpphone, String productid, String productname, String quantity, String price, String discount) {
        this.userpphone = userpphone;
        this.productid = productid;
        this.productname = productname;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    public String getUserpphone() {
        return userpphone;
    }

    public void setUserpphone(String userpphone) {
        this.userpphone = userpphone;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
