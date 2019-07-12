package com.example.changeme.kitchenapp.Model;

public class Favourites {

    private String foodid, foodname,foodprice, foodmenuid, foodimage, fooddiscount, fooddescription, userphone;

    public Favourites(){

    }

    public Favourites(String foodid, String foodname, String foodprice, String foodmenuid, String foodimage, String fooddiscount, String fooddescription, String userphone) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.foodmenuid = foodmenuid;
        this.foodimage = foodimage;
        this.fooddiscount = fooddiscount;
        this.fooddescription = fooddescription;
        this.userphone = userphone;
    }

    public String getFoodid() {
        return foodid;
    }

    public void setFoodid(String foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(String foodprice) {
        this.foodprice = foodprice;
    }

    public String getFoodmenuid() {
        return foodmenuid;
    }

    public void setFoodmenuid(String foodmenuid) {
        this.foodmenuid = foodmenuid;
    }

    public String getFoodimage() {
        return foodimage;
    }

    public void setFoodimage(String foodimage) {
        this.foodimage = foodimage;
    }

    public String getFooddiscount() {
        return fooddiscount;
    }

    public void setFooddiscount(String fooddiscount) {
        this.fooddiscount = fooddiscount;
    }

    public String getFooddescription() {
        return fooddescription;
    }

    public void setFooddescription(String fooddescription) {
        this.fooddescription = fooddescription;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }
}
