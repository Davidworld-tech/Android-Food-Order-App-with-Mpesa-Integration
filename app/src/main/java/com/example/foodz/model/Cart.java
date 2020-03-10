package com.example.foodz.model;

public class Cart {
    private String  price, Pname, pid, quantity_Ordered, discount, quantity;

    public Cart() {
    }

    public Cart(String price, String pname, String pid, String quantity_Ordered, String discount, String quantity) {
        this.price = price;
        Pname = pname;
        this.pid = pid;
        this.quantity_Ordered = quantity_Ordered;
        this.discount = discount;
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity_Ordered() {
        return quantity_Ordered;
    }

    public void setQuantity_Ordered(String quantity_Ordered) {
        this.quantity_Ordered = quantity_Ordered;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
