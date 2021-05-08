package com.example.gurukulguru.ModelClasses;

public class Teachers {
    String Name;
    String Phone;
    String Aadhaar;
    String PicUrl;
    String Email;
    String Password;

    public Teachers() {
    }

    public Teachers(String name, String phone, String aadhaar, String picUrl, String email, String password) {
        Name = name;
        Phone = phone;
        Aadhaar = aadhaar;
        PicUrl = picUrl;
        Email = email;
        Password = password;
    }

    public Teachers(String name, String aadhaar, String picUrl, String email, String password) {
        Name = name;
        Aadhaar = aadhaar;
        PicUrl = picUrl;
        Email = email;
        Password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAadhaar() {
        return Aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        Aadhaar = aadhaar;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
