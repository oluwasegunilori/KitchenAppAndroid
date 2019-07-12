package com.example.changeme.kitchenapp.Model;

/**
 * Created by SHEGZ on 12/4/2017.
 */
public class User {
    private  String name;
    private String password;
    private String phone;
    private String isStaff;
    private  String secureCode;

    public User(){

    }

    public User(String Name, String Password, String secureCode){
        name = Name;
        password = Password;
        isStaff = "false";
        this.secureCode = secureCode;
    }

    public String getSecurecode() {
        return secureCode;
    }

    public void setSecurecode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String Phone) {
        phone = Phone;
    }

    public  String getName(){
        return name;

    }

    public void setName(String Name) {
        name = Name;
    }

    public String getPassword() {
        return password;
    }


}
