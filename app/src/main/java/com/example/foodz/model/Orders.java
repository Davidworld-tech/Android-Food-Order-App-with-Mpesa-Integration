package com.example.foodz.model;

public class Orders {

    private String Address, date, name, phoneNumber, state,time, totalAmount;

    public Orders() {
    }

    public Orders(String address, String date, String name, String phoneNumber, String state, String time, String totalAmount) {
        Address = address;
        this.date = date;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.time = time;
        this.totalAmount = totalAmount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
