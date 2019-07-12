package com.example.changeme.kitchenapp.Model;

import java.util.List;

/**
 * Created by SHEGZ on 1/1/2018.
 */
public class Request {
    private String phone;
    private String name;
    private String address;
    private String status;
    private String pay_status;
    private String message;
    private String delivery_mode;
    private String phoneplusstatus;
    private String total;
    private String date;
    private String time;
    private List<Order> lifes;

    public Request(){

    }
    public Request(String phone, String name, String address, String status, String pay_status, String message, String delivery_mode, String phoneplusstatus, String total, String date, String time, List<Order> lifes) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.status = status;
        this.pay_status = pay_status;
        this.message = message;
        this.delivery_mode = delivery_mode;
        this.phoneplusstatus = phoneplusstatus;
        this.total = total;
        this.date = date;
        this.time = time;
        this.lifes = lifes;
    }

    public  String getMessage() {
        return message;
    }

    public  void setMessage(String message) {
        this.message = message;
    }

    public  String getPay_status() {
        return pay_status;
    }

    public  void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public  String getDelivery_mode() {
        return delivery_mode;
    }

    public void setDelivery_mode(String delivery_mode) {
        this.delivery_mode = delivery_mode;
    }

    public String getPhoneplusstatus() {
        return phoneplusstatus;
    }

    public void setPhoneplusstatus(String phoneplusstatus) {
        this.phoneplusstatus = phoneplusstatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return pay_status;
    }

    public void setComments(String comments) {
        this.pay_status = comments;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getLifes() {
        return lifes;
    }

    public void setLifes(List<Order> lifes) {
        this.lifes = lifes;
    }
}