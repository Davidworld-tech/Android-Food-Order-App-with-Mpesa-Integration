package com.example.foodz.model;

public class Admins {
    private String PhoneNumber, Name, pwd, HotelLogo, location;

    public Admins() {
    }

    public Admins(String phoneNumber, String name, String pwd, String hotelLogo, String location) {
        PhoneNumber = phoneNumber;
        Name = name;
        this.pwd = pwd;
        HotelLogo = hotelLogo;
        this.location = location;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getHotelLogo() {
        return HotelLogo;
    }

    public void setHotelLogo(String hotelLogo) {
        HotelLogo = hotelLogo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
