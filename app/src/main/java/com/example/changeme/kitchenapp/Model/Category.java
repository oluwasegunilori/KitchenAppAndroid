package com.example.changeme.kitchenapp.Model;

/**
 * Created by SHEGZ on 12/30/2017.
 */
public class Category {
    private String name;
    private String image;

    public Category(){

    }


    public Category(String name, String image){
        this.name = name;
        this.image = image;



    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
