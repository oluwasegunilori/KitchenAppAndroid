package com.example.changeme.kitchenapp.Model;

/**
 * Created by SHEGZ on 12/31/2017.
 */
public class Food {
    private String name;
    private String menuid;
    private String image;
    private String description;
    private String price;
    private String discount;





public Food(){

}
    public Food(String Name, String Menuid, String Image, String Description , String Price , String Discount){

name = Name;
        menuid = Menuid;
        price = Price;
        image = Image;
        description = Description;
        discount = Discount;

    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        name = Name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String Discount) {
        discount = Discount;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String Menuid) {
        menuid = Menuid;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String Price) {
        price = Price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String Image) {
        image = Image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        description = Description;
    }
}
