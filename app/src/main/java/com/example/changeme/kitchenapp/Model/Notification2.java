package com.example.changeme.kitchenapp.Model;

/**
 * Created by SHEGZ on 1/31/2018.
 */
public class Notification2 {
    public String body;
    public  String title;

    public Notification2(String body, String title) {

        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
